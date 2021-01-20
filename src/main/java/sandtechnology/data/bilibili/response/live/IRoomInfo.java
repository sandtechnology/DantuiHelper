package sandtechnology.data.bilibili.response.live;

import sandtechnology.utils.CacheImage;

public interface IRoomInfo {
    long getRoomID();

    long getStartTime();

    CacheImage getPreview();

    String getUserName();

    default String getRoomURL() {
        return "https://live.bilibili.com/" + getRoomID();
    }


    String getTitle();

    String getParentArea();

    String getSubArea();

    LiveStatus getStatus();
}
