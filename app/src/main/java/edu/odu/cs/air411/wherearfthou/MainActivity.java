package edu.odu.cs.air411.wherearfthou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button button2;
    private Button button3;
    //private Button button4;


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

   /**     button4 = findViewById(R.id.captureFront);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openTakePhotoActivity();

            }
        }); */

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

    /**private void openTakePhotoActivity()
    {
        Intent intent = new Intent(MainActivity.this, TakePhotoActivity.class);
        startActivity(intent);
    } */


    }


