package ai.aja.sdk.dialogue;


import ai.aja.sdk.dialogue.model.Session;
import ai.aja.sdk.dialogue.model.question.Key;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface QuestionsApi {

    @PUT("v1/sessions/{id}")
    Call<Session> question(@Path("id") String id, @Body Key key);

}
