package ai.aja.sdk.speech;

import android.support.annotation.FloatRange;

public interface AjaSpeechVolumeListener {

    /**
     * 用户说话音量变化
     *
     * @param volume 当前实时音量
     */
    void onVolumeChanged(@FloatRange(from = 0.0, to = 1.0) float volume);

}
