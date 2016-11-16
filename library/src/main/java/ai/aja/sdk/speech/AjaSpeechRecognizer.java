package ai.aja.sdk.speech;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

public class AjaSpeechRecognizer {

    private final SpeechRecognizer recognizer;
    private final RecognizerListener recognizerListener;

    private AjaSpeechActionListener actionListener = null;
    private AjaSpeechVolumeListener volumeListener = null;
    private AjaSpeechResultListener resultListener = null;

    private AjaSpeechRecognizer(Context context) {
        this.recognizer = SpeechRecognizer.createRecognizer(context, null);
        this.recognizer.setParameter(SpeechConstant.VAD_EOS, "30000");
        this.recognizer.setParameter(SpeechConstant.RESULT_TYPE, "plain");

        this.recognizerListener = new InternalRecognizerListener();
    }

    public static AjaSpeechRecognizer createRecognizer(Context context) {
        return new AjaSpeechRecognizer(context);
    }

    public void setActionListener(AjaSpeechActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void setVolumeListener(AjaSpeechVolumeListener volumeListener) {
        this.volumeListener = volumeListener;
    }

    public void setResultListener(AjaSpeechResultListener resultListener) {
        this.resultListener = resultListener;
    }

    public void startListening() {
        this.recognizer.startListening(this.recognizerListener);
    }

    public void stopListening() {
        this.recognizer.stopListening();
    }

    public void cancel() {
        this.recognizer.cancel();
    }

    private final class InternalRecognizerListener implements RecognizerListener {

        private final StringBuilder buffer = new StringBuilder();

        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            if (volumeListener != null) {
                volumeListener.onVolumeChanged(i / 30f);
            }
        }

        @Override
        public void onBeginOfSpeech() {
            if (actionListener != null) {
                actionListener.onSpeechStart();
            }
        }

        @Override
        public void onEndOfSpeech() {
            if (actionListener != null) {
                actionListener.onSpeechEnd();
            }
        }

        @Override
        public void onResult(RecognizerResult result, boolean last) {
            if (result == null) {
                return;
            }

            if (resultListener == null) {
                return;
            }

            final String text = result.getResultString();

            if (!TextUtils.isEmpty(text)) {
                buffer.append(text);
                resultListener.onRecognizing(buffer.toString());
            }

            if (last) {
                resultListener.onRecognized(buffer.toString());
                buffer.setLength(0);
            }
        }

        @Override
        public void onError(SpeechError error) {
            if (resultListener != null) {
                resultListener.onError(error);
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }

    }

}
