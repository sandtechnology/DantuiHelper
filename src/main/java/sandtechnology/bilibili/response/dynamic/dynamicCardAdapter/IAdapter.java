package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter;

import sandtechnology.holder.WriteOnlyMessage;

public interface IAdapter {

    default WriteOnlyMessage getContent() {
        return getContent(new WriteOnlyMessage());
    }

    WriteOnlyMessage getContent(WriteOnlyMessage message);

    String getActionText();
}

