package com.example.harnessdemoapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
public class HelloController {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
    }

    // A simple endpoint that is vulnerable to SQL Injection
    @GetMapping("/hello")
    public String hello(@RequestParam String name) {
        String response = "Hello, " + name + "!";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // This is the vulnerable line
            stmt.executeUpdate("INSERT INTO users VALUES ('" + name + "')");
        } catch (SQLException e) {
            return "Error occurred: " + e.getMessage();
        }
        return response;
    }
}
