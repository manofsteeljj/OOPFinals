package controllers;

import includes.BCrypt;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {
    @FXML
    private Label statusLabel;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button registerButton;

    @FXML
    private Button backToLoginButton;

    // JDBC connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/dormdb_sasa";
    private static final String DB_USER = "root"; // Replace with your DB username
    private static final String DB_PASSWORD = ""; // Replace with your DB password

    @FXML
    private void initialize() {
        // Action for Register button
        registerButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                statusLabel.setTextFill(Paint.valueOf("red"));
                statusLabel.setText("All fields are required.");
            } else if (!password.equals(confirmPassword)) {
                statusLabel.setTextFill(Paint.valueOf("red"));
                statusLabel.setText("Passwords do not matched.");
            } else {
                if (registerUser(username, password)) {
                    statusLabel.setTextFill(Paint.valueOf("green"));
                    statusLabel.setText("User registered successfully.");
                } else {
                    statusLabel.setTextFill(Paint.valueOf("red"));
                    statusLabel.setText("User registered failed.");
                }
            }
        });

        backToLoginButton.setOnAction(event -> {
            statusLabel.setTextFill(Paint.valueOf("green"));
            statusLabel.setText("Navigating back to Login.");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/Login.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) backToLoginButton.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Register");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Registers a new user in the database with hashed password.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return True if the user was registered successfully, false otherwise.
     */
    private boolean registerUser(String username, String password) {
        String insertUserQuery = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery)) {

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Set query parameters
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);

            // Execute the query
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }
}
