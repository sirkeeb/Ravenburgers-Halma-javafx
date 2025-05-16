package be.kdg.integration2.mvpglobal.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the database connection and persistence operations for Halma games.
 * <p>
 * Establishes a JDBC connection to a PostgreSQL database, creates necessary tables,
 * and provides methods to insert and query users, games, and moves.
 * If the database is not available, most operations are bypassed.
 * </p>
 */
public class DatabaseConnections {
    private final String URL = "jdbc:postgresql://10.134.178.3:5432/game";
    private final String USER = "game";
    private final String PASSWORD = "7sur7";

    private Connection connection;
    /**
     * Indicates whether the database connection was successfully established.
     */
    public boolean dbAvailable = false;

    /**
     * Attempts to connect to the database using the configured URL, user, and password.
     * <p>
     * Sets {@link #dbAvailable} to true if successful; otherwise prints an error
     * and leaves {@code dbAvailable} as false.
     * </p>
     */
    public DatabaseConnections() {
        try {
            DriverManager.setLoginTimeout(5);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            dbAvailable = true;
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.out.println("Could not connect to database in 5 seconds: " + e.getMessage());
        }
    }

    /**
     * Returns the active JDBC connection.
     *
     * @return the {@link Connection} if {@code dbAvailable} is true; otherwise {@code null}
     */
    public Connection getConnection() {
        return dbAvailable ? connection : null;
    }

    /**
     * Closes the JDBC connection if it is open.
     * <p>
     * Ignores any SQLExceptions thrown during close.
     * </p>
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignore) {
            }
        }
    }

    /**
     * Creates the users, games, and moves tables if they do not already exist.
     * <p>
     * Executes three DDL statements to ensure the schema is in place.
     * If {@code dbAvailable} is false, this method returns without action.
     * </p>
     */
    public void createTables() {
        if (!dbAvailable) return;

        String createUsers = """
                    CREATE TABLE IF NOT EXISTS users (
                        user_id SERIAL PRIMARY KEY,
                        username VARCHAR(50) NOT NULL UNIQUE,
                        email    VARCHAR(50) NOT NULL UNIQUE
                    );
                """;
        String createGames = """
                    CREATE TABLE IF NOT EXISTS games (
                        game_id SERIAL PRIMARY KEY,
                        user_id INT NOT NULL,
                        is_win  BOOLEAN NOT NULL
                    );
                """;
        String createMoves = """
                    CREATE TABLE IF NOT EXISTS moves (
                        move_id  SERIAL PRIMARY KEY,
                        game_id  INT    NOT NULL,
                        duration DOUBLE PRECISION NOT NULL
                    );
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createUsers);
            stmt.executeUpdate(createGames);
            stmt.executeUpdate(createMoves);
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    /**
     * Inserts a new user record with the given username and email.
     * <p>
     * Uses an "ON CONFLICT DO NOTHING" clause to avoid duplicates.
     * If {@code dbAvailable} is false, this method returns without action.
     * </p>
     *
     * @param username the unique username of the player
     * @param email    the email address of the player
     */
    public void insertUser(String username, String email) {
        if (!dbAvailable) return;
        String sql = "INSERT INTO users(username, email) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("insertUser failed: " + e.getMessage());
        }
    }

    /**
     * Retrieves the user_id for a given username.
     * <p>
     * Returns 0 if the user is not found or if {@code dbAvailable} is false.
     * </p>
     *
     * @param username the username to search for
     * @return the corresponding user_id, or 0 if not found
     */
    public int getUsername(String username) {
        if (!dbAvailable) return 0;
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("getUsername failed: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Inserts a new game record associated with a user.
     * <p>
     * Records whether the game was won.
     * If {@code dbAvailable} is false, this method returns without action.
     * </p>
     *
     * @param userId the ID of the user who played
     * @param isWin  {@code true} if the user won; {@code false} otherwise
     */
    public void insertGame(int userId, boolean isWin) {
        if (!dbAvailable) return;
        String sql = "INSERT INTO games(user_id, is_win) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setBoolean(2, isWin);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("insertGame failed: " + e.getMessage());
        }
    }

    /**
     * Updates the most recent game record for a user to set its win status.
     * <p>
     * Finds the latest game by user_id and updates its is_win flag.
     * If {@code dbAvailable} is false, this method returns without action.
     * </p>
     *
     * @param userId the ID of the user whose latest game to update
     * @param isWin  the new win status to set
     */
    public void updateGame(int userId, boolean isWin) {
        if (!dbAvailable) return;
        String sql = """
                     UPDATE games
                     SET is_win = ?
                     WHERE user_id = (
                       SELECT user_id FROM games
                       WHERE user_id = ?
                       ORDER BY game_id DESC
                       LIMIT 1
                     )
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, isWin);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updateGame failed: " + e.getMessage());
        }
    }

    /**
     * Inserts a move record for a given game, recording its duration.
     * <p>
     * If {@code dbAvailable} is false or {@code gameId} &le; 0, this method returns without action.
     * </p>
     *
     * @param gameId   the ID of the game in which the move was made
     * @param duration the time taken to perform the move, in milliseconds
     */
    public void insertMove(int gameId, double duration) {
        if (!dbAvailable || gameId <= 0) return;
        String sql = "INSERT INTO moves(game_id, duration) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, gameId);
            stmt.setDouble(2, duration);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("insertMove failed: " + e.getMessage());
        }
    }

    /**
     * Retrieves the most recent game_id for a given user.
     * <p>
     * Returns 0 if no games are found or if {@code dbAvailable} is false.
     * </p>
     *
     * @param userId the ID of the user whose latest game to query
     * @return the latest game_id, or 0 if none exists
     */
    public int getLatestGameId(int userId) {
        if (!dbAvailable) return 0;
        String sql = "SELECT game_id FROM games WHERE user_id = ? ORDER BY game_id DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("getLatestGameId failed: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Retrieves detailed statistics for the most recent game played used for the stats screen
     * <p>
     * This includes:
     * <ul>
     *     <li>Total duration of the latest game</li>
     *     <li>Number of moves in the game</li>
     *     <li>Average move duration</li>
     *     <li>List of individual move durations (in order)</li>
     *     <li>The indices of outlier moves</li>
     * </ul>
     * Outliers are defined as moves whose duration differs from the global average
     * (across all games for the user) by more than two standard deviations.
     * </p>
     *
     * @param userId The ID of the user for whom to retrieve game statistics.
     * @return A map containing the following keys:
     * <ul>
     *     <li>{@code "winner"} (Boolean): always {@code true} (assumes user is human)</li>
     *     <li>{@code "totalTime"} (Double): total time spent in the latest game</li>
     *     <li>{@code "moveCount"} (Integer): number of moves in the latest game</li>
     *     <li>{@code "average"} (Double): average duration per move in the latest game</li>
     *     <li>{@code "durations"} (List&lt;Double&gt;): durations of all moves in the latest game</li>
     *     <li>{@code "outliers"} (List&lt;Integer&gt;): indices of moves in the latest game that are outliers</li>
     * </ul>
     */
    public Map<String, Object> getDetailedGameStats(int userId) {
        Map<String, Object> stats = new HashMap<>();
        if (!dbAvailable) return stats;

        try {
            int latestGameId = getLatestGameId(userId);

            // Get move durations for the latest game
            List<Double> currentDurations = new ArrayList<>();
            String currentMovesQuery = "SELECT duration FROM moves WHERE game_id = ? ORDER BY move_id ASC";
            try (PreparedStatement stmt = connection.prepareStatement(currentMovesQuery)) {
                stmt.setInt(1, latestGameId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    currentDurations.add(rs.getDouble("duration"));
                }
            }

            // Get all move durations for this user across all games
            List<Double> allDurations = new ArrayList<>();
            String allMovesQuery = """
                        SELECT m.duration
                        FROM moves m
                        JOIN games g ON m.game_id = g.game_id
                        WHERE g.user_id = ?
                    """;
            try (PreparedStatement stmt = connection.prepareStatement(allMovesQuery)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    allDurations.add(rs.getDouble("duration"));
                }
            }

            // Compute total time, average, and outliers
            double totalTime = currentDurations.stream().mapToDouble(Double::doubleValue).sum();
            double average = currentDurations.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            int moveCount = currentDurations.size();

            double globalMean = allDurations.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double stdDev = Math.sqrt(allDurations.stream()
                    .mapToDouble(d -> Math.pow(d - globalMean, 2)).average().orElse(0.0));

            List<Integer> outlierIndices = new ArrayList<>();
            for (int i = 0; i < currentDurations.size(); i++) {
                double d = currentDurations.get(i);
                if (Math.abs(d - globalMean) > 2 * stdDev) {
                    outlierIndices.add(i);
                }
            }

            stats.put("winner", true); // Always human if we're querying by userId
            stats.put("totalTime", totalTime);
            stats.put("moveCount", moveCount);
            stats.put("average", average);
            stats.put("durations", currentDurations);
            stats.put("outliers", outlierIndices);
        } catch (SQLException e) {
            System.out.println("getDetailedGameStats failed: " + e.getMessage());
        }

        return stats;
    }

    /**
     * Retrieves the username associated with a given user ID from the database.
     *
     * @param userId The unique identifier of the user.
     * @return The username corresponding to the provided user ID, or "Unknown" if no user is found
     * or a database error occurs.
     */
    public String getUsernameById(int userId) {
        String sql = "SELECT username FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("username");
        } catch (SQLException e) {
            System.out.println("getUsernameById failed: " + e.getMessage());
        }
        return "Unknown";
    }

}
