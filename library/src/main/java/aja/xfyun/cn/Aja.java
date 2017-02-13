package aja.xfyun.cn;

import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

public class Aja {

    private static final String XFYUN_APP_ID = "582c0827";

    private static String secretKey;

    private Aja() {
    }

    /**
     * 初始化 AJA 实例
     *
     * @param context Application Context
     */
    public static void init(Context context) {
        init(context, AjaManifestUtil.resolveSecretFromManifest(context));
    }

    public static void init(Context context, String secret) {
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=" + XFYUN_APP_ID);
        secretKey = secret;
    }

    public static AjaDialogue createDialogue(Context context) {
        return createDialogue(context, null);
    }

    public static AjaDialogue createDialogue(Context context, String openId) {
        return new AjaDialogue(context, secretKey, openId);
    }

}
