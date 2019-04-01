package edu.odu.cs.air411.wherearfthou;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final int MY_REQUEST_INT;
        MY_REQUEST_INT = 177;
        LatLng lostPetMarker = new LatLng(36.887014, -76.302157);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Code if location permissions aren't granted (yet?):
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_INT);
            }
            return;
        } else {
            //Code if permission is granted:
            //Location currentLocation = LocationServices.FusedLocationApi.getLastLocation;
            mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        }


        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(15);

        CreateSampleMarker(lostPetMarker);

        ArrayList<ReportData> foundReports = new ArrayList<>();

        ReportData testReport = new ReportData("Dog running around ODU's campus", "Old Dominion University", "757-141-3422", false);
        foundReports.add(testReport);

        ReportData testReport2 = new ReportData("Dog hiding in the bushes near the Ted", "Ted Constant Convocation Center", "757-163-2422", false);
        foundReports.add(testReport2);

        ReportData testReport3 = new ReportData("Brown cat walking down the sidewalk", "1400 Melrose Pkwy", "N/A", true);
        foundReports.add(testReport3);

        ReportData testReport4 = new ReportData("Black cat sitting on a rock near W 49th St", "1416 W 49th Street, Norfolk Virginia", "catfinder@gmail.com", false);
        foundReports.add(testReport4);

        PopulateReportMap(foundReports);

        try {
            PopulateMapFromJson(GetReports());
        } catch (IOException e)
        {
            //handle the exception
        }


        mMap.moveCamera(CameraUpdateFactory.newLatLng(lostPetMarker));
    }

    public void PopulateReportMap(ArrayList<ReportData> reports) {
        for (int i = 0; i < reports.size(); i++) {

            if (reports.size() == 0)
                break;

            List<Address> addressList = new ArrayList<>();
            Geocoder geocoder = new Geocoder(this);

            ReportData loopData = reports.get(i);

            if (loopData.isFound() == false) {
                loopData.setImage("lost_dog");// + Integer.toString(i));
                try {
                    addressList = geocoder.getFromLocationName(loopData.getLocation(), 1);
                } catch (IOException e) {
                    Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT)
                            .show();
                    e.printStackTrace();

                } finally {
                    Address address = addressList.get(0);

                    if (address.hasLatitude() && address.hasLongitude()) {
                        loopData.setLatitude(address.getLatitude());
                        loopData.setLongitude(address.getLongitude());
                    }
                }

                Marker currentMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(loopData.getLatitude(), loopData.getLongitude()))
                        .title("Lost Pet Sighting"));
                currentMarker.setTag(loopData);
            }
        }//end of for loop
    }//end of PopulateReportMap

    public void CreateSampleMarker(LatLng lostPetMarker)
    {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(lostPetMarker)
                .title("Lost Pet Sighting")
                .snippet("Near 48th Street and Hampton Blvd")
                //.icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE));
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dog_icon));
        ReportData report = new ReportData();
        report.setImage("lost_dog");
        report.setDescription("Saw this white dog roaming with no collar");

        ArrayList<String> tagSample = new ArrayList<String>(Arrays.asList("white", "dog", "small", "no collar"));
        report.setTags(tagSample);
        //info.setContactInfo("Contact me if you have any info at: 123-456-7890");

        CustomInfoWindow customInfoWindow = new CustomInfoWindow(this);
        mMap.setInfoWindowAdapter(customInfoWindow);

        Marker m = mMap.addMarker(markerOptions);
        m.setTag(report);
        //m.showInfoWindow();
    }

    public static ArrayList<ReportData> GetReports() throws IOException
    {
        ArrayList<ReportData> reportsFromJson= new ArrayList<>();

        //URL to access report data in JSON format
        URL url = new URL("http://wherearfthou.duckdns.org/wherearf/db/v1/Api.php?apicall=getreports");

        URLConnection request = url.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

        if (root instanceof JsonObject) {
            Gson gson = new Gson();
            JsonObject jobject = root.getAsJsonObject();
            JsonArray jarray = jobject.get("reports").getAsJsonArray();
            java.lang.reflect.Type listType = new TypeToken<ArrayList<ReportData>>(){}.getType();
            reportsFromJson = gson.fromJson(jarray, listType);
            for(int i = 0; i < reportsFromJson.size(); i++)
            {
                ReportData currentReport = reportsFromJson.get(i);
                currentReport.setLatitude(36.888014 + i/100);
                currentReport.setLongitude(-76.304157 + i/100);
                reportsFromJson.set(i, currentReport);
            }
        }

        return reportsFromJson;
    }

    public void PopulateMapFromJson(ArrayList<ReportData> reports)
    {
        for(int i = 0; i < reports.size(); i++)
        {
            ReportData currentReport = reports.get(i);
            double latitude = .01;
            double longitude = .01;
            currentReport.setLatitude(36.888014 + latitude*i);
            currentReport.setLongitude(-76.304157 + longitude*i);
            currentReport.setImage("lost_dog");

            Marker currentMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(currentReport.getLatitude(), currentReport.getLongitude()))
                    .title("Lost Pet Sighting"));
            currentMarker.setTag(currentReport);

        }
    }
}//end of class
