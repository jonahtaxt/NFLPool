using Microsoft.AspNetCore.Mvc;
using NFLPool.Interface;
using NFLPool.Model;

namespace NFLPool.Api.Controllers;

[Route("api/[controller]")]
[ApiController]
public class Score : ControllerBase
{
    private readonly IConfiguration _configuration;
    private readonly IFileReader _excelReader;
    private readonly IGoogleAPI _googleApi;
    private readonly INFLCrawler _nflCrawler;
    private readonly IPoolService _poolService;

    public Score(INFLCrawler nflCrawler, IFileReader excelReader,
        IGoogleAPI googleApi, IPoolService poolService, IConfiguration configuration)
    {
        _nflCrawler = nflCrawler;
        _excelReader = excelReader;
        _googleApi = googleApi;
        _poolService = poolService;
        _configuration = configuration;
    }

    [HttpGet]
    public async Task<WeekResults> Get(int year, int week)
    {
        return await _poolService.GetWeekResults(_nflCrawler, _googleApi, _excelReader,
            _configuration["Google:JsonCredentialsPath"], year, week);
    }
}