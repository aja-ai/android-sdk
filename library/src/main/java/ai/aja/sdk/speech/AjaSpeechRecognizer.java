package ai.aja.sdk.speech;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

/**
 * 语音识别类
 */
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

    /**
     * 创建语音识别实例
     *
     * @param context Activity Context
     * @return 一个新的 {@link AjaSpeechRecognizer} 实例
     */
    public static AjaSpeechRecognizer createRecognizer(Context context) {
        return new AjaSpeechRecognizer(context);
    }

    /**
     * 设置动作事件监听器
     *
     * @param actionListener 动作事件监听器
     */
    public void setActionListener(AjaSpeechActionListener actionListener) {
        this.actionListener = actionListener;
    }

    /**
     * 设置音量事件监听器
     *
     * @param volumeListener 音量事件监听器
     */
    public void setVolumeListener(AjaSpeechVolumeListener volumeListener) {
        this.volumeListener = volumeListener;
    }

    /**
     * 设置识别结果监听器
     *
     * @param resultListener 音量结果监听器
     */
    public void setResultListener(AjaSpeechResultListener resultListener) {
        this.resultListener = resultListener;
    }

    /**
     * 开始录音及识别
     */
    public void startListening() {
        this.recognizer.startListening(this.recognizerListener);
    }

    /**
     * 停止录音及识别
     */
    public void stopListening() {
        this.recognizer.stopListening();
    }

    /**
     * 取消当前识别动作
     */
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
