package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter;

import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.holder.WriteOnlyMessage;

public interface IRepostAdapter extends IAdapter {
    WriteOnlyMessage getContent(WriteOnlyMessage out, DynamicData dynamicData);

    @Override
    default WriteOnlyMessage getContent(WriteOnlyMessage message) {
        throw new UnsupportedOperationException("RepostAdapter Must Input DynamicData");
    }

    boolean isShareDynamic();
}
