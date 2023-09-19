using NFLPool.Model;

namespace NFLPool.Interface
{
    public interface IPoolService
    {
        Task<WeekResults> GetWeekResults(INFLCrawler nflCrawler, IGoogleAPI googleAPI,
            IFileReader excelReader, int year, int week, int seasontype);
    }
}