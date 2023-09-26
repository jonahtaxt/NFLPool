using NFLPool.Interface;
using NFLPool.Model;

namespace NFLPool.Service;

public class PoolService : IPoolService
{
    private readonly IDataService _dataService;
    private readonly IFileReader _fileReader;
    private readonly IGoogleAPI _googleApi;
    private readonly INFLCrawler _nflCrawler;
    private readonly CouchbaseDto<List<Participant>> _participantsCollection;

    public PoolService(INFLCrawler nflCrawler, IGoogleAPI googleApi,
        IFileReader fileReader, IDataService dataService, CouchbaseConfiguration couchbaseConfiguration)
    {
        _nflCrawler = nflCrawler;
        _googleApi = googleApi;
        _fileReader = fileReader;
        _dataService = dataService;
        _participantsCollection = new CouchbaseDto<List<Participant>>("Participants")
        {
            Bucket = "nflpooldb",
            Scope = "nflpoolscope",
            CouchbaseConfiguration = couchbaseConfiguration
        };
    }

    public async Task<WeekResults> GetWeekResults(string? gAuthPath, int year, int week)
    {
        if (gAuthPath is null) throw new ArgumentNullException(gAuthPath, "Google Auth File Path is null");

        var results = new WeekResults();

        var gameScoresTask = _nflCrawler.GetWeekScoresAsync(year, week);

        var poolScoresTask = GetPoolParticipants(gAuthPath, year, week);

        await Task.WhenAll(gameScoresTask, poolScoresTask);

        results.GameScores = gameScoresTask.Result;
        results.Participants = poolScoresTask.Result;

        if (results.Participants.Count <= 0) return results;

        CalculateTotalPoints(results);

        var mondayNightGame = CalculateMondayNightPoints(results);

        OrderParticipantsByWinner(results);

        if (mondayNightGame?.AwayScore > 0 || mondayNightGame?.HomeScore > 0)
            results.ParticipantWinnerId = results.Participants.First().Id;

        _participantsCollection.Document = results.Participants;
        _participantsCollection.Key = $"{year}.{week}";
        await _dataService.Insert(_participantsCollection);

        return results;
    }

    private async Task<List<Participant>> GetPoolParticipants(string gAuthPath, int year, int week)
    {
        _participantsCollection.Key = $"{year}.{week}";
        await _dataService.GetDocumentByKey(_participantsCollection);

        if (_participantsCollection.Document != null) return _participantsCollection.Document;

        await using var fileStream = await _googleApi.DownloadFile(gAuthPath, $"{week}{year}.txt");

        if (fileStream == null) return new List<Participant>();

        fileStream.Position = 0;
        var participants = _fileReader.ReadFile(fileStream);

        return participants;
    }

    private static void OrderParticipantsByWinner(WeekResults results)
    {
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

    private static GameScore? CalculateMondayNightPoints(WeekResults results)
    {
        var mondayNightGame = results.GameScores.FirstOrDefault(gameScore =>
            gameScore.HomeTeam?.PoolName == results.Participants[0].Bets.Last() ||
            gameScore.AwayTeam?.PoolName == results.Participants[0].Bets.Last());

        if (mondayNightGame != null)
            if (mondayNightGame.AwayScore > 0 || mondayNightGame.HomeScore > 0)
            {
                var mondayNightScoreDifference = mondayNightGame.AwayScore + mondayNightGame.HomeScore;

                results.Participants.ForEach(participant =>
                {
                    var pointDifference = mondayNightScoreDifference - participant.MondayNightPoints;

                    if (pointDifference < 0) pointDifference *= -1;
                    participant.MondayNightPointsDifference = pointDifference;
                });
            }

        return mondayNightGame;
    }

    private static void CalculateTotalPoints(WeekResults results)
    {
        foreach (var winningTeam in results.GameScores.Select(gameScore => gameScore.WinningTeam)
                     .Where(winningTeam => winningTeam != null))
            results.Participants.ForEach(participant =>
            {
                if (participant.Bets.Any(bet => bet == winningTeam)) participant.TotalPoints++;
            });
    }
}