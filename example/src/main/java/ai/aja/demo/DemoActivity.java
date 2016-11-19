package ai.aja.demo;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;

import com.iflytek.cloud.SpeechError;

import java.util.List;

import ai.aja.sdk.Aja;
import ai.aja.sdk.AjaDialogue;
import ai.aja.sdk.dialogue.model.result.Card;
import ai.aja.sdk.dialogue.resolver.ActionResolver;
import ai.aja.sdk.dialogue.resolver.CardsResolver;
import ai.aja.sdk.dialogue.resolver.TextResolver;
import ai.aja.sdk.speech.AjaSpeechRecognizer;
import ai.aja.sdk.speech.AjaSpeechResultListener;

@SuppressLint("SetTextI18n")
public class DemoActivity extends DemoActivityBase implements LocationListener {

    private AjaDialogue dialogue;

    private Location location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建对话管理器

        dialogue = Aja.createDialogue(this);

        dialogue.registerResolver(new TextResolver() {
            @Override
            protected boolean onIntro(String intro) {
                return false;
            }

            @Override
            protected boolean onText(String text) {
                responseView.setText(text);
                return true;
            }
        });

        dialogue.registerResolver(new ActionResolver() {
            @Override
            protected boolean onAction(Intent intent) {
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ignored) {
                }
                return true;
            }
        });

        dialogue.registerResolver(new CardsResolver() {
            @Override
            protected boolean onCards(List<Card> cards) {
                if (!cards.isEmpty()) {
                    final Card card = cards.get(0);
                    cardView.setCard(card);
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(card.href)));
                            } catch (ActivityNotFoundException ignored) {
                            }
                        }
                    });
                }
                return true;
            }
        });

        // 创建听写

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
                dialogue.start(text, location);
            }

            @Override
            public void onError(SpeechError error) {
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean checked) {
                if (checked) {
                    textView.setText(null);
                    responseView.setText(null);
                    cardView.setCard(null);
                    sonicView.setVisibility(View.VISIBLE);
                    recognizer.startListening();
                } else {
                    sonicView.setVisibility(View.GONE);
                    recognizer.stopListening();
                }
            }
        });
    }

    @Override
    @SuppressWarnings("MissingPermission")
    protected void onPermissionDone() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            locationView.setText(location.toString());
        }
    }

    @Override
    @SuppressWarnings("MissingPermission")
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        DemoActivity.this.location = location;
        locationView.setText(location.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        locationView.setText("GPS 状态：" + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        locationView.setText("GPS 已开启");
    }

    @Override
    public void onProviderDisabled(String provider) {
        locationView.setText("GPS 已关闭");
    }

}
