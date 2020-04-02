package sandtechnology.bilibili.response.dynamic.display;

import com.google.gson.annotations.SerializedName;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.ImageManager;

public class Emoji {
    @SerializedName("url")
    private final String url;
    @SerializedName("text")
    private String text;
    private ImageManager.CacheImage cacheImage;

    public Emoji(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    public WriteOnlyMessage format(WriteOnlyMessage out) {
        if (url != null && cacheImage == null) {
            cacheImage = ImageManager.getImageData(url + "@20w_20h_1e_1c.png");
        }
        WriteOnlyMessage result = new WriteOnlyMessage();
        out.getContent().forEach(
                pair -> {
                    String str = pair.getFirst();
                    //末尾带有字符
                    boolean addLast = false;
                    int lastWithTextIndex = str.length() - text.length();
                    if (str.lastIndexOf(text) == lastWithTextIndex) {
                        str = str.substring(0, lastWithTextIndex);
                        addLast = true;
                    }
                    String[] strings = str.split("\\[" + text.substring(1, text.length() - 1) + "\\]");
                    if (strings.length > 1) {
                        for (int i = 0; i < strings.length; i++) {
                            if (!strings[i].isEmpty()) {
                                result.add(strings[i]);
                            }
                            if (i != strings.length - 1) {
                                result.add(cacheImage);
                            }
                        }
                    } else {
                        result.add(str);
                    }
                    result.add(pair.getLast());
                    if (addLast) {
                        result.add(cacheImage);
                    }
                }
        );
        return result;
    }
}
