package sandtechnology.bilibili.dynamic;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import sandtechnology.BiliBiliDynamicChecker;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Dynamic {

    @SerializedName("item")
    DynamicItem item;

    @SerializedName("origin_user")
    JsonObject originUserObject;

    @SerializedName("origin")
    String originJson;

    String originAuthor;

    //video
    @SerializedName("dynamic")
    String videoComment;
    long aid;
    @SerializedName("pic")
    String videoPic;

    String title;

    //streaming
    String roomid;
    @SerializedName("round_status")
    int status;
    @SerializedName("uname")
    String streamer;

    //widget

    @SerializedName("vest")
    DynamicVest vest;
    @SerializedName("sketch")
    DynamicSketch sketch;

    //article
    @SerializedName("image_urls")
    String articleImageURL;
    @SerializedName("words")
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
                return "发了一篇文章（累计共"+json.totalWords+"字）：\n"+json.title+"\n"+new DynamicPicture(json.articleImageURL).getImgCQName();
            }
        },Video {
            public String getInfo(Dynamic json) {
                return "发了一个视频："+json.videoComment+"\n"+"https://www.bilibili.com/video/av"+json.aid+"\n"+json.title+"\n"+new DynamicPicture(json.videoPic).getImgCQName();
            }
        },MicroVideo{
            @Override
            public String getInfo(Dynamic json) {
                return "发了一个小视频："+json.item.desc+"\n"+new DynamicPicture(json.item.cover.get("unclipped").getAsString()).getImgCQName();
            }
        },WidgetShare{
            @Override
            public String getInfo(Dynamic json) {
                return "分享了挂件："+json.vest.content+new DynamicPicture(json.sketch.coverURL).getImgCQName();
            }
        };
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

    public static class DynamicPicture {
        @SerializedName("img_src")
        String imgUrl;

        public String getImgCQName() {
            try {
                URL url=new URL(imgUrl);
                URLConnection connection=new URL(imgUrl).openConnection();
                connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0");
                String fileName=url.getFile();
                fileName=fileName.substring(fileName.lastIndexOf("/")+1);
                Path file= Paths.get("data","image",fileName).toAbsolutePath();
                try (FileOutputStream writer=new FileOutputStream(file.toFile(),false); BufferedInputStream inputStream=new BufferedInputStream(connection.getInputStream());){
                int code;
                byte[] buff = new byte[1024];
                    while ((code = inputStream.read(buff)) != -1) {
                        writer.write(buff,0,code);
                    }
                    BiliBiliDynamicChecker.addFileToDeleted(file);
                return "[CQ:image,file="+fileName+"]";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return getImgCQName();
            }
        }
        public DynamicPicture(String imgUrl){
            this.imgUrl=imgUrl;
        }
    }

    public static class DynamicVest {
        @SerializedName("content")
        String content;
    }
    public static class DynamicSketch{
        @SerializedName("desc_text")
        String desc;
        @SerializedName("title")
        String name;
        @SerializedName("cover_url")
        String coverURL;
    }
}
