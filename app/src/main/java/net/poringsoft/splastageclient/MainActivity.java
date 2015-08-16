package net.poringsoft.splastageclient;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * メイン画面
 */
public class MainActivity extends ActionBarActivity {
    //定数
    //----------------------------------------------------------
    private static final String URL_OFFICIAL_IKARING = "https://splatoon.nintendo.net/";

    //フィールド
    //----------------------------------------------------------


    //メソッド
    //----------------------------------------------------------
    /**
     * 画面起動時処理
     * @param savedInstanceState 保存データ
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PSDebug.d("call");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new MainFragment()).commit();
        }
    }

    /**
     * 復旧時
     */
    @Override
    protected void onResume() {
        super.onResume();
        PSDebug.d("call");
    }

    /**
     * 停止時
     */
    @Override
    protected void onPause() {
        super.onPause();
        PSDebug.d("call");
    }

    /**
     * メニュー表示設定
     * @param menu メニュー
     * @return 設定時はtrue
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * メニュー項目選択イベント
     * @param item 選択メニューアイテム
     * @return 選択処理を行ったかどうか
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startSetting();
                break;
            case R.id.action_jump_official:
                startJumpWebSite(URL_OFFICIAL_IKARING);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 設置画面を表示する
     */
    private void startSetting() {
        Intent intent = new Intent(this, PrefActivity.class);
        this.startActivity(intent);
    }

    /**
     * 指定したURLでブラウザを開く
     * @param url URL文字列
     */
    private void startJumpWebSite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
