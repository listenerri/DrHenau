package com.listenerri.drhenau;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.widget.Toast;


public class MyNavigationItemSelectedListener
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String QQGroupKey = "j5-1ftQ4sGZ-mQ8qaw8Q8Kba5ndGBr0F";

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // 处理抽屉页面的选项
        int id = item.getItemId();

        if (id == R.id.nav_qq) {
            // 加群
            if (!joinQQGroup()) {
                Toast.makeText(MainActivity.mContext, "未安装手Q或安装的版本不支持", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_github) {
            // 用浏览器打开github
            openGithub();
        } else if (id == R.id.nav_setting) {
            // 设置页面
            openSettings();
        } else if (id == R.id.nav_about) {
            // 关于窗口
            openAboutDialog();
        }

        // 关闭抽屉
        MainActivity.drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean joinQQGroup() {
        return MainActivity.mTencent.joinQQGroup((Activity) MainActivity.mContext, QQGroupKey);
    }

    private void openGithub() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri urlGithub = Uri.parse("https://github.com/ListenerRi/DrHenau");
        intent.setData(urlGithub);
        MainActivity.mContext.startActivity(intent);
    }

    private void openSettings() {
        // TODO: 设置页面
    }


    private void openAboutDialog() {
        // TODO: 关于页面
    }
}
