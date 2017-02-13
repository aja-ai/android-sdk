package aja.xfyun.cn.dialogue;

import aja.xfyun.cn.dialogue.model.Envelope;
import aja.xfyun.cn.dialogue.model.Session;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SessionApi {

    @POST("v1/sessions")
    Call<Session> create(@Body Envelope envelope);

}
