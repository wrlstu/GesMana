package com.sixosix.gesmana.gestureApp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sixosix.gesmana.R;
import com.sixosix.gesmana.utils.ApkTool;
import com.sixosix.gesmana.utils.ApplicationUtil;
import com.sixosix.gesmana.utils.Apps;
import com.sixosix.gesmana.utils.BaseActivity;
import com.sixosix.gesmana.utils.MyAppInfo;

import java.util.ArrayList;
import java.util.List;

public class AddApp extends BaseActivity {
    public Handler mHandler = new Handler();
    private GridView lv_app_list;
    private AppAdapter mAppAdapter;
    private Bundle bundle;
    private String packagesName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);

        initView();
        mAppAdapter = new AppAdapter();
        lv_app_list.setAdapter(mAppAdapter);
        lv_app_list.setOnItemClickListener(new ClickListener());
        initAppList();
    }

    @Override
    public void back(View view) {
        finish();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        bundle = getIntent().getBundleExtra("package");
        packagesName = bundle.getString("packagesName");
        lv_app_list = findViewById(R.id.lv_app_list);
    }


    private void initAppList() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //扫描得到APP列表
                final List<MyAppInfo> appInfos = ApkTool.scanLocalInstallAppList(AddApp.this.getPackageManager());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAppAdapter.setData(appInfos);
                    }
                });

            }
        }.start();
    }


    class AppAdapter extends BaseAdapter {

        List<MyAppInfo> myAppInfos = new ArrayList<MyAppInfo>();

        public List<MyAppInfo> getData() {
            return myAppInfos;
        }

        public void setData(List<MyAppInfo> myAppInfos) {
            this.myAppInfos = myAppInfos;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (myAppInfos != null && myAppInfos.size() > 0) {
                return myAppInfos.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (myAppInfos != null && myAppInfos.size() > 0) {
                return myAppInfos.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AppAdapter.ViewHolder mViewHolder;
            MyAppInfo myAppInfo = myAppInfos.get(position);
            if (convertView == null) {
                mViewHolder = new AppAdapter.ViewHolder();
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_app_info, null);
                mViewHolder.iv_app_icon = convertView.findViewById(R.id.iv_app_icon);
                mViewHolder.tx_app_name = convertView.findViewById(R.id.tv_app_name);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (AppAdapter.ViewHolder) convertView.getTag();
            }
            mViewHolder.iv_app_icon.setImageDrawable(myAppInfo.getImage());
            mViewHolder.tx_app_name.setText(ApplicationUtil.getProgramNameByPackageName(AddApp.this, myAppInfo.getAppName()));
            Apps apps = new Apps();
            apps.setAppName(ApplicationUtil.getProgramNameByPackageName(AddApp.this, myAppInfo.getAppName()));
            return convertView;
        }

        class ViewHolder {
            ImageView iv_app_icon;
            TextView tx_app_name;
        }
    }

    private class ClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                long arg3) {
            String packageName = mAppAdapter.getData().get(arg2).getAppName();
            Bundle bundle = new Bundle();
            bundle.putString("packageName", packageName);
            bundle.putString("packages", packagesName);
            Intent intent = new Intent(AddApp.this, AddGesture.class);
            intent.putExtra("package", bundle);
            startActivity(intent);
        }
    }
}
