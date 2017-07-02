package com.ybc.bmbhome.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ybc.bmbhome.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class TelRegisterFragment extends Fragment {

    private static final String APPKEY = "1d354b908394b";
    private static final String APPSECRECT = "0892d9990b562afb49e2e0685ac6b037";
    private EditText mEtPhone;
    private EditText mEtPhoneCode;
    private Button mBtnSubmitUser;
    private Button mTvGetPhoneCode;
    private LinearLayout linearLayout;
    private static final int RETURN_OK = 0;
    private static final int RETURN_FAIL = 1;

    private final Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RETURN_OK:
                    Toast.makeText(getContext(), "验证成功", Toast.LENGTH_SHORT).show();
                    break;
                case RETURN_FAIL:
                    Toast.makeText(getContext(), "验证失败", Toast.LENGTH_SHORT).show();

                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        linearLayout = (LinearLayout) view.findViewById(R.id.snacklayout);
        mEtPhone = (EditText) view.findViewById(R.id.et_phone);
        mEtPhoneCode = (EditText) view.findViewById(R.id.et_phone_code);
        mEtPhoneCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEtPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
        mTvGetPhoneCode = (Button) view.findViewById(R.id.tv_get_phone_code);
        mBtnSubmitUser = (Button) view.findViewById(R.id.btn_submit_user);
        initSMSSDK();
        mTvGetPhoneCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvGetPhoneCode.requestFocus();
                if (validatePhone()) {
                    //启动获取验证码 86是中国
                    String phone = mEtPhone.getText().toString().trim();
                    SMSSDK.getVerificationCode("86", phone);//发送短信验证码到手机号
                    timer.start();//使用计时器 设置验证码的时间限制
                } else {
                    Snackbar.make(linearLayout, "请输入正确手机号", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        mBtnSubmitUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitInfo();
            }
        });
        return view;


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
    public void onDestroy() {
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
     *
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
     * 注册短信回调
     */
    private void initSMSSDK() {
        //初始化短信验证
        SMSSDK.initSDK(getContext(), APPKEY, APPSECRECT);

        //注册短信回调
        SMSSDK.registerEventHandler(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                switch (event) {
                    case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            Message msg = msgHandler.obtainMessage();
                            msg.what = RETURN_OK;
                            msgHandler.sendMessage(msg);
                        } else {
                            Message msg = msgHandler.obtainMessage();
                            msg.what = RETURN_FAIL;
                            msgHandler.sendMessage(msg);
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

