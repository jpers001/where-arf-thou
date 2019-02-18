package edu.odu.cs.air411.wherearfthou;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



public class TakePhotoActivity extends Activity {

    public final static String DEBUG_TAG = "TakePhotoActivity";
    private Camera camera;
    private int cameraId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int MY_REQUEST_INT =0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo); /** <<<< should be .main? */

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED)
        {
            //get camera permission

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String [] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_INT);
            }
        } //end if permission


        if(!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA))
        {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        }
        else
        {
            cameraId = findFrontFacingCamera();
            if (cameraId < 0)
            {
                Toast.makeText(this, "No Front Facing Camera Found",
                                    Toast.LENGTH_LONG).show();
            }
            else
            {
                camera = Camera.open(cameraId);
                camera.startPreview();
                camera.takePicture(null, null, new PhotoHandler(getApplicationContext()));
            }
        }
    } //end onCreate

    public void onClick (View view)
    {
        camera.startPreview();
        camera.takePicture(null, null, new PhotoHandler(getApplicationContext()));
    } //end onClick

    private int findFrontFacingCamera()
    {
        int cameraId = -1;
        //Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();

        for (int i=0; i< numberOfCameras; i++)
        {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);

            if (info.facing == CameraInfo.CAMERA_FACING_FRONT)
            {
                Log.d(DEBUG_TAG, "Camera Found");
                cameraId=i;
                break;
            }
        }

        return cameraId;
    } //end findFrontFacingCamera

    @Override
    protected void onPause()
    {
        if (camera !=null)
        {
            camera.release();
            camera = null;
        }

        super.onPause();
    }



} //end TakePhotoActivity
