package sandtechnology.data.bilibili.response.dynamic.display;

import com.google.gson.annotations.SerializedName;

public class TopicInfo {

    //新话题
    @SerializedName("new_topic")
    private NewTopic newTopic;

    public NewTopic getNewTopic() {
        return newTopic;
    }

    public static class NewTopic {

        @SerializedName("name")
        private String name;

        @SerializedName("link")
        private String link;

        @SerializedName("id")
        private int id;

        public String getName() {
            return name;
        }

        public String getLink() {
            return link;
        }

        public int getId() {
            return id;
        }
    }
}