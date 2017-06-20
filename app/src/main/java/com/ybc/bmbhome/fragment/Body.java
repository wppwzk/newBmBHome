package com.ybc.bmbhome.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;
import com.ybc.bmbhome.database.DbHelper;
import com.ybc.bmbhome.function.CameraActivity;
import com.ybc.bmbhome.function.MapActivity;
import com.ybc.bmbhome.function.MobileActivity;
import com.ybc.bmbhome.step.Pedometer;
import com.ybc.bmbhome.utils.DbUtil;
import com.ybc.bmbhome.view.TasksCompletedView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 身体fragment*/
public class Body extends Fragment implements View.OnTouchListener {
    int ToltalProgress = 0;
    int stepNow = 0;
    int b;
    private Button butmobole;
    private Button butt;
    private int mCurrentProgress;
    private TasksCompletedView mTasksView;
    private Button runbt, camerabt;
    private TextView tv;
    private Pedometer pedometer;
    private DbHelper dbHelper;

    public static Body newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        Body fragment = new Body();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_body, container, false);
        pedometer = new Pedometer(getContext());
        pedometer.register();

        tv = (TextView) view.findViewById(R.id.nonstepfpuncition);
        butmobole = (Button) view.findViewById(R.id.buttonmobile);
        camerabt = (Button) view.findViewById(R.id.but_photo_body);
        camerabt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CameraActivity.class);
                startActivity(intent);
            }
        });
        if (!pedometer.step) {
            // tv.setText("此手机没有计步传感器，不支持计步");
        }
        mTasksView = (TasksCompletedView) view.findViewById(R.id.tasks_view);
        initVariable();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                while (mCurrentProgress < ToltalProgress) {
                    mCurrentProgress = (int) pedometer.getStepCount();
                    stepNow = mCurrentProgress;
                    mTasksView.setProgress(mCurrentProgress, ToltalProgress);
                    try {
                        Thread.sleep(80);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        };

        Timer timer = new Timer();
        timer.schedule(task, 100, 1000);
        butmobole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MobileActivity.class);
                startActivity(intent);
            }
        });
        runbt = (Button) view.findViewById(R.id.run_body);
        runbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                startActivity(intent);
            }
        });
        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(getActivity());
        localSystemBarTintManager.setStatusBarTintResource(R.color.blue);
        localSystemBarTintManager.setStatusBarTintEnabled(true);
        //return inflater.inflate(R.layout.fragment_body, container, false);
        return view;
    }

    private void seeStep() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);
        //Step laststep = DataSupport.findLast(Step.class);


    }

    private void initVariable() {
        SharedPreferences ss = getActivity().getSharedPreferences("com.ybc.bmbhome_preferences", Context.MODE_PRIVATE);
        String step = ss.getString("set_user_step", "");
        try {
            b = Integer.parseInt(step);
        } catch (NumberFormatException e) {
            b = 15000;
        }
        ToltalProgress = b;
        mCurrentProgress = 0;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pedometer.register();
    }

    @Override
    public void onStop() {
        super.onStop();
        pedometer.unRegister();
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
        if (paramBoolean) {
            DbUtil db = new DbUtil(getContext());
            //db.insertStep(stepNow);

            SharedPreferences.Editor e = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
            e.putString("setpnow", stepNow + "");
            e.apply();
            return;
        }

        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(getActivity());
        localSystemBarTintManager.setStatusBarTintResource(R.color.blue);
        localSystemBarTintManager.setStatusBarTintEnabled(true);

        SharedPreferences ss = getActivity().getSharedPreferences("com.ybc.bmbhome_preferences", Context.MODE_PRIVATE);
        String step = ss.getString("set_user_step", "");
        try {
            b = Integer.parseInt(step);
        } catch (NumberFormatException e) {
            b = 15000;
        }
        ToltalProgress = b;


    }


}
