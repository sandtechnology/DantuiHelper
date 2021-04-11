package sandtechnology.holder;

import net.mamoe.mirai.message.data.MessageChain;
import sandtechnology.utils.CacheImage;
import sandtechnology.utils.Pair;

import java.util.List;

public interface IWriteOnlyMessage {
    IWriteOnlyMessage add(CacheImage image);

    IWriteOnlyMessage add(List<CacheImage> image);

    List<Pair<StringBuilder, List<CacheImage>>> getContent();

    IWriteOnlyMessage addFirst(String str);

    IWriteOnlyMessage addFirst(IWriteOnlyMessage str);

    IWriteOnlyMessage replace(String str, String replacement);

    default IWriteOnlyMessage add(Object obj) {
        if (obj instanceof CacheImage) {
            return add((CacheImage) obj);
        }
        return add(obj.toString());
    }
   default boolean isErrorMessage(){
        return false;
   }
    default void setErrorMessage(boolean isError){

    }
    IWriteOnlyMessage add(String str);

    IWriteOnlyMessage add(IWriteOnlyMessage msg);

    default IWriteOnlyMessage newLine() {
        return add("\n");
    }

    void trimImage();

    MessageChain toMessageChain(WriteOnlyMessage.ExtraData data);

    boolean isLongMessage();

    String toCQString();

    @Override
    String toString();

    IWriteOnlyMessage clone();
}
