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
        return out.add("分享了以下内容：").add(dynamicData.getDisplayContent().getEmojiInfo().format(vest.content)).add("\n").add(sketch.targetURL).add("\n").add(sketch.title).add("\n").add(ImageManager.getImageData(sketch.coverURL));
    }

    class Vest {
        long uid;
        String content;

    }

    class Sketch {
        String title;
        @SerializedName("desc_text")
        String desc;
        @SerializedName("cover_url")
        String coverURL;
        @SerializedName("target_url")
        String targetURL;
    }
}
