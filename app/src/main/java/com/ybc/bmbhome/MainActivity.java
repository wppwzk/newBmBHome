package com.ybc.bmbhome;

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
    }

    /**
     * 将3个主界面的fragment添加到list中*/
    private ArrayList<Fragment> getFragments() {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(Mood.newInstance("Mood"));
        localArrayList.add(Body.newInstance("Body"));
        localArrayList.add(User.newInstance("User"));
        return localArrayList;
    }

    /**
     * 这只启动时默认显示的fragment*/
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.layFrame, Mood.newInstance("Mood"));
        transaction.commit();
    }

    /**
     * 当底部导航栏点击时fragment的显示和隐藏
     * @param position 选择的位置*/
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
     * @param position 选择的位置*/
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
