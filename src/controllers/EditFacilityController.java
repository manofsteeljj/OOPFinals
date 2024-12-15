package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EditFacilityController {
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
    private TextField facilityTypeField;

    @FXML
    private TextField descriptionField;

    @FXML
    private ComboBox<String> statusComboBox;

    private int facilityId;


    private static final String DB_URL = "jdbc:mysql://localhost:3306/dormdb_sasa";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
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

    }
    public void setFacilityId(int id) {
        this.facilityId = id;
        loadFacilityDetails();
    }

    private void loadFacilityDetails() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM facilities WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, facilityId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                facilityTypeField.setText(resultSet.getString("equipment_type"));
                descriptionField.setText(resultSet.getString("description"));
                statusComboBox.setValue(resultSet.getString("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateFacility() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE facilities SET equipment_type = ?, description = ?, status = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, facilityTypeField.getText());
            statement.setString(2, descriptionField.getText());
            statement.setString(3, statusComboBox.getValue());
            statement.setInt(4, facilityId);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Facility updated successfully!");
                handleCancel(); // Close the window after update
            } else {
                System.out.println("Update failed. No rows affected.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) facilityTypeField.getScene().getWindow();
        stage.close();
    }
}
