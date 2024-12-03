package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import models.Room;

import java.sql.*;

public class DashboardController {

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
                    Room room = getTableView().getItems().get(getIndex());
                    handleManageRoom(room);
                });

                editButton.setOnAction(event -> {
                    Room room = getTableView().getItems().get(getIndex());
                    handleEditRoom(room);
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

        // Load initial data
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

    @FXML
    private void handleAddRoom() {
        // Logic to add a new room
        System.out.println("Add Room button clicked");
    }

    private void handleManageRoom(Room room) {
        // Logic to manage a room
        System.out.println("Manage room: " + room);
    }

    private void handleEditRoom(Room room) {
        // Logic to edit a room
        System.out.println("Edit room: " + room);
    }

    private void handleDeleteRoom(Room room) {
        // Logic to delete a room
        roomList.remove(room);
        System.out.println("Delete room: " + room);
    }
}
