package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Tenant;
import db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RoomManageAction {
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
    private Label roomNumberLabel, roomTypeLabel, totalSlotsLabel, remainingSlotsLabel;

    @FXML
    private TableView<Tenant> tenantsTable;

    @FXML
    private TableColumn<Tenant, Integer> tenantIdCol;
    @FXML
    private TableColumn<Tenant, String> tenantNameCol, tenantGenderCol, tenantContactCol, tenantStayFromCol, tenantStayToCol;

    private ObservableList<Tenant> tenants;

    private int roomId;

    // Method to set the roomId
    public void setRoomId(int roomId) {
        this.roomId = roomId;
        loadRoomDetails(roomId);
        loadTenants(roomId);
    }

    @FXML
    public void initialize() {
        handleTransition(roomManage, "/FxmlFiles/Dashboard.fxml");
        handleTransition(manageNewTenant, "/FxmlFiles/ManageNewTenant.fxml");
        handleTransition(manageTenant, "/FxmlFiles/ManageTenant.fxml");
        handleTransition(manageFacility, "/FxmlFiles/ManageFacilities.fxml");
        handleTransition(logoutButton, "/FxmlFiles/Login.fxml");

        tenants = FXCollections.observableArrayList();

        tenantIdCol.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        tenantNameCol.setCellValueFactory(data -> data.getValue().fullNameProperty());
        tenantGenderCol.setCellValueFactory(data -> data.getValue().genderProperty());
        tenantContactCol.setCellValueFactory(data -> data.getValue().mobileNumberProperty());
        tenantStayFromCol.setCellValueFactory(data -> data.getValue().stayFromProperty());
        tenantStayToCol.setCellValueFactory(data -> data.getValue().stayToProperty());
    }

    private void handleTransition(Button btn, String path) {
        btn.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
                Parent root = loader.load();

                Stage stage = (Stage) btn.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Manage Tenant");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadRoomDetails(int roomId) {
        String query = "SELECT * FROM rooms WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, roomId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                roomNumberLabel.setText(rs.getString("room_number"));
                roomTypeLabel.setText(rs.getString("room_type"));
                totalSlotsLabel.setText(String.valueOf(rs.getInt("total_slots")));
                remainingSlotsLabel.setText(String.valueOf(rs.getInt("remaining_slots")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTenants(int roomId) {
        String query = "SELECT tenants.*, rooms.room_number FROM tenants " +
                "JOIN rooms ON tenants.room_id = rooms.id WHERE tenants.room_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, roomId);
            ResultSet rs = statement.executeQuery();

            tenants.clear(); // Clear existing tenants before loading new ones
            while (rs.next()) {
                tenants.add(new Tenant(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("gender"),
                        rs.getString("mobile_number"),
                        rs.getString("stay_from"),
                        rs.getString("stay_to"),
                        rs.getString("room_number")  // room_number is now fetched from the rooms table
                ));
            }
            tenantsTable.setItems(tenants);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleCheckoutTenant(MouseEvent event) {
        Tenant selectedTenant = tenantsTable.getSelectionModel().getSelectedItem();
        if (selectedTenant != null) {
            String query = "DELETE FROM tenants WHERE id = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setInt(1, selectedTenant.getId());
                statement.executeUpdate();

                tenants.remove(selectedTenant);
                remainingSlotsLabel.setText(String.valueOf(Integer.parseInt(remainingSlotsLabel.getText()) + 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
