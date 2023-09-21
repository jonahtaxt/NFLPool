using NFLPool.Model;

namespace NFLPool.Interface;

public interface IPoolService
{
    Task<WeekResults> GetWeekResults(INFLCrawler nflCrawler, IGoogleAPI googleApi,
        IFileReader fileReader, string? gAuthPath, int year, int week);
}