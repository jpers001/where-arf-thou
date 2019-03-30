package edu.odu.cs.air411.wherearfthou;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import helper.Api;
import helper.RequestHandler;

public class FoundReportActivity extends AppCompatActivity {

    /**
     * New Additions.
     *   Changed entry of the EditTexts (so they're public)
     *     Followed this tutorial: https://www.simplifiedcoding.net/android-mysql-tutorial-to-perform-basic-crud-operation/
     *
     *   default info: petname, ownername - hardcoded as Unknown
     */

    EditText descriptEditText, locationEditText, contactEditText;
    String defaultOwner = "Unknown";
    String defaultPetName = "Unknown";

    public static final int CODE_POST_REQUEST = 1025;
    public static final int CODE_GET_REQUEST = 1024;

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

        //db: description
        descriptEditText = findViewById(R.id.descriptEditText);
        String description = descriptEditText.getText().toString();

        //db: last_seen
        locationEditText = findViewById(R.id.locationEditText);
        String location = locationEditText.getText().toString();

        //db: contact
        contactEditText = findViewById(R.id.contactEditText);
        String contact = contactEditText.getText().toString();


        /**
         * QUERY: What is the below stuff used for? Do we need this anymore?
         */
        ReportData entry = new ReportData(description, location, contact, true);
        entry.setPhotoUri(TakePhotoActivity.imageFilePath);
        report.add(entry);


        submit.putExtra("entry", entry);

        startActivity(submit);

        /**
         * Below performs the actual sending to the database
         *   NOTE:
         *   0.
         *   1. We are not doing any input validation at this point.
         *   2. Not sending photo data yet
         *   3. Hardcoded owner and pet_name
         *   4. Needs GPS data
         */
        HashMap<String,String> params = new HashMap<>();
        params.put("owner", defaultOwner);
        params.put("pet_name", defaultPetName);
        params.put("last_seen", location);
        params.put("contact", contact);
        params.put("description", description);
        // Call api to create report
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_REPORT,params,CODE_POST_REQUEST);
        request.execute();



    } //end submitForm


    /**
     * What's this doing? Can we delete it?     *
     */
    public ArrayList getReport(){return report;}


    /**
     *  Perform Network Request
     */
//inner class to perform network request extending an AsyncTask
    public class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //the url where we need to send the request
        String url;

        //the parameters
        HashMap<String, String> params;

        //the request code to define whether it is a GET or POST
        int requestCode;

        //constructor to initialize values
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //when the task started displaying a progressbar
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
            //Toast toast = Toast.makeText(getApplicationContext(),"PREExecute",Toast.LENGTH_SHORT);
            //toast.show();

        }


        //this method will give the response from the request
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressBar.setVisibility(GONE);
            Toast toast = Toast.makeText(getApplicationContext(),"Report Sent!",Toast.LENGTH_SHORT);
            toast.show();
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    //refreshing the reportlist after every operation
                    //so we get an updated list
                    //we will create this method right now it is commented
                    //because we haven't created it yet
                    //refreshReportList(object.getJSONArray("reports"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //the network operation will be performed in background
        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    } //end PerformNetworkReuest


} //end FoundReport
