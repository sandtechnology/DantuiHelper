package sandtechnology.checker;

import sandtechnology.data.weibo.ResponseData;
import sandtechnology.data.weibo.card.Card;
import sandtechnology.data.weibo.card.CardDetail;
import sandtechnology.utils.DataContainer;
import sandtechnology.utils.http.WeiboHTTPHelper;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WeiboChecker implements IChecker {
    private final Set<Long> sendWeiboIDSet = new LinkedHashSet<>();
    private WeiboHTTPHelper weiboHTTPHelper = null;

    private long mostLateTime = -1L;

    public WeiboChecker(long containerID, Set<Long> groupIDs) {
        weiboHTTPHelper = new WeiboHTTPHelper("https://m.weibo.cn/api/container/getIndex?is_all[]=1&containerid=" + containerID, response -> {
            ResponseData responseData = response.getData();
            if (responseData != null && !responseData.getCardList().isEmpty()) {
                List<CardDetail> cardDetails = responseData.getCardList().stream().filter(resp -> {
                            CardDetail cardDetail = resp.getCardDetail();
                            return cardDetail != null && !sendWeiboIDSet.contains(cardDetail.getID()) && !cardDetail.isOnTop();
                        }
                ).map(Card::getCardDetail).collect(Collectors.toList());

                if (cardDetails.isEmpty()) {
                    return;
                }

                if (sendWeiboIDSet.isEmpty()) {
                    sendWeiboIDSet.addAll(cardDetails.stream().map(CardDetail::getID).collect(Collectors.toList()));
                        CardDetail firstDetail = cardDetails.get(0);
                    mostLateTime = firstDetail.getTimeSec();
                        if (weiboHTTPHelper != null) {
                            weiboHTTPHelper.setReferer("https://m.weibo.cn/u/" + firstDetail.getUserInfo().getId() + "?is_all=1");
                        }

                        DataContainer.getMessageHelper().sendGroupMsg(DataContainer.getMasterGroup(), firstDetail.toWriteOnlyMessage());
                    return;
                } else {
                    for (CardDetail cardDetail : cardDetails) {
                        sendWeiboIDSet.add(cardDetail.getID());
                        if (mostLateTime <= cardDetail.getTimeSec()) {
                            for (long groupID : groupIDs) {
                                DataContainer.getMessageHelper().sendGroupMsg(groupID, cardDetail.toWriteOnlyMessage());
                            }
                        }
                    }
                    mostLateTime = Math.max(mostLateTime, cardDetails.stream().mapToLong(CardDetail::getTimeSec).sorted().findFirst().orElse(0L));
                }

                if (sendWeiboIDSet.size() > 200) {
                    sendWeiboIDSet.clear();
                    sendWeiboIDSet.addAll(cardDetails.stream().map(CardDetail::getID).collect(Collectors.toList()));
                }
            }
        });
        weiboHTTPHelper.setUserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
    }

    @Override
    public void check() {
        weiboHTTPHelper.execute();
    }
}
