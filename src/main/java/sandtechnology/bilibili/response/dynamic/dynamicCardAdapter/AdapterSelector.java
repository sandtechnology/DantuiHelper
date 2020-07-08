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

    public static WriteOnlyMessage getString(DynamicData data, boolean withLink, String... additionWord) {
        WriteOnlyMessage message = new WriteOnlyMessage();
        if (withLink) {
            message.add("动态链接：\n").add("https://t.bilibili.com/").add(data.getDesc().getDynamicID()).newLine();
        }
        //获取动态解析器
        IAdapter adapter = getGsonInstance().fromJson(data.getCard(), getAdapter(data.getDesc().getType()));
        //添加用户名
        message.add(data.getDesc().getUserProfile().getInfo().getUserName());
        //添加操作的文本
        String actionText = data.getDisplayContent().getActionText();
        message.add(actionText == null || actionText.isEmpty() ? adapter.getActionText() : actionText).add("：").newLine().add(String.join("", additionWord));
        //解析详细内容
        if (adapter instanceof IRepostAdapter) {
            message = ((IRepostAdapter) adapter).getContent(message, data);
        } else {
            message = adapter.getContent(message);
        }
        //解析表情
        message = data.getDisplayContent().getEmojiInfo().format(message);

        return message
                //解析投票信息，转发的动态会重复解析，因此需要避免
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
            case 4098:
            case 4099:
            case 512:
                adapterClass = FilmAdapter.class;
                break;
            case 4200:
                adapterClass = LiveRoomAdapter.class;
                break;
        }
        return adapterClass;
    }
}
