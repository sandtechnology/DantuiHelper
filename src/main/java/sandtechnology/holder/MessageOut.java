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

public class MessageOut {

    private List<Pair<String, List<ImageManager.CacheImage>>> list = new LinkedList<>();

    public MessageOut add(ImageManager.CacheImage image) {
        if (list.isEmpty()) {
            list.add(new Pair<>("", new ArrayList<>(Collections.singleton(image))));
        } else {
            list.get(list.size() - 1).getLast().add(image);
        }
        return this;
    }

    public MessageOut add(List<ImageManager.CacheImage> image) {
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

    public MessageOut add(String str) {
        list.add(new Pair<>(str, new ArrayList<>()));
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

    public enum Type {Friend, Group}
}
