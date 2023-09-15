using NFLPool.Model;

namespace NFLPool.Interface
{
    public interface IPoolService
    {
        Task<WeekResults> GetWeekResults(INFLCrawler nflCrawler, IGoogleAPI googleAPI,
            IExcelReader excelReader, int year, int week, int seasontype);
    }
}