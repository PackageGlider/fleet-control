# Fleet Control - Drone Fleet Dashboard

Android application for real-time drone fleet monitoring and supervision.

## Overview

Fleet Control provides a unified dashboard for monitoring multiple drones in real-time. The app aggregates data from two backend services:

- **Healthcheck API (HC)**: Live telemetry and status data
- **Monitor API (MONS)**: Historical data and evaluations

## Features

### Current
- Real-time drone status display
- Live telemetry data from Healthcheck API
- Backend environment switching (dev/prod)

### Planned
- Historical data visualization from Monitor API
- Fleet-wide statistics and analytics
- Alert history and notifications
- Drone detail views with telemetry graphs

## Backend APIs

### Development Environment
- Healthcheck API: `https://ping.uphi.cc`
  - Status endpoint: `https://ping.uphi.cc/status`
  - OpenAPI spec: `https://ping.uphi.cc/openapi.json`
- Monitor API: `https://monitor.uphi.cc`
  - OpenAPI spec: `https://monitor.uphi.cc/openapi.json`

### Production Environment
- Healthcheck API: `https://ping.uphi.ch`
- Monitor API: `https://monitor.uphi.ch`

## Architecture

- **MVVM Pattern**: Separation of concerns with ViewModel and LiveData
- **Repository Pattern**: Single source of truth for data
- **Retrofit**: HTTP client for API communication
- **Coroutines**: Asynchronous programming
- **Material Design 3**: Modern Android UI

## Tech Stack

- Kotlin
- Android SDK (API 26+)
- Retrofit + OkHttp
- Gson
- Kotlin Coroutines
- AndroidX Lifecycle
- Material Design 3

## Development

### Requirements
- Android Studio Hedgehog or later
- JDK 17
- Android SDK 34

### Building
```bash
./gradlew assembleDebug
```

### Installing
```bash
./gradlew installDebug
```

## Project Status

🚧 **Work in Progress** - Initial development phase

Core functionality is being implemented. See issues and project board for current status.

## Related Projects

- **drone-alert**: Push notification app for immediate drone alerts
- **nebula-healthcheck-service**: Backend service providing live telemetry
- **monitor-service**: Data evaluation and analytics backend

---

**To be continued** - More features and documentation coming soon.
