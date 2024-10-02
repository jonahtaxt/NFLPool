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

    [HttpGet("{year}/{week}")]
    public async Task<ActionResult<WeekResults>> Get(int year, int week)
    {
        try
        {
            return await _poolService.GetWeekResults(year, week);
        }
        catch (Exception ex)
        {
            _logger.LogCritical($"An error occurred during score/get: {ex.Message + ex.InnerException}");
            return StatusCode(500);
        }
    }

    [HttpPost]
    public async Task<IActionResult> Post(PoolFile poolFile)
    {
        try
        {
            var gAuthPath = _configuration["Google:JsonCredentialsPath"];
            if (string.IsNullOrEmpty(gAuthPath))
                throw new ArgumentNullException("Google credentials not provided");
            
            if (!poolFile.Name.Contains("quiniela"))
            {
                _logger.LogInformation(
                    $"File {poolFile.Name} discarded");
                return Ok();
            }
            
            if (string.IsNullOrEmpty(poolFile.Name) || poolFile.Name.Split('_').Length != 3)
            {
                throw new Exception("Pool file name incorrect format");
            }
            
            await _poolService.InsertPoolParticipants(gAuthPath, poolFile.Name, poolFile.Year(), poolFile.Week());
            
            _logger.LogInformation(
                $"Pool Participants for file {poolFile.Name} year {poolFile.Year()} week {poolFile.Week()} added successfully");
            return Ok();
        }
        catch (Exception ex)
        {
            _logger.LogCritical($"An error occurred during score/post: {ex.Message + ex.InnerException}");
            return StatusCode(500);
        }
    }
}