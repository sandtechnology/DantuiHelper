package sandtechnology.weibo.image;

import com.google.gson.annotations.SerializedName;

public class Image {
    protected String size;
    protected String url;
    @SerializedName("geo")
    protected Dimension dimension;
}
