package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.bilibili.response.dynamic.element.Picture;
import sandtechnology.holder.WriteOnlyMessage;

import java.util.List;
import java.util.stream.Collectors;

public class TextWithPicAdapter implements IAdapter {

    @SerializedName("item")
    private CommonItem item;

    @Override
    public WriteOnlyMessage getContent(WriteOnlyMessage out) {
        return out.add(item.text).newLine()
                .add(item.pictures.stream().map(Picture::getCacheImage).collect(Collectors.toList()));
    }

    @Override
    public String getActionText() {
        return "发了一条带图动态";
    }

    private static class CommonItem {
        @SerializedName("description")
        private String text;
        @SerializedName("pictures")
        private
        List<Picture> pictures;
    }
}
