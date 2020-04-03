package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.DynamicData;
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
    public WriteOnlyMessage addMessage(WriteOnlyMessage out, DynamicData dynamicData) {
        return out.add(dynamicData.getDisplayContent().getActionText()).add("：").add(dynamicData.getDisplayContent().getEmojiInfo().format(dynamic)).newLine().add(getVideoLink(dynamicData)).newLine().add(title).newLine().add(ImageManager.getImageData(picURL));
    }

    private String getVideoLink(DynamicData dynamicData) {
        String bvid = dynamicData.getDesc().getBvid();
        return "https://www.bilibili.com/video/" + (bvid == null ? "av" + aid : bvid);
    }
}
