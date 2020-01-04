package com.sixosix.gesmana.otherSetting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sixosix.gesmana.MainActivity;
import com.sixosix.gesmana.R;
import com.sixosix.gesmana.floatBall.BallWindowManager;
import com.sixosix.gesmana.utils.FinalName;

import java.util.ArrayList;
import java.util.HashMap;

public class IconActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor = MainActivity.editor;
    private int[] images;
    private GridView gridView;
    private ArrayList<HashMap<String, Integer>> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon);
        initView();
    }

    /**
     * 初始化控件及相应状态
     */
    private void initView() {
        gridView = findViewById(R.id.gd_icons);
        getData();
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.activity_icon_item,
                new String[]{"iconItem"}, new int[]{R.id.icon_img});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getApplicationContext(), "选择了第【" + (i + 1) + "】个图标", Toast.LENGTH_SHORT).show();
                if (BallWindowManager.isBallShowing()) {
                    BallWindowManager.setImageID(images[i]);
                    editor.putInt(FinalName.IMAGE_ID, images[i]);
                    editor.commit();
                }
            }
        });
    }

    /**
     * 初始化图标
     */
    private void getData() {
        images = new int[]{
                R.drawable.ball, R.drawable.icon1, R.drawable.icon2, R.drawable.icon3,
                R.drawable.icon4, R.drawable.icon5, R.drawable.icon6, R.drawable.icon7,
                R.drawable.icon8, R.drawable.icon9,
                R.drawable.teaser1, R.drawable.teaser4, R.drawable.teaser7, R.drawable.teaser8,
                R.drawable.teaser10, R.drawable.teaser11, R.drawable.teaser17, R.drawable.teaser18,
                R.drawable.teaser19, R.drawable.teaser20, R.drawable.teaser21, R.drawable.teaser29,
                R.drawable.teaser31
        };

        items = new ArrayList<>();
        for (int image : images) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("iconItem", image);
            items.add(map);
        }
    }
}
