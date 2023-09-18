using NFLPool.Interface;
using NFLPool.Model;

namespace NFLPool.Service
{
    public class FileReader : IFileReader
    {
        public PoolWeekScores? ReadFile(Stream fileStream)
        {
            var poolTeams = new List<PoolTeam>();
            var poolParticipants = new List<Participant>();
            using (var sr = new StreamReader(fileStream))
            {
                var line = string.Empty;
                var firstLine = true;
                var participantId = 1;
                while ((line = sr.ReadLine()) != null)
                {
                    if (firstLine)
                    {
                        var teams = line.Split(',');
                        foreach (var team in teams)
                        {
                            poolTeams.Add(new PoolTeam { TeamName = team.Trim() });
                        }
                        firstLine = false;
                    }
                    else
                    {
                        var participant = new Participant { Id = participantId };
                        var participantData = line.Split(",");
                        for (var i = 0; i < participantData.Length; i++)
                        {
                            if (i == 0)
                            {
                                participant.Name = participantData[i].Trim();
                            }
                            else if (i == (participantData.Length - 1))
                            {
                                participant.MondayNightPoints = Convert.ToInt32(participantData[i].Trim());
                            }
                            else
                            {
                                participant.Bets.Add(participantData[i].Trim());
                            }
                        }
                        poolParticipants.Add(participant);
                        participantId++;
                    }
                }
            }

            return new PoolWeekScores(poolParticipants, poolTeams);
        }
    }
}