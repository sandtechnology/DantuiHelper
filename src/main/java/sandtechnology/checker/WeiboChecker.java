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
    private final WeiboHTTPHelper weiboHTTPHelper;

    public WeiboChecker(long containerID, Set<Long> groupIDs) {
        weiboHTTPHelper = new WeiboHTTPHelper("https://m.weibo.cn/api/container/getIndex?is_all[]=1&containerid=" + containerID, response -> {
            ResponseData responseData = response.getData();
            if (responseData != null && !responseData.getCardList().isEmpty()) {
                List<CardDetail> cardDetails = responseData.getCardList().stream().filter(resp -> {
                            CardDetail cardDetail = resp.getCardDetail();
                            return cardDetail != null && !sendWeiboIDSet.contains(cardDetail.getID());
                        }
                ).map(Card::getCardDetail).collect(Collectors.toList());

                if (cardDetails.isEmpty()) {
                    return;
                }

                if (sendWeiboIDSet.isEmpty()) {
                    sendWeiboIDSet.addAll(cardDetails.stream().map(CardDetail::getID).collect(Collectors.toList()));
                    for (long groupID : groupIDs) {
                        DataContainer.getMessageHelper().sendGroupMsg(groupID, cardDetails.get(0).toWriteOnlyMessage());
                    }
                    return;
                } else {
                    for (CardDetail cardDetail : cardDetails) {
                        sendWeiboIDSet.add(cardDetail.getID());
                        for (long groupID : groupIDs) {
                            DataContainer.getMessageHelper().sendGroupMsg(groupID, cardDetail.toWriteOnlyMessage());
                        }
                    }
                }

                if (sendWeiboIDSet.size() > 200) {
                    sendWeiboIDSet.clear();
                    sendWeiboIDSet.addAll(cardDetails.stream().map(CardDetail::getID).collect(Collectors.toList()));
                }
            }
        });
    }

    @Override
    public void check() {
        weiboHTTPHelper.execute();
    }
}
