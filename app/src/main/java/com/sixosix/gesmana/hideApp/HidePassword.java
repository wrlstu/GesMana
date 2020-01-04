package com.sixosix.gesmana.hideApp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;

import com.sixosix.gesmana.FloatingItem;
import com.sixosix.gesmana.MainActivity;
import com.sixosix.gesmana.R;
import com.sixosix.gesmana.utils.BaseActivity;

public class HidePassword extends BaseActivity {
    public static final int REQUEST_CODE_CONFIRM_PATTERN = 1024;
    private static final String SWITCH_NAME = "pattern_visible";
    private SharedPreferences preferences = MainActivity.preferences;
    private SharedPreferences.Editor editor = MainActivity.editor;
    private FloatingItem itemDefine;
    private FloatingItem itemVisible;
    private FloatingItem itemClear;
    private SwitchCompat switchCompat;
    private TextView tv_define;
    private boolean isChecked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_pattern);
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
        itemDefine.setOnSettingItemClick(new FloatingItem.OnSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                startActivityForResult(new Intent(HidePassword.this,
                        MySetPatternActivity.class), REQUEST_CODE_CONFIRM_PATTERN);
            }
        });
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor.putBoolean(SWITCH_NAME, b);
                editor.commit();
            }
        });
        itemClear.setOnSettingItemClick(new FloatingItem.OnSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                new AlertDialog.Builder(HidePassword.this)
                        .setTitle("清除图案")
                        .setMessage("确认清除已保存图案？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editor.putBoolean("lock", false);
                                editor.putString("pattern", null);
                                editor.putString("define_hint", "【未设置】");
                                editor.commit();
                                recreate();
                            }
                        }).setNegativeButton("取消", null).create().show();
            }
        });
    }
    /**
     * 初始化控件及相应状态
     */
    private void initView() {
        itemDefine = findViewById(R.id.pattern_me);
        itemVisible = findViewById(R.id.pattern_visible);
        itemClear = findViewById(R.id.pattern_clear);
        switchCompat = itemVisible.findViewById(R.id.rightSwitch);
        tv_define = itemDefine.findViewById(R.id.tv_rightText);
        tv_define.setText(preferences.getString("define_hint", "【未设置】"));
        isChecked = preferences.getBoolean("pattern_visible", true);
        switchCompat.setChecked(isChecked);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String string = data.getExtras().getString("result");
        tv_define.setText(string);
        editor.putBoolean("lock", true);
        editor.commit();
        recreate();
    }
}
