using NFLPool.Model;

namespace NFLPool.Interface
{
    public interface IExcelReader
    {
        PoolWeekScores? ReadFile(Stream xlsStream);
    }
}