package aja.xfyun.cn.widget.html;

import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.InputStream;

public class HtmlLoader implements StreamModelLoader<String> {

    private final Context context;

    public HtmlLoader(Context context) {
        this.context = context;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(String model, int width, int height) {
        return new HtmlRender(context, model, width, height);
    }

}
