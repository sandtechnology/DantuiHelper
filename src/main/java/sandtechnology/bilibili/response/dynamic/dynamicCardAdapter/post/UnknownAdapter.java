package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.holder.WriteOnlyMessage;

public class UnknownAdapter implements IAdapter {
    @Override
    public WriteOnlyMessage addMessage(WriteOnlyMessage out, DynamicData dynamicData) {
        return out.add("发了一条无法解析的动态：");
    }
}
