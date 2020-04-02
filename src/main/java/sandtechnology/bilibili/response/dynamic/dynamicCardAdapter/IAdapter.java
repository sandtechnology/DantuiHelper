package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter;

import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.holder.WriteOnlyMessage;

public interface IAdapter {

    WriteOnlyMessage addMessage(WriteOnlyMessage out, DynamicData dynamicData);
}

