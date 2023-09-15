using Google.Apis.Auth.OAuth2;
using Google.Apis.Drive.v3;
using Google.Apis.Services;
using NFLPool.Interface;

namespace NFLPool.Service
{
    public class GoogleAPI : IGoogleAPI
    {
        public async Task<Stream?> DownloadFile(string fileName)
        {
            var gCredential = GoogleCredential
                .FromFile(@"C:\temp\nflpool-397315-afa7e689e1ba.json")
                .CreateScoped(DriveService.Scope.Drive);

            var gService = new DriveService(new BaseClientService.Initializer
            {
                HttpClientInitializer = gCredential,
                ApplicationName = "NFL Pool"
            });

            var listRequest = gService.Files.List();
            listRequest.Q = $"name = '{fileName}' and trashed=false";
            var files = await listRequest.ExecuteAsync();
            var fileInfo = files.Files.FirstOrDefault();
            if (fileInfo != null)
            {
                var fileRequest = gService.Files.Get(fileInfo.Id);
                var stream = new MemoryStream();
                fileRequest.Download(stream);
                return stream;
            }
            return null;
        }
    }
}