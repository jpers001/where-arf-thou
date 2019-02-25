package edu.odu.cs.air411.wherearfthou;

public class ReportData {

    private String description;
    private String location;
    private String contact;

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

    public void setContact(String contact) {
        this.contact = contact;
    }

    ReportData(){
        description = "N/A";
        location = "N/A";
        contact = "555-555-5555";
    }

    ReportData(String description, String location, String contact){
        this.description = description;
        this.location = location;
        this.contact = contact;
    }
}
