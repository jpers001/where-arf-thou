package edu.odu.cs.air411.wherearfthou;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

public class LostReportActivity extends AppCompatActivity {


    public ArrayList<ReportData> report = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_report);

        Button submitBtn2 = findViewById(R.id.submitBtn2);
        submitBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm(report);
            }
        });

        ImageButton addPhotoImgBtn = findViewById(R.id.addPhotoImgBtn);
        addPhotoImgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent openTakePhotoActivity = new Intent(LostReportActivity.this, TakePhotoActivity.class);
                startActivity(openTakePhotoActivity);

            }

        });

        ImageView imageView = findViewById(R.id.imageFromCamera);
        imageView.setImageURI(Uri.parse(TakePhotoActivity.imageFilePath));


    }



    public void submitForm(ArrayList<ReportData> report){
        Intent submit = new Intent(LostReportActivity.this, ReportConfirmationActivity.class);

        EditText nameEditText = findViewById(R.id.editText);
        String name = nameEditText.getText().toString();

        EditText descriptEditText2 = findViewById(R.id.descriptEditText2);
        String description = descriptEditText2.getText().toString();

        EditText lastSeenEditText = findViewById(R.id.editText2);
        String lastSeen = lastSeenEditText.getText().toString();

        EditText contactEditText = findViewById(R.id.editText3);
        String contact = contactEditText.getText().toString();

        ReportData entry = new ReportData(name, description, lastSeen, contact, false);
        report.add(entry);

        submit.putExtra("entry", entry);

        startActivity(submit);
    }

}
