package sandtechnology.data.bilibili.response.dynamic.adapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.adapter.IAdapter;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.utils.ImageManager;

public class FilmAdapter implements IAdapter {

    @SerializedName("apiSeasonInfo")
    private SeasonInfo seasonInfo;
    private String url;
    @SerializedName("cover")
    private String coverURL;
    @SerializedName("new_desc")
    private String desc;

    public String getTitle() {
        return desc == null || desc.isEmpty() ? seasonInfo.title : desc;
    }

    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage out) {
        return out.add(url).newLine().add(getTitle()).newLine().add(ImageManager.getImageData(coverURL));
    }

    @Override
    public String getActionText() {
        return "分享了一部" + seasonInfo.type;
    }

    private static class SeasonInfo {
        @SerializedName("bgm_type")
        int bgmType;
        @SerializedName("cover")
        String coverURL;
        @SerializedName("is_finish")
        short finish;
        String title;
        @SerializedName("type_name")
        String type;
    }
}
