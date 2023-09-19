using NFLPool.Interface;
using NFLPool.Model;

namespace NFLPool.Service
{
    public class PoolService : IPoolService
    {
        public async Task<WeekResults> GetWeekResults(INFLCrawler nflCrawler, IGoogleAPI googleAPI,
            IFileReader excelReader, int year, int week, int seasontype)
        {
            var results = new WeekResults();
            List<PoolTeam> poolTeams;
            PoolWeekScores? poolWeekScores = null;
            var gameScoresTask = nflCrawler.GetWeekScoresAsync(year, week, seasontype);
            var poolScoresTask = Task.Run(async () =>
            {
                using (var fileStream = await googleAPI.DownloadFile("/home/cygnus/temp/nflpool-auth.json", $"{week}{year}.txt"))
                {
                    if (fileStream != null)
                    {
                        fileStream.Position = 0;
                        poolWeekScores = excelReader.ReadFile(fileStream);
                        results.Participants = poolWeekScores.Participants;
                    }
                }
            });

            await Task.WhenAll(gameScoresTask, poolScoresTask);

            results.GameScores = gameScoresTask.Result;

            if (poolWeekScores != null)
            {
                foreach (var gameScore in results.GameScores)
                {
                    var winningTeam = gameScore.WinningTeam;
                    if (winningTeam != null)
                    {
                        results.Participants.ForEach(participant =>
                        {
                            if (participant.Bets.Any(bet => bet == winningTeam))
                            {
                                participant.TotalPoints++;
                            }
                        });
                    }
                }

                var mondayNightGame = results.GameScores.FirstOrDefault(gameScore => (gameScore.HomeTeam.PoolName == results.Participants[0].Bets.Last() || gameScore.AwayTeam.PoolName == results.Participants[0].Bets.Last()));

                var mondayNightAwayScore = mondayNightGame.AwayScore;
                var mondayNightHomeScore = mondayNightGame.HomeScore;

                if (mondayNightAwayScore > 0 || mondayNightHomeScore > 0)
                {
                    var mondayNightScoreDifference = mondayNightAwayScore + mondayNightHomeScore;

                    results.Participants.ForEach(participant =>
                    {
                        var pointDifference = mondayNightScoreDifference - participant.MondayNightPoints;

                        if (pointDifference < 0)
                        {
                            pointDifference *= -1;
                        }
                        participant.MondayNightPointsDifference = pointDifference;
                    });
                }

                results.Participants.ForEach(participant =>
                {
                    var orderedBets = new List<string>();
                    results.GameScores.ForEach(gameScore =>
                    {
                        if(participant.Bets.Any(bet => gameScore.AwayTeam.PoolName == bet))
                        {
                            orderedBets.Add(gameScore.AwayTeam.PoolName);
                        }
                        else if (participant.Bets.Any(bet => gameScore.HomeTeam.PoolName == bet))
                        {
                            orderedBets.Add(gameScore.HomeTeam.PoolName);
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

            return results;
        }
    }
}