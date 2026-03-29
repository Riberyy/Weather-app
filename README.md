# 🌤️ Nimbus — Weather Application

A full-stack weather application built with **Java Spring Boot** (backend) and **HTML/CSS/JavaScript** (frontend), powered by the **Open-Meteo API** (no API key required!).

---

## 📁 Project Structure

```
weather-app/
├── backend/                    # Spring Boot application
│   ├── pom.xml
│   └── src/main/java/com/weather/app/
│       ├── WeatherApplication.java
│       ├── controller/WeatherController.java
│       ├── service/WeatherService.java
│       ├── model/WeatherResponse.java
│       └── config/AppConfig.java
├── frontend/                   # Static HTML/CSS/JS
│   ├── index.html
│   └── netlify.toml
├── render.yaml                 # Backend deployment config
└── README.md
```

---

## 🔧 Local Setup

### Prerequisites
- Java 17+
- Maven 3.8+
- Any modern browser

### 1. Run the Backend

```bash
cd backend
mvn clean package
java -jar target/weather-app-1.0.0.jar
```

Backend starts at: `http://localhost:8080`

Test endpoints:
```
GET http://localhost:8080/weather?city=Mumbai
GET http://localhost:8080/weather?lat=19.07&lon=72.87
GET http://localhost:8080/weather/health
```

### 2. Run the Frontend

Simply open `frontend/index.html` in a browser.

> ⚠️ **Geolocation** requires HTTPS in production. For local dev, `localhost` works fine.

---

## 🌐 Deployment

### Backend → Render (Free Tier)

1. Push this repo to GitHub.
2. Go to [render.com](https://render.com) → **New Web Service**
3. Connect your GitHub repo.
4. Settings:
   - **Environment**: Java
   - **Build Command**: `cd backend && mvn clean package -DskipTests`
   - **Start Command**: `java -jar backend/target/weather-app-1.0.0.jar`
   - **Health Check Path**: `/weather/health`
5. Deploy. Copy the generated URL (e.g., `https://your-app.onrender.com`)

### Backend → Railway (Alternative)

1. Go to [railway.app](https://railway.app) → **New Project → GitHub Repo**
2. Set:
   - Root directory: `backend`
   - Build command: `mvn clean package -DskipTests`
   - Start command: `java -jar target/weather-app-1.0.0.jar`
3. Railway auto-provides `$PORT` — Spring Boot auto-reads it.

### Frontend → Netlify

1. In `frontend/index.html`, update line:
   ```js
   const API_BASE = 'https://your-backend.onrender.com';
   ```
2. Go to [netlify.com](https://netlify.com) → **Add New Site → Import from Git**
3. Set:
   - **Base directory**: `frontend`
   - **Publish directory**: `frontend`
4. Deploy → HTTPS is enabled automatically ✅

---

## 📡 API Reference

| Endpoint | Description |
|----------|-------------|
| `GET /weather?city={city}` | Get weather by city name |
| `GET /weather?lat={lat}&lon={lon}` | Get weather by coordinates |
| `GET /weather/health` | Health check |

### Sample Response

```json
{
  "city": "Mumbai, India",
  "latitude": 19.0760,
  "longitude": 72.8777,
  "temperature": 32.4,
  "windspeed": 18.5,
  "weatherCode": 2,
  "weatherCondition": "Partly Cloudy",
  "weatherIcon": "⛅",
  "humidity": 75,
  "timezone": "Asia/Kolkata",
  "isDay": true
}
```

---

## 🗺️ External APIs Used

| API | Purpose | Docs |
|-----|---------|------|
| Open-Meteo Weather | Real-time weather by coordinates | [docs.open-meteo.com](https://open-meteo.com) |
| Open-Meteo Geocoding | City name → lat/lon | [geocoding-api.open-meteo.com](https://open-meteo.com/en/docs/geocoding-api) |
| Nominatim (OSM) | lat/lon → city name (reverse geocoding) | [nominatim.org](https://nominatim.org) |

---

## ✨ Features

- 🌍 **Geolocation** — one-click "My Location" button
- 🔍 **City Search** — search any city worldwide
- 🕐 **Recent Searches** — saved to localStorage, clickable chips
- 🌙 **Dark / Light Mode** — persists across sessions
- ⏰ **Live Clock** — real-time date and time in header
- 📊 **Weather Details** — temperature, humidity, wind speed, timezone
- 🗺️ **Map Link** — opens exact location in Google Maps
- 📱 **Fully Responsive** — works on mobile and desktop
- ⚡ **Loading Indicators** and error handling built-in

---

## 🧱 Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.2, WebFlux (WebClient) |
| Frontend | Vanilla HTML5, CSS3, JavaScript (ES2022) |
| Weather API | Open-Meteo (free, no key) |
| Geocoding | Open-Meteo + Nominatim |
| Fonts | Syne (display), DM Sans (body) |
| Deploy (BE) | Render or Railway |
| Deploy (FE) | Netlify |
