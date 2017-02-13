package aja.xfyun.cn.dialogue;


import android.support.annotation.NonNull;

import aja.xfyun.cn.dialogue.model.Session;
import aja.xfyun.cn.dialogue.model.question.Key;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface QuestionsApi {

    @PUT("v1/sessions/{id}")
    Call<Session> question(@Path("id") String id, @Body @NonNull Key key);

}
