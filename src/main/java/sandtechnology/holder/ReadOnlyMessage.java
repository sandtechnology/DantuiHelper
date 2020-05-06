package sandtechnology.holder;

import kotlin.Unit;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;

/**
 * 接收消息的包装类
 */
public class ReadOnlyMessage {

    private final MessageChain chain;


    public ReadOnlyMessage(MessageChain chain) {
        this.chain = chain;
    }

    public ReadOnlyMessage(String msg) {
        this.chain = MessageUtils.newChain(msg);
    }

    public MessageChain get() {
        return chain;
    }

    public String toString(boolean miraiCode) {
        if (miraiCode) {
            StringBuilder stringBuilder = new StringBuilder();
            chain.forEachContent((x) -> {
                stringBuilder.append(x.toString());
                return Unit.INSTANCE;
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
        ReadOnlyMessage that = (ReadOnlyMessage) o;
        return that.chain.contentEquals(chain, false);
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
