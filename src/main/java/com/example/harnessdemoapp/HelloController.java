package com.example.harnessdemoapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
public class HelloController {

    private static final String DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    
    @PostConstruct
    public void init() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE users (name VARCHAR(255))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/hello")
    public String hello(@RequestParam String name) {
        String response;
        String sql = "INSERT INTO users (name) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // FIX: Using PreparedStatement to safely set user input
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            response = "Hello, " + name + "! Your name was securely inserted.";
        } catch (SQLException e) {
            response = "Error occurred: " + e.getMessage();
        }
        return response;
    }
    
    // An endpoint to verify the table content after a successful insertion
    @GetMapping("/users")
    public String getUsers() {
        StringBuilder response = new StringBuilder("Users in database: ");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            var rs = stmt.executeQuery("SELECT name FROM users");
            while (rs.next()) {
                response.append(rs.getString("name")).append(", ");
            }
        } catch (SQLException e) {
            return "Error retrieving users: " + e.getMessage();
        }
        return response.toString();
    }
}
