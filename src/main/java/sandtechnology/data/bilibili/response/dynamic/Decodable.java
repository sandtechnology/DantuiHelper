package sandtechnology.data.bilibili.response.dynamic;

import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.holder.WriteOnlyMessage;

/**
 * 表示可以获取 WriteOnlyMessage 的对象
 *
 * @see sandtechnology.holder.WriteOnlyMessage
 */
public interface Decodable {
    /**
     * 获取解析结果的便利方法
     *
     * @return 包含解析结果的 WriteOnlyMessage
     */
    default IWriteOnlyMessage getContent() {
        return getContent(new WriteOnlyMessage());
    }

    /**
     * 获取解析结果
     *
     * @param message 传入的 WriteOnlyMessage
     * @return 加上解析结果后的 WriteOnlyMessage
     */
    IWriteOnlyMessage getContent(IWriteOnlyMessage message);
}
