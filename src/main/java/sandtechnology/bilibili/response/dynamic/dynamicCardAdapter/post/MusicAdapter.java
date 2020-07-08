package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
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
    public WriteOnlyMessage getContent(WriteOnlyMessage out) {
        return out.add(dynamic).newLine().add("\nhttps://www.bilibili.com/audio/au").add(id + "\n").add(ImageManager.getImageData(coverURL));
    }

    @Override
    public String getActionText() {
        return "发布了音频";
    }
}
