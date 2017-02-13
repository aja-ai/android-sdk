package aja.xfyun.cn;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

class AjaManifestUtil {

    @Nullable
    static String resolveSecretFromManifest(Context context) {
        final ApplicationInfo applicationInfo;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }

        final Bundle meta = applicationInfo.metaData;

        if (meta == null) {
            return null;
        }

        return meta.getString("AJA_SECRET", null);
    }

}
