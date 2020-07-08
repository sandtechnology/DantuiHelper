package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import com.google.gson.annotations.SerializedName;
import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.holder.WriteOnlyMessage;
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
    public WriteOnlyMessage addMessage(WriteOnlyMessage out, DynamicData dynamicData) {
        return out.add("分享了一部").add(seasonInfo.type).add("：").newLine().add(url).newLine().add(getTitle()).newLine().add(ImageManager.getImageData(coverURL));
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
