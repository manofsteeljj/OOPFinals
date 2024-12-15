package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Facility;
import models.Tenant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManageFacilityController {
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
    private TableView<Facility> facilitiesTable;
    @FXML
    private TableColumn<Facility, Integer> idColumn;
    @FXML
    private TableColumn<Facility, String> typeColumn;
    @FXML
    private TableColumn<Facility, String> descriptionColumn;
    @FXML
    private TableColumn<Facility, String> statusColumn;
    @FXML
    private TableColumn<Facility, String> actionsColumn;
    @FXML
    private TextField searchField;

    private ObservableList<Facility> facilityList = FXCollections.observableArrayList();

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

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("equipmentType"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-padding: 2 10;");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 2 10;");

                editButton.setOnAction(event -> {
                    Facility selectedRoom = getTableView().getItems().get(getIndex());
                    if (selectedRoom != null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/EditFacility.fxml"));
                            Parent root = loader.load();

                            EditFacilityController controller = loader.getController();
                            controller.setFacilityId(selectedRoom.getId());

                            Stage stage = (Stage) editButton.getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.setResizable(false);
                            stage.setTitle("Edit Facility for: " + selectedRoom.getDescription());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                deleteButton.setOnAction(event -> handleDelete(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actionButtons = new HBox(5, editButton, deleteButton);
                    setGraphic(actionButtons);
                }
            }
        });

        loadFacilities();
    }

    private void loadFacilities() {
        facilityList.clear();
        String query = "SELECT * FROM facilities";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                facilityList.add(new Facility(
                        rs.getInt("id"),
                        rs.getString("equipment_type"),
                        rs.getString("description"),
                        rs.getString("status")
                ));
            }
            facilitiesTable.setItems(facilityList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load facilities.");
        }
    }

    @FXML
    private void onSearch() {
        String filter = searchField.getText().toLowerCase();
        ObservableList<Facility> filteredList = FXCollections.observableArrayList();
        for (Facility facility : facilityList) {
            if (facility.getEquipmentType().toLowerCase().contains(filter) ||
                    facility.getDescription().toLowerCase().contains(filter) ||
                    facility.getStatus().toLowerCase().contains(filter)) {
                filteredList.add(facility);
            }
        }
        facilitiesTable.setItems(filteredList);
    }


    private void handleDelete(Facility facility) {
        String query = "DELETE FROM facilities WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, facility.getId());
            stmt.executeUpdate();
            showAlert("Success", "Facility deleted successfully!");
            loadFacilities();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not delete facility.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
