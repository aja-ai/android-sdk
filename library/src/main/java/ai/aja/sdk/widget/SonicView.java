package ai.aja.sdk.widget;

import android.content.Context;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

import ai.aja.sdk.speech.AjaSpeechVolumeListener;

/**
 * 用于显示录音时的水波动画
 * 可通过集成覆写 {@link #createSonicLayers()} 根据你的需要定制水波外观
 */
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

    /**
     * 覆写这个方法来定制水波外观
     *
     * @return 一个 {@link SonicDrawable} 数组，代表不同的层级
     */
    protected SonicDrawable[] createSonicLayers() {
        return new SonicDrawable[]{
                new SonicDrawable(2, 0xFF50E3C2, 700),
                new SonicDrawable(3, 0xB246DBBA, 500),
                new SonicDrawable(5, 0xCC47CCC2, 400)
        };
    }

    /**
     * 设置波动幅度，越大波动越厉害
     *
     * @param level 波动级别
     */
    public void setLevel(@FloatRange(from = 0.0, to = 1.0) float level) {
        for (SonicDrawable sonic : sonics) {
            sonic.setLevel(Math.max(level, 0.1f));
        }
    }

    /**
     * 设置波动周期，越小波动越厉害
     *
     * @param period 波动周期，单位为毫秒
     */
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
    public final void onVolumeChanged(@FloatRange(from = 0.0, to = 1.0) float volume) {
        Log.d("volume", "volume: " + volume);
        setLevel(volume);
    }

}
