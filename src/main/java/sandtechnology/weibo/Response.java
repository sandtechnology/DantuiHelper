package sandtechnology.weibo;

import com.google.gson.annotations.SerializedName;
import sandtechnology.weibo.card.Card;
import sandtechnology.weibo.card.CardListInfo;

import java.util.Collections;
import java.util.List;

public class Response {
    private int ok;
    @SerializedName("msg")
    private String message;
    @SerializedName("cards")
    private List<Card> cardList;
    @SerializedName("cardlistInfo")
    private CardListInfo info;

    public boolean isOk() {
        return ok == 1;
    }

    public List<Card> getCardList() {
        return cardList == null ? Collections.emptyList() : cardList;
    }

    public CardListInfo getInfo() {
        return info;
    }

    public String getMessage() {
        return message;
    }
}
