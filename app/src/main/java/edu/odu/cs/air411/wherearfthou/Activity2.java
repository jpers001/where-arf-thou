package edu.odu.cs.air411.wherearfthou;

import android.os.Bundle;
import android.app.Activity;

public class Activity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
