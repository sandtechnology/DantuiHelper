package sandtechnology.holder;

import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;

/**
 * 接收消息的包装类
 */
public class ReadOnlyMessage {

    private final MessageChain chain;


    public ReadOnlyMessage(MessageChain chain) {
        this.chain = chain;
    }

    public ReadOnlyMessage(String msg) {
        this.chain = MessageUtils.newChain(new PlainText(msg));
    }

    public MessageChain get() {
        return chain;
    }

    public String toString(boolean miraiCode) {
        if (miraiCode) {
            StringBuilder stringBuilder = new StringBuilder();
            chain.forEach((x) -> {
                if (!(x instanceof MessageSource)) {
                    stringBuilder.append(x.toString());
                }
            });
            return stringBuilder.toString();
        } else {
            return chain.contentToString();
        }
    }

    @Override
    public String toString() {
        return toString(false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return ((ReadOnlyMessage) o).chain.contentEquals(chain, false, true);
    }

    public WriteOnlyMessage toWriteOnlyMessage() {
        return new WriteOnlyMessage() {
            @Override
            public MessageChain toMessageChain(ExtraData data) {
                return chain;
            }

            @Override
            public String toCQString() {
                return ReadOnlyMessage.this.toString(true);
            }
        };
    }

    @Override
    public int hashCode() {
        return chain.hashCode() * 31;
    }
}
