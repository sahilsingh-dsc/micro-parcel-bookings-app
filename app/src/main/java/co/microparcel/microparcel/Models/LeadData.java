package co.microparcel.microparcel.Models;

public class LeadData {

    String pickoff_location;
    String dropoff_location;
    String vehicle;
    String vehicle_type;
    String fare;
    String kilometer;

    LeadData(){

    }

    public LeadData(String pickoff_location, String dropoff_location, String vehicle, String vehicle_type, String fare, String kilometer) {
        this.pickoff_location = pickoff_location;
        this.dropoff_location = dropoff_location;
        this.vehicle = vehicle;
        this.vehicle_type = vehicle_type;
        this.fare = fare;
        this.kilometer = kilometer;
    }

    public String getPickoff_location() {
        return pickoff_location;
    }

    public void setPickoff_location(String pickoff_location) {
        this.pickoff_location = pickoff_location;
    }

    public String getDropoff_location() {
        return dropoff_location;
    }

    public void setDropoff_location(String dropoff_location) {
        this.dropoff_location = dropoff_location;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getKilometer() {
        return kilometer;
    }

    public void setKilometer(String kilometer) {
        this.kilometer = kilometer;
    }
}
