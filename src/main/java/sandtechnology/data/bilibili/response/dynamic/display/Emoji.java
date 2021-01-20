package sandtechnology.data.bilibili.response.dynamic.display;

import com.google.gson.annotations.SerializedName;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;
import sandtechnology.utils.CacheImage;
import sandtechnology.utils.ImageManager;
import sandtechnology.utils.Pair;

import java.util.List;

public class Emoji {
    @SerializedName("url")
    private final String url;
    @SerializedName("text")
    private final String text;
    private CacheImage cacheImage;

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

    public IWriteOnlyMessage format(IWriteOnlyMessage out) {
        if (url != null && cacheImage == null) {
            cacheImage = ImageManager.getImageData(url + "@50w_50h_1e_1c.png");
        }
        IWriteOnlyMessage result = new WriteOnlyMessage();
        for (Pair<StringBuilder, List<CacheImage>> pair : out.getContent()) {
            StringBuilder str = pair.getFirst();
            //末尾表情的识别
            int addLastCounter = 0;
            int lastWithTextIndex;
            while (str.lastIndexOf(text) == (lastWithTextIndex = str.length() - text.length()) && lastWithTextIndex >= 0) {
                str.delete(lastWithTextIndex, str.length());
                addLastCounter++;
            }


            //按表情分隔
            String[] strings = str.toString().split
                    (
                            //正则生成：
                            //"[表情]"->"表情"->"\[表情\]"（语法糖的原因后面的]不需要再次加入转义符号）
                            "\\[" + text.substring(1, text.length() - 1) + "]?"
                    );
            if (strings.length > 1) {
                for (int i = 0; i < strings.length; i++) {
                    //防止多余的元素
                    if (!strings[i].isEmpty()) {
                        result.add(strings[i]);
                    }
                    //排除末尾元素，因使用表情作为分隔符
                    //表情必须从中间加入，末尾元素会在后面处理
                    if (i != strings.length - 1) {
                        result.add(cacheImage);
                    }
                }
            } else {
                //添加原有的字符串
                result.add(str.toString());
            }
            //添加原有的图片
            result.add(pair.getLast());

            //处理末尾图片
            while (addLastCounter > 0) {
                result.add(cacheImage);
                addLastCounter--;
            }
        }
        return result;
    }
}
