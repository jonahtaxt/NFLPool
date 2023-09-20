namespace NFLPool.Model
{
    public class WeekResults
    {
        public WeekResults()
        {
            GameScores = new List<GameScore>();
            Participants = new List<Participant>();
        }
        public List<GameScore> GameScores { get; set; }
        public List<Participant> Participants { get; set; }
    }
}