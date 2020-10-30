package sandtechnology.weibo.image;

import com.google.gson.annotations.SerializedName;

public class WeiboImage extends Image {

    private String pid;
    @SerializedName("large")
    private Image largeSize;

    public String getPid() {
        return pid;
    }

    public Image getLargeSize() {
        return largeSize;
    }
}
