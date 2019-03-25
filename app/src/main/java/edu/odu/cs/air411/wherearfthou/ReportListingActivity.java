package edu.odu.cs.air411.wherearfthou;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import helper.CheckNetworkStatus;
import helper.HttpJsonParser;

public class ReportListingActivity extends AppCompatActivity {

    private static final String KEY_SUCCESS = "succes";
    private static final String KEY_DATA = "data";
    private static final String KEY_OWNER = "owner";
    private static final String KEY_PET = "pet_name";
    private static final String BASE_URL = "http://wherearfthou.duckdns.org/wherearf/";
    private ArrayList<HashMap<String, String>> reportList;
    private ListView reportListView;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_listing);
        reportListView = (ListView) findViewById(R.id.reportList);
        new FetchReportsAsyncTask().execute();
    }

    //Fetch list of reports from server

    private class FetchReportsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(ReportListingActivity.this);
            pDialog.setMessage("Loading Reports. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

    @Override
        protected String doInBackground(String... params){
        HttpJsonParser httpJsonParser = new HttpJsonParser();
        JSONObject jsonObject = httpJsonParser.makeHTTpRequest(BASE_URL + "fetch_all_reports.php", "GET", null);
        try {
            int success = jsonObject.getInt(KEY_SUCCESS);
            JSONArray reports;
            if (success == 1) {
                reportList = new ArrayList<>();
                reports = jsonObject.getJSONArray(KEY_DATA);
                //Iterate through response, populate report list
                for(int i = 0; i < reports.length(); i++){
                    JSONObject report = reports.getJSONObject(i);
                    String ownerName = report.getString(KEY_OWNER);
                    String petName = report.getString(KEY_PET);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(KEY_OWNER, ownerName);
                    map.put(KEY_PET, petName);
                    reportList.add(map);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    populateReportList();
                }
            });
    }
    }

    //Update parsed JSON data into ListView
    private void populateReportList(){
        ListAdapter adapter = new SimpleAdapter(ReportListingActivity.this, reportList, R.layout.list_item, new String[]{KEY_OWNER, KEY_PET}, new int[]{R.id.ownerName, R.id.petName});

        //update listview
        reportListView.setAdapter(adapter);
        //call report
        reportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //check network connectivity
                if(CheckNetworkStatus.isNetworkAvailable(getApplicationContext())){
                    String owner = ((TextView) view.findViewById(R.id.ownerName)).getText().toString();
                    //Intent intent = new Intent(getApplicationContext(),ReportUpdateDeleteActivity.class);
                    //intent.putExtra(KEY_OWNER, owner);
                    //startActivityForResult(intent, 20);
                } else {
                    Toast.makeText(ReportListingActivity.this, "Unable to connect to databse", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==20){
            //if result code is 20 that means the user has deletec/update the movie
            //so refresh
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}
