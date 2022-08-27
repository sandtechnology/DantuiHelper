package sandtechnology.data.bilibili.response.dynamic.adapter;

import com.google.gson.reflect.TypeToken;
import sandtechnology.data.bilibili.NormalResponse;
import sandtechnology.data.bilibili.response.dynamic.DynamicData;
import sandtechnology.data.bilibili.response.dynamic.adapter.post.*;
import sandtechnology.data.bilibili.response.dynamic.adapter.repost.RepostAdapter;
import sandtechnology.data.bilibili.response.dynamic.cardextension.CardExtension;
import sandtechnology.data.bilibili.response.dynamic.display.TopicInfo;
import sandtechnology.data.bilibili.response.dynamic.display.contentLink.ContentLinkList;
import sandtechnology.data.bilibili.response.dynamic.extension.VoteInfo;
import sandtechnology.data.bilibili.response.dynamic.lottery.LotteryData;
import sandtechnology.data.bilibili.response.dynamic.rich.DisputeInfo;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.http.BiliBiliHTTPHelper;
import sandtechnology.utils.http.DataGetter;

import java.util.List;
import java.util.Map;

import static sandtechnology.utils.JsonHelper.getGsonInstance;

public class AdapterSelector {


    private final static DataGetter<NormalResponse, LotteryData> lotteryInfoGetter;

    static {
        BiliBiliHTTPHelper biliBiliHTTPHelper = new BiliBiliHTTPHelper("https://api.vc.bilibili.com/lottery_svr/v1/lottery_svr/lottery_notice", null);
        biliBiliHTTPHelper.setOriginURL("https://t.bilibili.com");
        biliBiliHTTPHelper.setReferer("https://t.bilibili.com/lottery/h5/index/");
        lotteryInfoGetter = new DataGetter<>(biliBiliHTTPHelper, new TypeToken<LotteryData>() {
        }, "dynamic_id");

    }

    public static IWriteOnlyMessage getString(DynamicData data) {
        return getString(data, true);
    }

    public static IWriteOnlyMessage getString(DynamicData data, boolean withLink, String... additionWord) {
        IWriteOnlyMessage message = new WriteOnlyMessage();
        if (withLink) {
            message.add("动态链接：\n").add("https://t.bilibili.com/").add(data.getDesc().getDynamicID()).newLine();
        }
        //获取动态解析器
        IAdapter adapter = getGsonInstance().fromJson(data.getCard(), getAdapter(data.getDesc().getType()));
        //添加用户名
        message.add("【").add(data.getDesc().getUserProfile().getInfo().getUserName()).add("】");
        //添加操作的文本
        String actionText = data.getDisplayContent().getActionText();
        message.add(actionText == null || actionText.isEmpty() ? adapter.getActionText() : actionText).add("：").newLine().add(String.join("", additionWord));

        //解析提醒信息
        DisputeInfo disputeInfo = data.getRichMessageInfo().getDisputeInfo();
        if (disputeInfo != null && !disputeInfo.getContent().isEmpty()) {
            message.add("⚠").add(disputeInfo.getContent()).newLine();
        }

        //解析新版话题 手动添加话题名称
        TopicInfo.NewTopic newTopic = data.getDisplayContent().getNewTopicInfo();
        if (newTopic != null) {
            message.add("#").add(newTopic.getName()).add("#").newLine();
        }

        //解析详细内容
        if (adapter instanceof IRepostAdapter) {
            message = ((IRepostAdapter) adapter).getContent(message, data);
        } else {
            message = adapter.getContent(message);
        }

        //解析表情
        message = data.getDisplayContent().getEmojiInfo().format(message);

        //解析需要替换的文本
        ContentLinkList contentLinkList = data.getDisplayContent().getContentLinkList();
        if (contentLinkList != null) {
            Map<String, String> replaceStrMap = contentLinkList.getReplaceStrMap();
            if (!replaceStrMap.isEmpty()) {
                message.getContent().forEach(
                        stringBuilderListPair ->
                                replaceStrMap.forEach(
                                        (oldStr, newStr) -> stringBuilderListPair.setFirst(
                                                new StringBuilder(stringBuilderListPair.getFirst().toString().replace(oldStr, newStr)
                                                )
                                        )
                                )
                );
            }
        }

        //解析扩展卡片
        List<CardExtension> cardExtensionList = data.getDisplayContent().getCardExtensionList();
        if (cardExtensionList != null && !cardExtensionList.isEmpty()) {
            for (CardExtension cardExtension : cardExtensionList) {
                cardExtension.getContent(message);
            }
        }


        if (data.getRichMessageInfo().getLotteryInfo() != null) {
            //解析阿B的特殊字符——互动抽奖
            message.replace("\u200B互动抽奖", "\uD83C\uDF81互动抽奖");
            //添加互动抽奖信息
            lotteryInfoGetter.query(data.getDesc().getDynamicID());
            LotteryData lotteryData = lotteryInfoGetter.getData();
            message.add(lotteryData != null ? lotteryData.toWriteOnlyMessage() : "\n获取失败，请自行前往链接查看");
        }

        //解析阿B的特殊字符——投票
        VoteInfo voteInfo = data.getExtension().getVoteInfo();
        if (voteInfo != null) {
            message.replace("\u200B" + voteInfo.getTitle(), "\ud83d\udcca" + voteInfo.getTitle());
        }

        //移除多余的特殊字符
        message.replace("\u200B", "").replace("\u200D", "");
        return message;
    }

    public static Class<? extends IAdapter> getAdapter(int type) {
        Class<? extends IAdapter> adapterClass = UnknownAdapter.class;
        switch (type) {
            case 1:
                adapterClass = RepostAdapter.class;
                break;
            case 2:
                adapterClass = TextWithPicAdapter.class;
                break;
            case 4:
                adapterClass = PlainTextAdapter.class;
                break;
            case 8:
                adapterClass = VideoAdapter.class;
                break;
            case 16:
                adapterClass = MiniVideoAdapter.class;
                break;
            case 64:
                adapterClass = ArticleAdapter.class;
                break;
            case 256:
                adapterClass = MusicAdapter.class;
                break;
            case 2048:
                adapterClass = ShareAdapter.class;
                break;
            case 4308:
                adapterClass = StreamingAdapter.class;
                break;
            case 4098:
            case 4099:
            case 512:
                adapterClass = FilmAdapter.class;
                break;
            case 4200:
                adapterClass = LiveRoomAdapter.class;
                break;
            case 4300:
                adapterClass = FavoriteAdapter.class;
                break;
        }
        return adapterClass;
    }
}
