using Couchbase;
using Couchbase.KeyValue;
using NFLPool.Interface;
using NFLPool.Model;

namespace NFLPool.Service;

public class CouchbaseService : IDataService
{
    private IBucket? _bucket;
    private ICluster? _cluster;
    private ICouchbaseCollection? _collection;
    private IScope? _scope;

    public async Task Insert<T>(CouchbaseDto<T> couchbaseDto) where T : class
    {
        await ConnectToDb(couchbaseDto);
        await _collection?.UpsertAsync(couchbaseDto.Key, couchbaseDto.Document)!;
    }

    public async Task GetDocumentByKey<T>(CouchbaseDto<T> couchbaseDto) where T : class
    {
        await ConnectToDb(couchbaseDto);

        if (_collection == null) throw new Exception("Couchbase collection is null");

        var result = await _collection.GetAsync(couchbaseDto.Key);
        couchbaseDto.Document = result.ContentAs<T>();
    }

    private async Task ConnectToDb<T>(CouchbaseDto<T> couchbaseDto) where T : class
    {
        _cluster = await Cluster.ConnectAsync(couchbaseDto.CouchbaseConfiguration.CouchbaseIp,
            couchbaseDto.CouchbaseConfiguration.UserName, couchbaseDto.CouchbaseConfiguration.Password);
        await _cluster.WaitUntilReadyAsync(TimeSpan.FromMinutes(1));
        _bucket = await _cluster.BucketAsync(couchbaseDto.Bucket);
        _scope = await _bucket.ScopeAsync(couchbaseDto.Scope);
        _collection = await _scope.CollectionAsync(couchbaseDto.Collection);
    }
}