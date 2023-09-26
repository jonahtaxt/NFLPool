using NFLPool.Interface;
using NFLPool.Model;

namespace NFLPool.Service;

public class FileReader : IFileReader
{
    public List<Participant> ReadFile(Stream fileStream)
    {
        var poolParticipants = new List<Participant>();
        using var sr = new StreamReader(fileStream);
        var firstLine = true;
        var participantId = 1;
        while (sr.ReadLine() is { } line)
            if (!firstLine)
            {
                var participant = new Participant { Id = participantId };
                var participantData = line.Split(",");
                for (var i = 0; i < participantData.Length; i++)
                    if (i == 0)
                        participant.Name = participantData[i].Trim();
                    else if (i == participantData.Length - 1)
                        participant.MondayNightPoints = Convert.ToInt32(participantData[i].Trim());
                    else
                        participant.Bets.Add(participantData[i].Trim());
                poolParticipants.Add(participant);
                participantId++;
            }
            else
            {
                firstLine = false;
            }

        return poolParticipants;
    }
}