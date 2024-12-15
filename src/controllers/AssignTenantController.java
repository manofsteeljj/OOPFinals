package controllers;

import db.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssignTenantController {

    @FXML
    private ComboBox<String> roomComboBox;
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
    private DatePicker stayFromPicker;

    @FXML
    private DatePicker stayToPicker;
    private int tenantID;

    public void initialize() {
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

        loadAvailableRooms();
    }
    public void setTenantId(int tenantId) {
        this.tenantID = tenantId;
    }

    private void loadAvailableRooms() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM rooms WHERE remaining_slots > 0";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String room = rs.getString("room_number") + " (" + rs.getString("room_type") + ")";
                roomComboBox.getItems().add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to load available rooms.");
        }
    }

    @FXML
    private void handleAssignTenant() {
        String selectedRoom = roomComboBox.getValue();
        if (selectedRoom == null || stayFromPicker.getValue() == null || stayToPicker.getValue() == null) {
            showError("Please fill out all fields.");
            return;
        }

        // Extract room ID and dates from the selection
        String roomId = selectedRoom.split(" ")[0];
        String stayFrom = stayFromPicker.getValue().toString();
        String stayTo = stayToPicker.getValue().toString();

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Assign the tenant to the room
            String query = "UPDATE tenants SET room_id = ?, stay_from = ?, stay_to = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, roomId);
            stmt.setString(2, stayFrom);
            stmt.setString(3, stayTo);
            stmt.setInt(4, 1); // Replace with actual tenant ID

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                // Update room's remaining slots
                String updateRoomQuery = "UPDATE rooms SET remaining_slots = remaining_slots - 1 WHERE id = ?";
                PreparedStatement updateRoomStmt = conn.prepareStatement(updateRoomQuery);
                updateRoomStmt.setString(1, roomId);
                updateRoomStmt.executeUpdate();

                showInfo("Tenant assigned successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to assign tenant.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
