package ai.aja.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.iflytek.cloud.SpeechError;

import ai.aja.sdk.speech.AjaSpeechRecognizer;
import ai.aja.sdk.speech.AjaSpeechResultListener;
import ai.aja.sdk.widget.SonicView;

public class DemoActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_RECORD_AUDIO = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo_acitivity);

        final SonicView sonicView = (SonicView) findViewById(R.id.sonic);
        final TextView textView = (TextView) findViewById(R.id.text);

        final AjaSpeechRecognizer recognizer = AjaSpeechRecognizer.createRecognizer(this);

        recognizer.setVolumeListener(sonicView);

        recognizer.setResultListener(new AjaSpeechResultListener() {
            @Override
            public void onRecognizing(@NonNull String text) {
                textView.setText(text);
            }

            @Override
            public void onRecognized(@NonNull String text) {
                textView.setText(text);
            }

            @Override
            public void onError(SpeechError error) {
            }
        });

        final ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggle);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean checked) {
                if (checked) {
                    textView.setText(null);
                    sonicView.setVisibility(View.VISIBLE);
                    recognizer.startListening();
                } else {
                    sonicView.setVisibility(View.GONE);
                    recognizer.stopListening();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_RECORD_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_RECORD_AUDIO:
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

}
