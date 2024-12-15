package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EditRoomActionController {
    @FXML
    private Button roomManage;
    @FXML
    private Button manageNewTenant;
    @FXML
    private Button manageTenant;
    @FXML
    private Button manageFacility;
    @FXML
    private Button logoutButton;

    @FXML
    private Button cancelButton;
    @FXML
    private AnchorPane sidebar;

    @FXML
    private TextField roomNumberField;

    @FXML
    private ComboBox<String> roomTypeComboBox;

    @FXML
    private TextField totalSlotsField;

    @FXML
    private TextField remainingSlotsField;

    private int roomId; // Variable to store the passed roomId

    // JDBC connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/dormdb_sasa"; // Update your database name
    private static final String DB_USER = "root"; // Update with your database username
    private static final String DB_PASSWORD = ""; // Update with your database password

    // Method to set the roomId and load room details
    public void setRoomId(int roomId) {
        this.roomId = roomId;
        loadRoomDetails();
    }

    @FXML
    public void initialize() {
        // Populate roomTypeComboBox with values
        roomTypeComboBox.getItems().addAll("Male Double", "Female Double", "Female Single", "Male Single");

        cancelButton.setOnAction(event -> navigateToDashboard());
        logoutButton.setOnAction(actionEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/Login.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) logoutButton.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Login");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        manageTenant.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/ManageTenant.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) manageTenant.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Manage Tenant");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        manageNewTenant.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/ManageNewTenant.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) manageNewTenant.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Manage New Tenant");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        roomManage.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/Dashboard.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) roomManage.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Manage Rooms");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        manageFacility.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/ManageFacilities.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) manageFacility.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Manage Facility");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void navigateToDashboard() {
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
    }

    private void loadRoomDetails() {
        String query = "SELECT * FROM rooms WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Populate the fields with room data
                roomNumberField.setText(rs.getString("room_number"));
                roomTypeComboBox.setValue(rs.getString("room_type"));
                totalSlotsField.setText(String.valueOf(rs.getInt("total_slots")));
                remainingSlotsField.setText(String.valueOf(rs.getInt("remaining_slots")));
            } else {
                showAlert("Error", "Room not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not load room details.");
        }
    }

    @FXML
    private void handleUpdateRoom() {
        // Get the input data
        String roomNumber = roomNumberField.getText();
        String roomType = roomTypeComboBox.getValue();
        String totalSlotsText = totalSlotsField.getText();
        String remainingSlotsText = remainingSlotsField.getText();

        // Validate inputs
        if (roomNumber.isEmpty() || roomType == null || totalSlotsText.isEmpty() || remainingSlotsText.isEmpty()) {
            showAlert("Validation Error", "All fields must be filled out.");
            return;
        }

        int totalSlots;
        int remainingSlots;

        try {
            totalSlots = Integer.parseInt(totalSlotsText);
            remainingSlots = Integer.parseInt(remainingSlotsText);

            // Additional validation (e.g., check if remaining slots are not greater than total slots)
            if (remainingSlots > totalSlots) {
                showAlert("Validation Error", "Remaining slots cannot be greater than total slots.");
                return;
            }

            // Call a method to update the room in the database
            updateRoomInDatabase(roomNumber, roomType, totalSlots, remainingSlots);
            showAlert("Success", "Room updated successfully!");

            // Navigate back to the dashboard after updating
            navigateToDashboard();

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Total slots and remaining slots must be numbers.");
        }
    }

    private void updateRoomInDatabase(String roomNumber, String roomType, int totalSlots, int remainingSlots) {
        String query = "UPDATE rooms SET room_number = ?, room_type = ?, total_slots = ?, remaining_slots = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, roomNumber);
            stmt.setString(2, roomType);
            stmt.setInt(3, totalSlots);
            stmt.setInt(4, remainingSlots);
            stmt.setInt(5, roomId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                showAlert("Error", "Room update failed. Room ID not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not update the room.");
        }
    }

    @FXML
    private void handleCancel() {
        // Clear fields or reset to original values
        loadRoomDetails();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
