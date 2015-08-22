package net.poringsoft.splastageclient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 開始日ごとに集計したデータ
 */
public class StageSchduleInfo {
    //内部クラス
    //---------------------------------------------------------
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

    //定数
    //---------------------------------------------------------
    public static final String KEY_MATCH_NAWABARI = "レギュラーマッチ";
    public static final String KEY_MATCH_GACHI = "ガチマッチ";
    public static final String KEY_MATCH_FES = "フェスマッチ";

    //フィールド
    //---------------------------------------------------------
    private String m_timeText;
    private long m_startTimeTick;
    private Map<String, List<MatchNameInfo>> m_matchList;


    //プロパティ
    //---------------------------------------------------------
    public String getTimeText() {
        return m_timeText;
    }
    public long getStartTimeTick() { return m_startTimeTick; }
    public Map<String, List<MatchNameInfo>> getMatchList() {
        return m_matchList;
    }


    //メソッド
    //---------------------------------------------------------
    /**
     * コンストラクタ
     * @param timeText 時間表示文字列
     * @param matchList マッチごとのステージ名リスト
     */
    public StageSchduleInfo(long startTimeTick, String timeText, Map<String, List<MatchNameInfo>> matchList) {
        m_startTimeTick = startTimeTick;
        m_timeText = timeText;
        m_matchList = matchList;
    }

    /**
     * リスト表示用のスケジュールリストを生成する
     * @param stageInfoList ステージ情報リスト
     * @return スケジュールリスト
     */
    public static List<StageSchduleInfo> CreateList(List<StageInfo> stageInfoList) {
        if (stageInfoList == null || stageInfoList.size() == 0) {
            return new ArrayList<>();
        }

        Map<Long, List<StageInfo>> timeGroupList = new HashMap<>();
        for (StageInfo info : stageInfoList) {
            PSDebug.d("info:" + info.toString());
            if (!timeGroupList.containsKey(info.getStartTime().getTime())) {
                timeGroupList.put(info.getStartTime().getTime(), new ArrayList<StageInfo>());
            }

            timeGroupList.get(info.getStartTime().getTime()).add(info);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
        List<StageSchduleInfo> resultList = new ArrayList<>();
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

                PSDebug.d("timeText=" + timeText);
                resultList.add(new StageSchduleInfo(key, timeText, matchList));
            }
        }

        //時間順になるように並び替える
        Collections.sort(resultList, new Comparator<StageSchduleInfo>() {
            @Override
            public int compare(StageSchduleInfo lhs, StageSchduleInfo rhs) {
                if (lhs.getStartTimeTick() > rhs.getStartTimeTick()) {
                    return 1;
                }
                else if (lhs.getStartTimeTick() < rhs.getStartTimeTick()) {
                    return -1;
                }

                return 0;
            }
        });

        return resultList;
    }
}
