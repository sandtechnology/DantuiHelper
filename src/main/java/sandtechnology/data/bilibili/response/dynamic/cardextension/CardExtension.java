package sandtechnology.data.bilibili.response.dynamic.cardextension;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.Decodable;
import sandtechnology.data.bilibili.response.dynamic.cardextension.goods.GoodsCard;
import sandtechnology.data.bilibili.response.dynamic.cardextension.related.RelatedCard;
import sandtechnology.data.bilibili.response.dynamic.cardextension.reserve.ReserveCard;
import sandtechnology.data.bilibili.response.dynamic.cardextension.ugc.UGCCard;
import sandtechnology.data.bilibili.response.dynamic.cardextension.vote.VoteCard;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.utils.JsonHelper;

public class CardExtension implements Decodable {

    /**
     *
     */
    @SerializedName("add_on_card_show_type")
    private int extensionType;

    @SerializedName("goods_card")
    private String goodsCard;
    @SerializedName("attach_card")
    private RelatedCard relatedCard;
    @SerializedName("reserve_attach_card")
    private ReserveCard reserveCard;
    @SerializedName("vote_card")
    private String voteCard;
    @SerializedName("ugc_attach_card")
    private UGCCard ugcCard;

    public ExtensionType getExtensionType() {
        for (ExtensionType value : ExtensionType.values()) {
            if (value.id == extensionType) {
                return value;
            }
        }
        return ExtensionType.Unknown;
    }

    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage message) {
        //换行
        message.newLine().add("===").newLine();
        switch (extensionType) {
            //推荐商品卡片
            case 1:
                return JsonHelper.fromJson(goodsCard, GoodsCard.class).getContent(message);
            //相关内容卡片
            case 2:
                return relatedCard.getContent(message);
            //投票内容卡片
            case 3:
                return JsonHelper.fromJson(voteCard, VoteCard.class).getContent(message);
            //内容分享卡片
            case 5:
                return ugcCard.getContent(message);
            //预约内容卡片
            case 6:
                return reserveCard.getContent(message);
            default:
                return message.add("未知内容扩展类型" + extensionType + "，请打开动态查看");

        }
    }


    public enum ExtensionType {
        GOODS(1),
        RELATED(2),
        RESERVE(6),
        VOTE(3),
        UGC(5),
        Unknown(-1);
        final int id;

        ExtensionType(int id) {
            this.id = id;
        }
    }
}