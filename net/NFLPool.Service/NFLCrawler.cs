using System.Net.Http.Headers;
using HtmlAgilityPack;
using NFLPool.Interface;
using NFLPool.Model;

namespace NFLPool.Service;

public class NFLCrawler : INFLCrawler
{
    private const string SCORE_ROW_SELECTOR =
        "//section[contains(@class,'Scoreboard bg-clr-white flex flex-auto justify-between')]";

    private const string TEAMS_SELECTOR = "ScoreCell__TeamName ScoreCell__TeamName--shortDisplayName truncate db";

    private const string SCORE_SELECTOR =
        "ScoreCell__Score h4 clr-gray-01 fw-heavy tar ScoreCell_Score--scoreboard pl2";

    private readonly HtmlDocument _htmlDocument;
    private readonly HttpClient _httpClient;

    public NFLCrawler()
    {
        _httpClient = new HttpClient();
        _htmlDocument = new HtmlDocument();

        _httpClient.DefaultRequestHeaders.CacheControl = new CacheControlHeaderValue
        {
            NoCache = true
        };
    }

    public async Task<List<GameScore>> GetWeekScoresAsync(int year, int week)
    {
        _htmlDocument.LoadHtml(
            await _httpClient.GetStringAsync(
                $"https://www.espn.com/nfl/scoreboard/_/week/{week}/year/{year}/seasontype/2"));

        var gameResults = new List<GameScore>();
        var gameScoreId = 1;

        _htmlDocument.DocumentNode.SelectNodes(SCORE_ROW_SELECTOR).ToList().ForEach(score =>
        {
            Type? homeType = null;
            Type? awayType = null;
            Team? homeTeam = null;
            Team? awayTeam = null;
            var awayScore = string.Empty;
            var homeScore = string.Empty;

            var teams = score.Descendants("div")
                .Where(node => node.GetAttributeValue("class", "") == TEAMS_SELECTOR).ToList();
            var gameScores = score.Descendants("div")
                .Where(node => node.GetAttributeValue("class", "") == SCORE_SELECTOR).ToList();

            if (teams[0].InnerText != "49ers" && teams[1].InnerText != "49ers")
            {
                awayType = Type.GetType($"NFLPool.Model.{teams[0].InnerText}, NFLPool.Model");
                homeType = Type.GetType($"NFLPool.Model.{teams[1].InnerText}, NFLPool.Model");
            }
            else if (teams[0].InnerText == "49ers")
            {
                awayType = Type.GetType("NFLPool.Model._49ers, NFLPool.Model");
                homeType = Type.GetType($"NFLPool.Model.{teams[1].InnerText}, NFLPool.Model");
            }
            else
            {
                awayType = Type.GetType($"NFLPool.Model.{teams[0].InnerText}, NFLPool.Model");
                homeType = Type.GetType("NFLPool.Model._49ers, NFLPool.Model");
            }

            if (awayType is not null) awayTeam = Activator.CreateInstance(awayType) as Team;
            if (homeType is not null) homeTeam = Activator.CreateInstance(homeType) as Team;

            if (gameScores.Count > 0)
            {
                awayScore = gameScores[0].InnerText;
                homeScore = gameScores[1].InnerText;
            }

            gameResults.Add(new GameScore
            {
                GameScoreId = gameScoreId,
                AwayTeam = awayTeam,
                HomeTeam = homeTeam,
                AwayScore = !string.IsNullOrEmpty(awayScore) ? Convert.ToInt32(awayScore) : 0,
                HomeScore = !string.IsNullOrEmpty(homeScore) ? Convert.ToInt32(homeScore) : 0
            });

            gameScoreId += 1;
        });

        return gameResults;
    }
}