package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import models.Room;

public class EditRoomActionController {
    @FXML
    private Button roomManage;
    @FXML
    private AnchorPane sidebar;

    @FXML
    private TextField roomNumberField;

    @FXML
    private ComboBox<String> roomTypeComboBox;

    @FXML
    private TextField totalSlotsField;

    @FXML
    private TextField remainingSlotsField;

    @FXML
    public void initialize() {
        roomManage.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FxmlFiles/Dashboard.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) roomManage.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setTitle("Register");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleUpdateRoom() {
        // Get the input data
        String roomNumber = roomNumberField.getText();
        String roomType = roomTypeComboBox.getValue();
        String totalSlotsText = totalSlotsField.getText();
        String remainingSlotsText = remainingSlotsField.getText();

        // Validate inputs
        if (roomNumber.isEmpty() || roomType == null || totalSlotsText.isEmpty() || remainingSlotsText.isEmpty()) {
            showAlert("Validation Error", "All fields must be filled out.");
            return;
        }

        int totalSlots;
        int remainingSlots;

        try {
            totalSlots = Integer.parseInt(totalSlotsText);
            remainingSlots = Integer.parseInt(remainingSlotsText);

            // Additional validation (e.g., check if remaining slots are not greater than total slots)
            if (remainingSlots > totalSlots) {
                showAlert("Validation Error", "Remaining slots cannot be greater than total slots.");
                return;
            }

            // Call a method to update the room (this would involve interacting with a database, for example)
            updateRoom(roomNumber, roomType, totalSlots, remainingSlots);
            showAlert("Success", "Room updated successfully!");

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Total slots and remaining slots must be numbers.");
        }
    }

    @FXML
    private void handleCancel() {
        // Logic for cancel (e.g., clear fields or navigate away)
        roomNumberField.clear();
        roomTypeComboBox.setValue(null);
        totalSlotsField.clear();
        remainingSlotsField.clear();
    }

    private void updateRoom(String roomNumber, String roomType, int totalSlots, int remainingSlots) {
        // Implement the logic to update the room in the database
        // Example:
        // Room room = new Room(roomNumber, roomType, totalSlots, remainingSlots);
        // roomService.updateRoom(room);
        System.out.println("Room updated: " + roomNumber + ", " + roomType + ", " + totalSlots + " slots, " + remainingSlots + " remaining");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
