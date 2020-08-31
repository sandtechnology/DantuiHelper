package sandtechnology.bilibili.response.live;

import sandtechnology.utils.ImageManager;

public interface IRoomInfo {
    long getRoomID();

    long getStartTime();

    ImageManager.CacheImage getPreview();

    String getUserName();

    default String getRoomURL() {
        return "https://live.bilibili.com/" + getRoomID();
    }


    String getTitle();

    String getParentArea();

    String getSubArea();

    LiveStatus getStatus();
}
