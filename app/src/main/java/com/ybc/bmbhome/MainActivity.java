package com.ybc.bmbhome;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ybc.bmbhome.fragment.Body;
import com.ybc.bmbhome.fragment.Mood;
import com.ybc.bmbhome.fragment.User;
import com.ybc.bmbhome.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    public static final String LOCK = "lock";
    public static final String LOCK_KEY = "lock_key";
    private ActionBar actionBar;
    private ArrayList<Fragment> fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.actionBar = getSupportActionBar();
        BottomNavigationBar localBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        localBottomNavigationBar.setMode(1);
        localBottomNavigationBar.setBackgroundStyle(1);
        localBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.mood, "情绪").setActiveColorResource(R.color.colorGreen))
                .addItem(new BottomNavigationItem(R.drawable.body, "身体").setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.drawable.uesr, "我的").setActiveColorResource(R.color.colorAccent))
                .setFirstSelectedPosition(0).initialise();
        this.fragments = getFragments();

        setDefaultFragment();

        localBottomNavigationBar.setTabSelectedListener(this);

        PreferenceManager.setDefaultValues(this, R.xml.preference, false);

        createShortCut(MainActivity.this);
    }

    /**
     * 将3个主界面的fragment添加到list中
     */
    private ArrayList<Fragment> getFragments() {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(Mood.newInstance("Mood"));
        localArrayList.add(Body.newInstance("Body"));
        localArrayList.add(User.newInstance("User"));
        return localArrayList;
    }

    /**
     * 设置启动时默认显示的fragment
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.layFrame, Mood.newInstance("Mood"));
        transaction.commit();
    }

    /**
     * 当底部导航栏点击时fragment的显示和隐藏
     *
     * @param position 选择的位置
     */
    @Override
    public void onTabSelected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                if (fragment.isAdded()) {
                    //ft.replace(R.id.layFrame, fragment);
                    for (int i = 0; i < fragments.size(); i++) {
                        ft.hide(fragments.get(i));
                    }
                    ft.show(fragment);
                } else {
                    ft.add(R.id.layFrame, fragment);
                }
                ft.commitAllowingStateLoss();
            }
        }
    }

    /**
     * 当底部导航栏未被点击时fragment的显示和隐藏
     *
     * @param position 选择的位置
     */
    @Override
    public void onTabUnselected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.hide(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    // Called in Android UI's main thread
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageEvent event) {
        String s = event.getMessage();
        if (s.equals("clear_pass")) {

        }
    }

    /**
     * 创建快捷方式：发送广播
     * 需要设置好程序名，图标，和启动的Activity。
     */
    public static void createShortCut(Context contxt) {
        String applicationName = getApplicationName(contxt);//程序名称，不是packageName

        if (isInstallShortcut(contxt, applicationName)) {// 如果已经创建了一次就不会再创建了
            return;
        }
        Intent sIntent = new Intent(Intent.ACTION_MAIN);
        sIntent.addCategory(Intent.CATEGORY_LAUNCHER);// 加入action,和category之后，程序卸载的时候才会主动将该快捷方式也卸载
        sIntent.setClass(contxt, MainActivity.class);//点击后进入的Activity

        Intent installer = new Intent();
        installer.putExtra("duplicate", false);//false标示不重复创建
        installer.putExtra("android.intent.extra.shortcut.INTENT", sIntent);
        //设置应用的名称
        installer.putExtra("android.intent.extra.shortcut.NAME", applicationName);
        //设置图标
        installer.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(contxt, R.mipmap.apphead));
        installer.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        contxt.sendBroadcast(installer);//发送安装桌面图标的通知
    }

    /**
     * 检测是否创建过该快捷方式
     * 使用ContentResolver查询数据库，看是否存在同名的shortcut。因为历史原因，用两个launcher，需要根据sdk版本进行查询。
     */
    public static boolean isInstallShortcut(Context context, String applicationName) {
        boolean isInstallShortcut = false;
        ContentResolver cr = context.getContentResolver();
        //sdk大于8的时候,launcher2的设置查找
        String AUTHORITY = "com.android.launcher2.settings";
        Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI, new String[]{"title", "iconResource"},
                "title=?", new String[]{applicationName}, null);
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }
        if (c != null) {
            c.close();
        }
        //如果存在先关闭cursor，再返回结果
        if (isInstallShortcut) {
            return isInstallShortcut;
        }
        //android.os.Build.VERSION.SDK_INT < 8时
        AUTHORITY = "com.android.launcher.settings";
        CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        c = cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?",
                new String[]{applicationName}, null);
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }
        if (c != null) {
            c.close();
        }
        return isInstallShortcut;
    }

    /**
     * 获取当前app的应用程序名称
     */
    public static String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
