package sandtechnology.weibo.card;

import com.google.gson.annotations.SerializedName;

public class Card {

    @SerializedName("card_type")
    int type;
    @SerializedName("itemid")
    String itemID;
    @SerializedName("mblog")
    CardDetail cardDetail;

    public String getItemID() {
        return itemID;
    }

    public CardDetail getCardDetail() {
        return cardDetail;
    }
}
