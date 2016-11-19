package ai.aja.sdk.widget.html;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Locale;

class HtmlRender implements DataFetcher<InputStream> {

    private final Context context;

    private final Handler handler;

    private final BitmapPool bitmapPool;

    private final String html;

    private final int width;
    private final int height;

    private WebView view;

    public HtmlRender(Context context, String html, int width, int height) {
        this.context = context;
        this.handler = new Handler(context.getMainLooper());

        this.bitmapPool = Glide.get(context).getBitmapPool();

        this.html = html;

        this.width = width;
        this.height = height;
    }

    @Override
    public String getId() {
        return String.format(Locale.US, "%d|%d|%s", width, height, html);
    }

    private void createWebView() {
        if (Build.VERSION.SDK_INT >= 21) {
            WebView.enableSlowWholeDocumentDraw();
        }

        view = new WebView(context);

        view.getSettings().setDisplayZoomControls(false);
        view.getSettings().setLoadWithOverviewMode(false);
        view.getSettings().setAllowUniversalAccessFromFileURLs(true);

        view.layout(0, 0, width, height);
    }

    @Override
    public InputStream loadData(Priority priority) throws Exception {
        final HtmlRenderingFuture future = new HtmlRenderingFuture();

        handler.post(new Runnable() {
            @Override
            public void run() {
                createWebView();

                view.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                render(future);
                            }
                        }, 1000);
                    }
                });

                view.loadDataWithBaseURL("file:///", html, "text/html; charset=utf-8", null, null);
            }
        });

        final Bitmap bitmap = future.get();

        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        if (!bitmapPool.put(bitmap)) {
            bitmap.recycle();
        }

        return new ByteArrayInputStream(stream.toByteArray());
    }

    private void render(HtmlRenderingFuture future) {
        if (view == null) {
            return;
        }

        Bitmap bitmap = bitmapPool.get(width, height, Bitmap.Config.RGB_565);

        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        }

        final Canvas canvas = new Canvas(bitmap);

        view.draw(canvas);

        future.done(bitmap);
    }

    @Override
    public void cancel() {
        handler.removeCallbacksAndMessages(null);

        if (view != null) {
            view.stopLoading();
            view = null;
        }
    }

    @Override
    public void cleanup() {
        handler.removeCallbacksAndMessages(null);
        view = null;
    }

}
