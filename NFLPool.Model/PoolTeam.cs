namespace NFLPool.Model
{
    public class PoolTeam
    {
        public PoolTeam()
        {
            TeamName = string.Empty;
            Participants = new List<int>();
        }
        public string TeamName { get; set; }
        public List<int> Participants { get; set; }
    }
}