using NFLPool.Model;

namespace NFLPool.Interface;

public interface IPoolService
{
    Task<WeekResults> GetWeekResults(int year, int week);

    Task InsertPoolParticipants(string gAuthPath, string fileName, int year, int week);
}