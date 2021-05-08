package sandtechnology.data.bilibili.response.dynamic.cardextension.goods;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.Decodable;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.utils.ImageManager;

import java.util.List;

public class GoodsCard implements Decodable {

    @SerializedName("list")
    private List<ListItem> list;

    public List<ListItem> getList() {
        return list;
    }

    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage message) {
        IWriteOnlyMessage result = message.add(list.get(0).getAdMark()).add("：").newLine();
        list.forEach(item -> result.add(item.getName()).add(" ").add(item.getPriceStr()).newLine().add(ImageManager.getImageData(item.getImgURL())).newLine());
        return result;
    }
}