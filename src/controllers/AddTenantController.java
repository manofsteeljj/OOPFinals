package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddTenantController {

    @FXML
    private TextField fullNameField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField mobileNumberField;

    @FXML
    private Button addTenantButton;

    // JDBC connection parameters
    private final String DB_URL = "jdbc:mysql://localhost:3306/dormdb_sasa";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    @FXML
    private void handleAddTenant() {
        String fullName = fullNameField.getText();
        String gender = genderComboBox.getValue();
        String mobileNumber = mobileNumberField.getText();

        // Input validation
        if (fullName.isEmpty() || gender == null || mobileNumber.isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "Please fill all the fields.");
            return;
        }

        if (!mobileNumber.matches("\\d{11}")) {
            showAlert(AlertType.ERROR, "Validation Error", "Please enter a valid 11-digit mobile number.");
            return;
        }

        String query = "INSERT INTO tenants (full_name, gender, mobile_number) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, fullName);
            statement.setString(2, gender);
            statement.setString(3, mobileNumber);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Tenant added successfully.");
                clearFields();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Failed to add tenant. Please try again.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        fullNameField.clear();
        genderComboBox.getSelectionModel().clearSelection();
        mobileNumberField.clear();
    }
}
