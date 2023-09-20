namespace NFLPool.Model;

public class PoolWeekScores
{
    public PoolWeekScores(List<Participant> participants, List<PoolTeam> teams)
    {
        Participants = participants;
        Teams = teams;
    }

    public List<Participant> Participants { get; }

    public List<PoolTeam> Teams { get; }
}