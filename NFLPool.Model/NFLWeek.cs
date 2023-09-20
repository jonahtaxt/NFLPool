namespace NFLPool.Model;

public class NFLWeek
{
    public int WeekId { get; set; }
    public int Year { get; set; }
    public DateTime StartDay { get; set; }
    public DateTime EndDay { get; set; }
}