package com.sixosix.gesmana.slideMenu;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sixosix.gesmana.R;
import com.suke.widget.SwitchButton;

public class SetActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_menu);
        SwitchButton switchButton = findViewById(R.id.switch_button);

        switchButton.setChecked(true);
        switchButton.isChecked();
        switchButton.toggle();     //switch state
        switchButton.toggle(true);//switch without animation
        switchButton.setShadowEffect(true);//disable shadow effect
        switchButton.setEnabled(true);//disable button
        switchButton.setEnableEffect(true);//disable the switch animation
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                // TODO: do your job
                // TODO: check, do what job??
            }
        });
        //手势使用记录界面
        findViewById(R.id.pj_gesture_history_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SetActivity.this, GestureHistoryActivity.class);
                startActivity(intent);
            }
        });
        //关于界面
        findViewById(R.id.pj_about_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SetActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });
        //备份界面
        findViewById(R.id.pj_backups_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SetActivity.this, BackupsActivity.class);
                startActivity(intent);
            }
        });
    }
}
