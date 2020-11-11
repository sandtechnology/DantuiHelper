package sandtechnology.data.weibo.card;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.weibo.image.PageImage;

public class PageInfo {
    @SerializedName("type")
    private String typeStr;
    @SerializedName("object_type")
    private int type;
    @SerializedName("url_ori")
    private String shortURL;
    @SerializedName("page_pic")
    private PageImage image;
    @SerializedName("page_url")
    private String URL;
    @SerializedName("object_id")
    private String id;
    @SerializedName("page_title")
    private String pageTitle;
    private String title;
    @SerializedName("content1")
    private String mainTitle;
    @SerializedName("content2")
    private String subTitle;

    public String getTitle() {
        return title == null ? mainTitle + '：' + subTitle : title;
    }

    public PageImage getImage() {
        return image;
    }
}
