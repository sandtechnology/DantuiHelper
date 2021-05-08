package sandtechnology.data.bilibili.response.dynamic.adapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.adapter.IAdapter;
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
        if (text != null) {
            out.add(text).newLine();
        }
        return out.add("https://www.bilibili.com/read/cv").add(Long.toString(id)).newLine().add(title).newLine().add(imageURL != null && !imageURL.isEmpty() ? ImageManager.getImageData(imageURL.get(0)) : ImageManager.emptyImage);
    }

    @Override
    public String getActionText() {
        return "发了一篇专栏";
    }
}