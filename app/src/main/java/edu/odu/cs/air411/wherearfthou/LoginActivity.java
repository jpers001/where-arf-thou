package edu.odu.cs.air411.wherearfthou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import helper.BackgroundTask;

public class LoginActivity extends AppCompatActivity
{



    TextView sign_up_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        {
            ActionBar actionBar = getSupportActionBar(); // or getActionBar();
            String title = actionBar.getTitle().toString(); // get the title
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.ic_wat_icon);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sign_up_text = (TextView) findViewById(R.id.sign_up);
        sign_up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));




            }
        });

    }

}