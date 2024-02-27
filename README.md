# Game of Three

## Requirements

- JDK 21
- Spring Boot 3.2.3
- Docker (for test environment)
- H2 database (Already exists)

## Getting Started

Follow these steps to run the code using Gradle and Spring Boot:

1. **Clone the repository:**

    ```bash
    git clone <repository-url>
    ```

2. **Navigate to the project directory:**

    ```bash
    cd game-of-three
    ```

3. **Build the project using Gradle:**

    ```bash
    ./gradlew build
    ```

4. **Run the Spring Boot application:**

    ```bash
    ./gradlew bootRun
    ```
5. **Run the Spring Boot test:**

    ```bash
    ./gradlew test 
    ```
   
6. **Access Swagger UI for API documentation:**

   Once the application is running, you can access Swagger UI at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) for detailed API documentation.

## Game Configuration Properties

### `game.config.mod`

This property defines the modulus value used in the game logic. The game logic checks if a move is valid based on the remainder when added to the current number. Adjusting this value can impact the game rules.

Example configuration in application.yml:
```yaml
game.config.mod=3
```

### `game.config.startingNumberRange`
This property determines the range for generating the starting number in the game. The starting number is randomly chosen within this range at the beginning of each game.

Example configuration in application.yml:
```yaml
game.config.startingNumberRange=100000
```


## Application Design and Architecture

### Design Approach

The Game of Three application is designed to simulate a two-player game where each player makes moves that affect the game's state. The application follows a Domain-Driven Design (DDD) approach and incorporates Event-Driven Architecture (EDA) for handling game events.

### Classes and Modules

1. **Controllers**
    - `GameController`: Manages REST endpoints related to the game, such as creating a game, joining a game, making moves, and retrieving game information.
    - `PlayerController`: Handles player-related endpoints, including creating players.

2. **Services**
    - `GameServiceImpl`: Implements the business logic for game-related operations, such as creating a game, joining a game, making moves, and ending a game.
    - `PlayerServiceImpl`: Manages player-related operations, like creating, updating, and retrieving players.

3. **Repositories**
    - `GameRepository`: Provides CRUD operations for the `Game` entity.
    - `PlayerRepository`: Offers CRUD operations for the `Player` entity.

4. **Entities**
    - `Game`: Represents a game instance with player information, starting and current numbers, game state, and more.
    - `Player`: Represents a player with a name, availability status, and player type (manual or automatic).

5. **Strategies and Factories**
    - `MoveStrategy`, `ManualMoveStrategy`, `RandomMoveStrategy`: Implement strategies for generating moves, allowing for different player types.
    - `MoveStrategyFactory`: Creates the appropriate move strategy based on the player type.

6. **Event Handling**
    - `GameEventPublisher`: Publishes various game events, such as move made, game update, game ended, and save game events.
    - `GameEventListener`: Handles events related to game updates and moves, triggering further actions.

7. **Event Objects**
    - `GameCreatedEvent`, `GameEndedEvent`, `GameSaveEvent`, `GameUpdateEvent`, `MoveMadeEvent`: Represent different events in the application.

8. **Application Entry Point**
    - `GotApplication`: Main class with the `main` method to start the Spring Boot application.

### Docker Test Environment

For testing purposes, the application provides a Dockerized environment using TestContainers. This allows for integration testing with external dependencies, ensuring a more robust and reliable test suite.

## Future Improvements

1. **User Authentication and Authorization:**
   Implement user authentication and authorization mechanisms to secure API endpoints, ensuring that only authorized users can perform certain actions.

2. **Game Statistics and Analytics:**
   Introduce a feature to collect and display game statistics and analytics. This could include win/loss ratios, average game duration, and other relevant metrics.

3. **Real-time Updates:**
   Enhance the user experience with real-time updates for game events. Utilize WebSocket or Server-Sent Events (SSE) to push updates to clients as they occur.

4. **UI for Monitoring and Administration:**
   Develop a user interface for monitoring and administration, allowing administrators to view ongoing games, manage users, and perform other administrative tasks.

5. **Caching Mechanism:**
   Implement a caching mechanism to store frequently accessed data, such as player profiles or game state, improving overall application performance.

6. **Internationalization (i18n) Support:**
   Add internationalization support to make the application accessible to users from different regions by providing multilingual support.

7. **Integration with External APIs:**
   Explore opportunities to integrate the game with external APIs or services for additional functionalities, such as fetching real-time weather data to influence the game.
