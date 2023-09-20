FROM mcr.microsoft.com/dotnet/sdk:7.0 AS build-env
WORKDIR /app

COPY . ./
RUN dotnet restore

COPY . ./
RUN dotnet publish NFLPool.Api -c Release -o out

FROM mcr.microsoft.com/dotnet/aspnet:7.0
WORKDIR /app
COPY --from=build-env /app/out .
ENV ASPNETCORE_URLS=http://+:7000
EXPOSE 7000
ENTRYPOINT ["dotnet", "NFLPool.Api.dll"]