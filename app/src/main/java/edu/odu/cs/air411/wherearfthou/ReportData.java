package edu.odu.cs.air411.wherearfthou;

import java.io.Serializable;
import java.util.ArrayList;

public class ReportData implements Serializable {

    private String image;
    private String name;
    private String description;
    private String location; ///might need two strings/variables, one for latitude and one for longitude
    private double latitude;
    private double longitude;
    private String contact;
    private String reportDate;
    /*private String tags;*/
    private ArrayList<String> tags = new ArrayList<>();
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
        latitude = 0.00;
        longitude = 0.00;
        contact = "555-555-5555";
    }

    ReportData(String description, String location, String contact, boolean isFound){
        this.name = "N/A";
        this.description = description;
        this.location = location;
        this.latitude = 0.00;
        this.longitude = 0.00;
        this.contact = contact;
        this.isFound = isFound;
    }

    ReportData(String name, String description, String location, String contact, boolean isFound){
        this.name = name;
        this.description = description;
        this.location = location;
        this.latitude = 0.00;
        this.longitude = 0.00;
        this.contact = contact;
        this.isFound = isFound;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void addTag(String tag){
        this.tags.add(tag);
    }
}
