package com.ybc.bmbhome.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;
import com.ybc.bmbhome.adapter.UserAdapter;
import com.ybc.bmbhome.function.LoginActivity;
import com.ybc.bmbhome.function.PreferenceActivity;
import com.ybc.bmbhome.function.UserMoodActivity;
import com.ybc.bmbhome.function.UserStepActivity;
import com.ybc.bmbhome.utils.DividerItemDecoration;
import com.ybc.bmbhome.utils.MessageEvent;
import com.ybc.bmbhome.utils.UserItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的fragment*/
public class User extends Fragment implements View.OnTouchListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private List<UserItem> userItemList = new ArrayList<>();
    private CircleImageView user_login_bt;
    private TextView usrnametxt, usermood, userNowStep;

    public static User newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        User fragment = new User();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        user_login_bt = (CircleImageView) view.findViewById(R.id.user_login);
        usrnametxt = (TextView) view.findViewById(R.id.user_id_text);
        // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        usermood = (TextView) view.findViewById(R.id.mood_user_text);
        userNowStep = (TextView) view.findViewById(R.id.bushu_user_text);
        SharedPreferences s = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String mood = s.getString("usermood", "心情");
        String step = s.getString("setpnow", "0步");
        usermood.setText(mood);
        userNowStep.setText(step);
        EventBus.getDefault().register(this);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.ybc.bmbhome_preferences", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("set_user_name", "点击登录");
        usrnametxt.setText("点击登录");
        usrnametxt.setText(name);
        initUserItem();
        RecyclerView rev = (RecyclerView) view.findViewById(R.id.user_recleView);
        LinearLayoutManager laymanager = new LinearLayoutManager(getContext());
        rev.setLayoutManager(laymanager);
        //添加装饰类
        rev.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL, 25, getResources().getColor(R.color.background_greey)));
        UserAdapter adapter = new UserAdapter(userItemList);
        rev.setAdapter(adapter);
        adapter.setItemClickListener(new UserAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                switch (position) {
                    case 0:
                        Intent intent0 = new Intent(getContext(), UserMoodActivity.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getContext(), UserStepActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getContext(), PreferenceActivity.class);
                        startActivity(intent2);
                        break;
                }
            }
        });
        user_login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                //Toast.makeText(getContext(),"ds", Toast.LENGTH_SHORT).show();
            }
        });

        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(getActivity());
        localSystemBarTintManager.setStatusBarTintResource(R.color.colorAccent);
        localSystemBarTintManager.setStatusBarTintEnabled(true);
        return view;
    }

    private void initUserItem() {
        UserItem userstep = new UserItem("我的步数", R.drawable.ic_action_walk);
        UserItem usermood = new UserItem("我的心情", R.drawable.ic_action_mood);
        UserItem userconf = new UserItem("我的设置", R.drawable.ic_action_setting);
        userItemList.add(usermood);
        userItemList.add(userstep);
        userItemList.add(userconf);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(MessageEvent event) {
        String s = event.getMessage();
        String as = s.substring(0, 7);
        //String bs = s.substring(0, 7);
        if (as.equals("setpnow")) {
            String ss2 = as.substring(7);
            userNowStep.setText(ss2);
        }
        if (as.equals("moodnow")) {
            String a2 = s.substring(7);
            usermood.setText(a2);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

            return;
        }

        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(getActivity());
        localSystemBarTintManager.setStatusBarTintResource(R.color.colorAccent);
        localSystemBarTintManager.setStatusBarTintEnabled(true);

        SharedPreferences ss = getActivity().getSharedPreferences("com.ybc.bmbhome_preferences", Context.MODE_PRIVATE);
        String name = ss.getString("set_user_name", "");
        usrnametxt.setText(name);

        SharedPreferences s = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String mood = s.getString("usermood", "心情");
        String step = s.getString("setpnow", "0步");
        usermood.setText(mood);
        userNowStep.setText(step);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
