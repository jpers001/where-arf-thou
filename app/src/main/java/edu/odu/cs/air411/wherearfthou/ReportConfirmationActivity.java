package edu.odu.cs.air411.wherearfthou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReportConfirmationActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView descriptTextView;
    private TextView locationTextView;
    private TextView contactTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_confirmation);

        ReportData entry = (ReportData)getIntent().getSerializableExtra("entry");

        Button okBtn = findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent returnToMain = new Intent(ReportConfirmationActivity.this, MainActivity.class);
                startActivity(returnToMain);
            }
        });

        nameTextView = findViewById(R.id.nameTextView);
        nameTextView.setText("Name: " + entry.getName());

        descriptTextView = findViewById(R.id.descriptTextView);
        descriptTextView.setText("Description: " + entry.getDescription());

        locationTextView = findViewById(R.id.locationTextView);
        locationTextView.setText("Location: " + entry.getLocation());

        contactTextView = findViewById(R.id.contactTextView);
        contactTextView.setText("Contact Info: " + entry.getContact());
    }
}
