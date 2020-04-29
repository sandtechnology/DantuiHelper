package sandtechnology.holder;

import kotlin.Unit;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

public class ReadOnlyMessage {

    private final MessageChain obj;
    private String toStringCache;


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
        if (toStringCache == null) {
            StringBuilder stringBuilder = new StringBuilder();
            obj.forEachContent((x) -> {
                stringBuilder.append(x.toString());
                return Unit.INSTANCE;
            });
            return toStringCache = stringBuilder.toString();
        } else {
            return toStringCache;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadOnlyMessage that = (ReadOnlyMessage) o;
        return that.obj.contentEquals(obj, false);
    }

    public WriteOnlyMessage toWriteOnlyMessage() {
        return new WriteOnlyMessage() {
            @Override
            public MessageChain toMessageChain(ExtraData data) {
                return obj;
            }

            @Override
            public String toCQString() {
                return toString();
            }
        };
    }

    @Override
    public int hashCode() {
        return obj.hashCode() * 31;
    }
}
