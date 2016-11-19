package ai.aja.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import ai.aja.sdk.dialogue.model.result.Card;

public class CardView extends FrameLayout {

    private final WebView webView;

    private Card card;

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        webView = new WebView(context);
        webView.setLayoutParams(lp);
        webView.setEnabled(false);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        addView(webView);
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
        webView.loadUrl("https://www.baidu.com/");
//        webView.loadDataWithBaseURL("file:///", card.html, "text/html; charset=utf-8", null, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (card != null && card.width > 0 && card.height > 0) {
            float ratio = card.width / card.height;

            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);

            if (width > 0) {
                height = (int) ((float) width / ratio);
            } else if (height > 0) {
                width = (int) ((float) height * ratio);
            }

            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
