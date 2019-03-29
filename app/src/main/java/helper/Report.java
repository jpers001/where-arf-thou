package helper;


/**
 * Created by Belal on 9/9/2017.
 */

public class Report {

    public String owner, pet_name, last_seen, contact, description;


    public Report(String owner, String pet_name, String last_seen, String contact, String description) {
        this.owner = owner;
        this.pet_name = pet_name;
        this.last_seen = last_seen;
        this.contact = contact;
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public String getPet_name() {
        return pet_name;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public String getContact() {
        return contact;
    }

    public String getDescription() {
        return description;
    }
}