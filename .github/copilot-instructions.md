# Fleet Control - Drone Fleet Monitoring Dashboard

## Project Overview
Android Kotlin app for real-time drone fleet monitoring and status dashboard.

## Architecture
- **MVVM Pattern**: ViewModel + LiveData for reactive UI
- **Repository Pattern**: Centralized data management
- **Retrofit + Gson**: REST API communication
- **Kotlin Coroutines**: Async operations
- **Material Design 3**: Modern Android UI

## Backend APIs
- **Healthcheck API**: https://ping.uphi.cc (dev), https://ping.uphi.ch (prod)
- **Monitor API**: https://monitor.uphi.cc (dev), https://monitor.uphi.ch (prod)

## Key Features
- Real-time drone fleet status dashboard
- Shows drones with last_seen timestamp
- Settings for dev/prod backend switching
- Drone list with RecyclerView
- Pull-to-refresh for updates
- Offline-first with caching

## Development Guidelines
- Use Kotlin idioms and best practices
- Follow Material Design guidelines
- Implement proper error handling
- Use sealed classes for API responses
- Keep UI responsive with coroutines
- Store settings in SharedPreferences
