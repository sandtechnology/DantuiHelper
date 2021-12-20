package sandtechnology.checker;

import sandtechnology.data.bilibili.NormalResponse;
import sandtechnology.data.bilibili.response.dynamic.DynamicData;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.ThreadHelper;
import sandtechnology.utils.TimeUtil;
import sandtechnology.utils.http.BiliBiliHTTPHelper;

import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * B站新动态监听器
 * <p>
 * 当前检测方式适用的假设：
 * <p>
 * 1.审核的动态通过时在最新的第21条动态之内
 * 2.一个检测间隔内发送的动态小于21条
 * <p>
 * 检测详细机制：
 * <p>
 * 1.最初的初始化
 * <p>
 * a.记录后端返回的最晚动态的ID和返回的所有动态ID
 * b.记录最晚动态的时间
 * c.发送最新的一条动态确保运行正常
 * d.进入阶段2并进入下一个检测间隔
 * <p>
 * 2.检测并发送
 * <p>
 * a.以记录的动态ID对后端返回的动态取补集，初步取得新动态
 * b.排除与用户UID不一致的动态和找不到动态id的动态
 * c.发送新动态，并记录新增动态ID和最晚动态的时间
 * <p>
 * Note：最晚指后端返回最晚动态的时间，最大为最新动态的第21条
 *
 * @author sandtechnology
 * @since 2020/06/10
 */
public class DynamicChecker implements IChecker {

    private final BiliBiliHTTPHelper httpHelper;
    private final long uid;
    private long nextPageOffsetById = 0;
    private final Set<Long> sendDynamicIDSet = new HashSet<>();
    private long mostLateTimeStamp = 0;
    private boolean init = false;

    public DynamicChecker(long uid, Set<Long> groups) {
        this.uid = uid;
        String apiUrl = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?visitor_uid=0&host_uid=" + uid + "&offset_dynamic_id=" + nextPageOffsetById + "&need_top=0";
        Consumer<NormalResponse> handler = response -> {
            List<DynamicData> dynamicDataList = response.getDynamicsDataList().getDynamics();
            if (dynamicDataList == null || dynamicDataList.isEmpty()) {
                init = true;
                return;
            }

            DynamicData firstCard = dynamicDataList.get(0);
            if (!init && sendDynamicIDSet.isEmpty()) {
                //初始化 同时发出最新的一条动态确保运行正常
                DataContainer.getMessageHelper().sendingInfoMessage(firstCard.getMessage());
                //记录最晚的动态时间
                mostLateTimeStamp = dynamicDataList.get(dynamicDataList.size() - 1).getDesc().getTimestamp();
                //同时发出启动后发出的动态
                long startedTimeStamp = TimeUtil.nowSec() - (ManagementFactory.getRuntimeMXBean().getUptime() / 1000);
                if (!isTimestampValid(startedTimeStamp)) {
                    return;
                }
                List<DynamicData> list = dynamicDataList.stream().filter(
                        dynamicData -> isTimestampValid(dynamicData.getDesc().getTimestamp())
                ).filter(data -> startedTimeStamp <=
                        data.getDesc().getTimestamp()
                ).collect(Collectors.toList());
                for (int i = list.size() - 1; i >= 0; i--) {
                    DataContainer.getMessageHelper().sendingGroupMessage(groups, list.get(i).getMessage());
                }
                //记录当前的动态ID以便检测新动态
                sendDynamicIDSet.addAll(dynamicDataList.stream().map(dynamicData -> dynamicData.getDesc().getDynamicIdentifyNumber()).collect(Collectors.toList()));
                init = true;
                return;
            }

            List<DynamicData> list = response.getDynamicsDataList().getDynamics().stream()
                    .filter(d -> {
                        //动态已发送
                        if (sendDynamicIDSet.contains(d.getDesc().getDynamicIdentifyNumber())) {
                            return false;
                        }
                        //动态ID未找到
                        if (d.getDesc().getDynamicID() == null) {
                            return false;
                        }
                        //阿B有的时候会拉跨把其他人的动态混进去，特此过滤
                        if (d.getDesc().getUserProfile().getInfo().getUid() == uid) {
                            return true;
                        } else {
                            DataContainer.getMessageHelper().sendingDebugMessage(d.getMessage().addFirst("blocked:"));
                            return false;
                }
            }).filter(d -> d.getDesc().getTimestamp() > mostLateTimeStamp).collect(Collectors.toList());

            if (!list.isEmpty()) {
                //记录最晚的动态时间
                mostLateTimeStamp = dynamicDataList.get(dynamicDataList.size() - 1).getDesc().getTimestamp();
                //控制记忆的动态ID数量
                if (sendDynamicIDSet.size() >= 500) {
                    //清除并初始化记忆的动态ID
                    sendDynamicIDSet.clear();
                    sendDynamicIDSet.addAll(dynamicDataList.stream().map(dynamicData -> dynamicData.getDesc().getDynamicIdentifyNumber()).collect(Collectors.toList()));
                } else {
                    //添加新动态ID
                    sendDynamicIDSet.addAll(list.stream().map(data -> data.getDesc().getDynamicIdentifyNumber()).collect(Collectors.toList()));
                }
                //因为获取到的动态列表是时间倒序排序，所以需要反向遍历以正序发出
                for (int i = list.size() - 1; i >= 0; i--) {
                    DataContainer.getMessageHelper().sendingGroupMessage(groups, list.get(i).getMessage());
                }
            }
        };
        httpHelper = new BiliBiliHTTPHelper(apiUrl, handler);
        httpHelper.setOriginURL("https://space.bilibili.com");
        httpHelper.setReferer("https://space.bilibili.com/" + uid + "/dynamic");
    }

    private boolean isTimestampValid(long timeStampSec) {
        //避免时间戳突然变换单位 限制在2020-09-13 20:26:40和2160-02-18 18:40:00之间
        boolean result = 6000000000L > timeStampSec && timeStampSec > 1600000000L;
        if (!result) {
            DataContainer.getMessageHelper().sendingErrorMessage(new RuntimeException("时间戳不对！当前时间戳为" + timeStampSec + ", 相当于" + TimeUtil.getFormattedTimeSec(timeStampSec)));
        }
        return result;
    }

    public DynamicChecker(long uid, Consumer<NormalResponse> handler) {
        this.httpHelper = new BiliBiliHTTPHelper("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?visitor_uid=0&host_uid=" + uid + "&offset_dynamic_id=" + nextPageOffsetById + "&need_top=0", handler);
        this.uid = uid;
    }

    public DynamicChecker setNextPageOffsetById(long nextPageOffsetById) {
        this.nextPageOffsetById = nextPageOffsetById;
        httpHelper.setUrl("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?visitor_uid=0&host_uid=" + uid + "&offset_dynamic_id=" + nextPageOffsetById + "&need_top=0");
        return this;
    }

    public DynamicChecker setHandler(Consumer<NormalResponse> handler) {
        this.httpHelper.setHandler(handler);
        return this;
    }


    public void parse(NormalResponse response) {
        httpHelper.getHandler().accept(response);
    }

    public void check() {
        httpHelper.execute();
        ThreadHelper.sleep(2000);
    }
}
