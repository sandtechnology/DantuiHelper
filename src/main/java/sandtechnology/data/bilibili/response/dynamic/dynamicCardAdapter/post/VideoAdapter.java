package sandtechnology.data.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.utils.ImageManager;

public class VideoAdapter implements IAdapter {

    @SerializedName("dynamic")
    String dynamic;
    @SerializedName("aid")
    long aid;
    @SerializedName("pic")
    String picURL;
    @SerializedName("title")
    String title;

    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage out) {
        if (!dynamic.isEmpty()) {
            out.add(dynamic).newLine();
        }
        return out.add(getVideoLink()).newLine().add(title).newLine().add(ImageManager.getImageData(picURL));
    }

    @Override
    public String getActionText() {
        return "投稿了视频";
    }

    private String getVideoLink() {
        return "https://www.bilibili.com/video/" + ("av" + aid);
    }
}
