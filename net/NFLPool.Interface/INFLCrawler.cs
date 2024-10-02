using NFLPool.Model;

namespace NFLPool.Interface;

public interface INFLCrawler
{
    Task<List<GameScore>> GetWeekScoresAsync(int year, int week);
}