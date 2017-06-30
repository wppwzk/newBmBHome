package com.ybc.bmbhome.function;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;
import com.ybc.bmbhome.adapter.FragmentAdapter;
import com.ybc.bmbhome.fragment.EmailRegisterFragment;
import com.ybc.bmbhome.fragment.TelRegisterFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册页面
 */
public class RegisterActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_reg);
        setSupportActionBar(toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        initViewPager();
        setupActionBar();

        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(this);
        localSystemBarTintManager.setStatusBarTintResource(R.color.colorAccent);
        localSystemBarTintManager.setStatusBarTintEnabled(true);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("注册");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击back键finish当前activity
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void initViewPager() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        List<String> titles = new ArrayList<>();
        titles.add("邮箱注册");
        titles.add("电话注册");


        for (int i = 0; i < titles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();
        //for(int i=0;i<titles.size();i++){
        fragments.add(new EmailRegisterFragment());
        fragments.add(new TelRegisterFragment());
        //}
        FragmentAdapter mFragmentAdapteradapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mFragmentAdapteradapter);
        //将TabLayout和ViewPager关联起来。
        mTabLayout.setupWithViewPager(mViewPager);
        //给TabLayout设置适配器
        mTabLayout.setTabsFromPagerAdapter(mFragmentAdapteradapter);
    }
}
