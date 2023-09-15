namespace NFLPool.Model
{
    public class WeekScore
    {
        private readonly List<GameScore> _scores;
        public WeekScore()
        {
            _scores = new List<GameScore>();
        }
        public List<GameScore> GameScores => _scores;
    }
}