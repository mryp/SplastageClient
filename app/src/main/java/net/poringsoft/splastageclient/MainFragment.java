package net.poringsoft.splastageclient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * メインリスト画面用フラグメント
 */
public class MainFragment extends ListFragment {
    //フィールド
    //----------------------------------------------------------
    private MainListAdapter m_adapter = null;
    private ListView m_listView = null;

    //メソッド
    //----------------------------------------------------------
    /**
     * Fragmentのビュー要求
     * Fragmentで表示するビューの生成を行う
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    /**
     * ActivityのonCreate完了通知
     * 親Activityの生成完了後に必要なデータの生成
     * @param savedInstanceState セーブデータ
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PSDebug.d("call");

        //フィールド初期化
        m_listView = getListView();
        m_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return onListItemLongClick(i);
            }
        });

        StageNowAsyncTask task = new StageNowAsyncTask();
        task.execute(EnvOption.getUrlStageNowJson(getActivity()));
    }

    /**
     * データの保存
     * @param outState 保存用バンドル
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PSDebug.d("call");
    }

    /**
     * 復旧時
     */
    @Override
    public void onResume() {
        super.onResume();
        PSDebug.d("call");
    }

    /**
     * リストが1件もなかったときの表示文字の設定
     * @param text 表示文字列
     */
    @Override
    public void setEmptyText(CharSequence text) {
        TextView tv = (TextView)m_listView.getEmptyView();
        if (tv != null) {
            tv.setText(text);
        }
    }

    /**
     * リストアイテムを選択したとき
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //何もしない
    }

    /**
     * リストアイテム項目を長押ししたとき
     * @param pos 位置
     * @return 使用したかどうか
     */
    public boolean onListItemLongClick(int pos) {
        return false;
    }

    /**
     * 現在のステージ情報を非同期で取得するためのタスククラス
     */
    public class StageNowAsyncTask extends AsyncTask<String, String, List<StageInfo>> {
        /**
         * 更新前処理
         */
        @Override
        protected void onPreExecute() { /* 何もしない。。。 */  }

        /**
         * 処理開始
         * 空文字を指定するとすべてのデータを取得する
         * @param text サーバーURL
         * @return 取得結果JSON
         */
        @Override
        protected List<StageInfo> doInBackground(String... text) {
            String android_id = android.provider.Settings.Secure.getString(getActivity().getContentResolver()
                    , android.provider.Settings.Secure.ANDROID_ID);

            String url = text[0] + "?id=" + android_id;
            PSDebug.d("url=" + url);

            List<StageInfo> output = null;
            try {
                String json = getHttpString(url);
                output = StageInfo.CreateStageListFromJson(json);
            }
            catch (Exception e) {
                output = null;
                PSDebug.d("ERROR:" + e.getMessage());
            }

            return output;
        }

        /**
         * 完了処理
         * @param stageInfoList 結果リスト
         */
        @Override
        protected void onPostExecute(List<StageInfo> stageInfoList) {
            if (stageInfoList == null)
            {
                String messageText = "データの取得に失敗しました";
                setEmptyText(messageText);
            }
            else
            {
                List<StageSchduleInfo> schduleList = StageSchduleInfo.CreateList(stageInfoList);
                if (getActivity() != null && getListView() != null) {
                    m_adapter = new MainListAdapter(getActivity(), schduleList);
                    setListAdapter(m_adapter);
                }
            }
        }

        /**
         * HTTPのGET処理で文字列を取得する
         * @param urlText URL文字列
         * @return 取得した結果
         */
        public String getHttpString(String urlText) {
            String result = "";
            HttpURLConnection conn = null;
            try {
                URL url = new URL(urlText);
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.connect();
                int resCode = conn.getResponseCode();
                if (resCode == 200) {
                    result = streamToString(conn.getInputStream());
                }
                else {
                    PSDebug.d("ERROR status=" + resCode);
                    result = "";
                }
            } catch(IOException e) {
                e.printStackTrace();
            } finally {
                if(conn != null) {
                    conn.disconnect();
                }
            }

            return result;
        }

        /**
         * ストリームから文字列を生成する（UTF-8専用）
         * @param stream 入力ストリーム
         * @return 文字列
         * @throws IOException 例外
         */
        public String streamToString(InputStream stream) {
            StringBuilder sb = new StringBuilder();
            String line = "";
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                while((line = br.readLine()) != null){
                    sb.append(line);
                }
                try {
                    stream.close();
                } catch(Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }
            catch (Exception err) {
                err.printStackTrace();
                return "";
            }
            return sb.toString();
        }
    }
}
