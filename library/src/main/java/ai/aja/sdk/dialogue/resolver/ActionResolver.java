package ai.aja.sdk.dialogue.resolver;

import android.content.Intent;
import android.text.TextUtils;

import ai.aja.sdk.dialogue.model.Result;

public abstract class ActionResolver implements Resolver {

    @Override
    public boolean resolve(Result result) {
        if (result.action == null || result.action.uri == null) {
            return false;
        }

        final Intent intent = new Intent(Intent.ACTION_VIEW, result.action.uri);

        if (!TextUtils.isEmpty(result.action.target)) {
            intent.setPackage(result.action.target);
        }

        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return onAction(intent);
    }

    protected abstract boolean onAction(Intent intent);

}
