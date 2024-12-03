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

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty roomNumberProperty() {
        return roomNumber;
    }

    public StringProperty roomTypeProperty() {
        return roomType;
    }

    public IntegerProperty totalSlotsProperty() {
        return totalSlots;
    }

    public IntegerProperty remainingSlotsProperty() {
        return remainingSlots;
    }

    public String getRoomNumber() {
        return roomNumber.get();
    }

    public String getRoomType() {
        return roomType.get();
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id.get() +
                ", roomNumber='" + roomNumber.get() + '\'' +
                ", roomType='" + roomType.get() + '\'' +
                '}';
    }
}
