import org.junit.jupiter.api.Test;
import sandtechnology.bilibili.response.dynamic.display.Emoji;
import sandtechnology.bilibili.response.dynamic.display.EmojiChain;
import sandtechnology.holder.WriteOnlyMessage;

import java.util.LinkedList;
import java.util.List;

public class EmojiFormatTest {


    @Test
    public void test() {
        if (Tester.getTest()) {
            String textMessage = "[奋斗]瞅了一下，多半都是大会员专享跟大会员抢先看[妙啊]\n那~就👉https:\\\\www.bilibili.com\\blackboard\\activity-_A80QNbtP.html?from=dongtai每天三毛钱，快乐你懂的[奋斗]";
            List<Emoji> emojis = new LinkedList<>();
            String testMessage2 = "[奋斗]";
            emojis.add(new Emoji("[奋斗]", "https://i0.hdslb.com/bfs/emote/bb2060c15dba7d3fd731c35079d1617f1afe3376.png"));
            emojis.add(new Emoji("[妙啊]", "https://i0.hdslb.com/bfs/emote/b4cb77159d58614a9b787b91b1cd22a81f383535.png"));
            System.out.println(new EmojiChain(emojis).format(new WriteOnlyMessage(textMessage)).toCQString());
            System.out.println(new EmojiChain(emojis).format(new WriteOnlyMessage(testMessage2)).toCQString());

        }
    }
}
