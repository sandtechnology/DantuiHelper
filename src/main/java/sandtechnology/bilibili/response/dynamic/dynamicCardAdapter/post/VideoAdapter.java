package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.holder.WriteOnlyMessage;
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
    public WriteOnlyMessage getContent(WriteOnlyMessage out) {
        return out.add(dynamic).newLine().add(getVideoLink()).newLine().add(title).newLine().add(ImageManager.getImageData(picURL));
    }

    @Override
    public String getActionText() {
        return "发布了一个视频";
    }

    private String getVideoLink() {
        return "https://www.bilibili.com/video/" + ("av" + aid);
    }
}
