using NFLPool.Model;

namespace NFLPool.Interface;

public interface IPoolService
{
    Task<WeekResults> GetWeekResults(string? gAuthPath, int year, int week);
}