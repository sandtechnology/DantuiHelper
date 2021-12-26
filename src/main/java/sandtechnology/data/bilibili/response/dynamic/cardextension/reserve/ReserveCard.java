package sandtechnology.data.bilibili.response.dynamic.cardextension.reserve;

import com.google.gson.annotations.SerializedName;
import sandtechnology.data.bilibili.response.dynamic.Decodable;
import sandtechnology.holder.IWriteOnlyMessage;
import sandtechnology.utils.TimeUtil;

public class ReserveCard implements Decodable {

    @SerializedName("reserve_total")
    private int reserveTotal;

    @SerializedName("reserve_button")
    private ReserveButton reserveButton;

    @SerializedName("oid_str")
    private String oidStr;

    @SerializedName("stype")
    private int subType;

    @SerializedName("livePlanStartTime")
    private int livePlanStartTime;

    @SerializedName("state")
    private int state;

    @SerializedName("type")
    private String type;

    @SerializedName("title")
    private String title;

    @SerializedName("desc_first")
    private DescTitle descTitle;

    @SerializedName("desc_second")
    private String descSubTitle;

    @SerializedName("origin_state")
    private int originState;

    @SerializedName("reserve_lottery")
    private ReserveLottery reserveLottery;

    public int getReserveTotal() {
        return reserveTotal;
    }

    public ReserveButton getReserveButton() {
        return reserveButton;
    }

    public String getOidStr() {
        return oidStr;
    }

    public int getSubType() {
        return subType;
    }

    public int getLivePlanStartTime() {
        return livePlanStartTime;
    }

    public DescTitle getDescFirst() {
        return descTitle;
    }

    public int getState() {
        return state;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescSubTitle() {
        return descSubTitle;
    }

    public int getOriginState() {
        return originState;
    }

    /**
     * @return 是否处在预约中的状态
     */
    public boolean isReserving() {
        //150 如期进行（可能进行中或者结束）
        //-100 已撤销
        //100 正常
        return originState == 100;
    }

    @Override
    public IWriteOnlyMessage getContent(IWriteOnlyMessage message) {

        switch (subType) {
            //视频预约
            case 1:
                message.add(descTitle.getText()).add("：").newLine()
                        .add("预约内容：").add(title).newLine()
                        .add("预约情况：").add(isReserving() ? reserveTotal + "人已预约" : reserveButton.getStatusText());
                break;
            //直播预约
            case 2:
                message.add(title).newLine()
                        .add("预定直播时间：").add(TimeUtil.getFormattedUTC8TimeSec(livePlanStartTime)).newLine()
                        .add("预约情况：").add(isReserving() ? reserveTotal + "人已预约" : reserveButton.getStatusText());
                break;
            default:
                message.add("未知预约内容ID" + subType + "，请打开动态查看");
        }
        //预约抽奖解析
        return reserveLottery == null ? message : reserveLottery.getContent(message);
    }
}