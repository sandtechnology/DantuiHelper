package sandtechnology.checker;

import sandtechnology.bilibili.NormalResponse;
import sandtechnology.bilibili.response.dynamic.DynamicData;
import sandtechnology.utils.HTTPHelper;
import sandtechnology.utils.MessageHelper;
import sandtechnology.utils.ThreadHelper;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BiliBiliDynamicChecker implements IChecker {

    private long lastTimestamp;
    private final Set<Long> groups=new LinkedHashSet<>();
    private final HTTPHelper httpHelper;

    public BiliBiliDynamicChecker(long uid) {
        String apiUrl = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?visitor_uid=0&host_uid=" + uid + "&offset_dynamic_id=0&need_top=0";
        Consumer<NormalResponse> handler = response -> {
            List<DynamicData> dynamicData = response.getDynamicsDataList().getDynamics();
            if (dynamicData == null || dynamicData.isEmpty()) {
                return;
            }
            DynamicData firstCard = dynamicData.get(0);
            if (lastTimestamp == 0) {
                lastTimestamp = firstCard.getDesc().getTimestamp();
                MessageHelper.sendingInfoMessage(firstCard.getMessage());
                return;
            }
            List<DynamicData> list = response.getDynamicsDataList().getDynamics().stream().filter(d -> d.getDesc().getTimestamp() > lastTimestamp).filter(d -> {
                if (d.getDesc().getUserProfile().getInfo().getUid() == uid) {
                    return true;
                } else {
                    MessageHelper.sendingDebugMessage(d.getMessage().addFirst("blocked:"));
                    return false;
                }
            }).collect(Collectors.toList());
            if (!list.isEmpty()) {
                lastTimestamp = list.get(0).getDesc().getTimestamp();
                list.forEach(d -> {
                    MessageHelper.sendingGroupMessage(groups, d.getMessage());
                    ThreadHelper.sleep(1000);
                });
            }
        };
        httpHelper = new HTTPHelper(apiUrl, handler);
    }

    public BiliBiliDynamicChecker addGroups(Long...groups) {
        this.groups.addAll(Arrays.asList(groups));
        return this;
    }

    public BiliBiliDynamicChecker setHandler(Consumer<NormalResponse> handler) {
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
