package sandtechnology.weibo.image;

import com.google.gson.annotations.SerializedName;

public class Dimension {

    private int width;
    private int height;
    @SerializedName("croped")
    private boolean cropped;
}
