package aja.xfyun.cn;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.List;

import aja.xfyun.cn.dialogue.QuestionsApi;
import aja.xfyun.cn.dialogue.SessionApi;
import aja.xfyun.cn.dialogue.model.Data;
import aja.xfyun.cn.dialogue.model.Envelope;
import aja.xfyun.cn.dialogue.model.Location;
import aja.xfyun.cn.dialogue.model.Result;
import aja.xfyun.cn.dialogue.model.Session;
import aja.xfyun.cn.dialogue.model.question.Key;
import aja.xfyun.cn.dialogue.model.question.Question;
import aja.xfyun.cn.dialogue.model.question.Questions;
import aja.xfyun.cn.dialogue.model.result.Card;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AjaDialogue {

    private static final String TAG = "AjaDialogue";

    private final SessionApi sessionApi;
    private final QuestionsApi questionsApi;

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
        questionsApi = retrofit.create(QuestionsApi.class);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    /**
     * 请求 question 接口
     * @param key
     * @param id
     */
    public void question(@NonNull final Key key, String id) {
        questionsApi.question(id, key).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                final Session session = response.body();
                if (session != null) {
                    client.resolve(session);
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                t.printStackTrace();
            }
        });
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
                    client.resolve(session);
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static class Client {

        public final void resolve(Session session) {
            resolveText(session);
            resolveAction(session.result);
            resolveCards(session.result);
        }

        private void resolveText(Session session) {
            final Result result = session.result;
            final Data data = session.data;
            if (result == null) {
                if (data.questions.size() > 0) {
                    Questions questions = data.questions.get(0);
                    onText(questions.text);
                    onQuestion(questions.question, session.id);
                }
            } else {
                if (!TextUtils.isEmpty(result.text)) {
                    onText(result.text);
                }

                if (!TextUtils.isEmpty(result.intro)) {
                    onIntro(result.intro);
                }
            }
        }

        protected void onIntro(String intro) {
        }

        protected void onText(String text) {
        }

        protected void onQuestion(Question question, String id) {
        }

        private void resolveAction(Result result) {
            if (result == null) return;

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
            if (result == null) return;

            if (result.cards != null && !result.cards.isEmpty()) {
                onCards(result.cards);
            }
        }

        protected void onCards(List<Card> cards) {
        }

    }

}
