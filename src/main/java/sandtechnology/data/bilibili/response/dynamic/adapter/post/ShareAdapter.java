package sandtechnology.data.bilibili.response.dynamic.adapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.adapter.IAdapter;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.utils.ImageManager;

public class ShareAdapter implements IAdapter {
    Vest vest;
    Sketch sketch;

    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage out) {
        return out.add(vest.content).newLine().add(sketch.targetURL).newLine().add(sketch.title).newLine().add(ImageManager.getImageData(sketch.coverURL));
    }

    @Override
    public String getActionText() {
        return "分享了以下内容";
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
