namespace NFLPool.Model
{
    public class GameScore
    {
        public int GameScoreId { get; set; }
        public Team AwayTeam { get; set; }
        public Team HomeTeam { get; set; }
        public int AwayScore { get; set; }
        public int HomeScore { get; set; }

        public List<int> AwayParticipants { get; set; }
        public List<int> HomeParticipants { get; set; }

        public Team? WinningTeam
        {
            get
            {
                if (AwayScore > HomeScore)
                {
                    return AwayTeam;
                }
                else if (AwayScore < HomeScore)
                {
                    return HomeTeam;
                }
                else
                {
                    return null;
                }
            }
        }
    }
}