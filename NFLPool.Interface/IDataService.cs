using NFLPool.Model;

namespace NFLPool.Interface
{
    public interface IDataService
    {
        void SetDataConfiguration(DBConfiguration dBConfiguration);
        Task<List<NFLWeek>> GetNFLWeeksPerYear(int year);
    }
}