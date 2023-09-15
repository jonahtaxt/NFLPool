namespace NFLPool.Model
{
    public class PoolTeam
    {
        public PoolTeam()
        {
            Participants = new List<int>();
        }
        public string TeamName { get; set; }
        public List<int> Participants { get; set; }
    }
}