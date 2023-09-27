using Microsoft.AspNetCore.Mvc;
using NFLPool.Interface;
using NFLPool.Model;

namespace NFLPool.Api.Controllers;

[Route("api/[controller]")]
[ApiController]
public class Score : ControllerBase
{
    private readonly IConfiguration _configuration;
    private readonly IDataService _dataService;
    private readonly IFileReader _excelReader;
    private readonly IGoogleAPI _googleApi;
    private readonly INFLCrawler _nflCrawler;
    private readonly IPoolService _poolService;
    private readonly ILogger _logger;

    public Score(INFLCrawler nflCrawler, IFileReader excelReader,
        IGoogleAPI googleApi, IPoolService poolService, IDataService dataService, IConfiguration configuration,
        ILogger<Score> logger)
    {
        _nflCrawler = nflCrawler;
        _excelReader = excelReader;
        _googleApi = googleApi;
        _poolService = poolService;
        _configuration = configuration;
        _dataService = dataService;
        _logger = logger;
    }

    [HttpGet]
    public async Task<IActionResult> Get(int year, int week)
    {
        try
        {
            return Ok(await _poolService.GetWeekResults(_configuration["Google:JsonCredentialsPath"], year, week));
        }
        catch (Exception ex)
        {
            _logger.LogCritical($"An error occurred during score/get: {ex.Message + ex.InnerException}");
            return StatusCode(500);
        }
    }
}