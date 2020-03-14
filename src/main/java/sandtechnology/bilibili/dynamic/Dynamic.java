package sandtechnology.bilibili.dynamic;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import sandtechnology.utils.ImageManager;

import java.util.List;
import java.util.stream.Collectors;

public class Dynamic {

    @SerializedName("item")
    private
    DynamicItem item;

    @SerializedName("origin_user")
    private
    JsonObject originUserObject;

    @SerializedName("origin")
    private
    String originJson;

    private String originAuthor;

    //video
    @SerializedName("dynamic")
    private
    String videoComment;
    private long aid;
    @SerializedName("pic")
    private
    String videoPic;

    private String title;

    //streaming
    private String roomid;
    @SerializedName("round_status")
    private
    int status;
    @SerializedName("uname")
    private
    String streamer;

    //widget

    @SerializedName("vest")
    private
    DynamicVest vest;
    @SerializedName("sketch")
    private
    DynamicSketch sketch;

    //article
    @SerializedName("image_urls")
    private
    List<String> articleImageURL;
    @SerializedName("words")
    private
    int totalWords;


    public Dynamic init(){
        if(originUserObject!=null){
            JsonObject infoObject=originUserObject.getAsJsonObject("info");
                originAuthor = infoObject.get("uname").getAsString();
        }
        return this;
    }

    interface IDynamicInfo{
        String getInfo(Dynamic json);
    }
    enum DynamicType implements IDynamicInfo {
        Unknown{
            public String getInfo(Dynamic json) {
                return "有新动态，但动态解析失败";
            }
        },PlainText {
            public String getInfo(Dynamic json) {
               return "发了一条动态：\n"+json.item.content;
            }
        }, RePostWithPlainText {
            public String getInfo(Dynamic json) {
                if(json.item.delete==1){
                    return "转发了一条已被删除的动态："+json.item.content;
                }
                Dynamic originDynamic=new Gson().fromJson(json.originJson, Dynamic.class);
                if(originDynamic.roomid!=null){
                    return "转发了"+originDynamic.streamer+"的直播间："+json.item.content
                            + "\n直播间标题："+originDynamic.title
                            +"\n直播间状态："+(originDynamic.status==1 ? "直播中":"未直播")
                            +"\n直播间链接："+"https://live.bilibili.com/"+originDynamic.roomid;
                }
                return "转发了"+json.originAuthor+"的动态："+json.item.content
                        + "\n原动态信息：\n"+json.originAuthor+ DynamicItem.getType(json.item.originType).getInfo(originDynamic);
            }
        },TextWithPictures {
            public String getInfo(Dynamic json) {
                return "发了一条带图动态：\n"+json.item.desc+json.item.dynamicPictures.stream().map(DynamicPicture::getImgCQName).collect(Collectors.joining());
            }
        },Article {
            public String getInfo(Dynamic json) {
                return "发了一篇文章（累计共" + json.totalWords + "字）：\n" + json.title + "\n" + json.articleImageURL.stream().map(url -> ImageManager.getImageData(url).toCQCode()).collect(Collectors.joining());
            }
        },Video {
            public String getInfo(Dynamic json) {
                return "发了一个视频：" + json.videoComment + "\n" + "https://www.bilibili.com/video/av" + json.aid + "\n" + json.title + "\n" + ImageManager.getImageData(json.videoPic).toCQCode();
            }
        },MicroVideo{
            @Override
            public String getInfo(Dynamic json) {
                return "发了一个小视频：" + json.item.desc + "\n" + ImageManager.getImageData(json.item.cover.get("unclipped").getAsString()).toCQCode();
            }
        },WidgetShare{
            @Override
            public String getInfo(Dynamic json) {
                return "分享了挂件：" + json.vest.content + ImageManager.getImageData(json.sketch.coverURL).toCQCode();
            }
        }
    }


    public static class DynamicItem{
        //for dynamic of plainText
        @SerializedName("content")
        String content;

        //for dynamic of pictures with text
        @SerializedName("description")
        String desc;

        @SerializedName("cover")
        JsonObject cover;

        @SerializedName("miss")
        int delete;
        @SerializedName("pictures")
        List<DynamicPicture> dynamicPictures;

        @SerializedName("orig_type")
        int originType;

        public static DynamicType getType(int type) {
            switch (type){
                case 1:
                    return DynamicType.RePostWithPlainText;
                case 2:
                    return DynamicType.TextWithPictures;
                case 4:
                    return DynamicType.PlainText;
                case 64:
                    return DynamicType.Article;
                case 8:
                    return DynamicType.Video;
                case 16:
                    return DynamicType.MicroVideo;
                case 2048:
                    return DynamicType.WidgetShare;
                default:
                    return DynamicType.Unknown;
            }
        }

    }

    static class DynamicPicture {
        @SerializedName("img_src")
        final
        String imgUrl;

        DynamicPicture(String imgUrl) {
            this.imgUrl=imgUrl;
        }

        String getImgCQName() {
            return ImageManager.getImageData(imgUrl).toCQCode();
        }
    }

    static class DynamicVest {
        @SerializedName("content")
        String content;
    }

    static class DynamicSketch {
        @SerializedName("desc_text")
        String desc;
        @SerializedName("title")
        String name;
        @SerializedName("cover_url")
        String coverURL;
    }
}
