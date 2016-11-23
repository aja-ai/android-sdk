package ai.aja.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import ai.aja.sdk.widget.SonicView;

public class DemoActivityBase extends AppCompatActivity {

    protected SonicView sonicView;
    protected TextView textView;
    protected TextView responseView;
    protected ViewGroup cardsLayout;
    protected ToggleButton toggleButton;
    protected TextView locationView;

    protected LocationManager locationManager;

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo_acitivity);

        sonicView = (SonicView) findViewById(R.id.sonic);
        textView = (TextView) findViewById(R.id.text);
        responseView = (TextView) findViewById(R.id.response);
        cardsLayout = (ViewGroup) findViewById(R.id.cards);
        toggleButton = (ToggleButton) findViewById(R.id.toggle);
        locationView = (TextView) findViewById(R.id.location);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (checkPermission(Manifest.permission.RECORD_AUDIO)
                && checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            onPermissionDone();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION,
            }, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        for (int grant : grantResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
                finish();
                break;
            }
        }

        onPermissionDone();
    }

    protected void onPermissionDone() {
    }

}
