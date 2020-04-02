package sandtechnology.holder;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageUtils;
import sandtechnology.utils.ImageManager;
import sandtechnology.utils.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class WriteOnlyMessage {

    private List<Pair<String, List<ImageManager.CacheImage>>> list = new LinkedList<>();

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

    public MessageChain toMessageChain(Bot bot, Type type, long id) {
        MessageChainBuilder builder = new MessageChainBuilder();
        for (Pair<String, List<ImageManager.CacheImage>> pair : list) {
            builder.add(pair.getFirst());
            builder.addAll(type == Type.Friend ? pair.getLast().stream().map(img -> MessageUtils.newImage(bot.getFriend(id).uploadImage(img.getFile()).getImageId())).collect(Collectors.toList()) : pair.getLast().stream().map(img -> MessageUtils.newImage(bot.getGroup(id).uploadImage(img.getFile()).getImageId())).collect(Collectors.toList()));
        }
        return builder.asMessageChain();
    }

    public String toCQString() {
        StringBuilder builder = new StringBuilder();
        for (Pair<String, List<ImageManager.CacheImage>> pair : list) {
            builder.append(pair.getFirst());
            builder.append(pair.getLast().stream().map(ImageManager.CacheImage::toCQCode).collect(Collectors.joining()));
        }
        return builder.toString();
    }

    @Override
    public WriteOnlyMessage clone() {
        return new WriteOnlyMessage().add(this);
    }

    public enum Type {Friend, Group}
}
