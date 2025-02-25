# MovieHive 🎬

![App Logo](https://via.placeholder.com/150) <!-- You can replace this with your app logo -->

MovieHive is a comprehensive movie discovery application that provides quick access to detailed information about nearly all films worldwide. The application offers an intuitive user interface for browsing films by category, popularity, ratings, and current theatrical releases.

## Features ✨

- **Extensive Movie Catalog**: Access to a vast database of films from all over the world
- **Categorized Browsing**: Browse movies organized by genre, recent popularity, all-time ratings, and currently playing in theaters
- **Intuitive Search**: Find specific movies quickly through the search functionality
- **Offline Watchlist**: Save movies to your watchlist for future reference, accessible without an internet connection
- **Detailed Information**: View comprehensive details about each movie, including synopsis, cast, release date, and ratings
- **Trailer Integration**: Watch official trailers directly through YouTube integration
- **User-Friendly Interface**: Smooth navigation with a visually appealing design

## Screenshots 📱

->

## Architecture 🏗️

MovieHive is built with modern Android development practices:

- **MVVM Architecture**: Ensures separation of concerns and maintainable codebase
- **Repository Pattern**: Abstracts data sources and provides a clean API for the ViewModel
- **Single Activity Pattern**: Uses Navigation Component for fragment management
- **Reactive UI Updates**: Leverages Kotlin Flows for reactive programming

## Technologies Used 💻

- **Kotlin**: Primary programming language
- **Jetpack Components**:
- **ViewModel**: Manages UI-related data in a lifecycle-conscious way
- **LiveData & Flow**: Build data objects that notify views when underlying data changes
- **Room**: Provides abstraction layer over SQLite for offline data persistence
- **Navigation Component**: Handles fragment transactions and deep linking
- **Hilt**: Dependency injection library to manage dependencies
- **Retrofit**: Type-safe HTTP client for API communication
- **Glide**: Image loading and caching library
- **TMDB API**: Provides movie data and metadata
- **Coroutines**: For asynchronous programming
- **ViewBinding**: For type-safe view access

## Implementation Details 🔧

### Clean Architecture

MovieHive follows clean architecture principles with clearly defined layers:

- **Presentation Layer**: Contains UI components (Fragments) and ViewModels
- **Domain Layer**: Contains business models and use cases
- **Data Layer**: Manages data retrieval from API and local database

### Offline Support

The app implements a robust offline caching strategy:
- Movies browsed and marked as favorites are stored locally
- Room database serves as the single source of truth
- Repository pattern handles the data source selection based on connectivity

### Responsive UI

- RecyclerView with efficient adapters for smooth scrolling
- Optimized layouts for different screen sizes
- Transition animations for enhanced user experience


## Setup Instructions 🚀

1. Clone the repository:
   ```bash
   git clone https://github.com/ali-aktas/MovieHive.git
   ```

2. Open the project in Android Studio

3. Add your TMDB API Key in the 'local.properties' or 'gradle.properties' file:
   ```
   tmdb_api_key=YOUR_API_KEY
   ```

4. Build and run the application on your device or emulator


## Future Enhancements 🔮
- Integration with additional movie data sources
- User authentication and cloud synchronization of watchlists
- Personalized movie recommendations based on viewing history
- Advanced filtering and sorting options
- Social features to share and discuss movies with friends
- Jetpack Compose migration for modern UI implementation

## API Reference 📚
This application uses the [TMDB API](https://www.themoviedb.org/documentation/api) to fetch movie data.

## Contact 📧
Ali Aktas - [GitHub](https://github.com/ali-aktas)

## Acknowledgments 🙏
- [TMDB](https://www.themoviedb.org/) for providing the comprehensive movie database API
- All the open-source libraries used in this project
- Android developer community for continuous inspiration and resources