package com.sixosix.gesmana.hideApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.sixosix.gesmana.FloatingItem;
import com.sixosix.gesmana.MainActivity;
import com.sixosix.gesmana.R;
import com.sixosix.gesmana.gestureApp.AppGesture;
import com.sixosix.gesmana.utils.BaseActivity;

import static com.sixosix.gesmana.utils.FinalName.HIDE_APP_PACKAGES;

public class HideActivity extends BaseActivity {
    private static final String SWITCH_NAME = "hideSwitch";
    private FloatingItem rlSwitch;
    private FloatingItem itemAdd;
    private FloatingItem itemAddGesture;
    private FloatingItem itemEnter;
    private SwitchCompat switchCompat;
    private LinearLayout llSetting;
    private boolean isChecked;
    private SharedPreferences preferences = MainActivity.preferences;
    private SharedPreferences.Editor editor = MainActivity.editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_app);
        initView();
        bindEvent();
    }

    @Override
    public void back(View view) {
        finish();
    }

    /**
     * 给控件绑定事件
     */
    private void bindEvent() {
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean(SWITCH_NAME, b);
                editor.commit();
                if (compoundButton.isChecked()) {
                    llSetting.setVisibility(View.VISIBLE);
                } else llSetting.setVisibility(View.GONE);
            }
        });
        itemAdd.setOnSettingItemClick(new FloatingItem.OnSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(HideActivity.this, HideAppManager.class);
                startActivity(intent);
            }
        });
        itemAddGesture.setOnSettingItemClick(new FloatingItem.OnSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(HideActivity.this, AppGesture.class);
                Bundle bundle = new Bundle();
                bundle.putString("packagesName", HIDE_APP_PACKAGES);
                intent.putExtra("package", bundle);
                startActivity(intent);
            }
        });
        itemEnter.setOnSettingItemClick(new FloatingItem.OnSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(HideActivity.this, HidePassword.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化控件及相应状态
     */
    private void initView() {
        rlSwitch = findViewById(R.id.hide_switch);
        switchCompat = rlSwitch.findViewById(R.id.rightSwitch);
        llSetting = findViewById(R.id.hide_settings);
        itemAdd = llSetting.findViewById(R.id.hide_add);
        itemAddGesture = llSetting.findViewById(R.id.hide_add_gesture);
        itemEnter = llSetting.findViewById(R.id.hide_enter);
        isChecked = preferences.getBoolean(SWITCH_NAME, true);
        switchCompat.setChecked(isChecked);
        if (isChecked)
            llSetting.setVisibility(View.VISIBLE);
        else llSetting.setVisibility(View.GONE);
    }

}