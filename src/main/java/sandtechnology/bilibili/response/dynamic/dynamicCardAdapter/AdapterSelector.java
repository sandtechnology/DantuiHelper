package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter;

import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post.PlainTextAdapter;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post.UnknownAdapter;
import sandtechnology.holder.MessageOut;

import static sandtechnology.utils.JsonHelper.getGsonInstance;

class AdapterSelector {

    private static MessageOut getString(DynamicData data) {
        IAdapter adapter;
        switch (data.getDesc().getType()){

            case 1:
                adapter=getGsonInstance().fromJson(data.getCard(), PlainTextAdapter.class);
                break;
            default:
                adapter=new UnknownAdapter();
               // adapter=getGsonInstance().fromJson(data.getCard(),)
        }
        return adapter.getMessage();
    }
}
