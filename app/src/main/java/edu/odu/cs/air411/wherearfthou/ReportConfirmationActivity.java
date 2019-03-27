package edu.odu.cs.air411.wherearfthou;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import com.twitter.sdk.android.core.Twitter;

public class ReportConfirmationActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView descriptTextView;
    private TextView locationTextView;
    private TextView contactTextView;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_confirmation);
        Twitter.initialize(this);

        ReportData entry = (ReportData)getIntent().getSerializableExtra("entry");

        if(entry.isFound()){
            type = "Reporting a sighted stray pet found around ";
        }
        else if (!entry.isFound()){
            type = "My pet was last seen around ";
        }

        final String tweetString = type + entry.getLocation() + " ";
        final String tweetPhoto = entry.getPhotoUri();

        Button okBtn = findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent returnToMain = new Intent(ReportConfirmationActivity.this, MainActivity.class);
                startActivity(returnToMain);
            }
        });

        Button tweetBtn = findViewById(R.id.tweetBtn);
        tweetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TweetComposer.Builder builder = new TweetComposer.Builder(ReportConfirmationActivity.this)
                        .text(tweetString)
                        .image(Uri.parse(tweetPhoto));
                builder.show();
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
