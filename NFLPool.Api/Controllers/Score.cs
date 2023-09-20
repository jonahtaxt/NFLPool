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
        private readonly IFileReader _excelReader;
        private readonly IGoogleAPI _googleAPI;
        private readonly IPoolService _poolService;
        private readonly IConfiguration _configuration;

        public Score(INFLCrawler nflCrawler, IFileReader excelReader,
            IGoogleAPI googleAPI, IPoolService poolService, IConfiguration configuration)
        {
            _nflCrawler = nflCrawler;
            _excelReader = excelReader;
            _googleAPI = googleAPI;
            _poolService = poolService;
            _configuration = configuration;
        }

        [HttpGet]
        public async Task<WeekResults> Get(int year, int week)
        {
            return await _poolService.GetWeekResults(_nflCrawler, _googleAPI, _excelReader, _configuration["Google:JsonCredentialsPath"], year, week);
        }
    }
}