package ai.aja.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import ai.aja.sdk.dialogue.model.result.Card;
import ai.aja.sdk.widget.html.HtmlLoader;

public class CardView extends ViewGroup {

    private final ImageView imageView;

    private Card card;

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (isInEditMode()) {
            imageView = null;
            card = new Card();
            card.width = 16;
            card.height = 9;
        } else {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            addView(imageView);
        }
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
        requestLayout();

        final float density = getContext().getResources().getDisplayMetrics().density;

        final float width = 640f * density;
        final float height = width / (float) card.width * (float) card.height;

        Glide.with(getContext())
                .using(new HtmlLoader(getContext()))
                .load(card.html)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override((int) Math.floor(width), (int) Math.floor(height))
                .into(imageView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final float radioWidth, radioHeight;
        if (card != null && card.width > 0 && card.height > 0) {
            radioWidth = card.width;
            radioHeight = card.height;
        } else {
            radioWidth = 16;
            radioHeight = 9;
        }

        float ratio = radioWidth / radioHeight;

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) ((float) width / ratio);

        setMeasuredDimension(width, height);

        if (imageView != null) {
            final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    getMeasuredWidth(), MeasureSpec.EXACTLY);
            final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    getMeasuredHeight(), MeasureSpec.EXACTLY);
            imageView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (imageView != null) {
            imageView.layout(0, 0, right - left, bottom - top);
        }
    }

}
