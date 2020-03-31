package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.bilibili.response.dynamic.element.Picture;
import sandtechnology.holder.MessageOut;

import java.util.List;
import java.util.stream.Collectors;

public class TextWithPicAdapter implements IAdapter {

    @SerializedName("description")
    private String text;
    @SerializedName("pictures")
    private
    List<Picture> pictures;
    @Override
    public MessageOut addMessage(MessageOut out) {
        return out.add("发了一条带图动态：\n")
                .add(text + "\n")
                .add(pictures.stream().map(Picture::getCacheImage).collect(Collectors.toList()));
    }
}
