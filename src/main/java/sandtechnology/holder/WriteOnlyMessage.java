package sandtechnology.holder;

import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalImage;
import net.mamoe.mirai.utils.ExternalImageJvmKt;
import sandtechnology.utils.Counter;
import sandtechnology.utils.ImageManager;
import sandtechnology.utils.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class WriteOnlyMessage {

    private final List<Pair<String, List<ImageManager.CacheImage>>> list = new LinkedList<>();

    public WriteOnlyMessage() {
    }

    public WriteOnlyMessage(String s) {
        add(s);
    }

    public WriteOnlyMessage add(ImageManager.CacheImage image) {
        if (list.isEmpty()) {
            list.add(new Pair<>("", new ArrayList<>(Collections.singleton(image))));
        } else {
            list.get(list.size() - 1).getLast().add(image);
        }
        return this;
    }

    public WriteOnlyMessage add(List<ImageManager.CacheImage> image) {
        if (list.isEmpty()) {
            list.add(new Pair<>("", new ArrayList<>(image)));
        } else {
            list.get(list.size() - 1).getLast().addAll(image);
        }
        return this;
    }

    public List<Pair<String, List<ImageManager.CacheImage>>> getContent() {
        return list;
    }

    private <T> T getLastElement(List<T> list) {
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }

    public WriteOnlyMessage addFirst(String str) {
        list.add(0, new Pair<>(str, new ArrayList<>()));
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
        str = str.replace("\u200B", "");
        if (!list.isEmpty() && getLastElement(list).getLast().isEmpty()) {
            Pair<String, List<ImageManager.CacheImage>> old = list.remove(list.size() - 1);
            list.add(old.setFirst(old.getFirst() + str));
        } else {
            list.add(new Pair<>(str, new ArrayList<>()));
        }
        return this;
    }

    public WriteOnlyMessage add(WriteOnlyMessage msg) {
        list.addAll(msg.getContent());
        return this;
    }

    /**
     * 将多个相同的图片合并
     */
    public void trimImage() {
        String nextAdd = null;
        for (Pair<String, List<ImageManager.CacheImage>> entry : list) {
            if (nextAdd != null) {
                entry.setFirst(nextAdd + entry.getFirst());
                nextAdd = null;
            }
            ImageManager.CacheImage lastImage = null;
            Counter counter = new Counter();
            Iterator<ImageManager.CacheImage> iterator = entry.getLast().iterator();
            while (iterator.hasNext()) {
                ImageManager.CacheImage image = iterator.next();
                if (lastImage == null) {
                    lastImage = image;
                    counter.firstSeen();
                    continue;
                }
                //因为存在缓存管理器，引用比较可用，且节省性能
                if (lastImage == image) {
                    iterator.remove();
                    counter.seenAgain();
                } else {
                    if (counter.now() > 1) {
                        nextAdd = "（x" + counter + "）";
                        counter.firstSeen();
                    }
                    lastImage = image;
                }
            }
            if (counter.now() > 1) {
                nextAdd = "（x" + counter + "）";
            }
        }
        //防止直接跳出循环的情况
        if (nextAdd != null) {
            add(nextAdd);
        }
    }

    public MessageChain toMessageChain(Bot bot, Type type, long id) {
        trimImage();
        MessageChainBuilder builder = new MessageChainBuilder();
        for (Pair<String, List<ImageManager.CacheImage>> pair : list) {
            builder.add(pair.getFirst());

            builder.addAll(pair.getLast().stream().map(
                    img -> {

                        if (type == Type.Friend) {
                            return bot.getFriend(id).uploadImage(getExternalImage(img.getFile()));
                        } else {
                            return bot.getGroup(id).uploadImage(getExternalImage(img.getFile()));
                        }
                    }).collect(Collectors.toList()));
        }
        return builder.asMessageChain();
    }

    private ExternalImage getExternalImage(File file) {
        ExternalImage externalImage;
        try {
            externalImage = ExternalImageJvmKt.toExternalImage(file);
        } catch (FileNotFoundException e) {
            return getExternalImage(ImageManager.emptyImage.getFile());
        } catch (Exception e) {
            return getExternalImage(file);
        }
        return externalImage;
    }

    public String toCQString() {
        trimImage();
        StringBuilder builder = new StringBuilder();
        for (Pair<String, List<ImageManager.CacheImage>> pair : list) {
            builder.append(toCQEmoji(pair.getFirst()));
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
        return new WriteOnlyMessage().add(this);
    }

    public enum Type {Friend, Group}
}
