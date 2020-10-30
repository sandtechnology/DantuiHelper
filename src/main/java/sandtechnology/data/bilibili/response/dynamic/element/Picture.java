package sandtechnology.data.bilibili.response.dynamic.element;

import com.google.gson.annotations.SerializedName;
import sandtechnology.utils.ImageManager;

public class Picture {
    @SerializedName("img_src")
    private String imgUrl;
    private ImageManager.CacheImage cacheImage;

    public ImageManager.CacheImage getCacheImage() {
        if (cacheImage == null) {
            cacheImage = ImageManager.getImageData(imgUrl);
        }
        return cacheImage;
    }
}
