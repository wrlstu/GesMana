package com.sixosix.gesmana.gestureApp;

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
import com.sixosix.gesmana.utils.BaseActivity;

import static com.sixosix.gesmana.utils.FinalName.APP_PACKAGES;

public class GestureAppActivity extends BaseActivity {
    private static final String SWITCH_NAME = "gestureSwitch";
    private FloatingItem rlSwitch;
    private FloatingItem itemAdd;
    private SwitchCompat switchCompat;
    private LinearLayout llSetting;
    private boolean isChecked;
    private SharedPreferences preferences = MainActivity.preferences;
    private SharedPreferences.Editor editor = MainActivity.editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_app);
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
                Intent intent = new Intent(GestureAppActivity.this, AppGesture.class);
                Bundle bundle = new Bundle();
                bundle.putString("packagesName", APP_PACKAGES);
                intent.putExtra("package", bundle);
                startActivity(intent);
            }
        });

    }

    /**
     * 初始化控件
     */
    private void initView() {
        rlSwitch = findViewById(R.id.fi_gesture_switch);
        switchCompat = rlSwitch.findViewById(R.id.rightSwitch);
        llSetting = findViewById(R.id.ll_gesture_settings);
        itemAdd = llSetting.findViewById(R.id.fi_add_app);
        isChecked = preferences.getBoolean(SWITCH_NAME, true);
        switchCompat.setChecked(isChecked);
        if (isChecked)
            llSetting.setVisibility(View.VISIBLE);
        else llSetting.setVisibility(View.GONE);

    }


}