package models;

import javafx.beans.property.*;

public class Tenant {
    private IntegerProperty id;
    private StringProperty fullName, gender, mobileNumber;
    private StringProperty stayFrom, stayTo, roomNumber;

    // Constructor for new tenants (room_id is NULL)
    public Tenant(int id, String fullName, String gender, String mobileNumber) {
        this.id = new SimpleIntegerProperty(id);
        this.fullName = new SimpleStringProperty(fullName);
        this.gender = new SimpleStringProperty(gender);
        this.mobileNumber = new SimpleStringProperty(mobileNumber);
        this.stayFrom = new SimpleStringProperty(""); // Default to empty
        this.stayTo = new SimpleStringProperty("");   // Default to empty
        this.roomNumber = new SimpleStringProperty(""); // Default to empty
    }

    // Constructor for tenants with full details
    public Tenant(int id, String fullName, String gender, String mobileNumber, String stayFrom, String stayTo, String roomNumber) {
        this.id = new SimpleIntegerProperty(id);
        this.fullName = new SimpleStringProperty(fullName);
        this.gender = new SimpleStringProperty(gender);
        this.mobileNumber = new SimpleStringProperty(mobileNumber);
        this.stayFrom = new SimpleStringProperty(stayFrom);
        this.stayTo = new SimpleStringProperty(stayTo);
        this.roomNumber = new SimpleStringProperty(roomNumber);
    }

    // Getters and Property Methods
    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public String getFullName() { return fullName.get(); }
    public StringProperty fullNameProperty() { return fullName; }
    public String getGender() { return gender.get(); }
    public StringProperty genderProperty() { return gender; }
    public String getMobileNumber() { return mobileNumber.get(); }
    public StringProperty mobileNumberProperty() { return mobileNumber; }
    public String getStayFrom() { return stayFrom.get(); }
    public StringProperty stayFromProperty() { return stayFrom; }
    public String getStayTo() { return stayTo.get(); }
    public StringProperty stayToProperty() { return stayTo; }
    public String getRoomNumber() { return roomNumber.get(); }
    public StringProperty roomNumberProperty() { return roomNumber; }

    // Optional: Setters for modifying properties if needed
    public void setId(int id) { this.id.set(id); }
    public void setFullName(String fullName) { this.fullName.set(fullName); }
    public void setGender(String gender) { this.gender.set(gender); }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber.set(mobileNumber); }
    public void setStayFrom(String stayFrom) { this.stayFrom.set(stayFrom); }
    public void setStayTo(String stayTo) { this.stayTo.set(stayTo); }
    public void setRoomNumber(String roomNumber) { this.roomNumber.set(roomNumber); }
}
