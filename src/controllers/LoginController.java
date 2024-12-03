package controllers;

import includes.BCrypt;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    @FXML
    private Label usernameChecker;
    @FXML
    private Label passwordChecker;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink signUpLink;

    // JDBC connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/dormdb_sasa";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private void initialize() {
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty()) {
                usernameChecker.setTextFill(Paint.valueOf("red"));
                usernameChecker.setText("Username is required!");
                return;
            } else {
                usernameChecker.setText("");
            }

            if (password.isEmpty()) {
                passwordChecker.setTextFill(Paint.valueOf("red"));
                passwordChecker.setText("Password is required!");
                return;
            } else {
                passwordChecker.setText("");

            }

            if (validateCredentials(username, password)) {
                System.out.println("Login successful!");
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/Dashboard.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) loginButton.getScene().getWindow();

                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.setTitle("Dashboard");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Invalid username or password.");
            }
        });

        signUpLink.setOnAction(event -> {
            System.out.println("Sign Up Now clicked.");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/Register.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) signUpLink.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Register");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private boolean validateCredentials(String username, String password) {
        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");

                // converting sql hashed password to bcrypt java
                if (hashedPassword.startsWith("$2y$")) {
                    hashedPassword = hashedPassword.replaceFirst("\\$2y\\$", "\\$2a\\$");
                }

                return BCrypt.checkpw(password, hashedPassword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
