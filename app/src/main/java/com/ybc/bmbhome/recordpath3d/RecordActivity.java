package com.ybc.bmbhome.recordpath3d;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.amap.api.maps.model.LatLng;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ybc.bmbhome.R;
import com.ybc.bmbhome.database.DbAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    //记录界面

    private RecordAdapter mAdapter;
    private ListView recordlist;
    private DbAdapter DbHepler;
    private Cursor mCursor;
    private List<PathRecord> listdata = new ArrayList<PathRecord>();
    private Toolbar toolbar;

    private static ArrayList<LatLng> parseLocations(String latLonStr) {
        ArrayList<LatLng> latLonPoints = new ArrayList<LatLng>();
        String[] latLonStrs = latLonStr.split(";");
        for (int i = 0; i < latLonStrs.length; i++) {
            latLonPoints.add(parseLocation(latLonStrs[i]));
        }
        return latLonPoints;
    }

    private static LatLng parseLocation(String latLonStr) {
        if (latLonStr == null || latLonStr.equals("") || latLonStr.equals("[]")) {
            return null;
        }
        double lat = 0.0;
        double lon = 0.0;
        String[] loc = latLonStr.split(",");
        if (loc.length != 2) {
            return null;
        }
        lat = Double.parseDouble(loc[0]);
        lon = Double.parseDouble(loc[1]);
        return new LatLng(lat, lon);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordlist);

        toolbar = (Toolbar) findViewById(R.id.toolbar_Record);
        toolbar.setTitle("轨迹记录");
        setSupportActionBar(toolbar);

        recordlist = (ListView) findViewById(R.id.recordlist);
        DbHepler = new DbAdapter(this);
        DbHepler.open();
        addRecorddata();
        mAdapter = new RecordAdapter(this, listdata);//记录界面的列表
        recordlist.setAdapter(mAdapter);
        //列表选项
        recordlist.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                PathRecord recorditem = (PathRecord) parent.getAdapter().getItem(position);
                Intent intent = new Intent(RecordActivity.this, RecordShowActivity.class);
                intent.putExtra("recorditem", recorditem);
                startActivity(intent);
            }
        });
//		recordlist.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
//
//				Log.i("MY", position+"");
//				boolean b = DbHepler.delete(listdata.get(position).getId());
//				if (b) {
//					listdata.remove(position);
//					mAdapter.notifyDataSetChanged();
//					recordlist.invalidate();
//				}
//				return false;
//			}
//		});
        SystemBarTintManager localSystemBarTintManager = new SystemBarTintManager(this);
        localSystemBarTintManager.setStatusBarTintResource(R.color.blue);
        localSystemBarTintManager.setStatusBarTintEnabled(true);
    }

    private void addRecorddata() {

        mCursor = DbHepler.getallrecord();
        while (mCursor.moveToNext()) {
            PathRecord record = new PathRecord();
//			record.setId(mCursor.getInt(mCursor.getColumnIndex(DbAdapter.KEY_ROWID)));
            record.setDistance(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DISTANCE)));
            record.setDuration(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DURATION)));
            record.setDate(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DATE)));
            String lines = mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_LINE));
            record.setPathline(parseLocations(lines));
            record.setStartpoint(parseLocation(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_STRAT))));
            record.setEndpoint(parseLocation(mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_END))));
            listdata.add(record);
        }
        Collections.reverse(listdata);
    }

    public void onBackClick(View view) {
        this.finish();
    }

}
