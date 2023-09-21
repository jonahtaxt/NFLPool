namespace NFLPool.Model;

public class WeekResults
{
    public List<GameScore> GameScores { get; set; } = new();
    public List<Participant> Participants { get; set; } = new();
    public Participant? Winner { get; set; } = null;
}