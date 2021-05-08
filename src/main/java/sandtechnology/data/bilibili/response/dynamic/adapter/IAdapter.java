package sandtechnology.data.bilibili.response.dynamic.adapter;

import sandtechnology.data.bilibili.response.dynamic.Decodable;


public interface IAdapter extends Decodable {

    /***
     * 在B站未提供文本下的默认文本
     * @return 动作文本 如 “投稿了视频”
     */
    String getActionText();
}

