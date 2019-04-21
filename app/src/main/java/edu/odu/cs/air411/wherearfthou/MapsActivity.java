package edu.odu.cs.air411.wherearfthou;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
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
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import helper.Report;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, ReportGetter.ReportGetterListener {

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
        mMap.setMinZoomPreference(16);
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        CustomInfoWindow customInfoWindow = new CustomInfoWindow(this);
        mMap.setInfoWindowAdapter(customInfoWindow);

        new ReportGetter(this).execute();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(lostPetMarker));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ///TODO: A proper method of zooming, currently it's a rough implementation and doesn't work if the
                ///TODO: user zooms in, or if the info window is very tall (or very short)

                final LatLng newLatLng = new LatLng(marker.getPosition().latitude + .004, marker.getPosition().longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(newLatLng));
                marker.showInfoWindow();
                return true;
            }
        });

    }

    public void onReportGetterCompleted(ArrayList<ReportData> reports)
    {

        for (int i = 0; i < reports.size(); i++) {

            List<Address> addressList = new ArrayList<>();
            Geocoder geocoder = new Geocoder(this);

            ReportData currentReport = reports.get(i);

            if (currentReport.isFound() == false) {
                byte[] decodedString = Base64.decode(currentReport.getImage(), Base64.DEFAULT);

                Bitmap image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if(currentReport.getLocation() != "") {
                    try {
                        addressList = geocoder.getFromLocationName(currentReport.getLocation(), 1);
                    } catch (IOException e) {
                        Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT)
                                .show();
                        e.printStackTrace();

                    } finally {
                        if(addressList.size() != 0) {
                            Address address = addressList.get(0);

                            if (address.hasLatitude() && address.hasLongitude()) {
                                currentReport.setLatitude(address.getLatitude());
                                currentReport.setLongitude(address.getLongitude());
                            }
                        }//end of if addressList.size() != 0
                    }
                }
                else
                {
                    currentReport.setLatitude(36.888014);
                    currentReport.setLongitude(-76.304157 + .001*i);
                }
                String title = "";
                if(currentReport.getName().equals("Unknown"))
                {
                    title = title + "Pet Sighting";
                }
                else{
                    title = title + "Lost Pet Report";
                }
                Marker currentMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(currentReport.getLatitude(), currentReport.getLongitude()))
                        .title(title)
                        .icon(BitmapDescriptorFactory.fromBitmap(getCroppedBitmap(image))));
                currentMarker.setTag(currentReport);
            }
        }//end of for loop
    }

    public void onReportGetterError(String error)
    {
        System.out.println(error);
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        Bitmap _bmp = Bitmap.createScaledBitmap(output, 150, 150, true);
        return _bmp;
        //return output;
    }

}//end of class
