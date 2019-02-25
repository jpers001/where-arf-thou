package edu.odu.cs.air411.wherearfthou;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

public class FoundReportActivity extends AppCompatActivity {

    public ArrayList<ReportData> report = new ArrayList<>();

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

        ImageButton addPhotoImgBtn = findViewById(R.id.addPhotoImgBtn);
        addPhotoImgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent openTakePhotoActivity = new Intent(FoundReportActivity.this, TakePhotoActivity.class);
                startActivity(openTakePhotoActivity);
            }
        });
    }

    public void submitForm(ArrayList<ReportData> report){
        Intent submit = new Intent(FoundReportActivity.this, ReportConfirmationActivity.class);

        EditText descriptEditText = findViewById(R.id.descriptEditText);
        String description = descriptEditText.getText().toString();

        EditText locationEditText = findViewById(R.id.locationEditText);
        String location = locationEditText.getText().toString();

        EditText contactEditText = findViewById(R.id.contactEditText);
        String contact = contactEditText.getText().toString();

        ReportData entry = new ReportData(description, location, contact, true);
        report.add(entry);

        submit.putExtra("entry", entry);

        startActivity(submit);
    }

}
