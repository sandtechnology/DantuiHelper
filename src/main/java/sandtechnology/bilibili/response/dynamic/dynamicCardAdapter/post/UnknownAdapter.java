package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post;

import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.IAdapter;
import sandtechnology.holder.WriteOnlyMessage;

public class UnknownAdapter implements IAdapter {
    @Override
    public WriteOnlyMessage getContent(WriteOnlyMessage out) {
        return out;
    }

    @Override
    public String getActionText() {
        return "发了一条无法解析的动态";
    }
}
