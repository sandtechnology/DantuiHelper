package sandtechnology.data.bilibili.response.dynamic.adapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.adapter.IAdapter;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.utils.ImageManager;

public class MusicAdapter implements IAdapter {

    long id;
    @SerializedName("cover")
    String coverURL;
    @SerializedName("intro")
    String dynamic;

    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage out) {
        return out.add(dynamic).newLine().add("\nhttps://www.bilibili.com/audio/au").add(id + "\n").add(ImageManager.getImageData(coverURL));
    }

    @Override
    public String getActionText() {
        return "发布了音频";
    }
}
