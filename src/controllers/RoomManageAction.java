package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class RoomManageAction {

    @FXML private Label roomNumberLabel;
    @FXML private Label roomTypeLabel;
    @FXML private Label totalSlotsLabel;
    @FXML private Label remainingSlotsLabel;
    @FXML private TableView tenantsTable;
    @FXML private TableColumn tenantIdCol;
    @FXML private TableColumn tenantNameCol;
    @FXML private TableColumn tenantGenderCol;
    @FXML private TableColumn tenantContactCol;
    @FXML private TableColumn tenantStayFromCol;
    @FXML private TableColumn tenantStayToCol;
    @FXML private TableColumn tenantActionsCol;

    @FXML
    public void initialize() {
        // Fetch data and populate the room details and tenants table
    }

    // Logic for handling the check-out action for tenants
    @FXML
    private void handleCheckoutTenant(MouseEvent event) {
        // Handle checkout logic here
    }
}
