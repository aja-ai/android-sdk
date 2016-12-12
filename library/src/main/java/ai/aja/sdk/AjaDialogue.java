package ai.aja.sdk;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.List;

import ai.aja.sdk.dialogue.SessionApi;
import ai.aja.sdk.dialogue.model.Envelope;
import ai.aja.sdk.dialogue.model.Location;
import ai.aja.sdk.dialogue.model.Result;
import ai.aja.sdk.dialogue.model.Session;
import ai.aja.sdk.dialogue.model.result.Card;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AjaDialogue {

    private final SessionApi sessionApi;

    private final String openId;

    private Client client = new Client();

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

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
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
                final Session session = response.body();
                if (session != null) {
                    client.resolve(session.result);
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
            }
        });
    }

    public static class Client {

        public final void resolve(Result result) {
            resolveText(result);
            resolveAction(result);
            resolveCards(result);
        }

        private void resolveText(Result result) {
            if (!TextUtils.isEmpty(result.intro)) {
                onIntro(result.intro);
            }

            if (!TextUtils.isEmpty(result.text)) {
                onText(result.text);
            }
        }

        protected void onIntro(String intro) {
        }

        protected void onText(String text) {
        }

        private void resolveAction(Result result) {
            if (result.action == null || result.action.uri == null) {
                return;
            }

            final Intent intent = new Intent(Intent.ACTION_VIEW, result.action.uri);

            if (!TextUtils.isEmpty(result.action.target)) {
                intent.setPackage(result.action.target);
            }

            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            onAction(intent);
        }

        protected void onAction(Intent intent) {
        }

        private void resolveCards(Result result) {
            if (result.cards != null && !result.cards.isEmpty()) {
                onCards(result.cards);
            }
        }

        protected void onCards(List<Card> cards) {
        }

    }

}
