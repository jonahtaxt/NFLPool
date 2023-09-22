FROM mcr.microsoft.com/dotnet/sdk:7.0 AS build-env
WORKDIR /app

COPY *.sln .
COPY NFLPool.Api/*.csproj ./NFLPool.Api/
COPY NFLPool.Interface/*.csproj ./NFLPool.Interface/
COPY NFLPool.Model/*.csproj ./NFLPool.Model/
COPY NFLPool.Service/*.csproj ./NFLPool.Service/
RUN dotnet restore

COPY NFLPool.Api/. ./NFLPool.Api/
COPY NFLPool.Interface/. ./NFLPool.Interface/
COPY NFLPool.Model/. ./NFLPool.Model/
COPY NFLPool.Service/. ./NFLPool.Service/
RUN rm -r NFLPool.Api/bin
RUN rm -r NFLPool.Api/obj
RUN rm -r NFLPool.Interface/bin
RUN rm -r NFLPool.Interface/obj
RUN rm -r NFLPool.Model/bin
RUN rm -r NFLPool.Model/obj
RUN rm -r NFLPool.Service/bin
RUN rm -r NFLPool.Service/obj
WORKDIR /app/NFLPool.Api
RUN dotnet publish -c Release -o out
WORKDIR /app
COPY NFLPool.Api/out ./out/
RUN rm -r NFLPool.Api
RUN rm -r NFLPool.Interface
RUN rm -r NFLPool.Model
RUN rm -r NFLPool.Service
RUN rm *.sln

FROM mcr.microsoft.com/dotnet/aspnet:7.0
WORKDIR /app
COPY --from=build-env /app/out ./
ENV ASPNETCORE_URLS=http://+:7000
EXPOSE 7000
ENTRYPOINT ["dotnet", "NFLPool.Api.dll"]