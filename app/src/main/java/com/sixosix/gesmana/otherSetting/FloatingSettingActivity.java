package com.sixosix.gesmana.otherSetting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.sixosix.gesmana.FloatingItem;
import com.sixosix.gesmana.MainActivity;
import com.sixosix.gesmana.R;
import com.sixosix.gesmana.floatBall.BallWindowManager;
import com.sixosix.gesmana.utils.BaseActivity;

import static com.sixosix.gesmana.utils.FinalName.ALPHA;
import static com.sixosix.gesmana.utils.FinalName.BLUR;
import static com.sixosix.gesmana.utils.FinalName.MOVE;
import static com.sixosix.gesmana.utils.FinalName.SIZE;
import static com.sixosix.gesmana.utils.FinalName.STICK;

public class FloatingSettingActivity extends BaseActivity {
    private FloatingItem icon_set;
    private FloatingItem move_layout;
    private FloatingItem stick_layout;
    private FloatingItem blur_layout;
    private AppCompatSeekBar size_seekBar;
    private AppCompatSeekBar alpha_seekBar;
    private AppCompatCheckBox move_checkBox;
    private AppCompatCheckBox stick_checkBox;
    private AppCompatCheckBox blur_checkBox;
    private SharedPreferences preferences = MainActivity.preferences;
    private SharedPreferences.Editor editor = MainActivity.editor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
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
        icon_set.setOnSettingItemClick(new FloatingItem.OnSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(FloatingSettingActivity.this, IconActivity.class);
                startActivity(intent);
            }
        });
        move_layout.setOnSettingItemLongClick(new FloatingItem.OnSettingItemLongClick() {
            @Override
            public void longClick() {
                Toast.makeText(FloatingSettingActivity.this,
                        "开启后，可以 【长按】 悬浮球来移动位置。否则固定悬浮球位置", Toast.LENGTH_SHORT).show();
            }
        });
        stick_layout.setOnSettingItemLongClick(new FloatingItem.OnSettingItemLongClick() {
            @Override
            public void longClick() {
                Toast.makeText(FloatingSettingActivity.this,
                        "开启后，当启用【长按移动】时，移动悬浮球到屏幕某一位置并释放，悬浮球将自动吸附在较近的屏幕边缘", Toast.LENGTH_SHORT).show();
            }
        });
        blur_layout.setOnSettingItemLongClick(new FloatingItem.OnSettingItemLongClick() {
            @Override
            public void longClick() {
                Toast.makeText(FloatingSettingActivity.this,
                        "开启后，在使用悬浮球后，一段时间后悬浮球将变为虚化状态", Toast.LENGTH_SHORT).show();
            }
        });
        size_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progress = seekBar.getProgress();
                progress = progress < 50 ? 50 : (progress > 150) ? 150 : progress;
                BallWindowManager.setSizePercent(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                progress = progress < 50 ? 50 : (progress > 150) ? 150 : progress;
//                BallWindowManager.setSizePercent(progress);
                editor.putInt(SIZE, progress);
                editor.commit();
            }
        });
        alpha_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progress = seekBar.getProgress();
                BallWindowManager.setAlphaPercent(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editor.putInt(ALPHA, seekBar.getProgress());
                editor.commit();
            }
        });
        move_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                BallWindowManager.setCanMove(compoundButton.isChecked());
                editor.putBoolean(MOVE, compoundButton.isChecked());
                editor.commit();
            }
        });
        stick_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                BallWindowManager.setCanStick(compoundButton.isChecked());
                editor.putBoolean(STICK, compoundButton.isChecked());
                editor.commit();
            }
        });
        blur_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                BallWindowManager.setCanBlur(compoundButton.isChecked());
                editor.putBoolean(BLUR, compoundButton.isChecked());
                editor.commit();
            }
        });
    }

    /**
     * 初始化控件及相应状态
     */
    private void initView() {
        icon_set = findViewById(R.id.icon_set);
        size_seekBar = findViewById(R.id.sb_size);
        alpha_seekBar = findViewById(R.id.sb_alpha);
        move_layout = findViewById(R.id.move);
        stick_layout = findViewById(R.id.stick);
        blur_layout = findViewById(R.id.blur);
        move_checkBox = move_layout.findViewById(R.id.rightCheck);
        stick_checkBox = stick_layout.findViewById(R.id.rightCheck);
        blur_checkBox = blur_layout.findViewById(R.id.rightCheck);
        int sizeProgress = preferences.getInt(SIZE, 100);
        int alphaProgress = preferences.getInt(ALPHA, 100);
        boolean moveChecked = preferences.getBoolean(MOVE, true);
        boolean stickChecked = preferences.getBoolean(STICK, true);
        boolean blurChecked = preferences.getBoolean(BLUR, true);
        size_seekBar.setProgress(sizeProgress, true);
        alpha_seekBar.setProgress(alphaProgress, true);
        move_checkBox.setChecked(moveChecked);
        stick_checkBox.setChecked(stickChecked);
        blur_checkBox.setChecked(blurChecked);
    }
}
