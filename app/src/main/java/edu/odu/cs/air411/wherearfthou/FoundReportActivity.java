package edu.odu.cs.air411.wherearfthou;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;

public class FoundReportActivity extends AppCompatActivity {

    public static final int IMAGE_REQ = 998;
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
                startActivityForResult(openTakePhotoActivity, IMAGE_REQ);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = findViewById(R.id.imageFromCamera);
        imageView.setImageURI(Uri.parse(TakePhotoActivity.imageFilePath));

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
        entry.setPhotoUri(TakePhotoActivity.imageFilePath);
        report.add(entry);


        submit.putExtra("entry", entry);

        startActivity(submit);
    }

    public ArrayList getReport(){return report;}

}
