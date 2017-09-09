package com.listenerri.drhenau;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.Toast;


public class MyNavigationItemSelectedListener
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int QQGROUP = 0;
    private static final int GITHUB = 1;
    private static final int SETTINGS = 2;
    private static final int ABOUT = 3;

    private Context mainContext = null;
    private DrawerLayout drawer = null;

    public MyNavigationItemSelectedListener(Context mContext, DrawerLayout drawer) {
        this.mainContext = mContext;
        this.drawer = drawer;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // 处理抽屉页面的选项
        int id = item.getItemId();

        if (id == R.id.nav_qq) {
            // 显示qq群号
            openQQDialog("QQ群 224855047", new String[]{"点击此处即可复制群号"}, QQGROUP);
        } else if (id == R.id.nav_github) {
            // 用浏览器打开github
            openQQDialog("提示", new String[]{"点击此处即可查看源代码"}, GITHUB);
        } else if (id == R.id.nav_setting) {
            // 设置页面
            openQQDialog("提示", new String[]{"暂不需要设置功能"}, SETTINGS);
        } else if (id == R.id.nav_about) {
            // 关于窗口
            openQQDialog("关于", new String[]{"作者: 梁东日", "版本: 1.0"}, ABOUT);
        }

        // 关闭抽屉
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openGithub() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri urlGithub = Uri.parse("https://github.com/ListenerRi/DrHenau");
        intent.setData(urlGithub);
        mainContext.startActivity(intent);
    }

    private void openQQDialog(String title, String[] msgArray, int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
        builder.setTitle(title);
        switch (type) {
            case QQGROUP: {
                builder.setItems(msgArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager)
                                mainContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("224855047", "224855047");
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(mainContext, "已复制到剪切板", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case GITHUB: {
                builder.setItems(msgArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openGithub();
                    }
                });
                break;
            }
            case SETTINGS: {
                builder.setItems(msgArray, null);
                break;
            }
            case ABOUT: {
                builder.setItems(msgArray, null);
                break;
            }
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
