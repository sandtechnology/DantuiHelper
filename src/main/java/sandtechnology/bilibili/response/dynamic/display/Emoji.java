package sandtechnology.bilibili.response.dynamic.display;

import com.google.gson.annotations.SerializedName;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.ImageManager;

public class Emoji {
    @SerializedName("url")
    private final String url;
    @SerializedName("text")
    private final String text;
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
            cacheImage = ImageManager.getImageData(url + "@50w_50h_1e_1c.png");
        }
        WriteOnlyMessage result = new WriteOnlyMessage();
        out.getContent().forEach(
                pair -> {
                    String str = pair.getFirst();
                    //末尾带有表情的识别
                    int addLastCounter = 0;
                    while (str.endsWith(text)) {
                        int lastWithTextIndex = str.length() - text.length();
                        str = str.substring(0, lastWithTextIndex);
                        addLastCounter++;

                    }

                    String[] strings = str.split
                            (
                                    //"[表情]"->"表情"->"\[表情\]"（语法糖的原因后面的]不需要再次加入转义符号）
                                    "\\[" + text.substring(1, text.length() - 1) + "]?"
                            );
                    if (strings.length > 1) {
                        for (int i = 0; i < strings.length; i++) {
                            //防止多余的元素
                            if (!strings[i].isEmpty()) {
                                result.add(strings[i]);
                            }
                            //排除末尾元素，末尾元素不需要加表情
                            if (i != strings.length - 1) {
                                result.add(cacheImage);
                            }
                        }
                    } else {
                        result.add(str);
                    }
                    result.add(pair.getLast());

                    while (addLastCounter > 0) {
                        result.add(cacheImage);
                        addLastCounter--;
                    }
                }
        );
        return result;
    }
}
