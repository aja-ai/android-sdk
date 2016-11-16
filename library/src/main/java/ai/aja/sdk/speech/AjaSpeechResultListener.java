package ai.aja.sdk.speech;

import android.support.annotation.NonNull;

import com.iflytek.cloud.SpeechError;

public interface AjaSpeechResultListener {

    void onRecognizing(@NonNull String text);

    void onRecognized(@NonNull String text);

    void onError(SpeechError error);

}
