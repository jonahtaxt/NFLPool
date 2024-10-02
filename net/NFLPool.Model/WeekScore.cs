namespace NFLPool.Model;

public class WeekScore
{
    public WeekScore()
    {
        GameScores = new List<GameScore>();
    }

    public List<GameScore> GameScores { get; }
}