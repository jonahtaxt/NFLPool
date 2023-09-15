using NFLPool.Interface;
using NFLPool.Model;

namespace NFLPool.Service
{
    public class PoolService : IPoolService
    {
        public async Task<WeekResults> GetWeekResults(INFLCrawler nflCrawler, IGoogleAPI googleAPI,
            IExcelReader excelReader, int year, int week, int seasontype)
        {
            var results = new WeekResults();
            List<PoolTeam> poolTeams;
            PoolWeekScores? poolWeekScores = null;
            var gameScoresTask = nflCrawler.GetWeekScoresAsync(year, week, seasontype);
            var poolScoresTask = Task.Run(async () =>
            {
                using (var excelStream = await googleAPI.DownloadFile($"{week}{year}.xlsx"))
                {
                    if (excelStream != null)
                    {
                        excelStream.Position = 0;
                        poolWeekScores = excelReader.ReadFile(excelStream);
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
                        poolWeekScores.Teams.FirstOrDefault(team => team.TeamName == winningTeam.PoolName)?.Participants
                             .ForEach(participantId =>
                             {
                                 poolWeekScores.Participants.FirstOrDefault(participant => participant.Id == participantId).TotalPoints += 1;
                             });
                    }
                }

                results.Participants.ForEach(participant =>
                {
                    participant.Bets = new List<string>();
                    for (var i = 0; results.GameScores.Count > i; i++)
                    {
                        var betOnTeam = string.Empty;
                        var awayTeam = results.GameScores[i].AwayTeam;
                        var homeTeam = results.GameScores[i].HomeTeam;

                        if(poolWeekScores.Teams.FirstOrDefault(team => team.TeamName == awayTeam.PoolName)
                            .Participants.Any(p => p == participant.Id))
                        {
                            betOnTeam = awayTeam.PoolName;
                        }
                        else if (poolWeekScores.Teams.FirstOrDefault(team => team.TeamName == homeTeam.PoolName)
                            .Participants.Any(p => p == participant.Id))
                        {
                            betOnTeam = homeTeam.PoolName;
                        }

                        participant.Bets.Add(betOnTeam);
                    }
                });

                var mondayNightAwayScore = results.GameScores.Last().AwayScore;
                var mondayNightHomeScore = results.GameScores.Last().HomeScore;

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

                results.Participants = results.Participants.OrderByDescending(participant => participant.TotalPoints)
                    .ThenBy(participant => participant.MondayNightPointsDifference)
                    .ThenBy(participant => participant.Id).ToList();
            }

            return results;
        }
    }
}