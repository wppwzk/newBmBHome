package com.ybc.bmbhome.function;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ybc.bmbhome.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OpenReadActivity extends AppCompatActivity {


    String title = null;
    String imgurl = null;
    int id;
    String body = null;
    StringBuffer test = null;
    TextView fruitContentText;
    StringBuffer str = new StringBuffer("");
    ImageView fruitImageView;
    String imgurlll="";
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String sss = String.valueOf(str);
                    String fruitContent = sss;
                    fruitContentText.setText(fruitContent);
                    Glide.with(OpenReadActivity.this).load("http://www.bmbhome.org" + imgurlll).into(fruitImageView);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_read);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        imgurl = intent.getStringExtra("imgurl");
        String ssid = intent.getStringExtra("openid");
        id = Integer.parseInt(ssid);
        //Toast.makeText(OpenReadActivity.this, id + "", Toast.LENGTH_SHORT).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_read);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        fruitImageView = (ImageView) findViewById(R.id.fruit_image_view);
        fruitContentText = (TextView) findViewById(R.id.fruit_content_text);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(title);

        sendOkhttp("http://www.bmbhome.org/api/article_detail/" + id);

    }


    private void sendOkhttp(final String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    body = response.body().string();
                    parseJSON(body);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private void parseJSON(String jsonData) {
        try {
            String s = "";

            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                s += jsonObject.getString("content");
                imgurlll=jsonObject.getString("imgUrl");
            }

            String regex = "<[\\s\\S]*?>|";
            //需要操作的内容
            String content = s;
            //操作后的结果resu
            String resu = content.replaceAll(regex, "");
            String con2 = resu;
            String x = con2.replaceAll("&nbsp;", "");
            str.append(x);
            System.out.println(resu);
            hander.sendEmptyMessage(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
