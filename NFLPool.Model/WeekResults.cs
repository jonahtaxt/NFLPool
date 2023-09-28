namespace NFLPool.Model;

public class WeekResults
{
    public int Id { get; set; }
    public List<GameScore> GameScores { get; set; } = new();
    public List<Participant>? Participants { get; set; } = new();
    public int? ParticipantWinnerId { get; set; } = null;
}