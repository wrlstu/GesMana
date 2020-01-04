package com.sixosix.gesmana.hideApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sixosix.gesmana.R;
import com.sixosix.gesmana.gestureApp.AddApp;
import com.sixosix.gesmana.utils.BaseActivity;

import static com.sixosix.gesmana.utils.FinalName.HIDE_APP_PACKAGES;

public class HideAppGesture extends BaseActivity {
    private FloatingActionButton fab;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_gesture_manager);
        initView();
        // TODO:为隐藏的应用设置手势
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HideAppGesture.this, AddApp.class);
                Bundle bundle = new Bundle();
                bundle.putString("packagesName", HIDE_APP_PACKAGES);
                intent.putExtra("package", bundle);
                startActivity(intent);
            }
        });
    }
    @Override
    public void back(View view) {
        finish();
    }

    private void initView() {
        fab = findViewById(R.id.floatingActionButton);
    }
}
