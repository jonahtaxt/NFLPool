using NFLPool.Model;

namespace NFLPool.Interface
{
    public interface IPoolService
    {
        Task<WeekResults> GetWeekResults(INFLCrawler nflCrawler, IGoogleAPI googleAPI,
            IFileReader excelReader, string? gAuthPath, int year, int week);
    }
}