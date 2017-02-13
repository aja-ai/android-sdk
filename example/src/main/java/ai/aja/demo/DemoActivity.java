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
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.iflytek.cloud.SpeechError;

import java.util.List;

import aja.xfyun.cn.Aja;
import aja.xfyun.cn.AjaDialogue;
import aja.xfyun.cn.dialogue.model.question.Question;
import aja.xfyun.cn.dialogue.model.result.Card;
import aja.xfyun.cn.speech.AjaSpeechRecognizer;
import aja.xfyun.cn.speech.AjaSpeechResultListener;
import aja.xfyun.cn.widget.CardView;

@SuppressLint("SetTextI18n")
public class DemoActivity extends DemoActivityBase implements LocationListener {

    private AjaDialogue dialogue;

    private Location location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建对话管理器

        dialogue = Aja.createDialogue(this);
        dialogue.setClient(new AjaDialogue.Client() {

            @Override
            protected void onText(String text) {
                responseView.setText(text);
            }

            @Override
            protected void onQuestion(Question question, String id) {
                /*Key key = new Key();
                key.key = question.name;
                key.value = "广州塔";

                dialogue.question(key, id, new AjaDialogue.OnQuestionResponse() {
                    @Override
                    public void onResponse(List<Object> values) {
                        responseView.setText(values.toString());
                    }
                });*/
            }

            @Override
            protected void onAction(Intent intent) {
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ignored) {
                }
            }

            @Override
            protected void onCards(List<Card> cards) {
                for (final Card card : cards) {
                    final ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                            getResources().getDimensionPixelSize(R.dimen.card_width),
                            ViewGroup.LayoutParams.WRAP_CONTENT);

                    final CardView cardView = new CardView(DemoActivity.this);
                    cardView.setLayoutParams(lp);
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

                    cardsLayout.addView(cardView);
                }
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
                    cardsLayout.removeAllViews();
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
