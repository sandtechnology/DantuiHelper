package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.bilibili.response.dynamic.element.Picture;
import sandtechnology.holder.WriteOnlyMessage;

import java.util.List;
import java.util.stream.Collectors;

public class TextWithPicAdapter implements IAdapter {

    @SerializedName("item")
    private CommonItem item;

    @Override
    public WriteOnlyMessage addMessage(WriteOnlyMessage out, DynamicData dynamicData) {
        return out.add("发了一条带图动态：\n")
                .add(dynamicData.getDisplayContent().getEmojiInfo().format(new WriteOnlyMessage(item.text + "\n")))
                .add(item.pictures.stream().map(Picture::getCacheImage).collect(Collectors.toList()));
    }

    private static class CommonItem {
        @SerializedName("description")
        private String text;
        @SerializedName("pictures")
        private
        List<Picture> pictures;
    }
}
