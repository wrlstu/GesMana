package com.sixosix.gesmana.hideApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sixosix.gesmana.R;
import com.sixosix.gesmana.gestureApp.AddApp;
import com.sixosix.gesmana.utils.BaseActivity;

public class HideAppManager extends BaseActivity {
    FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_app_manager);
        initView();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HideAppManager.this, AddApp.class);

                startActivity(intent);
            }
        });
        // TODO:添加隐藏文件类型（例如：游戏、浏览器、视频等）并设置文件夹手势（作用如下一条）、添加需隐藏应用
        // TODO:设置不同手势在桌面显示/隐藏特定文件夹的应用
    }

    @Override
    public void back(View view) {
        finish();
    }

    private void initView() {
        fab = findViewById(R.id.floatingActionButton);
    }
}
