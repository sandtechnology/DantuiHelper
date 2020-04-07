package sandtechnology.holder;

import kotlin.Unit;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

public class ReadOnlyMessage {

    private final MessageChain obj;

    public ReadOnlyMessage(MessageChain obj) {
        this.obj = obj;
    }

    public ReadOnlyMessage(String obj) {
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        chainBuilder.add(obj);
        this.obj = chainBuilder.asMessageChain();
    }

    public MessageChain get() {
        return obj;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        obj.forEachContent((x) -> {
            stringBuilder.append(x.toString());
            return Unit.INSTANCE;
        });
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadOnlyMessage that = (ReadOnlyMessage) o;
        return toString().equals(that.toString());
    }

    public WriteOnlyMessage toWriteOnlyMessage() {
        return new WriteOnlyMessage() {
            @Override
            public MessageChain toMessageChain(Bot bot, Type type, long id) {
                return obj;
            }

            @Override
            public String toCQString() {
                return obj.toString();
            }
        };
    }

    @Override
    public int hashCode() {
        return obj.hashCode() * 31;
    }
}
