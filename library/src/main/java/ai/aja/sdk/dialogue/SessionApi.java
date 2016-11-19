package ai.aja.sdk.dialogue;

import ai.aja.sdk.dialogue.model.Envelope;
import ai.aja.sdk.dialogue.model.Session;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SessionApi {

    @POST("v1/sessions")
    Call<Session> create(@Body Envelope envelope);

}
