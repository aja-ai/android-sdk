package ai.aja.sdk.dialogue.resolver;

import java.util.List;

import ai.aja.sdk.dialogue.model.Result;
import ai.aja.sdk.dialogue.model.result.Card;

public abstract class CardsResolver implements Resolver {

    @Override
    public boolean resolve(Result result) {
        return result.cards != null
                && !result.cards.isEmpty()
                && onCards(result.cards);
    }

    protected abstract boolean onCards(List<Card> cards);

}
