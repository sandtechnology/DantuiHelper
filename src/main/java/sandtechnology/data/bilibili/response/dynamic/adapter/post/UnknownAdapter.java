package sandtechnology.data.bilibili.response.dynamic.adapter.post;

import sandtechnology.data.bilibili.response.dynamic.adapter.IAdapter;
import sandtechnology.holder.IWriteOnlyMessage;

public class UnknownAdapter implements IAdapter {
    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage out) {
        return out;
    }

    @Override
    public String getActionText() {
        return "发了一条无法解析的动态";
    }
}
