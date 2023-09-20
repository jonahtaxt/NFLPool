namespace NFLPool.Model
{
    public abstract class Team
    {
        public Team()
        {
            PoolName = string.Empty;
            Logo = string.Empty;
        }
        public string Name
        {
            get
            {
                return GetType().Name;
            }
            set
            {
                Name = value;
            }
        } 
        public string Logo { get; set; }
        public string PoolName { get; set; }
    }
}