using NFLPool.Interface;
using NFLPool.Model;
using NFLPool.Service;

var builder = WebApplication.CreateBuilder(args);
builder.Logging.ClearProviders();
builder.Logging.AddConsole();

// Add services to the container.

builder.Services.AddControllers();

var couchbaseConfig = new CouchbaseConfiguration();
builder.Configuration.GetSection("Couchbase").Bind(couchbaseConfig);
builder.Services.AddSingleton(couchbaseConfig);

// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddScoped<INFLCrawler, NFLCrawler>();
builder.Services.AddScoped<IFileReader, FileReader>();
builder.Services.AddScoped<IGoogleAPI, GoogleAPI>();
builder.Services.AddScoped<IDataService, CouchbaseService>();
builder.Services.AddScoped<IPoolService, PoolService>();



builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAll", builder => builder
        .WithOrigins("http://localhost:5173",
            "http://192.168.50.125:4000",
            "http://localhost:4000",
            "https://quiniela.cloudsolo.net")
        .WithMethods("GET")
        .AllowAnyHeader());
});

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

// app.UseHttpsRedirection();

app.UseCors("AllowAll");

app.UseAuthorization();

app.MapControllers();

app.Logger.LogInformation("API Version 1.0.0");

app.Run();