package models;

import javafx.beans.property.*;

public class Room {
    private final IntegerProperty id;
    private final StringProperty roomNumber;
    private final StringProperty roomType;
    private final IntegerProperty totalSlots;
    private final IntegerProperty remainingSlots;

    public Room(int id, String roomNumber, String roomType, int totalSlots, int remainingSlots) {
        this.id = new SimpleIntegerProperty(id);
        this.roomNumber = new SimpleStringProperty(roomNumber);
        this.roomType = new SimpleStringProperty(roomType);
        this.totalSlots = new SimpleIntegerProperty(totalSlots);
        this.remainingSlots = new SimpleIntegerProperty(remainingSlots);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber.get();
    }

    public StringProperty roomNumberProperty() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType.get();
    }

    public StringProperty roomTypeProperty() {
        return roomType;
    }

    public int getTotalSlots() {
        return totalSlots.get();
    }

    public IntegerProperty totalSlotsProperty() {
        return totalSlots;
    }

    public int getRemainingSlots() {
        return remainingSlots.get();
    }

    public IntegerProperty remainingSlotsProperty() {
        return remainingSlots;
    }
}
