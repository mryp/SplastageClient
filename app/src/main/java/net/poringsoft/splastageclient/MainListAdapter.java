package net.poringsoft.splastageclient;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mry on 15/08/11.
 */
public class MainListAdapter extends BaseAdapter {

    private Context m_context;
    private LayoutInflater m_layoutInf;
    private List<StageSchduleInfo> m_stageList;

    /**
     * コンストラクタ
     */
    public MainListAdapter(Context context, List<StageSchduleInfo> stageList) {
        m_context = context;
        m_stageList = stageList;
        m_layoutInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * リスト個数
     * @return リスト個数
     */
    @Override
    public int getCount() {
        return m_stageList.size();
    }

    /**
     * 指定位置のアイテムを取得する
     * @param i 位置
     * @return 指定位置のオブジェクト（IdleInfo）
     */
    @Override
    public Object getItem(int i) {
        return m_stageList.get(i);
    }

    /**
     * 指定位置のアイテムIDを取得する
     * @param i 位置
     * @return アイテムID
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * リストビューアイテムを生成して返す
     * @param i 位置
     * @param view アイテムビュー
     * @param viewGroup 親ビュー
     * @return 生成したアイテムビュー
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        StageSchduleInfo info = m_stageList.get(i);
        if (view == null) {
            view = m_layoutInf.inflate(R.layout.main_list_item, null);
        }
        if (view == null) {
            return null;
        }

        //名前部
        Resources resources = m_context.getResources();
        TextView text = (TextView)view.findViewById(R.id.timeTextView);
        text.setText(info.getTimeText());

        for (String matchKey : info.getMatchList().keySet()) {
            List<StageSchduleInfo.MatchNameInfo> nameList = info.getMatchList().get(matchKey);
            if (matchKey.startsWith(StageSchduleInfo.KEY_MATCH_NAWABARI)) {
                ((TextView)view.findViewById(R.id.nawabariMatchRuleTextView)).setText(matchKey);
                setStageName(view, nameList,
                        Arrays.asList(R.id.nawabariNameTextView1, R.id.nawabariNameTextView2),
                        Arrays.asList(R.id.nawabariImageView1, R.id.nawabariImageView2));
            }
            else if (matchKey.startsWith(StageSchduleInfo.KEY_MATCH_GACHI)) {
                ((TextView)view.findViewById(R.id.gachiMatchRuleTextView)).setText(matchKey);
                setStageName(view, nameList,
                        Arrays.asList(R.id.gachiNameTextView1, R.id.gachiNameTextView2),
                        Arrays.asList(R.id.gachiImageView1, R.id.gachiImageView2));
            }
        }

        return view;
    }

    private void setStageName(View view, List<StageSchduleInfo.MatchNameInfo> nameList, List<Integer> textResIdList, List<Integer> imageResIdList) {
        if (nameList.size() > textResIdList.size() || nameList.size() > imageResIdList.size()) {
            return;
        }

        for (int i=0; i<nameList.size(); i++) {
            String name = nameList.get(i).getName();
            ((TextView)view.findViewById(textResIdList.get(i))).setText(name);

            String imageUrl = nameList.get(i).getImageUrl();
            if (!imageUrl.equals("")) {
                ImageView imageView = (ImageView)view.findViewById(imageResIdList.get(i));
                ImageLoader.getInstance().displayImage(imageUrl, imageView);
            }

            //520:292 = 260:146 = 130:73
        }
    }
}
