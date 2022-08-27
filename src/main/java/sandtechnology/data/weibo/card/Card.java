package sandtechnology.data.weibo.card;

import com.google.gson.annotations.SerializedName;

public class Card {

    @SerializedName("card_type")
    int type;
    @SerializedName("profile_type_id")
    String profileTypeId;
    @SerializedName("itemid")
    String itemID;
    @SerializedName("mblog")
    CardDetail cardDetail;

    public String getItemID() {
        return itemID;
    }

    public boolean isOnTop() {
        return (profileTypeId != null && profileTypeId.startsWith("proweibotop")) || cardDetail.isOnTop();
    }

    public CardDetail getCardDetail() {
        return cardDetail;
    }
}
