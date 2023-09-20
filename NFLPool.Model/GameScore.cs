namespace NFLPool.Model
{
    public class GameScore
    {
        public int GameScoreId { get; set; }
        public Team? AwayTeam { get; set; }
        public Team? HomeTeam { get; set; }
        public int AwayScore { get; set; }
        public int HomeScore { get; set; }

        public string? WinningTeam
        {
            get
            {
                if (AwayScore > HomeScore)
                {
                    return AwayTeam?.PoolName;
                }
                else if (AwayScore < HomeScore)
                {
                    return HomeTeam?.PoolName;
                }
                else
                {
                    return null;
                }
            }
        }
    }
}