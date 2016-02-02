package com.hexorqianghongbao.activities;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import com.hexorqianghongbao.R;
import com.hexorqianghongbao.utils.ConnectivityUtil;
import com.hexorqianghongbao.utils.UpdateTask;

import java.lang.reflect.Method;
import java.util.List;

/***
 * 加载主界面
 */
public class MainActivity extends Activity {
    /***
     * 跳转到服务设置中开始后台服务
     */
    private final Intent mAccessibleIntent =new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);

    private Button switchPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchPlugin = (Button) findViewById(R.id.button_accessible);
        handleMaterialStatusBar();
        updateServiceStatus();
        explicitlyLoadPreferences();
    }

    private void explicitlyLoadPreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    /**
     * 适配MIUI沉浸状态栏
     */
    @TargetApi(21)
    private void handleMaterialStatusBar() {
        // Not supported in APK level lower than 21
        if (Build.VERSION.SDK_INT < 21) return;
//
        Window window = this.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(0xffd84e43);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateServiceStatus();

        // Check for update when WIFI is connected or on first time.
        //在wifi下检查版本更新
//        if (ConnectivityUtil.isWifi(this) || UpdateTask.count == 0) new UpdateTask(this, false).update();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void updateServiceStatus() {
        boolean serviceEnabled = false;

        AccessibilityManager accessibilityManager =
                (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(getPackageName() + "/.services.HongbaoService")) {
                serviceEnabled = true;
                break;
            }
        }
        if (serviceEnabled) {
            switchPlugin.setText(R.string.service_off);
        } else {
            switchPlugin.setText(R.string.service_on);
        }
    }

    public void onButtonClicked(View view) {
        startActivity(mAccessibleIntent);
    }

    public void openGithub(View view) {
//        Intent webViewIntent = new Intent(this, WebViewActivity.class);
//        webViewIntent.putExtra("title", "Github项目主页");
//        webViewIntent.putExtra("url", "https://github.com/geeeeeeeeek/WeChatLuckyMoney");
//        startActivity(webViewIntent);
    }

    public void openGithubReleaseNotes(View view) {
//        Intent webViewIntent = new Intent(this, WebViewActivity.class);
//        webViewIntent.putExtra("title", "发布日志");
//        webViewIntent.putExtra("url", "https://github.com/geeeeeeeeek/WeChatLuckyMoney/issues?q=is%3Aissue+is%3Aopen+label%3A%22release+notes%22");
//        startActivity(webViewIntent);
    }

    /***
     * 跳转红包设置
     * @param view
     */
    public void openSettings(View view) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    /***
     * 打开红包攻略
     * @param view
     */
    public void openNews(View view) {
        Intent webViewIntent = new Intent(this, WebViewActivity.class);
        webViewIntent.putExtra("title", "红包攻略");
        webViewIntent.putExtra("url", "http://sec-cdn.static.xiaomi.net/secStatic/proj/luckyNewsInfo/0127/index.html?v=1&");
        startActivity(webViewIntent);
    }

}
