using Microsoft.AspNetCore.Mvc;
using NFLPool.Interface;
using NFLPool.Model;

namespace NFLPool.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class Score : ControllerBase
    {
        private readonly INFLCrawler _nflCrawler;
        private readonly IExcelReader _excelReader;
        private readonly IGoogleAPI _googleAPI;
        private readonly IPoolService _poolService;

        public Score(INFLCrawler nflCrawler, IExcelReader excelReader,
            IGoogleAPI googleAPI, IPoolService poolService)
        {
            _nflCrawler = nflCrawler;
            _excelReader = excelReader;
            _googleAPI = googleAPI;
            _poolService = poolService;
        }

        [HttpGet]
        public async Task<WeekResults> Get(int year, int week, int seasontype)
        {
            try
            {
                return await _poolService.GetWeekResults(_nflCrawler, _googleAPI, _excelReader, year, week, seasontype);
            }
            catch (Exception ex)
            {
                throw;
            }
        }
    }
}