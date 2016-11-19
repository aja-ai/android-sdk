package ai.aja.sdk;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

class AjaHttpClientFactory {

    static OkHttpClient createClient(final Context context, final String secret) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(chain.request().newBuilder()
                        .header("Accept", "application/json")
                        .header("Accept-Language", buildAcceptLanguage())
                        .header("User-Agent", buildUserAgent(context))
                        .header("X-SECRET", secret)
                        .build());
            }
        });

        return builder.build();
    }

    private static String buildAcceptLanguage() {
        final Locale locale = Locale.getDefault();
        return String.format(Locale.US, "%s-%s,%s;q=0.8,en-US;q=0.6,en;q=0.4",
                locale.getLanguage(), locale.getCountry(), locale.getLanguage());
    }

    private static String buildUserAgent(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return String.format(Locale.US, "Aja Android SDK %s (%d/%s; %d; %dx%d)",
                BuildConfig.VERSION_NAME,
                Build.VERSION.SDK_INT, Build.VERSION.RELEASE,
                metrics.densityDpi, metrics.widthPixels, metrics.heightPixels);
    }

}
