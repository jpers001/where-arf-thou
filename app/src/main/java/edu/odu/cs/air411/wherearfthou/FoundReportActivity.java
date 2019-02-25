package edu.odu.cs.air411.wherearfthou;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class FoundReportActivity extends AppCompatActivity {

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

    FoundReportActivity(String description, String location, String contact){
        this.description = description;
        this.location = location;
        this.contact = contact;
    }

    public ArrayList<FoundReportActivity> report = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_report);

        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm(report);
            }
        });
    }

    public void submitForm(ArrayList<FoundReportActivity> report){
        Intent submit = new Intent();

        EditText descriptEditText = findViewById(R.id.descriptEditText);
        String description = descriptEditText.getText().toString();

        EditText locationEditText = findViewById(R.id.locationEditText);
        String location = locationEditText.getText().toString();

        EditText contactEditText = findViewById(R.id.contactEditText);
        String contact = contactEditText.getText().toString();

        FoundReportActivity entry = new FoundReportActivity(description, location, contact);
        report.add(entry);
    }

}
