package edu.odu.cs.air411.wherearfthou;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    String defaultLocation = "Unknown";
    String encoded = "N/A"; //default
    String last_seen = "NoDate";
    String defaultTags = "None";

    public static final int CODE_POST_REQUEST = 1025;
    public static final int CODE_GET_REQUEST = 1024;

    public static final int IMAGE_REQ = 998;
    public ArrayList<ReportData> report = new ArrayList<>();
    public Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_report);

        //Submit Button
        Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm(report);
            }
        });

        //Photo Button
        ImageButton addPhotoImgBtn = findViewById(R.id.addPhotoImgBtn);
        addPhotoImgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent openTakePhotoActivity = new Intent(FoundReportActivity.this, TakePhotoActivity.class);
                startActivityForResult(openTakePhotoActivity, IMAGE_REQ);
            }
        });

        //Add tag button
        ImageButton tagImgBtn = findViewById(R.id.tagImgBtn);
        tagImgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO: Add code to remove text from tag text box and place inside ArrayList
            }
        });

        {
            ActionBar actionBar = getSupportActionBar(); // or getActionBar();
            String title = actionBar.getTitle().toString(); // get the title
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.ic_wat_icon);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = findViewById(R.id.imageFromCamera);
        imageView.setImageURI(Uri.parse(TakePhotoActivity.imageFilePath));

        // get absolute path of image file (photofile)
        String filePath = TakePhotoActivity.photoFile.getAbsolutePath();
        // create bitmap of photofile
        bitmap = BitmapFactory.decodeFile(filePath);
        // new bytearray
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // compress bitmap into bytearray
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        // creating bytearray
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        // encode bytearray into base64
        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        bitmap = null;
        byteArray = null;

    } //end onactivityresult

/*    public String convertBase64(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }*/

    public void submitForm(ArrayList<ReportData> report){
        Intent submit = new Intent(FoundReportActivity.this, ReportConfirmationActivity.class);

        //db: description
        descriptEditText = findViewById(R.id.descriptEditText);
        String description = descriptEditText.getText().toString();

        //db: location
        locationEditText = findViewById(R.id.locationEditText);
        String location = locationEditText.getText().toString();

        //db: contact
        contactEditText = findViewById(R.id.contactEditText);
        String contact = contactEditText.getText().toString();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
         last_seen = simpleDateFormat.format(new Date());



        /*if(hasImage){
            TextView testText = findViewById(R.id.bmpTest);
            String encodedImage = convertBase64(bitmap);
            testText.setText(encodedImage);
        }*/


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
        params.put("last_seen", last_seen);
        params.put("contact", contact);
        params.put("description", description);
        params.put("photo", encoded);
        params.put("location", location);
        // Call api to create report
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_REPORT,params,CODE_POST_REQUEST);
        request.execute();



    } //end submitForm

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
