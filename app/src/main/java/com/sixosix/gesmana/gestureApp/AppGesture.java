package com.sixosix.gesmana.gestureApp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sixosix.gesmana.MainActivity;
import com.sixosix.gesmana.R;
import com.sixosix.gesmana.utils.ApkTool;
import com.sixosix.gesmana.utils.ApplicationUtil;
import com.sixosix.gesmana.utils.BaseActivity;
import com.sixosix.gesmana.utils.FinalName;
import com.sixosix.gesmana.utils.MyAppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppGesture extends BaseActivity {
    private SharedPreferences preferences = MainActivity.preferences;
    private SharedPreferences.Editor editor = MainActivity.editor;
    private FloatingActionButton fab;
    private GridView lv_app_list;
    private ArrayList<HashMap<String, String>> drawables;
    private ArrayList<String> packageNames;
    private Set<String> packages;
    private Bundle bundle;
    private String packagesName;
    private AppAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_gesture_manager);
        initView();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppGesture.this, AddApp.class);
                intent.putExtra("package", bundle);
                startActivity(intent);
            }
        });
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
        fab = findViewById(R.id.floatingActionButton);
        packages = preferences.getStringSet(packagesName, new HashSet<String>());
        if (packages.size() <= 0)
            return;
        ArrayList<MyAppInfo> list = (ArrayList<MyAppInfo>) ApkTool.scanLocalInstallAppList(getPackageManager());
        ArrayList<MyAppInfo> addedList = new ArrayList<>();
        drawables = new ArrayList<>();
        packageNames = new ArrayList<>();
        for (String str : packages) {
            for (MyAppInfo info : list) {
                if (str.equals(info.getAppName())) {
                    addedList.add(info);
                    break;
                }
            }
        }
        adapter = new AppAdapter();
        adapter.setData(addedList);

        lv_app_list.setAdapter(adapter);
        lv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, int i, long l) {
                final String packageName = adapter.getData().get(i).getAppName();
                new AlertDialog.Builder(AppGesture.this)
                        .setTitle("确认清除【" + ApplicationUtil.getProgramNameByPackageName(
                                AppGesture.this, packageName) + "】手势")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String gesturePath = getFilesDir().getPath() + File.separator + FinalName.GESTURE_PATH;
                                Log.d("GESTURE", "PATH " + gesturePath);
                                GestureLibrary library = GestureLibraries.fromFile(gesturePath);
                                library.load();
                                packages = preferences.getStringSet(packagesName, new HashSet<String>());
                                packages.remove(packageName);
                                editor.putStringSet(packagesName, packages);
                                editor.commit();
                                library.removeEntry(packageName);
                                library.save();
                                recreate();
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    class AppAdapter extends BaseAdapter {
        List<MyAppInfo> myAppInfos = new ArrayList<>();

        public List<MyAppInfo> getData() {
            return this.myAppInfos;
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
        public Object getItem(int i) {
            if (myAppInfos != null && myAppInfos.size() > 0) {
                return myAppInfos.get(i);
            }
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            MyAppInfo myAppInfo = myAppInfos.get(i);
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_app_item, null);
                holder.iv_app = view.findViewById(R.id.iv_app);
                holder.tv_app_name = view.findViewById(R.id.tv_app_name);
                holder.tv_app_package = view.findViewById(R.id.tv_app_package);
                view.setTag(holder);
            } else
                holder = (ViewHolder) view.getTag();
            holder.iv_app.setImageDrawable(myAppInfo.getImage());
            holder.tv_app_name.setText(ApplicationUtil.getProgramNameByPackageName(
                    AppGesture.this, myAppInfo.getAppName()));
            holder.tv_app_package.setText(myAppInfo.getAppName());
            return view;
        }

        class ViewHolder {
            ImageView iv_app;
            TextView tv_app_name;
            TextView tv_app_package;
        }
    }
}



