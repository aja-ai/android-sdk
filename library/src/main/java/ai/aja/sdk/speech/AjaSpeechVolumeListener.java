package ai.aja.sdk.speech;

import android.support.annotation.FloatRange;

public interface AjaSpeechVolumeListener {

    void onVolumeChanged(@FloatRange(from = 0.0, to = 1.0) float volume);

}
