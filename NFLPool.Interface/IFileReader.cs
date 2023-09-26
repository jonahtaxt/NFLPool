using NFLPool.Model;

namespace NFLPool.Interface;

public interface IFileReader
{
    List<Participant> ReadFile(Stream fileStream);
}