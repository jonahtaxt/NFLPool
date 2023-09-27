namespace NFLPool.Model;

public class CouchbaseDto<T> where T : class
{
    private readonly string? _collection;

    public CouchbaseDto(CouchbaseConfiguration couchbaseConfiguration, string bucket, string scope, string key,
        T? document)
    {
        _collection = null;
        Bucket = bucket;
        Scope = scope;
        Key = key;
        Document = document;
        CouchbaseConfiguration = couchbaseConfiguration;
    }

    public CouchbaseDto(CouchbaseConfiguration couchbaseConfiguration, string bucket, string scope, string key,
        T? document, string collection)
    {
        _collection = collection;
        Bucket = bucket;
        Scope = scope;
        Key = key;
        Document = document;
        CouchbaseConfiguration = couchbaseConfiguration;
    }

    public CouchbaseConfiguration CouchbaseConfiguration { get; init; }
    public string Bucket { get; }
    public string Scope { get; }
    public string Collection => string.IsNullOrEmpty(_collection) ? typeof(T).Name : _collection;
    public string Key { get; set; }
    public T? Document { get; set; }
}