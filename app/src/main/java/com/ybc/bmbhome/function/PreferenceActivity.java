package com.ybc.bmbhome.function;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.ybc.bmbhome.R;

import static com.ybc.bmbhome.R.xml.preference;

/**
 * Created by YBC on 2017/4/25.
 * 设置页面
 */

public class PreferenceActivity extends AppCompatActivity {

    private EditTextPreference username;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_setting);
        super.onCreate(savedInstanceState);
        setupActionBar();
        //EventBus.getDefault().register(this);
        // 不要设置setcontentview
        if (savedInstanceState == null) {
            SettingFragment settingFragment = new SettingFragment();
            getFragmentManager().beginTransaction().add(android.R.id.content, settingFragment).commit();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("sa", "sas");
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("设置");
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

    public static class SettingFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        private SharedPreferences sp;
        private Preference cleanDiaryPassword;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // 加载xml资源文件
            addPreferencesFromResource(preference);
            sp = getPreferenceScreen().getSharedPreferences();
            findPreference("set_user_name").setSummary(sp.getString("set_user_name", "key1_default value"));
            findPreference("set_user_step").setSummary(sp.getString("set_user_step", "key1_default value"));

        }


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference pref = findPreference(key);
            if (pref instanceof EditTextPreference) {
                EditTextPreference etp = (EditTextPreference) pref;
                pref.setSummary(etp.getText());
            }
        }


        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }
    }
}
