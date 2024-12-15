package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class EditTenantActionController {

    @FXML
    private Button roomManage, manageNewTenant, manageTenant, manageFacility, logoutButton, cancelButton;

    @FXML
    private AnchorPane sidebar;

    @FXML
    private TextField tenantNameField, tenantPhoneField;

    @FXML
    private ComboBox<String> roomAssignedComboBox;

    @FXML
    private DatePicker stayFromPicker, stayToPicker;

    private int tenantId;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/dormdb_sasa";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
        loadTenantDetails();
    }

    @FXML
    public void initialize() {
        setupSidebarNavigation();
        loadAvailableRooms();
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void setupSidebarNavigation() {
        setupNavigationButton(roomManage, "/FxmlFiles/Dashboard.fxml", "Manage Rooms");
        setupNavigationButton(manageTenant, "/FxmlFiles/ManageTenant.fxml", "Manage Tenant");
        setupNavigationButton(manageNewTenant, "/FxmlFiles/ManageNewTenant.fxml", "Manage New Tenant");
        setupNavigationButton(manageFacility, "/FxmlFiles/ManageFacilities.fxml", "Manage Facility");
        setupNavigationButton(logoutButton, "/FxmlFiles/Login.fxml", "Login");
    }

    private void setupNavigationButton(Button button, String fxmlPath, String title) {
        button.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root = loader.load();
                Stage stage = (Stage) button.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle(title);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Navigation Error", "Failed to load the page.");
            }
        });
    }

    private void loadAvailableRooms() {
        String query = "SELECT id, room_number FROM rooms WHERE remaining_slots > 0";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String roomNumber = rs.getString("room_number");
                int roomId = rs.getInt("id");
                roomAssignedComboBox.getItems().add(roomNumber);  // Add room number to ComboBox
                roomAssignedComboBox.setUserData(roomId);  // Store room_id with ComboBox value
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not load available rooms.");
        }
    }

    private void loadTenantDetails() {
        String query = "SELECT * FROM tenants WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, tenantId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tenantNameField.setText(rs.getString("full_name"));
                tenantPhoneField.setText(rs.getString("mobile_number"));
                int roomId = rs.getInt("room_id");

                // Load room number in ComboBox
                loadRoomComboBoxById(roomId);

                // Check for null values and handle gracefully
                Date stayFrom = rs.getDate("stay_from");
                Date stayTo = rs.getDate("stay_to");

                if (stayFrom != null) {
                    stayFromPicker.setValue(stayFrom.toLocalDate());
                } else {
                    stayFromPicker.setValue(null);
                }

                if (stayTo != null) {
                    stayToPicker.setValue(stayTo.toLocalDate());
                } else {
                    stayToPicker.setValue(null);
                }
            } else {
                showAlert("Error", "Tenant not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not load tenant details.");
        }
    }

    private void loadRoomComboBoxById(int roomId) {
        String query = "SELECT room_number FROM rooms WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                roomAssignedComboBox.setValue(rs.getString("room_number"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not load room details.");
        }
    }

    @FXML
    private void handleUpdateTenant() {
        String tenantName = tenantNameField.getText();
        String tenantPhone = tenantPhoneField.getText();
        String roomAssigned = roomAssignedComboBox.getValue();
        LocalDate stayFrom = stayFromPicker.getValue();
        LocalDate stayTo = stayToPicker.getValue();

        // Input validation
        if (tenantName.isEmpty() || tenantPhone.isEmpty() || roomAssigned == null || stayFrom == null || stayTo == null) {
            showAlert("Validation Error", "All fields must be filled out.");
            return;
        }

        if (!tenantPhone.matches("\\d{10}")) {
            showAlert("Validation Error", "Please enter a valid 10-digit phone number.");
            return;
        }

        if (stayFrom.isAfter(stayTo)) {
            showAlert("Validation Error", "Stay From date cannot be after Stay To date.");
            return;
        }

        // Retrieve room_id from ComboBox's user data
        Integer roomId = (Integer) roomAssignedComboBox.getUserData();
        if (roomId == null) {
            showAlert("Validation Error", "Invalid room selected.");
            return;
        }

        // Update tenant in the database
        try {
            updateTenantInDatabase(tenantName, tenantPhone, roomId, stayFrom, stayTo);
            showAlert("Success", "Tenant updated successfully!");
            navigateToManageTenant();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update tenant.");
        }
    }

    private void updateTenantInDatabase(String name, String phone, int roomId, LocalDate stayFrom, LocalDate stayTo) {
        String query = "UPDATE tenants SET full_name = ?, mobile_number = ?, room_id = ?, stay_from = ?, stay_to = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setInt(3, roomId);  // Use room_id (integer) for the foreign key
            stmt.setDate(4, Date.valueOf(stayFrom));
            stmt.setDate(5, Date.valueOf(stayTo));
            stmt.setInt(6, tenantId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                showAlert("Error", "Tenant update failed. Tenant ID not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not update the tenant.");
        }
    }

    @FXML
    private void handleCancel() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Unsaved changes will be lost. Are you sure?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                loadTenantDetails();
            }
        });
    }

    private void navigateToManageTenant() {
        setupNavigationButton(manageTenant, "/FxmlFiles/ManageTenant.fxml", "Manage Tenant");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
