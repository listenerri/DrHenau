package com.listenerri.drhenau;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tencent.tauth.Tencent;


public class MainActivity extends AppCompatActivity {

    private static final String AppId = "222222";
    public static Tencent mTencent;
    public static Context mContext;
    public static DrawerLayout drawer = null;

    private ActionBarDrawerToggle toggle = null;
    private Toolbar toolbar = null;
    private NavigationView navigationView = null;
    private FragmentManager fm = null;
    private FragmentTransaction ft = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();
        setDefaultFragment();
    }

    public void initFields() {
        mTencent = Tencent.createInstance(AppId, this);
        mContext = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new MyNavigationItemSelectedListener());

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
    }

    public void setDefaultFragment() {
        ft.add(R.id.fragmentContainer, new LoginFragment());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        //如果抽屉时打开状态则关闭它
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // 设置
        // TODO: 进入设置页面
        if (id == R.id.action_settings) {
            //
        }

        return super.onOptionsItemSelected(item);
    }
}
