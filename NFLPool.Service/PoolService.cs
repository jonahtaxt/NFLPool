using NFLPool.Interface;
using NFLPool.Model;

namespace NFLPool.Service;

public class PoolService : IPoolService
{
    private readonly IDataService _dataService;
    private readonly IFileReader _fileReader;
    private readonly IGoogleAPI _googleApi;
    private readonly INFLCrawler _nflCrawler;
    private readonly CouchbaseDto<ParticipantWeekData> _participantWeekData;

    public PoolService(INFLCrawler nflCrawler, IGoogleAPI googleApi,
        IFileReader fileReader, IDataService dataService, CouchbaseConfiguration couchbaseConfiguration)
    {
        _nflCrawler = nflCrawler;
        _googleApi = googleApi;
        _fileReader = fileReader;
        _dataService = dataService;
        _participantWeekData = new CouchbaseDto<ParticipantWeekData>(couchbaseConfiguration, "nflpooldb",
            "nflpoolscope", "", null, string.Empty);
    }

    public async Task InsertPoolParticipants(string gAuthPath, string fileName, int year, int week)
    {
        if (string.IsNullOrEmpty(fileName) || year == 0 || week == 0)
            throw new ArgumentException(
                "FileName, year or int were not provided");

        await using var fileStream = await _googleApi.DownloadFile(gAuthPath, fileName);

        if (fileStream == null) return;

        fileStream.Position = 0;
        _participantWeekData.Key = $"{year}.{week}";
        var readFile = _fileReader.ReadFile(fileStream);
        _participantWeekData.Document = new ParticipantWeekData
        {
            Participants = readFile.Participants,
            MondayNightTeams = readFile.MondayNightTeams
        };
        if (_participantWeekData.Document is not null) await _dataService.Insert(_participantWeekData);
    }

    public async Task<WeekResults> GetWeekResults(int year, int week)
    {
        var results = new WeekResults();

        var gameScoresTask = _nflCrawler.GetWeekScoresAsync(year, week);

        var poolScoresTask = GetPoolParticipants(year, week);

        await Task.WhenAll(gameScoresTask, poolScoresTask);

        results.GameScores = gameScoresTask.Result;
        results.Participants = poolScoresTask.Result?.Participants;
        var mondayNightTeams = poolScoresTask.Result?.MondayNightTeams;

        if (results.Participants is null) return results;

        CalculateTotalPoints(results);

        if (mondayNightTeams != null)
        {
            var mondayNightGame = CalculateMondayNightPoints(results, mondayNightTeams);

            OrderParticipantsByWinner(results);

            if (mondayNightGame?.AwayScore > 0 || mondayNightGame?.HomeScore > 0)
                results.ParticipantWinnerId = results.Participants.First().Id;
        }

        if (poolScoresTask.Result != null)
        {
            poolScoresTask.Result.Participants = results.Participants;

            _participantWeekData.Document = poolScoresTask.Result;
        }

        _participantWeekData.Key = $"{year}.{week}";
        await _dataService.Insert(_participantWeekData);

        return results;
    }

    private async Task<ParticipantWeekData?> GetPoolParticipants(int year, int week)
    {
        _participantWeekData.Key = $"{year}.{week}";
        await _dataService.GetDocumentByKey(_participantWeekData);

        return _participantWeekData.Document ?? null;
    }

    private static void OrderParticipantsByWinner(WeekResults results)
    {
        if (results.Participants is null) return;

        results.Participants.ForEach(participant =>
        {
            var orderedBets = new List<string>();
            results.GameScores.ForEach(gameScore =>
            {
                if (participant.Bets.Any(bet => gameScore.AwayTeam?.PoolName == bet))
                {
                    if (gameScore.AwayTeam is not null) orderedBets.Add(gameScore.AwayTeam.PoolName);
                }
                else if (participant.Bets.Any(bet => gameScore.HomeTeam?.PoolName == bet))
                {
                    if (gameScore.HomeTeam is not null) orderedBets.Add(gameScore.HomeTeam.PoolName);
                }
                else
                {
                    orderedBets.Add(string.Empty);
                }
            });
            participant.Bets.Clear();
            participant.Bets.AddRange(orderedBets);
            orderedBets.Clear();
        });

        results.Participants = results.Participants.OrderByDescending(participant => participant.TotalPoints)
            .ThenBy(participant => participant.MondayNightPointsDifference)
            .ThenBy(participant => participant.Id).ToList();
    }

    private static GameScore? CalculateMondayNightPoints(WeekResults results, List<string> mondayNightFootballTeams)
    {
        if (results.Participants is null) return null;

        var mondayNightGame = results.GameScores.FirstOrDefault(gameScore =>
            gameScore.AwayTeam?.PoolName == mondayNightFootballTeams.First() &&
                gameScore.HomeTeam?.PoolName == mondayNightFootballTeams.Last());

        switch (mondayNightGame)
        {
            case null:
                return mondayNightGame;
            case { AwayScore: <= 0, HomeScore: <= 0 }:
                return mondayNightGame;
        }

        var mondayNightScoreDifference = mondayNightGame.AwayScore + mondayNightGame.HomeScore;

        results.Participants.ForEach(participant =>
        {
            var pointDifference = mondayNightScoreDifference - participant.MondayNightPoints;

            if (pointDifference < 0) pointDifference *= -1;
            participant.MondayNightPointsDifference = pointDifference;
        });

        return mondayNightGame;
    }

    private static void CalculateTotalPoints(WeekResults results)
    {
        if (results.Participants is null) return;

        results.Participants.ForEach(participant => { participant.TotalPoints = 0; });

        foreach (var winningTeam in results.GameScores.Select(gameScore => gameScore.WinningTeam)
                     .Where(winningTeam => winningTeam != null))
            results.Participants.ForEach(participant =>
            {
                if (participant.Bets.Any(bet => bet == winningTeam)) participant.TotalPoints++;
            });
    }
}