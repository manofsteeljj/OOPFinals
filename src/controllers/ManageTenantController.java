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
import models.Room;
import models.Tenant;

import java.sql.*;
import java.util.Optional;

public class ManageTenantController {

    @FXML
    private Button roomManage;

    @FXML
    private Button manageNewTenant;

    @FXML
    private Button manageTenant;
    @FXML
    private TableView<Tenant> tenantsTable;

    @FXML
    private TableColumn<Tenant, Integer> idColumn;

    @FXML
    private TableColumn<Tenant, String> nameColumn;

    @FXML
    private TableColumn<Tenant, String> roomColumn;

    @FXML
    private TableColumn<Tenant, String> stayFromColumn;

    @FXML
    private TableColumn<Tenant, String> stayToColumn;

    @FXML
    private TableColumn<Tenant, String> genderColumn;

    @FXML
    private TableColumn<Tenant, String> mobileNumberColumn;

    @FXML
    private TableColumn<Tenant, Void> actionsColumn;

    @FXML
    private TextField searchField;

    private ObservableList<Tenant> tenantsData = FXCollections.observableArrayList();

    private static final String DB_URL = "jdbc:mysql://localhost:3306/dormdb_sasa";
    private static final String DB_USER = "root"; // Change as per your setup
    private static final String DB_PASSWORD = ""; // Change as per your setup

    @FXML
    public void initialize() {

        roomManage.setOnAction(actionEvent -> {
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

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        stayFromColumn.setCellValueFactory(new PropertyValueFactory<>("stayFrom"));
        stayToColumn.setCellValueFactory(new PropertyValueFactory<>("stayTo"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        mobileNumberColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));

        // Load all tenants data from the database
        loadAllTenants();

        // Add action buttons
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-padding: 2 10;");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 2 10;");

                editButton.setOnAction(event -> {
                    Tenant selectedRoom = getTableView().getItems().get(getIndex());
                    if (selectedRoom != null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/EditTenantAction.fxml"));
                            Parent root = loader.load();

                            EditTenantActionController controller = loader.getController();
                            controller.setTenantId(selectedRoom.getId());

                            Stage stage = (Stage) editButton.getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.setResizable(false);
                            stage.setTitle("Edit Tenant for: " + selectedRoom.getFullName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


                deleteButton.setOnAction(event -> {
                    Tenant tenant = getTableView().getItems().get(getIndex());
                    deleteTenant(tenant.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actions = new HBox(10, editButton, deleteButton);
                    setGraphic(actions);
                }
            }
        });
    }

    private void loadAllTenants() {
        tenantsData.clear();
        String query = "SELECT t.id, t.full_name, t.gender, t.mobile_number, t.stay_from, t.stay_to, r.room_number " +
                "FROM tenants t LEFT JOIN rooms r ON t.room_id = r.id"; // No room filtering, fetch all tenants
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tenantsData.add(new Tenant(
                        resultSet.getInt("id"),
                        resultSet.getString("full_name"),
                        resultSet.getString("gender"),
                        resultSet.getString("mobile_number"),
                        resultSet.getString("stay_from"),
                        resultSet.getString("stay_to"),
                        resultSet.getString("room_number") // Room number fetched from the rooms table
                ));
            }
            tenantsTable.setItems(tenantsData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteTenant(int tenantId) {
        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this tenant?").showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM tenants WHERE id = ?")) {
                statement.setInt(1, tenantId);
                statement.executeUpdate();
                loadAllTenants();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onSearch() {
        String filter = searchField.getText().toLowerCase();
        tenantsTable.setItems(tenantsData.filtered(tenant ->
                tenant.getFullName().toLowerCase().contains(filter) ||
                        tenant.getRoomNumber().toLowerCase().contains(filter)
        ));
    }
}
