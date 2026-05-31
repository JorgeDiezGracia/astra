package com.svalero.astra.managers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreManager {

    private static ScoreManager instance;
    private Connection connection;

    private static final String DB_URL = "jdbc:sqlite:astra.db";

    private ScoreManager() {
        initDatabase();
    }

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    private void initDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            Statement stmt = connection.createStatement();
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS scores (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "score INTEGER NOT NULL," +
                    "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")"
            );
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public void saveScore(String name, int score) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO scores (name, score) VALUES (?, ?)"
            );
            ps.setString(1, name);
            ps.setInt(2, score);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Error saving score: " + e.getMessage());
        }
    }

    public List<ScoreEntry> getTopScores(int limit) {
        List<ScoreEntry> scores = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(
                "SELECT name, score FROM scores ORDER BY score DESC LIMIT ?"
            );
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                scores.add(new ScoreEntry(rs.getString("name"), rs.getInt("score")));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Error getting scores: " + e.getMessage());
        }
        return scores;
    }

    public void dispose() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }

    // Clase interna para representar una puntuación
    public static class ScoreEntry {
        public String name;
        public int score;

        public ScoreEntry(String name, int score) {
            this.name  = name;
            this.score = score;
        }
    }
}
