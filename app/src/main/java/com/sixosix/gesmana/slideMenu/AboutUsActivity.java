package com.sixosix.gesmana.slideMenu;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sixosix.gesmana.R;

public class AboutUsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
        findViewById(R.id.pj_check_update_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(AboutUsActivity.this, CheckUpdateActivity.class);
                startActivity(intent);
            }
        });
    }
}
