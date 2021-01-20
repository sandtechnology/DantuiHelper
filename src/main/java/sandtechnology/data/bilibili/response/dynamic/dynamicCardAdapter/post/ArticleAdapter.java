package sandtechnology.data.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.holder.IWriteOnlyMessage;
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
    public IWriteOnlyMessage getContent(IWriteOnlyMessage out) {
        return out.add(text).newLine().add("https://www.bilibili.com/read/cv").add(Long.toString(id)).newLine().add(title).newLine().add(imageURL != null && !imageURL.isEmpty() ? ImageManager.getImageData(imageURL.get(0)) : ImageManager.emptyImage);
    }

    @Override
    public String getActionText() {
        return "发了一篇专栏";
    }
}