package sandtechnology.data.bilibili.response.dynamic.cardextension.vote;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.Decodable;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.utils.ImageManager;

public class OptionsItem implements Decodable {

    @SerializedName("btn_str")
    private String btnStr;

    @SerializedName("cnt")
    private int count;

    @SerializedName("idx")
    private int index;

    @SerializedName("img_url")
    private String imageUrl;

    @SerializedName("title")
    private String title;

    @SerializedName("desc")
    private String text;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBtnStr() {
        return btnStr;
    }

    public int getCount() {
        return count;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage message) {
        message.add(index).add(".").add(text);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            message.newLine().add(ImageManager.getImageData(imageUrl));
        }
        return message;
    }
}