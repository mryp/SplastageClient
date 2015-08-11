package net.poringsoft.splastageclient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ステージ情報（サーバーから取得したデータ）
 */
public class StageInfo {
    private long m_id;
    private Date m_startTime;
    private Date m_endTime;
    private String m_matchType;
    private String m_rule;
    private String m_name;
    private String m_imageUrl;

    public long getId() {
        return m_id;
    }

    public Date getStartTime() {
        return m_startTime;
    }

    public Date getEndTime() {
        return m_endTime;
    }

    public String getMatchType() {
        return m_matchType;
    }

    public String getRule() {
        return m_rule;
    }

    public String getName() {
        return m_name;
    }

    public String getImageUrl() {
        return m_imageUrl;
    }

    public StageInfo(long id, String startTimeText, String endTimeText, String matchType, String rule, String name, String imageUrl) {
        m_id = id;
        m_matchType = matchType;
        m_rule = rule;
        m_name = name;
        m_startTime = convertDateString(startTimeText);
        m_endTime = convertDateString(endTimeText);
        m_imageUrl = imageUrl;
    }

    private Date convertDateString(String dateTimeStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            return sdf.parse(dateTimeStr);
        }
        catch (Exception e) {
            return new Date();
        }
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = sdf.format(m_startTime);
        String endTime = sdf.format(m_endTime);
        return String.format("id=%d, match=%s, rule=%s, name=%s, start=%s, end=%s",
                m_id, m_matchType, m_rule, m_name, startTime, endTime);
    }

    public static List<StageInfo> CreateStageListFromJson(String jsonText) {
        List<StageInfo> outputList = new ArrayList<>();
        try {
            JSONArray json = new JSONArray(jsonText);
            for (int i=0; i<json.length(); i++) {
                JSONObject item = json.getJSONObject(i);
                long id = item.getLong("id");
                String startTimeText = item.getString("starttime");
                String endTimeText = item.getString("endtime");
                String matchType = item.getString("matchtype");
                String rule = item.getString("rule");
                String name = item.getString("name");
                String imageUrl = item.getString("imageurl");

                outputList.add(new StageInfo(id, startTimeText, endTimeText, matchType, rule, name, imageUrl));
            }
        }
        catch (Exception e) {
            outputList.clear();
        }

        return outputList;
    }
}
