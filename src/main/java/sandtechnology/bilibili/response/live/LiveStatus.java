package sandtechnology.bilibili.response.live;

import org.jetbrains.annotations.NotNull;

public enum LiveStatus {
    NoStreaming("闲置中"),
    Streaming("直播中"),
    PlayingVideo("轮播中"),
    Unknown("未知");
    final String name;

    LiveStatus(String str) {
        name = str;
    }

    @NotNull
    public static LiveStatus getStatus(int roundStatus) {
        switch (roundStatus) {
            case 0:
                return NoStreaming;
            case 1:
                return Streaming;
            case 2:
                return PlayingVideo;
            default:
                return Unknown;
        }
    }

    public String getName() {
        return name;
    }
}
