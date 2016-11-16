package ai.aja.sdk.widget;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

import ai.aja.sdk.speech.AjaSpeechVolumeListener;

public class SonicView extends View implements AjaSpeechVolumeListener {

    private final SonicDrawable[] sonics;

    public SonicView(Context context) {
        this(context, null);
    }

    public SonicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SonicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.sonics = createSonicLayers();
        setBackground(new LayerDrawable(this.sonics));

        if (isInEditMode()) {
            setLevel(1f);
        }
    }

    protected SonicDrawable[] createSonicLayers() {
        return new SonicDrawable[]{
                new SonicDrawable(2, 0xFF50E3C2, 700),
                new SonicDrawable(3, 0xB246DBBA, 500),
                new SonicDrawable(5, 0xCC47CCC2, 400)
        };
    }

    public void setLevel(float level) {
        for (SonicDrawable sonic : sonics) {
            sonic.setLevel(Math.max(level, 0.1f));
        }
    }

    public void setPeriod(long period) {
        final Random random = new Random();
        for (SonicDrawable sonic : sonics) {
            sonic.setPeriod(period + (long) (random.nextFloat() * 50f));
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (SonicDrawable sonic : sonics) {
            sonic.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (SonicDrawable sonic : sonics) {
            sonic.stop();
        }
    }

    @Override
    public void onVolumeChanged(@FloatRange(from = 0.0, to = 1.0) float volume) {
        Log.d("volume", "volume: " + volume);
        setLevel(volume);
    }

}
