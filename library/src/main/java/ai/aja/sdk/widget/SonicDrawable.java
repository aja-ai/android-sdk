package ai.aja.sdk.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * 用于生成水波动画的 Drawable
 */
public class SonicDrawable extends Drawable implements Animatable {

    private final Paint paint = new Paint();

    private final Path path = new Path();

    private final int wave;

    private final ValueAnimator animator;

    private float level = 0f;

    private float currentPeek = 0f;
    private float pendingPeek = 0f;

    private int direction = 1;

    /**
     * 创建一个水波动画 Drawable，默认为 1 个波，黑色，周期 200 毫秒
     */
    public SonicDrawable() {
        this(1);
    }

    /**
     * 创建一个水波动画 Drawable，默认为黑色，周期 200 毫秒
     *
     * @param wave 波的数量
     */
    public SonicDrawable(int wave) {
        this(wave, Color.BLACK);
    }

    /**
     * 创建一个水波动画 Drawable，默认周期 200 毫秒
     *
     * @param wave  波的数量
     * @param color 波的颜色
     */
    public SonicDrawable(int wave, @ColorInt int color) {
        this(wave, color, 200);
    }

    /**
     * 创建一个水波动画 Drawable
     *
     * @param wave   波的数量
     * @param color  波的颜色
     * @param period 波动周期，单位为毫秒
     */
    public SonicDrawable(int wave, @ColorInt int color, long period) {
        this.wave = wave;

        this.paint.setAntiAlias(true);
        setColor(color);

        this.animator = ValueAnimator.ofFloat(0f, 2f);
        this.animator.setRepeatCount(ValueAnimator.INFINITE);
        this.animator.setRepeatMode(ValueAnimator.RESTART);
        this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                applyAnimatedValue((float) animator.getAnimatedValue());
            }
        });
        this.animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                direction = -direction;
                if (currentPeek != pendingPeek) {
                    currentPeek = pendingPeek;
                }
            }
        });

        setPeriod(period);
    }

    /**
     * 设置波动幅度，越大波动越厉害
     *
     * @param level 波动级别
     */
    public void setLevel(float level) {
        this.pendingPeek = level;
    }

    private void applyAnimatedValue(float value) {
        this.level = (1f - (float) Math.pow(value - 1f, 2f)) * currentPeek * direction;
        invalidatePath();
        invalidateSelf();
    }

    /**
     * 设置波动周期，越小波动越厉害
     *
     * @param period 波动周期，单位为毫秒
     */
    public void setPeriod(long period) {
        this.animator.setDuration(period / 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        animator.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        animator.end();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRunning() {
        return animator.isRunning();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        invalidatePath();
        invalidateSelf();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    private void invalidatePath() {
        final float width = getBounds().width();
        final float height = getBounds().height();

        final float middle = height / 2f;

        final float split = width / wave;

        path.reset();
        path.moveTo(0, middle);

        for (int i = 0; i < wave; i++) {
            final float l = split * 0.5f * level * (i % 2 == 0 ? -1 : 1);
            path.quadTo(split * (i + 0.5f), middle + l, split * (i + 1.0f), middle);
        }

        path.lineTo(width, height);
        path.lineTo(0, height);
        path.close();
    }

    /**
     * 设置水波颜色
     *
     * @param color 水波颜色
     */
    public void setColor(@ColorInt int color) {
        paint.setColor(color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

}
