package com.ybc.bmbhome.function;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 注册页面*/
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private static final String APPKEY = "1bcdc8304d8e3";
    private static final String APPSECRECT = "c9119442f9a5bb7ea2059b0f34636d5d";
    private EditText mEtPhone;
    private EditText mEtPhoneCode;
    private Button mBtnSubmitUser;
    private TextView mTvGetPhoneCode;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar = (Toolbar) findViewById(R.id.toolbar_user_register);
        linearLayout = (LinearLayout) findViewById(R.id.snacklayout);
        toolbar.setTitle("用户注册");
        setSupportActionBar(toolbar);
        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(this);
        localSystemBarTintManager.setStatusBarTintResource(R.color.colorAccent);
        localSystemBarTintManager.setStatusBarTintEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initSMSSDK();
        initView();
        setListener();
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

    private void initView() {
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPhoneCode = (EditText) findViewById(R.id.et_phone_code);
        mEtPhoneCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEtPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
        mTvGetPhoneCode = (TextView) findViewById(R.id.tv_get_phone_code);
        mBtnSubmitUser = (Button) findViewById(R.id.btn_submit_user);
    }

    private void setListener() {
        mTvGetPhoneCode.setOnClickListener(this);
        mBtnSubmitUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击获取验证码控件
            case R.id.tv_get_phone_code:
                mTvGetPhoneCode.requestFocus();
                if (validatePhone()) {
                    //启动获取验证码 86是中国
                    String phone = mEtPhone.getText().toString().trim();
                    SMSSDK.getVerificationCode("86", phone);//发送短信验证码到手机号
                    timer.start();//使用计时器 设置验证码的时间限制
                }else{
                    Snackbar.make(linearLayout, "请输入正确手机号", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            //点击提交信息按钮
            case R.id.btn_submit_user:
                submitInfo();
                break;
        }
    }

    /**
     * 验证用户的其他信息
     * 这里验证两次密码是否一致 以及验证码判断
     */
    private void submitInfo() {
        //密码验证
        String phone = mEtPhone.getText().toString().trim();
        String code = mEtPhoneCode.getText().toString().trim();
        SMSSDK.submitVerificationCode("86", phone, code);//提交验证码  在eventHandler里面查看验证结果
    }

    /**
     * 使用计时器来限定验证码
     * 在发送验证码的过程 不可以再次申请获取验证码 在指定时间之后没有获取到验证码才能重新进行发送
     * 这里限定的时间是60s
     */
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mTvGetPhoneCode.setText((millisUntilFinished / 1000) + "秒后可重发");
            mTvGetPhoneCode.setClickable(false);
        }

        @Override
        public void onFinish() {
            mTvGetPhoneCode.setEnabled(true);
            mTvGetPhoneCode.setText("获取验证码");
            mTvGetPhoneCode.setClickable(true);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止使用短信验证 产生内存溢出问题
        SMSSDK.unregisterAllEventHandler();
    }


    /**
     * 验证手机号码是否符合要求，11位 并且没有注册过
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     * @return 是否符合要求
     */
    private boolean validatePhone() {
        String phone = mEtPhone.getText().toString().trim();
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 初始化短信验证
     * 注册短信回调*/
    private void initSMSSDK() {
        //初始化短信验证
        SMSSDK.initSDK(this, APPKEY, APPSECRECT);

        //注册短信回调
        SMSSDK.registerEventHandler(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                switch (event) {
                    case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            Toast.makeText(RegisterActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            Snackbar.make(linearLayout, "验证码发送成功", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            Log.e("获取验证成功", "");
                        } else {
                            Snackbar.make(linearLayout, "验证码发送失败", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            Log.e("获取验证失败", "");
                        }
                        break;
                }
            }
        });
    }
}
