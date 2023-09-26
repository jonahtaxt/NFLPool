namespace NFLPool.Model;

public class CouchbaseDto<T> where T : class
{
    private readonly string? _collection;

    public CouchbaseDto()
    {
        _collection = null;
    }

    public CouchbaseDto(string collection)
    {
        _collection = collection;
    }

    public CouchbaseConfiguration CouchbaseConfiguration { get; set; }
    public string Bucket { get; set; }
    public string Scope { get; set; }

    public string Collection => string.IsNullOrEmpty(_collection) ? typeof(T).Name : _collection;

    public string Key { get; set; }
    public T? Document { get; set; }
}