package ai.aja.sdk.dialogue.resolver;

import android.text.TextUtils;

import ai.aja.sdk.dialogue.model.Result;

public abstract class TextResolver implements Resolver {

    @Override
    public final boolean resolve(Result result) {
        boolean resolved = false;

        if (!TextUtils.isEmpty(result.intro)) {
            resolved |= onIntro(result.intro);
        }

        if (!TextUtils.isEmpty(result.text)) {
            resolved |= onText(result.text);
        }

        return resolved;
    }

    protected abstract boolean onIntro(String intro);

    protected abstract boolean onText(String text);

}
