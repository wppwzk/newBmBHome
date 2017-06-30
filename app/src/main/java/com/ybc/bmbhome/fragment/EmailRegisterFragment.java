package com.ybc.bmbhome.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ybc.bmbhome.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EmailRegisterFragment extends Fragment {
    private EditText nicheng, email, password;
    private String nicheng1, email1, pass1;
    private Button zhuce;
    private static final int RETURN_OK = 0;
    private static final int RETURN_FAIL = 1;
    private static final int RETURN_WRONG = 2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private final Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RETURN_OK:
                    Toast.makeText(getContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    break;
                case RETURN_FAIL:
                    Toast.makeText(getContext(), "注册失败", Toast.LENGTH_SHORT).show();
                case RETURN_WRONG:
                    Toast.makeText(getContext(), "信息有误，请检查", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email_register, container, false);
        nicheng = (EditText) view.findViewById(R.id.et_nicheng);
        email = (EditText) view.findViewById(R.id.et_email);
        password = (EditText) view.findViewById(R.id.et_mima);
        zhuce = (Button) view.findViewById(R.id.btn_submit_email);
        zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nicheng1 = nicheng.getText().toString();
                email1 = email.getText().toString();
                pass1 = password.getText().toString();
                if (email1 != null) {
                    if (nicheng1 != null && ifEmail(email1) && pass1 != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                register(nicheng1, email1, pass1);
                            }
                        }).start();

                    }else{
                        Message msg = msgHandler.obtainMessage();
                        msg.what = RETURN_WRONG;
                        msgHandler.sendMessage(msg);
                    }
                }

            }
        });
        return view;
    }

    private boolean ifEmail(String em) {
        String regex = "\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}";
        // 判断输入数据是否为Email地址
        if (em.matches(regex)) {
            return true;
        } else {
            return false;
        }
    }

    private void register(String ni, String em, String pa) {
        OkHttpClient okHttpClient = new OkHttpClient();

        FormBody.Builder builderf = new FormBody.Builder();
        builderf.add("name", ni).add("email", em).add("password", pa);
        RequestBody formBody = builderf.build();

        Request.Builder builder = new Request.Builder();
        final Request request = builder.url("http://bbs.bmbhome.org/api/register").post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = msgHandler.obtainMessage();
                msg.what = RETURN_FAIL;
                msgHandler.sendMessage(msg);
                Log.e("sa","fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body().string().equals("success")){
                    Message msg = msgHandler.obtainMessage();
                    msg.what = RETURN_OK;
                    msgHandler.sendMessage(msg);
                    getActivity().finish();
                }else {
                    Message msg = msgHandler.obtainMessage();
                    msg.what = RETURN_FAIL;
                    msgHandler.sendMessage(msg);
                }
            }
        });
    }
}
