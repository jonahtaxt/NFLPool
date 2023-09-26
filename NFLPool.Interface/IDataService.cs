using NFLPool.Model;

namespace NFLPool.Interface;

public interface IDataService
{
    Task Insert<T>(CouchbaseDto<T> couchbaseDto) where T : class;
    Task GetDocumentByKey<T>(CouchbaseDto<T> couchbaseDto) where T : class;
}