package ai.aja.sdk.dialogue.model;

import java.util.List;

import ai.aja.sdk.dialogue.model.result.Action;
import ai.aja.sdk.dialogue.model.result.Card;

public class Result {

    public String intro;

    public String text;

    public boolean finish;

    public Action action;

    public List<Card> cards;

}
