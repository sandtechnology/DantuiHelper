package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter;

import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.holder.WriteOnlyMessage;

public interface IRepostAdapter extends IAdapter {
    WriteOnlyMessage getContent(WriteOnlyMessage out, DynamicData dynamicData);

    @Override
    default WriteOnlyMessage getContent(WriteOnlyMessage message) {
        throw new UnsupportedOperationException("RepostAdapter Must Input DynamicData");
    }

    /**
     * 是否是分享动态
     *
     * @return 是否是分享动态，如分享番剧或直播间
     */
    boolean isShareDynamic();
}
