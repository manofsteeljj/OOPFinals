package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Room;

import java.sql.*;

public class DashboardController {
    @FXML
    private Button manageFacility;
    @FXML
    private Button roomManage;
    @FXML
    private Button logoutButton;

    @FXML
    private Button manageNewTenant;

    @FXML
    private Button manageTenant;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField searchBar;

    @FXML
    private Button addRoomButton;

    @FXML
    private TableView<Room> roomsTable;

    @FXML
    private TableColumn<Room, Integer> idColumn;

    @FXML
    private TableColumn<Room, String> roomNumberColumn;

    @FXML
    private TableColumn<Room, String> roomTypeColumn;

    @FXML
    private TableColumn<Room, Integer> totalSlotsColumn;

    @FXML
    private TableColumn<Room, Integer> remainingSlotsColumn;

    @FXML
    private TableColumn<Room, Void> actionsColumn;

    private final ObservableList<Room> roomList = FXCollections.observableArrayList();

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








        addRoomButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/AddRoom.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) addRoomButton.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Register");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Initialize table columns
        idColumn.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        roomNumberColumn.setCellValueFactory(data -> data.getValue().roomNumberProperty());
        roomTypeColumn.setCellValueFactory(data -> data.getValue().roomTypeProperty());
        totalSlotsColumn.setCellValueFactory(data -> data.getValue().totalSlotsProperty().asObject());
        remainingSlotsColumn.setCellValueFactory(data -> data.getValue().remainingSlotsProperty().asObject());

        // Set up the Actions column
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button manageButton = new Button("Manage");
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                // Button styles
                manageButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-padding: 2 10;");
                editButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-padding: 2 10;");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-padding: 2 10;");

                // Button actions
                manageButton.setOnAction(event -> {
                    Room selectedRoom = getTableView().getItems().get(getIndex());
                    if (selectedRoom != null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/RoomManageAction.fxml"));
                            Parent root = loader.load();

                            // Access the controller and pass    the room ID
                            RoomManageAction controller = loader.getController();
                            controller.setRoomId(selectedRoom.getId());

                            // Set up the stage
                            Stage stage = (Stage) manageButton.getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.setResizable(false);
                            stage.setTitle("Manage Tenants for Room " + selectedRoom.getRoomNumber());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                editButton.setOnAction(event -> {
                    Room selectedRoom = getTableView().getItems().get(getIndex());
                    if (selectedRoom != null) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/EditRoomAction.fxml"));
                            Parent root = loader.load();

                            // Access the controller and pass  the room ID
                            EditRoomActionController controller = loader.getController();
                            controller.setRoomId(selectedRoom.getId());

                            // Set up the stage
                            Stage stage = (Stage) manageButton.getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.setResizable(false);
                            stage.setTitle("Manage Tenants for Room " + selectedRoom.getRoomNumber());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                deleteButton.setOnAction(event -> {
                    Room room = getTableView().getItems().get(getIndex());
                    handleDeleteRoom(room);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actionBox = new HBox(5, manageButton, editButton, deleteButton);
                    setGraphic(actionBox);
                }
            }
        });

        loadRoomData();
        roomsTable.setItems(roomList);
    }
    private void loadRoomData() {
        String url = "jdbc:mysql://localhost:3306/dormdb_sasa";
        String user = "root";
        String password = "";
        String query = "SELECT id, room_number, room_type, total_slots, remaining_slots FROM rooms";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            roomList.clear();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String roomNumber = resultSet.getString("room_number");
                String roomType = resultSet.getString("room_type");
                int totalSlots = resultSet.getInt("total_slots");
                int remainingSlots = resultSet.getInt("remaining_slots");
                roomList.add(new Room(id, roomNumber, roomType, totalSlots, remainingSlots));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading room data from database: " + e.getMessage());
        }
        roomsTable.setItems(roomList);
    }


    @FXML
    private void handleSearch() {
        String searchText = searchBar.getText().toLowerCase();
        ObservableList<Room> filteredList = FXCollections.observableArrayList();
        for (Room room : roomList) {
            if (room.getRoomNumber().toLowerCase().contains(searchText) || room.getRoomType().toLowerCase().contains(searchText)) {
                filteredList.add(room);
            }
        }
        roomsTable.setItems(filteredList);
    }




    private void handleEditRoom(Room room) {

    }


    private void handleDeleteRoom(Room room) {
        roomList.remove(room); // remove from table only

        String url = "jdbc:mysql://localhost:3306/dormdb_sasa";
        String user = "root";
        String password = "";

        String query = "DELETE FROM rooms WHERE room_number = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, room.getRoomNumber());
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    statusLabel.setText("Room " + room.getRoomNumber() + " deleted from the database.");
                } else {
                    statusLabel.setText("Room not found in the database.");
                }
            } catch (SQLException e) {
                statusLabel.setText("Error deleting room from the database: " + e.getMessage());
            }
        } catch (SQLException e) {
            statusLabel.setText("Connection error: " + e.getMessage());
        }
    }

}
