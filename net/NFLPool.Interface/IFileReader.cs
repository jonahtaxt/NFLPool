using NFLPool.Model;

namespace NFLPool.Interface;

public interface IFileReader
{
    (List<Participant> Participants, List<string> MondayNightTeams) ReadFile(Stream fileStream);
}