package edu.odu.cs.air411.wherearfthou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;

import static edu.odu.cs.air411.wherearfthou.R.drawable.tag_icon;
import static edu.odu.cs.air411.wherearfthou.R.drawable.waticon;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button button2;
    private Button button3;


    @Override
    protected void onCreate(Bundle savedInstanceState)


    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openFoundReportActivity();
            }

        });

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openLostReportActivity();
            }
        });

        button3 = findViewById(R.id.map);
        button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openReportMapActivity();
            }

        });


        {
            {
                ActionBar actionBar = getSupportActionBar(); // or getActionBar();
                String title = actionBar.getTitle().toString(); // get the title
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setLogo(R.drawable.ic_wat_icon);
                getSupportActionBar().setDisplayUseLogoEnabled(true);
            }

        }



    }


    private void openFoundReportActivity()
    {
        Intent intent = new Intent(MainActivity.this, FoundReportActivity.class);
        startActivity(intent);
    }
    private void openLostReportActivity()
    {
        Intent intent = new Intent(MainActivity.this, LostReportActivity.class);
        startActivity(intent);
    }
    private void openReportMapActivity()
    {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }


}


