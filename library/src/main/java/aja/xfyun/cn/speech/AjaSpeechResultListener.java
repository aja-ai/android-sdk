package aja.xfyun.cn.speech;

import android.support.annotation.NonNull;

import com.iflytek.cloud.SpeechError;

public interface AjaSpeechResultListener {

    /**
     * 实时输出识别结果
     *
     * @param text 当前识别结果
     */
    void onRecognizing(@NonNull String text);

    /**
     * 识别完成，输出最终结果
     *
     * @param text 最终识别结果
     */
    void onRecognized(@NonNull String text);

    /**
     * 发生错误
     *
     * @param error 错误信息
     */
    void onError(SpeechError error);

}
