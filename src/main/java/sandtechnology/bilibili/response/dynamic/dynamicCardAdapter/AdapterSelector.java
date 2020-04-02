package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter;

import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post.*;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.repost.RepostAdapter;
import sandtechnology.holder.WriteOnlyMessage;

import static sandtechnology.utils.JsonHelper.getGsonInstance;

public class AdapterSelector {


    public static WriteOnlyMessage getString(DynamicData data) {
        return getString(data, true);
    }

    public static WriteOnlyMessage getString(DynamicData data, boolean withPrefix) {
        WriteOnlyMessage message = new WriteOnlyMessage(data.getDesc().getUserProfile().getInfo().getUserName());
        if (withPrefix) {
            message.addFirst(new WriteOnlyMessage("动态链接：\n").add("https://t.bilibili.com/").add(data.getDesc().getDynamicID()).add("\n"));
        }
        return getGsonInstance().fromJson(data.getCard(), getAdapter(data.getDesc().getType()))
                .addMessage(message, data)
                //投票信息
                .add(data.getDesc().isRepost() ? "" : data.getExtension().getVoteInfo());
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
            case 4200:
                adapterClass = LiveRoomAdapter.class;
                break;
        }
        return adapterClass;
    }
}
