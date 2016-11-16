package ai.aja.sdk;

import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

public class Aja {

    private static final String APP_ID = "582c0827";

    /**
     * 初始化 AJA 实例
     *
     * @param context Application Context
     */
    public static void init(Context context) {
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=" + APP_ID);
    }

}
