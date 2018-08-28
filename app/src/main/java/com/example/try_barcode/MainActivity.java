package com.example.try_barcode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks, ZXingScannerView.ResultHandler {

    private static final String[] CAMERA_PERMISSION = {
            Manifest.permission.CAMERA
    };
    private  static final int CAMERA_REQCODE = 555;

    //triggering the scanner layout
    ZXingScannerView mScannerView;
    Button btnScanNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScannerView = new ZXingScannerView(this);
        mScannerView.setResultHandler(this);
    }

    public void btnScanNowClicked(View view) {
        doScanTask();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    boolean hasCameraPermissions(){
        return EasyPermissions.hasPermissions(this,CAMERA_PERMISSION);
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(CAMERA_REQCODE)
    public void doScanTask(){
        if(hasCameraPermissions()){
            //todo scan barcode here
            setContentView(mScannerView);
            mScannerView.startCamera();
        }else{
            EasyPermissions.requestPermissions(
                    this,
                    "Please enable camera for this feature",
                    CAMERA_REQCODE,
                    CAMERA_PERMISSION
            );
        }
    }

    @Override
    public void handleResult(Result result) {
        Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT).show();
        mScannerView.stopCameraPreview();
        setContentView(R.layout.activity_main);
        btnScanNow = findViewById(R.id.btnScanNow);
        btnScanNow.setText(result.getText());
    }
}
