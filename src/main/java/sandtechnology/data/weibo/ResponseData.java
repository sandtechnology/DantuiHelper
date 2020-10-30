package sandtechnology.data.weibo;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.weibo.card.Card;
import sandtechnology.data.weibo.card.CardListInfo;

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
