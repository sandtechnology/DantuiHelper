package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.ImageManager;

import java.util.List;

public class ArticleAdapter implements IAdapter {

    @SerializedName("title")
    String title;
    @SerializedName("image_urls")
    List<String> imageURL;
    @SerializedName("id")
    long id;
    @SerializedName("dynamic")
    String text;

    @Override
    public WriteOnlyMessage addMessage(WriteOnlyMessage out, DynamicData dynamicData) {
        return out.add("发了一篇专栏：\n").add(dynamicData.getDisplayContent().getEmojiInfo().format(text)).newLine().add("https://www.bilibili.com/read/cv").add(Long.toString(id)).newLine().add(title).newLine().add(imageURL != null && !imageURL.isEmpty() ? ImageManager.getImageData(imageURL.get(0)) : ImageManager.emptyImage);
    }
}