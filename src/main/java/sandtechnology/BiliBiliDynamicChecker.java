package sandtechnology;

import sandtechnology.bilibili.POJOResponse;
import sandtechnology.bilibili.dynamic.POJODynamic;
import sandtechnology.utils.HTTPHelper;
import sandtechnology.utils.MessageHelper;

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

    public static void main(String[] args) {
        //System.out.println(new POJODynamic.Dynamic.DynamicPicture("https://i0.hdslb.com/bfs/album/3412a766bbe175fb1d74c2c7bd41a396cc05f63a.jpg").getImgCQName());
        new BiliBiliDynamicChecker(453690235).check();
        new BiliBiliDynamicChecker(420249427).check();
    }

    public BiliBiliDynamicChecker(long uid) {
        String apiUrl = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history?visitor_uid=0&host_uid=" + uid + "&offset_dynamic_id=0&need_top=0";
        Consumer<POJOResponse> handler = response -> {
            if (response.getDynamicData().getDynamics() == null) {
                return;
            }
            POJODynamic.POJOCard firstCard = response.getDynamicData().getDynamics().get(0);
            if (lastTimestamp == 0) {
                lastTimestamp = firstCard.getTimestamp();
                MessageHelper.sendingInfoMessage(firstCard.getInfo());
                return;
            }
            List<POJODynamic.POJOCard> list = response.getDynamicData().getDynamics().stream().filter(d -> d.getTimestamp() > lastTimestamp).filter(d -> {
                if (d.getAuthorUID() == uid) {
                    return true;
                } else {
                    MessageHelper.sendingDebugMessage("blocked:",d.getInfo());
                    return false;
                }
            }).collect(Collectors.toList());
            if (!list.isEmpty()) {
                lastTimestamp = list.get(0).getTimestamp();
                list.forEach(d -> MessageHelper.sendingGroupMessage(groups, d.getInfo()));
            }
        };
        httpHelper = new HTTPHelper(apiUrl, handler);
    }

    public BiliBiliDynamicChecker addGroups(Long...groups) {
        this.groups.addAll(Arrays.asList(groups));
        return this;
    }

    public BiliBiliDynamicChecker setHandler(Consumer<POJOResponse> handler) {
        this.httpHelper.setHandler(handler);
        return this;
    }

    public BiliBiliDynamicChecker setLastTimestamp(long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
        return this;
    }

    public void check() {
        httpHelper.execute();
    }
}
