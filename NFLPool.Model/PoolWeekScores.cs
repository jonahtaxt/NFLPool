namespace NFLPool.Model
{
    public class PoolWeekScores
    {
        private List<Participant> _participants;
        private List<PoolTeam> _teams;

        public PoolWeekScores(List<Participant> participants, List<PoolTeam> teams)
        {
            _participants = participants;
            _teams = teams;
        }

        public List<Participant> Participants => _participants;
        public List<PoolTeam> Teams => _teams;
    }
}