package ai.aja.sdk;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;

import ai.aja.sdk.dialogue.SessionApi;
import ai.aja.sdk.dialogue.model.Envelope;
import ai.aja.sdk.dialogue.model.Location;
import ai.aja.sdk.dialogue.model.Session;
import ai.aja.sdk.dialogue.resolver.Resolver;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AjaDialogue {

    private final ArrayList<Resolver> resolvers = new ArrayList<>();

    private final SessionApi sessionApi;

    private final String openId;

    AjaDialogue(Context context, String secret, String openId) {
        this.openId = openId;

        final Gson gson = AjaGsonFactory.createGson();

        final OkHttpClient httpClient = AjaHttpClientFactory.createClient(context, secret);

        final Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BuildConfig.AJA_API_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        sessionApi = retrofit.create(SessionApi.class);
    }

    public void registerResolver(Resolver resolver) {
        resolvers.add(resolver);
    }

    public void start(String text) {
        start(text, null);
    }

    public void start(String text, android.location.Location location) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

        final Envelope envelope = new Envelope();

        envelope.text = text;

        if (location != null) {
            envelope.location = new Location();
            envelope.location.latitude = location.getLatitude();
            envelope.location.longitude = location.getLongitude();
        }

        if (openId != null) {
            envelope.openId = openId;
        }

        sessionApi.create(envelope).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                resolve(response.body());
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
            }
        });
    }

    private void resolve(Session session) {
        if (session == null) {
            return;
        }

        for (Resolver resolver : resolvers) {
            resolver.resolve(session.result);
        }
    }

}
