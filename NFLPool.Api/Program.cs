using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Options;
using NFLPool.Interface;
using NFLPool.Model;
using NFLPool.Service;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddScoped<INFLCrawler, NFLCrawler>();
builder.Services.AddScoped<IFileReader, FileReader>();
builder.Services.AddScoped<IGoogleAPI, GoogleAPI>();
builder.Services.AddScoped<IPoolService, PoolService>();
builder.Services.Configure<DBConfiguration>(builder.Configuration.GetSection("MongoDB"));
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAll", builder => builder.AllowAnyOrigin().AllowAnyMethod().AllowAnyHeader());
});

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
    app.UseCors("AllowAll");
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
