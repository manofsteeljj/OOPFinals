package controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.IOException;

public class AddRoomController {
    @FXML
    private Button roomManage; // sidebar

    @FXML
    private TextField roomNumberField;

    @FXML
    private ComboBox<String> roomTypeComboBox;

    @FXML
    private TextField totalSlotsField;

    @FXML
    private Button addRoomButton;

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/dormdb_sasa";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @FXML
    private void initialize() {
        roomManage.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/Dashboard.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) roomManage.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Dashboard");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        addRoomButton.setOnAction(actionEvent -> {
            String roomNumber = roomNumberField.getText();
            String roomType = roomTypeComboBox.getValue();
            String totalSlotsText = totalSlotsField.getText();

            if (roomNumber.isEmpty() || roomType == null || totalSlotsText.isEmpty()) {
                showAlert("Error", "Please fill all fields.");
                return;
            }

            int totalSlots;
            try {
                totalSlots = Integer.parseInt(totalSlotsText);
            } catch (NumberFormatException e) {
                showAlert("Error", "Total Slots must be a valid number.");
                return;
            }

            // Insert the new room into the database
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String query = "INSERT INTO rooms (room_number, room_type, total_slots, remaining_slots) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, roomNumber);
                    stmt.setString(2, roomType);
                    stmt.setInt(3, totalSlots);
                    stmt.setInt(4, totalSlots);
                    stmt.executeUpdate();
                    showAlert("Success", "Room added successfully.");
                    clearFields();
                    redirectToDashboard();
                } catch (SQLException e) {
                    showAlert("Error", "Error adding room to the database: " + e.getMessage());
                }
            } catch (SQLException e) {
                showAlert("Error", "Connection error: " + e.getMessage());
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        roomNumberField.clear();
        roomTypeComboBox.getSelectionModel().clearSelection();
        totalSlotsField.clear();
    }

    private void redirectToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/Dashboard.fxml"));
            Scene dashboardScene = new Scene(loader.load());

            Stage stage = (Stage) addRoomButton.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load the dashboard: " + e.getMessage());
        }
    }
}
