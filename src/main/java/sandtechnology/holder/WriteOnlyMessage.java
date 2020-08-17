package sandtechnology.holder;

import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalImage;
import net.mamoe.mirai.utils.ExternalImageJvmKt;
import sandtechnology.Mirai;
import sandtechnology.utils.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 发送消息的包装类
 */
public class WriteOnlyMessage {


    private final List<Pair<StringBuilder, List<ImageManager.CacheImage>>> list = Collections.synchronizedList(new LinkedList<>());

    public WriteOnlyMessage() {
    }

    public WriteOnlyMessage(String s) {
        add(s);
    }

    public WriteOnlyMessage(List<Pair<StringBuilder, List<ImageManager.CacheImage>>> list) {
        this.list.addAll(list);
    }

    public WriteOnlyMessage add(ImageManager.CacheImage image) {
        if (list.isEmpty()) {
            list.add(new Pair<>(new StringBuilder(), new ArrayList<>(Collections.singleton(image))));
        } else {
            list.get(list.size() - 1).getLast().add(image);
        }
        return this;
    }

    public WriteOnlyMessage add(List<ImageManager.CacheImage> image) {
        if (list.isEmpty()) {
            list.add(new Pair<>(new StringBuilder(), new ArrayList<>(image)));
        } else {
            list.get(list.size() - 1).getLast().addAll(image);
        }
        return this;
    }

    public List<Pair<StringBuilder, List<ImageManager.CacheImage>>> getContent() {
        return list;
    }

    private <T> T getLastElement(List<T> list) {
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

    public WriteOnlyMessage addFirst(String str) {
        list.get(0).getFirst().insert(0, str);
        return this;
    }

    public WriteOnlyMessage addFirst(WriteOnlyMessage str) {
        list.addAll(0, str.getContent());
        return this;
    }

    public WriteOnlyMessage add(String str) {
        if (str == null || str.isEmpty()) {
            return this;
        }
        //移除识别的特殊字符
        str = StringUtil.delete(str, '\u200B', '\u200D');
        if (!list.isEmpty() && getLastElement(list).getLast().isEmpty()) {
            list.get(list.size() - 1).getFirst().append(str);
        } else {
            list.add(new Pair<>(new StringBuilder(str), new ArrayList<>()));
        }
        return this;
    }

    public WriteOnlyMessage add(WriteOnlyMessage msg) {
        list.addAll(msg.getContent());
        return this;
    }

    public WriteOnlyMessage newLine() {
        return add("\n");
    }

    /**
     * 将多个相同的图片合并
     */
    public void trimImage() {
        String nextAdd = null;
        for (Pair<StringBuilder, List<ImageManager.CacheImage>> entry : list) {
            if (nextAdd != null) {
                entry.getFirst().insert(0, nextAdd);
                nextAdd = null;
            }
            ImageManager.CacheImage lastImage = null;
            SeenCounter seenCounter = new SeenCounter();
            Iterator<ImageManager.CacheImage> iterator = entry.getLast().iterator();
            while (iterator.hasNext()) {
                ImageManager.CacheImage image = iterator.next();
                if (lastImage == null) {
                    lastImage = image;
                    seenCounter.firstSeen();
                    continue;
                }
                //因为存在缓存管理器，引用比较可用，且节省性能
                if (lastImage == image) {
                    iterator.remove();
                    seenCounter.seenAgain();
                } else {
                    if (seenCounter.now() > 1) {
                        nextAdd = "（x" + seenCounter + "）";
                        seenCounter.firstSeen();
                    }
                    lastImage = image;
                }
            }
            if (seenCounter.now() > 1) {
                nextAdd = "（x" + seenCounter + "）";
            }
        }
        //防止直接跳出循环的情况
        if (nextAdd != null) {
            add(nextAdd);
        }
    }

    public MessageChain toMessageChain(ExtraData data) {
        trimImage();
        MessageChainBuilder builder = new MessageChainBuilder();
        for (Pair<StringBuilder, List<ImageManager.CacheImage>> pair : list) {
            builder.add(pair.getFirst().toString());

            builder.addAll(pair.getLast().stream().map(
                    img -> {
                        switch (data.getType()) {
                            case Friend:
                                return data.getBot().getFriend(data.getFromQQ()).uploadImage(getExternalImage(img.getFile()));
                            case Group:
                                return data.getBot().getGroup(data.getFromGroup()).uploadImage(getExternalImage(img.getFile()));
                            case Temp:
                                return data.getBot().getGroup(data.getFromGroup()).get(data.getFromQQ()).uploadImage(getExternalImage(img.getFile()));
                            default:
                                throw new IllegalArgumentException("类型不存在");
                        }
                    }).collect(Collectors.toList()));
        }
        //上传速度太快 等待QQ服务器同步
        ThreadHelper.sleep(1000);
        return builder.asMessageChain();
    }

    public enum Type {Friend, Group, Temp}

    private ExternalImage getExternalImage(File file) {
        ExternalImage externalImage;
        if (!file.exists()) {
            return getExternalImage(ImageManager.emptyImage.getFile());
        }
        try {
            externalImage = ExternalImageJvmKt.toExternalImage(file, false);
        } catch (Exception e) {
            DataContainer.getMessageHelper().sendingErrorMessage(e, "在转换图片为ExternalImage时出错");
            return getExternalImage(ImageManager.emptyImage.getFile());
        }
        return externalImage;
    }

    public boolean isLongMessage() {
        return list.stream().mapToInt(pair -> pair.getFirst().length() + pair.getLast().size() * 3).sum() > 300;
    }

    public String toCQString() {
        trimImage();
        StringBuilder builder = new StringBuilder();
        for (Pair<StringBuilder, List<ImageManager.CacheImage>> pair : list) {
            builder.append(toCQEmoji(pair.getFirst().toString()));
            builder.append(pair.getLast().stream().map(ImageManager.CacheImage::toCQCode).collect(Collectors.joining()));
        }
        return builder.toString();
    }

    private String toCQEmoji(String str) {

        if (EmojiManager.containsEmoji(str)) {
            boolean handleHtml = false;
            if (str.contains("&#")) {
                str = str.replace("&#", "|&stand-by&|");
                handleHtml = true;
            }
            str = EmojiParser.parseToHtmlDecimal(str).replaceAll("&#(?<id>[0-9]*);", "[CQ:emoji,id=${id}]");
            if (handleHtml) {
                str = str.replace("|&stand-by&|", "&#");
            }
        }
        return str;
    }

    @Override
    public WriteOnlyMessage clone() {
        List<Pair<StringBuilder, List<ImageManager.CacheImage>>> tempList = new ArrayList<>(list.size());
        for (Pair<StringBuilder, List<ImageManager.CacheImage>> pair : list) {
            tempList.add(new Pair<>(new StringBuilder(pair.getFirst()), new ArrayList<>(pair.getLast())));
        }
        return new WriteOnlyMessage(tempList);
    }

    public static class ExtraData {
        final Type type;
        final Bot bot;
        final long fromGroup;
        final long fromQQ;

        private ExtraData(Type type, Bot bot, long fromGroup, long fromQQ) {
            this.type = type;
            this.bot = bot;
            this.fromGroup = fromGroup;
            this.fromQQ = fromQQ;
        }

        public Type getType() {
            return type;
        }

        public Bot getBot() {
            return bot;
        }

        public long getFromGroup() {
            return fromGroup;
        }

        public long getFromQQ() {
            return fromQQ;
        }

        public static class ExtraDataBuilder {
            Type type = Type.Friend;
            Bot bot = Mirai.getBot();
            long fromGroup;
            long fromQQ;

            public ExtraDataBuilder() {
            }

            public ExtraDataBuilder type(Type type) {
                this.type = type;
                return this;
            }

            public ExtraDataBuilder bot(Bot bot) {
                this.bot = bot;
                return this;
            }

            public ExtraDataBuilder fromQQ(long fromQQ) {
                this.fromQQ = fromQQ;
                return this;
            }

            public ExtraDataBuilder fromGroup(long fromGroup) {
                this.fromGroup = fromGroup;
                return this;
            }

            public ExtraData build() {
                return new ExtraData(type, bot, fromGroup, fromQQ);
            }
        }
    }
}
