package models;

public  class Facility {
    private final int id;
    private final String equipmentType;
    private final String description;
    private final String status;

    public Facility(int id, String equipmentType, String description, String status) {
        this.id = id;
        this.equipmentType = equipmentType;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}