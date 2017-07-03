package com.ybc.bmbhome.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;
import com.ybc.bmbhome.function.DiaryActivity;
import com.ybc.bmbhome.function.ReadActivity;
import com.ybc.bmbhome.function.SleepActivity;
import com.ybc.bmbhome.function.VoiceActivity;
import com.ybc.bmbhome.function.WebActivity;
import com.ybc.bmbhome.utils.DbUtil;

import java.util.Calendar;

/**
 * 心情fragment
 */
public class Mood extends Fragment implements View.OnTouchListener {

    private Button wenzhangbt, ceshibt, doctorbt, jiangzuobt;
    private TextView datatext, moodText;

    private DbUtil db;
    private String date;
    String moodm = "快乐";

    public static Mood newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        Mood fragment = new Mood();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood, container, false);
        Calendar now = Calendar.getInstance();

        date = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH);
        moodText = (TextView) view.findViewById(R.id.textViewmood_now);
        SharedPreferences s = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String mood = s.getString("usermood", "设置心情");
        moodText.setText(mood);
        wenzhangbt = (Button) view.findViewById(R.id.wenzahng);
        ceshibt = (Button) view.findViewById(R.id.ceshi);
        jiangzuobt = (Button) view.findViewById(R.id.jiangzuo);
        doctorbt = (Button) view.findViewById(R.id.yisheng);
        datatext = (TextView) view.findViewById(R.id.textViewmooddate);
        datatext.setText(date);
        wenzhangbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReadActivity.class);
                //intent.putExtra("url", "http://www.bmbhome.org/article");
                startActivity(intent);
            }
        });
        ceshibt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("url", "http://www.bmbhome.org/question");
                startActivity(intent);
            }
        });
        jiangzuobt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("url", "http://www.bmbhome.org/chair");
                startActivity(intent);
            }
        });
        doctorbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("url", "http://www.bmbhome.org/findDoctor");
                startActivity(intent);
            }
        });

        //EventBus.getDefault().register(getActivity());
        View sleep = view.findViewById(R.id.sleepmoodlayout);
        View relax = view.findViewById(R.id.relaxmoodlayout);
        View jilumood = view.findViewById(R.id.jilumod);
        View moodtext = view.findViewById(R.id.textViewmood_now);
        moodtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMood();
            }
        });
        jilumood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DiaryActivity.class);
                startActivity(intent);

            }
        });
        sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SleepActivity.class);
                startActivity(intent);
            }
        });
        relax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VoiceActivity.class);
                startActivity(intent);
            }
        });
        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(getActivity());
        localSystemBarTintManager.setStatusBarTintResource(R.color.colorGreen);
        localSystemBarTintManager.setStatusBarTintEnabled(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // onTouch事件 将上层的触摸事件拦截
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // 拦截触摸事件，防止泄露下去
        view.setOnTouchListener(this);
    }

    public void onHiddenChanged(boolean paramBoolean) {
        super.onHiddenChanged(paramBoolean);
        if (paramBoolean)
            return;
        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(getActivity());
        localSystemBarTintManager.setStatusBarTintResource(R.color.colorGreen);
        localSystemBarTintManager.setStatusBarTintEnabled(true);

        SharedPreferences s = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String mood = s.getString("usermood", "设置心情");
        moodText.setText(mood);
    }

    private void showMood() {
        final String[] items = getResources().getStringArray(R.array.mooditem);
        db = new DbUtil(getContext());
        new AlertDialog.Builder(getContext())
                .setTitle("现在的心情")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String moodmow = items[which];
                        SharedPreferences.Editor e = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                        e.putString("usermood", moodmow);
                        e.apply();
                        moodText.setText(moodmow);
                        moodm = moodmow;
                        db.insertMood(moodm);
                    }
                }).show();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db != null)
            db.closeDB();
    }
}
