package edu.odu.cs.air411.wherearfthou;

public class ReportData {

    private String name;
    private String description;
    private String location;
    private String contact;
    // true = found report, false = lost report
    private boolean isFound;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public boolean isFound() {
        return isFound;
    }

    public void setFound(boolean found) {
        isFound = found;
    }

    ///Constructors
    public void setContact(String contact) {
        this.contact = contact;
    }

    ReportData(){
        name = "N/A";
        description = "N/A";
        location = "N/A";
        contact = "555-555-5555";
    }

    ReportData(String description, String location, String contact, boolean isFound){
        this.name = "N/A";
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.isFound = isFound;
    }

    ReportData(String name, String description, String location, String contact, boolean isFound){
        this.name = name;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.isFound = isFound;
    }
}
