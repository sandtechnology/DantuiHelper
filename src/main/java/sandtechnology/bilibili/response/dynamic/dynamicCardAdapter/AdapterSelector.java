package sandtechnology.bilibili.response.dynamic.dynamicCardAdapter;

import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post.PlainTextAdapter;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post.TextWithPicAdapter;
import sandtechnology.bilibili.response.dynamic.dynamicCardAdapter.post.UnknownAdapter;

import java.awt.*;

import static sandtechnology.utils.JsonHelper.getGsonInstance;

public class AdapterSelector {

    private static String getString(DynamicData data){
        IAdapter adapter;
        switch (data.getDesc().getType()){

            case 1:
                adapter=getGsonInstance().fromJson(data.getCard(), PlainTextAdapter.class);
                break;
            default:
                adapter=new UnknownAdapter();
               // adapter=getGsonInstance().fromJson(data.getCard(),)
        }
        return adapter.getString();
    }
}
