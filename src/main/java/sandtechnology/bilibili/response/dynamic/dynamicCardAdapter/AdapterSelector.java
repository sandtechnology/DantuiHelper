package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter;

import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post.PlainTextAdapter;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post.TextWithPicAdapter;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post.VoteAdapter;
import sandtechnology.holder.MessageOut;

import static sandtechnology.utils.JsonHelper.getGsonInstance;

class AdapterSelector {

    private static MessageOut getString(DynamicData data) {
        MessageOut messageOut = new MessageOut();
        Class<? extends IAdapter> adapterClass = null;
        switch (data.getDesc().getType()){
            case 1:
                adapterClass = PlainTextAdapter.class;
                break;
            case 2:
                adapterClass = TextWithPicAdapter.class;
                break;
            case 4:
                adapterClass = VoteAdapter.class;
                break;
               // adapter=getGsonInstance().fromJson(data.getCard(),)
        }
        if (adapterClass != null) {
            getGsonInstance().fromJson(data.getCard(), adapterClass).addMessage(messageOut);
            messageOut.add(data.getExtension().getVoteInfo());
        } else {
            messageOut.add("发了一条无法解析的动态：");
        }
        data.getDisplayContent().getEmojiInfo().format(messageOut);
        messageOut.addFirst(data.getDesc().getUserProfile().getInfo().getUserName());
        return messageOut;
    }
}
