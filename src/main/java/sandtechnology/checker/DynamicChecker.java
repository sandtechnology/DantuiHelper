package sandtechnology.checker;

import sandtechnology.bilibili.NormalResponse;
import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.HTTPHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * B站新动态监听器
 * <p>
 * 当前检测方式适用的假设：
 * 1.审核的动态通过时在最新的第21条动态之内
 * 2.一个检测间隔内发送的动态小于21条
 * <p>
 * 检测详细机制：
 * 1.最初的初始化
 * a.记录后端返回的最晚动态的ID
 * b.记录最晚动态的时间
 * c.发送最新的一条动态确保运行正常
 * d.进入阶段2并进入下一个检测间隔
 * 2.检测并发送
 * a.将上一次的最晚动态时间与现有的最新动态比较，
 * 如果全部小于动态时间，直接进入下一个检测间隔
 * b.以记录的动态ID对后端返回的动态取补集，初步取得新动态
 * c.排除与用户UID不一致的动态
 * d.发送新动态，并记录新增动态ID和最晚动态的时间
 * <p>
 * Note：最晚指后端返回最晚动态的时间，最大为最新动态的第21条
 *
 * @author sandtechnology
 * @since 2020/06/10
 */
public class DynamicChecker implements IChecker {

    private final HTTPHelper httpHelper;
    private final long uid;
    private long nextPageOffsetById = 0;
    private Set<Long> sendDynamicIDSet = new HashSet<>();
    private long mostLateTimeStamp = 0;

    public DynamicChecker(long uid, Set<Long> groups) {
        this.uid = uid;
        String apiUrl = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?visitor_uid=0&host_uid=" + uid + "&offset_dynamic_id=" + nextPageOffsetById + "&need_top=0";
        Consumer<NormalResponse> handler = response -> {
            List<DynamicData> dynamicDataList = response.getDynamicsDataList().getDynamics();
            if (dynamicDataList == null || dynamicDataList.isEmpty()) {
                return;
            }

            DynamicData firstCard = dynamicDataList.get(0);
            if (sendDynamicIDSet.isEmpty()) {
                //初始化 同时发出最新的一条动态确保运行正常
                DataContainer.getMessageHelper().sendingInfoMessage(firstCard.getMessage());
                //记录最晚的动态时间
                mostLateTimeStamp = dynamicDataList.get(dynamicDataList.size() - 1).getDesc().getTimestamp();
                //记录当前的动态ID以便检测新动态
                sendDynamicIDSet.addAll(dynamicDataList.stream().map(dynamicData -> Long.parseLong(dynamicData.getDesc().getDynamicID())).collect(Collectors.toList()));
                return;
            } else {
                if (firstCard.getDesc().getTimestamp() <= mostLateTimeStamp) {
                    return;
                }
            }

            List<DynamicData> list = response.getDynamicsDataList().getDynamics().stream().filter(rawDynamicData -> !sendDynamicIDSet.contains(Long.parseLong(rawDynamicData.getDesc().getDynamicID()))).filter(d -> {
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
                    sendDynamicIDSet.addAll(dynamicDataList.stream().map(dynamicData -> Long.parseLong(dynamicData.getDesc().getDynamicID())).collect(Collectors.toList()));
                } else {
                    //添加新动态ID
                    sendDynamicIDSet.addAll(list.stream().map(data -> Long.parseLong(data.getDesc().getDynamicID())).collect(Collectors.toList()));
                }
                for (DynamicData d : list) {
                    DataContainer.getMessageHelper().sendingGroupMessage(groups, d.getMessage());
                }
            }
        };
        httpHelper = new HTTPHelper(apiUrl, handler);
    }

    public DynamicChecker(long uid, Consumer<NormalResponse> handler) {
        this.httpHelper = new HTTPHelper("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?visitor_uid=0&host_uid=" + uid + "&offset_dynamic_id=" + nextPageOffsetById + "&need_top=0", handler);
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
    }
}
