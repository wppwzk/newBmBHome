package com.ybc.bmbhome.function;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;
import com.ybc.bmbhome.adapter.ReadAdapter;
import com.ybc.bmbhome.utils.ReadList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReadActivity extends AppCompatActivity {
    private Button shangbut, xiabut;
    ListView listView;
    ProgressBar progressBar;
    ReadAdapter readAdapter;
    TextView allpagetext;
    int pages = 1;
    int nowpage;
    Bitmap bm = null;
    private Context mContext;
    private List<ReadList> readListList = new ArrayList<>();
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    readAdapter.notifyDataSetChanged(); //发送消息通知ListView更新
                    listView.setAdapter(readAdapter); // 重新设置ListView的数据适配器
                    break;
                case 1:
                    allpagetext.setText(nowpage + " / " + pages);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        shangbut = (Button) findViewById(R.id.shangyiye);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_readlist);
        allpagetext = (TextView) findViewById(R.id.textView_allpage);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("读文章");
        }
        xiabut = (Button) findViewById(R.id.xiayiyebutton);
        if (savedInstanceState != null) {
            nowpage = savedInstanceState.getInt("Nowpage");
        } else {
            nowpage = 1;
        }
        String nowp = Integer.toString(nowpage);

        sendpages("http://www.bmbhome.org/api/article/num");
        sendOkhttp("http://www.bmbhome.org/api/article/page/" + nowp);
        allpagetext.setText(nowp + " / " + pages);
        readAdapter = new ReadAdapter(ReadActivity.this, R.layout.readlist, readListList);
        listView = (ListView) findViewById(R.id.test);
        listView.setAdapter(readAdapter);
        shangbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nowpage == 1) {
                    shangbut.setEnabled(false);
                } else {
                    clear();
                    shangbut.setEnabled(true);
                    nowpage -= 1;
                    String pa = Integer.toString(nowpage);
                    sendOkhttp("http://www.bmbhome.org/api/article/page/" + pa);
                    xiabut.setEnabled(true);
                }
                allpagetext.setText(nowpage + " / " + pages);
            }

        });

        xiabut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nowpage == pages) {
                    xiabut.setEnabled(false);
                } else {
                    clear();
                    xiabut.setEnabled(true);
                    nowpage += 1;
                    String pa = Integer.toString(nowpage);
                    sendOkhttp("http://www.bmbhome.org/api/article/page/" + pa);
                    shangbut.setEnabled(true);
                }
                allpagetext.setText(nowpage + " / " + pages);
            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReadList readList = readListList.get(position);
                int readid = readList.getId();
                String imgurl = readList.getImgUrl();
                String openid = "" + readid;
                String title = readList.getTitle();
                Intent intent = new Intent(ReadActivity.this, OpenReadActivity.class);
                intent.putExtra("imgurl", imgurl);
                intent.putExtra("openid", openid);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });

        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(ReadActivity.this);
        localSystemBarTintManager.setStatusBarTintResource(R.color.web_blue);
        localSystemBarTintManager.setStatusBarTintEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("Nowpage", nowpage);
    }

    private void clear() {
        readListList.clear();
        hander.sendEmptyMessage(0);
        if (bm != null && !bm.isRecycled())
            bm.recycle();
        System.gc();
    }

    private void sendpages(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    String body = response.body().string();
                    pages = Integer.parseInt(body);
                    hander.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void sendOkhttp(final String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response = client.newCall(request).execute();
                    String body = response.body().string();
                    parseJSON(body);
                } catch (Exception e) {
                    e.printStackTrace();
                    //progressBar.setVisibility(View.GONE);
                }

            }
        }).start();

    }

    private void parseJSON(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String imgUrl = jsonObject.getString("imgUrl");
                if (imgUrl.equals("/upload/1469499981628.jpg"))
                    continue;
                String title = jsonObject.getString("title");
                String readCount = jsonObject.getString("readCount");
                ReadList readList = new ReadList(title, readCount, getInternetPicture("http://www.bmbhome.org" + imgUrl), id, imgUrl);
                readListList.add(readList);
                hander.sendEmptyMessage(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    public Bitmap getInternetPicture(String UrlPath) {


        // 1、确定网址
        String urlpath = UrlPath;
        // 2、获取Uri
        try {
            URL uri = new URL(urlpath);
            // 3、获取连接对象、此时还没有建立连接
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            // 4、初始化连接对象
            // 设置请求的方法，注意大写
            connection.setRequestMethod("GET");
            // 读取超时
            connection.setReadTimeout(5000);
            // 设置连接超时
            connection.setConnectTimeout(5000);
            // 5、建立连接
            connection.connect();

            // 6、获取成功判断,获取响应码
            if (connection.getResponseCode() == 200) {
                // 7、拿到服务器返回的流，客户端请求的数据，就保存在流当中
                InputStream is = connection.getInputStream();
                // 8、从流中读取数据，构造一个图片对象GoogleAPI
                //BitmapFactory.Options options = new BitmapFactory.Options();//压缩
                //options.inSampleSize = 2;
                // BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                //bitmapOptions.inSampleSize = 4;
                Bitmap bit;
                bit = BitmapFactory.decodeStream(is);
                Matrix matrix = new Matrix();
                matrix.setScale(0.5f, 0.5f);
                bm = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),
                        bit.getHeight(), matrix, true);
                //bm = BitmapFactory.decodeStream(is);
                // 9、把图片设置到UI主线程
                // ImageView中,获取网络资源是耗时操作需放在子线程中进行,通过创建消息发送消息给主线程刷新控件；

                Log.i("", "网络请求成功");

            } else {
                Log.v("tag", "网络请求失败");
                bm = null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;

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
   /* private void showprogress() {
        if(progressBar.getVisibility()==View.GONE){
            progressBar.setVisibility(View.VISIBLE);
        }
    }
    private void noProgress(){
        if(progressBar.getVisibility()==View.VISIBLE){
            progressBar.setVisibility(View.GONE);
        }
    }*/


   /* private void createProgressBar() {
        mContext = this;
        //整个Activity布局的最终父布局,参见参考资料
        FrameLayout rootFrameLayout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        progressBar = new ProgressBar(mContext);
        progressBar.setLayoutParams(layoutParams);
        progressBar.setVisibility(View.VISIBLE);
        rootFrameLayout.addView(progressBar);
    }*/

}
