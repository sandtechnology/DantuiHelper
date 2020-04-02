package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.ImageManager;

public class MusicAdapter implements IAdapter {

    long id;
    @SerializedName("cover")
    String coverURL;
    @SerializedName("intro")
    String dynamic;

    @Override
    public WriteOnlyMessage addMessage(WriteOnlyMessage out, DynamicData dynamicData) {
        return out.add("发布了音频：").add(dynamicData.getDisplayContent().getEmojiInfo().format(dynamic)).add("\nhttps://www.bilibili.com/audio/au").add(id + "\n").add(ImageManager.getImageData(coverURL));
    }
}
