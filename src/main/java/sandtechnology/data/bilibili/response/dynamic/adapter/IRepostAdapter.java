package sandtechnology.data.bilibili.response.dynamic.adapter;

import sandtechnology.data.bilibili.response.dynamic.DynamicData;
import sandtechnology.holder.IWriteOnlyMessage;

public interface IRepostAdapter extends IAdapter {
    IWriteOnlyMessage getContent(IWriteOnlyMessage out, DynamicData dynamicData);

    @Override
    default IWriteOnlyMessage getContent(IWriteOnlyMessage message) {
        throw new UnsupportedOperationException("RepostAdapter Must Input DynamicData");
    }

    /**
     * 是否是分享动态
     *
     * @return 是否是分享动态，如分享番剧或直播间
     */
    boolean isShareDynamic();
}
