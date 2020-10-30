package sandtechnology.weibo;

import com.google.gson.annotations.SerializedName;
import sandtechnology.weibo.card.Card;
import sandtechnology.weibo.card.CardListInfo;

import java.util.List;

public class ResponseData {
    @SerializedName("cards")
    private List<Card> cardList;
    @SerializedName("cardlistInfo")
    private CardListInfo info;

    public List<Card> getCardList() {
        return cardList;
    }

    public CardListInfo getInfo() {
        return info;
    }

}
