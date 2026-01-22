# NFL Pool - La Quiniela

A full-stack NFL prediction pool application that allows participants to make weekly game predictions, tracks results, and calculates rankings with a Monday Night bonus scoring mechanism.

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
  - [Database Setup](#database-setup)
- [Docker Deployment](#docker-deployment)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Configuration](#configuration)
- [Project Structure](#project-structure)

## Features

- **Live Score Retrieval** - Automatically scrapes NFL game scores from ESPN
- **Excel Import** - Upload participant predictions from Excel files (.xlsx)
- **Score Calculation** - Automatically tallies correct predictions per participant
- **Monday Night Bonus** - Special scoring based on total points differential prediction
- **Rankings** - Displays participants sorted by total points with tiebreaker rules
- **Participant Details** - Modal view showing individual stats and predictions
- **Historical Data** - Stores and retrieves results by year and week
- **Responsive UI** - Material-UI based interface with team logos

## Architecture

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│                 │     │                 │     │                 │
│   React/Vite    │────▶│  Spring Boot    │────▶│    Couchbase    │
│   (Frontend)    │     │   (REST API)    │     │   (Database)    │
│                 │     │                 │     │                 │
└─────────────────┘     └────────┬────────┘     └─────────────────┘
      :4000                  :8083│
                                  │
                                  ▼
                         ┌─────────────────┐
                         │                 │
                         │   ESPN.com      │
                         │  (Web Scraping) │
                         │                 │
                         └─────────────────┘
```

## Tech Stack

### Backend
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Runtime |
| Spring Boot | 3.3.4 | Application framework |
| Spring Security | 3.2.8 | Authentication |
| Couchbase SDK | 3.6.3 | Database client |
| JSoup | 1.18.1 | HTML parsing/web scraping |
| Apache POI | 5.3.0 | Excel file processing |
| Maven | - | Build tool |

### Frontend
| Technology | Version | Purpose |
|------------|---------|---------|
| React | 18.2.0 | UI framework |
| Vite | 4.4.5 | Build tool & dev server |
| Material-UI | 5.14.10 | Component library |
| Axios | 1.5.0 | HTTP client |

### Infrastructure
| Technology | Version | Purpose |
|------------|---------|---------|
| Docker | - | Containerization |
| Docker Compose | - | Orchestration |
| Nginx | Alpine | Frontend web server |
| Couchbase | 7.2.1 | NoSQL database |

## Prerequisites

- **Java 21** or higher (Amazon Corretto recommended)
- **Maven 3.8+**
- **Node.js 18+** and npm
- **Docker** and **Docker Compose** (for containerized deployment)
- **Couchbase Server 7.x** (if running locally without Docker)

## Getting Started

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd NFLPool
   ```

2. **Configure Couchbase connection**

   Create a file at `/misc/couchbase.properties` (or your preferred location):
   ```properties
   couchbase.connectionstring=couchbase://localhost
   couchbase.username=your_username
   couchbase.password=your_password
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   java -jar target/nflpool-0.0.1-SNAPSHOT.jar
   ```

   The API will be available at `http://localhost:8080`

### Frontend Setup

1. **Navigate to the frontend directory**
   ```bash
   cd nflpoolsite
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure the API URL**

   Create a `.env` file:
   ```env
   VITE_API_URL=http://localhost:8080
   ```

4. **Start development server**
   ```bash
   npm run dev
   ```

   The application will be available at `http://localhost:5173`

5. **Build for production**
   ```bash
   npm run build
   ```

### Database Setup

1. **Create a Couchbase bucket** named `NFLPool-bucket`

2. **Create a scope** named `WeeklyResults-scope`

3. **Create a collection** named `ParticipantWeekData`

## Docker Deployment

### Using Docker Compose (Recommended)

1. **Configure external properties**

   Ensure your Couchbase properties file exists at the path specified in `compose.yml`

2. **Build and start all services**
   ```bash
   docker compose up -d
   ```

3. **Access the application**
   - Frontend: `http://localhost:4000`
   - Backend API: `http://localhost:8083`
   - Couchbase Console: `http://localhost:8091`

### Manual Docker Deployment

**Backend:**
```bash
# Build
docker build -t nflpoolapi:1.0.0 .

# Run
docker run -p 8080:8080 \
  -v /path/to/couchbase.properties:/misc/couchbase.properties \
  --name nflpoolapi \
  nflpoolapi:1.0.0
```

**Frontend:**
```bash
# Build
cd nflpoolsite
docker build -t nflpoolsite:1.0.1 .

# Run
docker run -p 4000:4000 --name nflpoolsite nflpoolsite:1.0.1
```

### Docker Network Configuration

The compose file creates a custom network (`nfl_pool_proxy`) with the following IP assignments:

| Service | IP Address | Port |
|---------|------------|------|
| pooldb (Couchbase) | 192.168.80.100 | 8091-8096, 11207-11211 |
| poolapi (Spring Boot) | 192.168.80.101 | 8083 |
| poolsite (Nginx) | 192.168.80.102 | 4000 |

## API Documentation

### Base URL
```
/scores
```

### Endpoints

#### Get Week Results
Retrieves game scores and participant predictions for a specific week.

```http
GET /scores/{year}/{week}
```

**Parameters:**
| Name | Type | Description |
|------|------|-------------|
| year | int | Season year (e.g., 2024) |
| week | int | Week number (1-18) |

**Response:**
```json
{
  "gameScores": [
    {
      "gameScoreId": 1,
      "homeTeam": { "poolName": "Chiefs", "logo": "base64..." },
      "awayTeam": { "poolName": "Ravens", "logo": "base64..." },
      "homeScore": 27,
      "awayScore": 20,
      "winningTeam": "Chiefs"
    }
  ],
  "participants": [
    {
      "id": 1,
      "name": "John Doe",
      "bets": ["Chiefs", "Cowboys", "Bills"],
      "totalPoints": 10,
      "mondayNightPoints": 45,
      "mondayNightPointsDifference": 3,
      "poolPosition": 1
    }
  ],
  "participantWinnerId": 1
}
```

**Authentication:** None (public endpoint)

---

#### Upload Participant Predictions
Uploads an Excel file containing participant predictions.

```http
POST /scores
```

**Parameters:**
| Name | Type | Description |
|------|------|-------------|
| file | MultipartFile | Excel file (.xlsx) with predictions |
| year | int | Season year |
| week | int | Week number |

**Authentication:** HTTP Basic Auth required

**Response:** Same format as GET endpoint

## Database Schema

### Document Structure

Documents are stored in Couchbase with IDs formatted as `{year}.{week}` (e.g., `2024.1`).

#### ParticipantWeekData
```json
{
  "participants": [
    {
      "id": 1,
      "name": "Participant Name",
      "bets": ["Team1", "Team2", "..."],
      "mondayNightPoints": 45,
      "mondayNightPointsDifference": 0,
      "totalPoints": 0,
      "poolPosition": 0
    }
  ],
  "mondayNightTeams": ["HomeTeam", "AwayTeam"]
}
```

### Supported Teams

All 32 NFL teams are supported with their pool names:
- AFC East: Bills, Dolphins, Patriots, Jets
- AFC North: Ravens, Bengals, Browns, Steelers
- AFC South: Texans, Colts, Jaguars, Titans
- AFC West: Broncos, Chiefs, Raiders, Chargers
- NFC East: Cowboys, Giants, Eagles, Commanders
- NFC North: Bears, Lions, Packers, Vikings
- NFC South: Falcons, Panthers, Saints, Buccaneers
- NFC West: Cardinals, Rams, 49ers, Seahawks

## Configuration

### Backend Configuration

**application.properties:**
```properties
spring.application.name=NFLPool
spring.config.import=optional:file:/misc/couchbase.properties
```

**couchbase.properties:**
```properties
couchbase.connectionstring=couchbase://localhost
couchbase.username=admin
couchbase.password=password
```

**CORS Configuration:**

Configure allowed origins via the property:
```properties
com.effisoft.nflpool.cors.allowedorigins=http://localhost:5173,http://localhost:4000
```

### Frontend Configuration

**.env:**
```env
VITE_API_URL=http://localhost:8080
```

## Project Structure

```
NFLPool/
├── src/
│   └── main/
│       ├── java/com/effisoft/nflpool/
│       │   ├── configuration/
│       │   │   ├── CouchbaseConfig.java      # Database configuration
│       │   │   └── WebSecurityConfig.java    # Security & CORS
│       │   ├── controllers/
│       │   │   └── ScoresController.java     # REST endpoints
│       │   ├── interfaces/
│       │   │   ├── NFLCrawler.java           # Web scraping interface
│       │   │   └── ParticipantService.java   # Participant service interface
│       │   ├── model/
│       │   │   ├── teams/                    # 32 NFL team classes
│       │   │   ├── GameScore.java
│       │   │   ├── Participant.java
│       │   │   ├── ParticipantWeekData.java
│       │   │   ├── Team.java
│       │   │   └── WeekResults.java
│       │   ├── services/
│       │   │   ├── NFLCrawlerService.java    # ESPN scraping logic
│       │   │   └── ParticipantServiceImpl.java
│       │   └── NflpoolApplication.java       # Main entry point
│       └── resources/
│           └── application.properties
├── nflpoolsite/
│   ├── public/
│   │   └── football-animated.gif
│   ├── src/
│   │   ├── App.css                           # Application styles
│   │   ├── App.jsx                           # Main React component
│   │   ├── index.css                         # Global styles
│   │   └── main.jsx                          # React entry point
│   ├── Dockerfile                            # Frontend container
│   ├── nginx.conf                            # Nginx configuration
│   ├── package.json                          # NPM dependencies
│   └── vite.config.js                        # Vite configuration
├── compose.yml                               # Docker Compose orchestration
├── Dockerfile                                # Backend container
├── pom.xml                                   # Maven configuration
└── README.md
```

## Scoring Rules

1. **Points** - 1 point for each correct game prediction
2. **Monday Night Tiebreaker** - Participants predict total points scored in the Monday Night game
3. **Ranking Order:**
   - Total points (descending)
   - Monday Night point difference (ascending - closer is better)
   - Submission ID (ascending - earlier submission wins)

## Excel File Format

The system expects predictions in an Excel file (.xlsx) with the following structure on Sheet 3 (index 2):

- Column 1: Participant name
- Columns 2-N: Team predictions for each game
- Last column: Monday Night total points prediction

---

**Built with by Effisoft**
