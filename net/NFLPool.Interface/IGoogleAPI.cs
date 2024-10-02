namespace NFLPool.Interface;

public interface IGoogleAPI
{
    Task<Stream?> DownloadFile(string authPath, string fileName);
}