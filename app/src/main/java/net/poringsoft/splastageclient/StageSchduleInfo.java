package net.poringsoft.splastageclient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mry on 15/08/11.
 */
public class StageSchduleInfo {

    public static class MatchNameInfo {
        private String m_name;
        private String m_imageUrl;

        public String getName() {
            return m_name;
        }

        public String getImageUrl() {
            return m_imageUrl;
        }

        public MatchNameInfo(String name, String imageUrl) {
            m_name = name;
            m_imageUrl = imageUrl;
        }
    }

    public static final String KEY_MATCH_NAWABARI = "レギュラーマッチ";
    public static final String KEY_MATCH_GACHI = "ガチマッチ";

    private String m_timeText;
    private Map<String, List<MatchNameInfo>> m_matchList;

    /**
     * 例：
     *
     2015/08/11 11:00 ～ 08/11 15:00
     【ナワバリバトル】
     デカライン高架下、ネギトロ炭鉱

     【ガチマッチ：ガチエリア】
     シオノメ油田、モズク農園

     https://splatoon.nintendo.net/assets/img/svg/stage/@2x/29d337c67ca79145136cf3e3d4ed754c755f097df2375db3c172eb87663a5561-8eb7cc82608b74009d6ffe89006756c7561d59b02a497cf52ee0fa3e42d76829.jpg
     * @return
     */

    public String getTimeText() {
        return m_timeText;
    }

    public Map<String, List<MatchNameInfo>> getMatchList() {
        return m_matchList;
    }

    public StageSchduleInfo(String timeText, Map<String, List<MatchNameInfo>> matchList) {
        m_timeText = timeText;
        m_matchList = matchList;
    }

    public static List<StageSchduleInfo> CreateList(List<StageInfo> stageInfoList) {
        List<StageSchduleInfo> resultList = new ArrayList<>();

        Map<Long, List<StageInfo>> timeGroupList = new HashMap<>();
        for (StageInfo info : stageInfoList) {
            PSDebug.d("info:" + info.toString());
            if (!timeGroupList.containsKey(info.getStartTime().getTime())) {
                timeGroupList.put(info.getStartTime().getTime(), new ArrayList<StageInfo>());
            }

            timeGroupList.get(info.getStartTime().getTime()).add(info);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
        for (Long key : timeGroupList.keySet()) {
            List<StageInfo> itemList = timeGroupList.get(key);
            if (itemList.size() > 0) {
                String timeText = sdf.format(itemList.get(0).getStartTime()) + " 〜　" + sdf.format(itemList.get(0).getEndTime());
                Map<String, List<MatchNameInfo>> matchList = new HashMap<>();
                for (StageInfo item : itemList) {
                    String matchKey = item.getMatchType() + " [" + item.getRule() + "]";
                    if (!matchList.containsKey(matchKey)) {
                        matchList.put(matchKey, new ArrayList<MatchNameInfo>());
                    }
                    matchList.get(matchKey).add(new MatchNameInfo(item.getName(), item.getImageUrl()));
                }

                resultList.add(new StageSchduleInfo(timeText, matchList));
            }
        }

        return resultList;
    }
}
