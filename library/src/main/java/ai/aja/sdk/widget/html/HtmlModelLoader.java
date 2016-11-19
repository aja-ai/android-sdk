package ai.aja.sdk.widget.html;

import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.InputStream;

public class HtmlModelLoader implements StreamModelLoader<HtmlModelLoader.Html> {

    private final Context context;

    public HtmlModelLoader(Context context) {
        this.context = context;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(Html model, int width, int height) {
        return new HtmlRender(context, model.html, width, height);
    }

    public static class Factory implements ModelLoaderFactory<Html, InputStream> {

        @Override
        public ModelLoader<Html, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new HtmlModelLoader(context);
        }

        @Override
        public void teardown() {
        }

    }

    public static class Html {

        public final String html;

        private Html(String html) {
            this.html = html;
        }

        public static Html from(String html) {
            return new Html(html);
        }

    }

}
