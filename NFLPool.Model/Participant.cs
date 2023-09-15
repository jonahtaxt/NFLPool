namespace NFLPool.Model
{
    public class Participant
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public int MondayNightPoints { get; set; }
        public int MondayNightPointsDifference { get; set; }
        public List<string> Bets { get; set; }
        public int TotalPoints { get; set; }
    }
}
