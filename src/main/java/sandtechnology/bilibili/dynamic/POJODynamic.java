package sandtechnology.bilibili.dynamic;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import sandtechnology.utils.JsonHelper;

import java.util.List;

public class POJODynamic {

    @SerializedName("has_more")
    private
    int hasMore;

    @SerializedName("cards")
    private
    List<POJOCard> dynamics;

    @SerializedName("next_offset")
    private
    long nextPageOffsetById;

    public boolean hasMore() {
        return hasMore==1;
    }

    public List<POJOCard> getDynamics() {
        return dynamics;
    }

    public long getNextPageOffsetById() {
        return nextPageOffsetById;
    }

    public static class POJOCard {


        @SerializedName("desc")
        JsonObject desc;
        @SerializedName("user_profile")
        JsonObject profile;

        String dynamicID;
        long timestamp;

        int type;

        @SerializedName("card")
        String info;


        POJOCard init() {
            if(desc!=null) {
                dynamicID = desc.get("dynamic_id_str").getAsString();
                type = desc.get("type").getAsInt();
                timestamp=desc.get("timestamp").getAsLong();
            }
            return this;
        }

        public long getTimestamp() {
            if(dynamicID==null){
                init();
            }
            return timestamp;
        }

        public long getAuthorUID(){
            return desc.getAsJsonObject("user_profile").getAsJsonObject("info").get("uid").getAsLong();
        }

        public long getDynamicID() {
            if(dynamicID==null){
                init();
            }
            return Long.parseLong(dynamicID);
        }


        public String getInfo() {
            if(dynamicID==null){
                init();
            }
            String result="动态链接："+getURL()+"\n"+desc.getAsJsonObject("user_profile").getAsJsonObject("info").get("uname").getAsString()+
                    Dynamic.DynamicItem.getType(type).getInfo(JsonHelper.fromJson(info, Dynamic.class).init());
            if(EmojiManager.containsEmoji(result)){
                result=EmojiParser.parseToHtmlDecimal(result).replaceAll("&#(?<id>[0-9]*);","[CQ:emoji,id=${id}]");
            }
            return result;
        }

        String getURL() {
            return "https://t.bilibili.com/"+dynamicID;
        }
    }

}
