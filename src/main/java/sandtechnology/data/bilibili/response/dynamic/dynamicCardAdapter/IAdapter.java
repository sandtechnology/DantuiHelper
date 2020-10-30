package sandtechnology.data.bilibili.response.dynamic.dynamicCardAdapter;

import sandtechnology.holder.WriteOnlyMessage;


public interface IAdapter {

    /**
     * 获取解析结果的便利方法
     *
     * @return 包含解析结果的 WriteOnlyMessage
     */
    default WriteOnlyMessage getContent() {
        return getContent(new WriteOnlyMessage());
    }

    /**
     * 获取解析结果 结果将直接显示给用户
     *
     * @param message 传入的 WriteOnlyMessage
     * @return 加上解析结果后的 WriteOnlyMessage
     */
    WriteOnlyMessage getContent(WriteOnlyMessage message);

    /***
     * 在B站未提供文本下的默认文本
     * @return 动作文本 如 “投稿了视频”
     */
    String getActionText();
}

