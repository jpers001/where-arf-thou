package edu.odu.cs.air411.wherearfthou;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_INT);
            }

            return;
        }else{
            //Code if permission is granted:
            //Location currentLocation = LocationServices.FusedLocationApi.getLastLocation;
            mMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            LatLng currLocation = new LatLng(lat, lng);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLocation, 15));

        }


        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(15);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(lostPetMarker)
                .title("Lost Pet Sighting")
                .snippet("Near 48th Street and Hampton Blvd")
                //.icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE));
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dog_icon));
        InfoWindowData info = new InfoWindowData();
        info.setImage("lost_dog");
        info.setDescription("Description: Saw this white dog roaming with no collar");
        info.setTags("Tags: white, dog, small, no collar");
        //info.setContactInfo("Contact me if you have any info at: 123-456-7890");

        CustomInfoWindow customInfoWindow = new CustomInfoWindow(this);
        mMap.setInfoWindowAdapter(customInfoWindow);

        Marker m = mMap.addMarker(markerOptions);
        m.setTag(info);
        //m.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(lostPetMarker));

        
    }

}
