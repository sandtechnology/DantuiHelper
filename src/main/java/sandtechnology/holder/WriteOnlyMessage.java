package sandtechnology.holder;

import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.EmptyMessageChain;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.Nullable;
import sandtechnology.Mirai;
import sandtechnology.utils.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 发送消息的包装类
 */
public class WriteOnlyMessage implements IWriteOnlyMessage {

    private static final IWriteOnlyMessage EMPTY_MESSAGE = new IWriteOnlyMessage() {

        @Override
        public IWriteOnlyMessage add(CacheImage image) {
            return this;
        }

        @Override
        public IWriteOnlyMessage add(List<CacheImage> image) {
            return this;
        }

        @Override
        public List<Pair<StringBuilder, List<CacheImage>>> getContent() {
            return Collections.emptyList();
        }

        @Override
        public IWriteOnlyMessage addFirst(String str) {
            return this;
        }

        @Override
        public IWriteOnlyMessage addFirst(IWriteOnlyMessage str) {
            return this;
        }

        @Override
        public IWriteOnlyMessage replace(String str, String replacement) {
            return this;
        }

        @Override
        public IWriteOnlyMessage add(Object obj) {
            return this;
        }

        @Override
        public IWriteOnlyMessage add(String str) {
            return this;
        }

        @Override
        public IWriteOnlyMessage add(IWriteOnlyMessage msg) {
            return this;
        }

        @Override
        public IWriteOnlyMessage newLine() {
            return this;
        }

        @Override
        public void trimImage() {

        }

        @Override
        public MessageChain toMessageChain(ExtraData data) {
            return EmptyMessageChain.INSTANCE;
        }

        @Override
        public boolean isLongMessage() {
            return false;
        }

        @Override
        public String toCQString() {
            return "";
        }

        @Override
        public IWriteOnlyMessage clone() {
            return this;
        }
    };
    private static final PlainText failedText = new PlainText("[图片上传失败]");
    private final List<Pair<StringBuilder, List<CacheImage>>> list = Collections.synchronizedList(new LinkedList<>());

    public WriteOnlyMessage() {
    }

    public WriteOnlyMessage(String s) {
        add(s);
    }

    public WriteOnlyMessage(List<Pair<StringBuilder, List<CacheImage>>> list) {
        this.list.addAll(list);
    }

    public static IWriteOnlyMessage emptyMessage() {
        return EMPTY_MESSAGE;
    }

    @Override
    public IWriteOnlyMessage add(CacheImage image) {
        if (list.isEmpty()) {
            list.add(new Pair<>(new StringBuilder(), new ArrayList<>(Collections.singleton(image))));
        } else {
            list.get(list.size() - 1).getLast().add(image);
        }
        return this;
    }

    @Override
    public IWriteOnlyMessage add(List<CacheImage> image) {
        if (list.isEmpty()) {
            list.add(new Pair<>(new StringBuilder(), new ArrayList<>(image)));
        } else {
            list.get(list.size() - 1).getLast().addAll(image);
        }
        return this;
    }

    private <T> T getLastElement(List<T> list) {
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

    @Override
    public List<Pair<StringBuilder, List<CacheImage>>> getContent() {
        return list;
    }

    @Override
    public IWriteOnlyMessage addFirst(String str) {
        list.get(0).getFirst().insert(0, str);
        return this;
    }

    @Override
    public IWriteOnlyMessage addFirst(IWriteOnlyMessage str) {
        list.addAll(0, str.getContent());
        return this;
    }

    @Override
    public IWriteOnlyMessage replace(String str, String replacement) {
        for (Pair<StringBuilder, List<CacheImage>> pair : list) {
            StringBuilder stringBuilder = pair.getFirst();
            int index = stringBuilder.indexOf(str);
            if (index != -1) {
                stringBuilder.delete(index, index + str.length());
                if (!replacement.isEmpty()) {
                    stringBuilder.insert(index, replacement);
                }
            }
        }
        return this;
    }

    @Override
    public IWriteOnlyMessage add(String str) {
        if (str == null || str.isEmpty()) {
            return this;
        }
        //自动合并
        if (!list.isEmpty() && getLastElement(list).getLast().isEmpty()) {
            list.get(list.size() - 1).getFirst().append(str);
        } else {
            list.add(new Pair<>(new StringBuilder(str), new ArrayList<>()));
        }
        return this;
    }

    @Override
    public IWriteOnlyMessage add(IWriteOnlyMessage msg) {
        list.addAll(msg.getContent());
        return this;
    }

    @Override
    public IWriteOnlyMessage newLine() {
        return add("\n");
    }

    /**
     * 将多个相同的图片合并
     */
    @Override
    public void trimImage() {
        String nextAdd = null;
        for (Pair<StringBuilder, List<CacheImage>> entry : list) {
            if (nextAdd != null) {
                entry.getFirst().insert(0, nextAdd);
                nextAdd = null;
            }
            CacheImage lastImage = null;
            SeenCounter seenCounter = new SeenCounter();
            Iterator<CacheImage> iterator = entry.getLast().iterator();
            while (iterator.hasNext()) {
                CacheImage image = iterator.next();
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

    @Override
    public MessageChain toMessageChain(ExtraData data) {
        trimImage();
        MessageChainBuilder builder = new MessageChainBuilder();
        for (Pair<StringBuilder, List<CacheImage>> pair : list) {
            builder.add(pair.getFirst().toString());

            builder.addAll(pair.getLast().stream().map(
                    img -> {
                        if (img instanceof CacheImage.ImagePlaceHolder) {
                            return new PlainText(img.toString());
                        }
                        try (ExternalResource resource = getExternalImage(img.getFile())) {
                            if (resource == null) {
                                return failedText;
                            }
                            switch (data.getType()) {
                                case Friend:
                                    return data.getBot().getFriendOrFail(data.getFromQQ()).uploadImage(resource);
                                case Group:
                                    return data.getBot().getGroupOrFail(data.getFromGroup()).uploadImage(resource);
                                case Temp:
                                    return data.getBot().getGroupOrFail(data.getFromGroup()).getOrFail(data.getFromQQ()).uploadImage(resource);
                                default:
                                    throw new IllegalArgumentException("类型不存在");
                            }
                        } catch (Exception e) {
                            return failedText;
                        }
                    }).collect(Collectors.toList()));
        }
        //上传速度太快 等待QQ服务器同步
        ThreadHelper.sleep(1000);
        return builder.asMessageChain();
    }

    public enum Type {Friend, Group, Temp}

    @Nullable
    private ExternalResource getExternalImage(File file) {
        ExternalResource externalImage;
        if (!file.exists()) {
            return null;
        }
        try {
            externalImage = ExternalResource.Companion.create(file);
        } catch (Exception e) {
            DataContainer.getMessageHelper().sendingErrorMessage(e, "在转换图片为ExternalImage时出错");
            return null;
        }
        return externalImage;
    }

    @Override
    public boolean isLongMessage() {
        return list.stream().mapToInt(pair -> pair.getFirst().length() + pair.getLast().size() * 3).sum() > 500;
    }

    @Override
    public String toCQString() {
        trimImage();
        StringBuilder builder = new StringBuilder();
        for (Pair<StringBuilder, List<CacheImage>> pair : list) {
            builder.append(toCQEmoji(pair.getFirst().toString()));
            builder.append(pair.getLast().stream().map(CacheImage::toCQCode).collect(Collectors.joining()));
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
    public String toString() {
        trimImage();
        StringBuilder builder = new StringBuilder();
        for (Pair<StringBuilder, List<CacheImage>> pair : list) {
            builder.append(pair.getFirst().toString());
            builder.append(pair.getLast().stream().map(CacheImage::toString).collect(Collectors.joining()));
        }
        return builder.toString();
    }

    @Override
    public IWriteOnlyMessage clone() {
        List<Pair<StringBuilder, List<CacheImage>>> tempList = new ArrayList<>(list.size());
        for (Pair<StringBuilder, List<CacheImage>> pair : list) {
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
