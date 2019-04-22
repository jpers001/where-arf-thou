package edu.odu.cs.air411.wherearfthou;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipDrawable;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import helper.Api;
import helper.RequestHandler;
import helper.SessionHandler;
import helper.User;

public class LostReportActivity extends AppCompatActivity {

    private SessionHandler session;

    public static final int PICK_MAP_POINT_REQUEST = 814;
    /**
     * New Additions.
     *   Changed entry of the EditTexts (so they're public)
     *     Followed this tutorial: https://www.simplifiedcoding.net/android-mysql-tutorial-to-perform-basic-crud-operation/
     *
     *   defaultOwner: I assume the Owner of the pet will be the user? I have hardcoded the name for now.
     */
    EditText nameEditText, descriptEditText2, lastSeenEditText, contactEditText, locationEditText, tagEditTextLost;



    String defaultOwner = "WhereArfThou"; //should be userName later.
    String defaultLocation = "N/A";
    String encoded = "N/A";
    String last_seen = "NoDate";
    String owner = defaultOwner;


    public static final int CODE_POST_REQUEST = 1025;
    public static final int CODE_GET_REQUEST = 1024;


    public static final int IMAGE_REQ = 997;
    public ArrayList<ReportData> report = new ArrayList<>();
    public ArrayList<String> tagData = new ArrayList<>();
    public Bitmap bitmap;

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

        FloatingActionButton addPhotoFAB = findViewById(R.id.addPhotoFAB);
        //Replacing ImageButton with FAB
        //ImageButton addPhotoImgBtn = findViewById(R.id.addPhotoImgBtn);
        addPhotoFAB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent openTakePhotoActivity = new Intent(LostReportActivity.this, TakePhotoActivity.class);
                startActivityForResult(openTakePhotoActivity, IMAGE_REQ);
            }

        });

        //Chip Group
        final ChipGroup chipGroup = findViewById(R.id.tagChipGroup2);
        //Add Tag button
        final ImageButton tagImgBtn2 = findViewById(R.id.tagImgBtn2);
        tagEditTextLost = findViewById(R.id.tagEditTextLost);
        tagImgBtn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(tagData.size() >= 10)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Maximum tags allowed: " + tagData.size(), Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(tagEditTextLost.getText().toString().length() >= 3)
                {
                    tagData.add(tagEditTextLost.getText().toString());
                    final Chip chip = getChip(chipGroup, tagEditTextLost.getText().toString());
                    chipGroup.addView(chip);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chipGroup.removeView(chip);
                            tagData.remove(chip.getText());
                        }
                    });
                    tagEditTextLost.setText("");
                }
                else if (tagEditTextLost.getText().toString().length() <= 3){
                    Toast toast = Toast.makeText(getApplicationContext(),"Tags must be at least 3 characters.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        tagEditTextLost.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    tagImgBtn2.performClick();
                    handled = true;
                }
                return handled;
            }
        });

        //Current GPS Location Button
        ImageButton getLocationImgBtn = findViewById(R.id.lostReportLocationBtn);
        getLocationImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickLocationOnMap();
            }
        });


        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        String title = actionBar.getTitle().toString(); // get the title
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_wat_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_MAP_POINT_REQUEST)
        {
            if (resultCode == RESULT_OK) {
                LatLng latLng = (LatLng) data.getParcelableExtra("picked_point");
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());
                String location = "";
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if(addresses.size() != 0)
                    {
                        location = location + addresses.get(0).getAddressLine(0);// + addresses.get(0).getPostalCode();
                    }
                    TextView lastSeenTV = findViewById(R.id.lastSeenLostReport);
                    lastSeenTV.setText(location);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Point Chosen: " + latLng.latitude + " " + latLng.longitude, Toast.LENGTH_LONG).show();
            }
        }
        else {

            super.onActivityResult(requestCode, resultCode, data);
            ImageView imageView = findViewById(R.id.imageFromCamera);
            imageView.setImageURI(Uri.parse(TakePhotoActivity.imageFilePath));

            // get absolute path of image file (photofile)
            String filePath = TakePhotoActivity.photoFile.getAbsolutePath();
            // create bitmap of photofile
            bitmap = BitmapFactory.decodeFile(filePath);
            bitmap = Bitmap.createScaledBitmap(bitmap, 600, 600, true);
            // new bytearray
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // compress bitmap into bytearray
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
            // creating bytearray
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            // encode bytearray into base64
            encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            bitmap = null;
            byteArray = null;
        }
    } //end onactivityresult

    private Chip getChip(final ChipGroup chipGroup, String text) {
        final Chip chip = new Chip(this);
        chip.setChipDrawable(ChipDrawable.createFromResource(this, R.xml.tag_chip));
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()
        );
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        chip.setText(text);
        chip.setTextAppearanceResource(R.style.ChipTextStyle);
        return chip;
    }

    public void submitForm(ArrayList<ReportData> report){
        Intent submit = new Intent(LostReportActivity.this, ReportConfirmationActivity.class);

        //db: owner
        //getting session userName
        session = new SessionHandler(getApplicationContext());
        User user = session.getUserDetails();
        defaultOwner = user.getUsername();
        //db: pet_name
         nameEditText = findViewById(R.id.editText);
        String name = nameEditText.getText().toString();

        //db: description
         descriptEditText2 = findViewById(R.id.descriptEditText2);
        String description = descriptEditText2.getText().toString();

        //db: location
         lastSeenEditText = findViewById(R.id.lastSeenLostReport);
        String location = lastSeenEditText.getText().toString();

        //db: contact
         contactEditText = findViewById(R.id.editText3);
        String contact = contactEditText.getText().toString();

        String tags = "";
        for(int i = 0; i < tagData.size(); i++){
            if(i == tagData.size() - 1){
                tags += tagData.get(i);
            }
            else{
                tags += tagData.get(i) + ", ";
            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        last_seen = simpleDateFormat.format(new Date());


        /**
         * QUERY: What is the below stuff used for? Do we need this anymore?
         * -> It's used for the confirmation page
         */
        ReportData entry = new ReportData(name, description, location, contact, false, tagData);
        entry.setTagString(tags);
        entry.setPhotoUri(TakePhotoActivity.imageFilePath);
        report.add(entry);

        submit.putExtra("entry", entry);

        startActivity(submit);

        /**
         *  Below performs the actual sending to the database
         *    NOTE:
         *    1. We are not doing any input validation at this point.
         *    2. Not sending photo data yet
         *    3. lastSeen is currently input reliant. Database and PHP are expecting a string
         *          as long as whatever this is changed to remains a String no changes needed with db or php
         *    4. Hard coded the "owner" field. Assuming it would eventually use the username?
         */
        HashMap<String,String> params = new HashMap<>();
        params.put("owner", owner);
        params.put("pet_name", name);
        params.put("last_seen", last_seen);
        params.put("contact", contact);
        params.put("description", description);
        params.put("photo", encoded);
        params.put("location", location);
        params.put("tag", tags);
        // Call api to create report
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_REPORT,params,CODE_POST_REQUEST);
        request.execute();
    }


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
} //end PerformNetworkRequest

    private void pickLocationOnMap(){
        Intent pickPointIntent = new Intent(this, MapForCoordinateSelection.class);
        startActivityForResult(pickPointIntent, PICK_MAP_POINT_REQUEST);
    }


} //end LostReportActivity
