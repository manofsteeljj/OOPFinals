package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Room;
import models.Tenant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManageNewTenantController {

    @FXML
    private AnchorPane sidebar;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button roomManage;

    @FXML
    private Button manageNewTenant;

    @FXML
    private Button manageTenant;

    @FXML
    private Button manageFacilities;

    @FXML
    private Button addTenantButton;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Tenant> tenantsTable;

    @FXML
    private TableColumn<Tenant, Integer> idColumn;

    @FXML
    private TableColumn<Tenant, String> nameColumn;

    @FXML
    private TableColumn<Tenant, String> genderColumn;

    @FXML
    private TableColumn<Tenant, String> mobileNumberColumn;

    @FXML
    private TableColumn<Tenant, Void> actionsColumn;

    private ObservableList<Tenant> tenantList = FXCollections.observableArrayList();

    private int tenantID;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/dormdb_sasa";
    private static final String DB_USER = "root";  // Replace with your database username
    private static final String DB_PASSWORD = ""; // Replace with your database password

    @FXML
    public void initialize() {
        // side bar

        handleTransition(manageFacilities, "/FxmlFiles/ManageFacilities.fxml");
        handleTransition(roomManage, "/FxmlFiles/Dashboard.fxml");
        handleTransition(manageTenant, "/FxmlFiles/ManageTenant.fxml");
        handleTransition(manageNewTenant, "/FxmlFiles/ManageNewTenant.fxml");
        handleTransition(logoutBtn, "/FxmlFiles/Login.fxml");

        handleTransition(addTenantButton, "/FxmlFiles/AddTenant.fxml");
        // Set up columns in the table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        mobileNumberColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button assignButton = new Button("Assign");
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                // Button styles
                assignButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-padding: 2 10;");
                editButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-padding: 2 10;");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 2 10;");

                // Button actions
                assignButton.setOnAction(event -> {
                    Tenant selectedTenant = getTableView().getItems().get(getIndex());
                    if (selectedTenant != null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/AssignTenant.fxml"));
                            Parent root = loader.load();

                            RoomManageAction controller = loader.getController();
                            controller.setRoomId(selectedTenant.getId());

                            // Set up the stage
                            Stage stage = (Stage) assignButton.getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.setResizable(false);
                            stage.setTitle("Assign Tenant for " + selectedTenant.getFullName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


                editButton.setOnAction(event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/EditRoomAction.fxml"));
                        Parent root = loader.load();

                        Stage stage = (Stage) editButton.getScene().getWindow();

                        stage.setScene(new Scene(root));
                        stage.setResizable(false);
                        stage.setTitle("Edit Tenant for " );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                deleteButton.setOnAction(event -> {
                    Tenant tenant = getTableView().getItems().get(getIndex());
                    //handleDeleteRoom(tenant);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actionBox = new HBox(5, assignButton, editButton, deleteButton);
                    setGraphic(actionBox);
                }
            }
        });



        // Assign Tenant, Edit Tenant, Delete Tenant.
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button assignButton = new Button("Assign");
                    assignButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");

                    assignButton.setOnAction(event -> {
                        Tenant selectedTenant = getTableView().getItems().get(getIndex());
                        if (selectedTenant != null) {
                            try {
                                // Load the FXML file for managing tenant assignment
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/AssignTenant.fxml"));
                                Parent root = loader.load();

                                // Access the controller and pass the tenant ID
                                AssignTenantController controller = loader.getController();
                                controller.setTenantId(selectedTenant.getId());  // Pass the tenant ID to the controller

                                // Set up the new scene with the appropriate stage
                                Stage stage = (Stage) assignButton.getScene().getWindow();
                                stage.setScene(new Scene(root));
                                stage.setResizable(false);
                                stage.setTitle("Assign Tenant: " + selectedTenant.getFullName()); // Assuming you have a name or identifier for the tenant
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });



                    Button editButton = new Button("Edit");
                    editButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
                    //editButton.setOnAction(e -> editTenant(getTableRow().getItem()));

                    Button deleteButton = new Button("Delete");
                    deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    //deleteButton.setOnAction(e -> deleteTenant(getTableRow().getItem()));

                    HBox buttons = new HBox(5, assignButton, editButton, deleteButton);
                    setGraphic(buttons);


                }
            }
        });

        // Load data from the database
        loadTenantData();

        // Set table data
        tenantsTable.setItems(tenantList);
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

    private void loadTenantData() {
        String query = "SELECT * FROM tenants WHERE room_id IS NULL"; // Only fetch tenants with NULL room_id
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            tenantList.clear(); // Clear the existing list to avoid duplicates
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String fullName = resultSet.getString("full_name");
                String gender = resultSet.getString("gender");
                String mobileNumber = resultSet.getString("mobile_number");

                tenantList.add(new Tenant(id, fullName, gender, mobileNumber));
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load tenant data from the database:\n" + e.getMessage());
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onSearch(KeyEvent event) {
        String filter = searchField.getText().toLowerCase();
        ObservableList<Tenant> filteredList = FXCollections.observableArrayList();
        for (Tenant tenant : tenantList) {
            if (tenant.getFullName().toLowerCase().contains(filter) ||
                    tenant.getGender().toLowerCase().contains(filter) ||
                    tenant.getMobileNumber().toLowerCase().contains(filter)) {
                filteredList.add(tenant);
            }
        }
        tenantsTable.setItems(filteredList);
    }
}
