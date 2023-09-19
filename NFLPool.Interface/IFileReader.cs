using NFLPool.Model;

namespace NFLPool.Interface
{
    public interface IFileReader
    {
        PoolWeekScores? ReadFile(Stream fileStream);
    }
}