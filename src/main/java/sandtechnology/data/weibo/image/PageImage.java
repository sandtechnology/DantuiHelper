package sandtechnology.data.weibo.image;

import com.google.gson.annotations.SerializedName;

public class PageImage {
    private int width;
    private String pid;
    private int source;
    @SerializedName("is_self_cover")
    private int isCustom;
    private int type;
    private String url;
    private int height;

    public String getUrl() {
        return url;
    }
}
