package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.ImageManager;

public class ShareAdapter implements IAdapter {
    Vest vest;
    Sketch sketch;

    @Override
    public WriteOnlyMessage addMessage(WriteOnlyMessage out, DynamicData dynamicData) {
        return out.add("分享了以下内容：").newLine().add(dynamicData.getDisplayContent().getEmojiInfo().format(vest.content)).newLine().add(sketch.targetURL).newLine().add(sketch.title).newLine().add(ImageManager.getImageData(sketch.coverURL));
    }

    static class Vest {
        long uid;
        String content;

    }

    static class Sketch {
        String title;
        @SerializedName("desc_text")
        String desc;
        @SerializedName("cover_url")
        String coverURL;
        @SerializedName("target_url")
        String targetURL;
    }
}
