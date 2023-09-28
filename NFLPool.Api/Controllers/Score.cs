using Microsoft.AspNetCore.Mvc;
using NFLPool.Interface;

namespace NFLPool.Api.Controllers;

[Route("api/[controller]")]
[ApiController]
public class Score : ControllerBase
{
    private readonly IConfiguration _configuration;
    private readonly IDataService _dataService;
    private readonly IFileReader _excelReader;
    private readonly IGoogleAPI _googleApi;
    private readonly ILogger _logger;
    private readonly INFLCrawler _nflCrawler;
    private readonly IPoolService _poolService;

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
            return Ok(await _poolService.GetWeekResults(year, week));
        }
        catch (Exception ex)
        {
            _logger.LogCritical($"An error occurred during score/get: {ex.Message + ex.InnerException}");
            return StatusCode(500);
        }
    }

    [HttpPost]
    public async Task<IActionResult> Post(string fileName, int year, int week)
    {
        try
        {
            var gAuthPath = _configuration["Google:JsonCredentialsPath"];
            if (string.IsNullOrEmpty(gAuthPath))
                throw new ArgumentNullException("Google credentials not provided");

            await _poolService.InsertPoolParticipants(gAuthPath, fileName,
                year, week);
            _logger.LogInformation($"Pool Participants for year {year} week {week} added successfully");
            return Ok();
        }
        catch (Exception ex)
        {
            _logger.LogCritical($"An error occurred during score/post: {ex.Message + ex.InnerException}");
            return StatusCode(500);
        }
    }
}