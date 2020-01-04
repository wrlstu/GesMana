package com.sixosix.gesmana;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.sixosix.gesmana.floatBall.BallService;
import com.sixosix.gesmana.floatBall.BallWindowManager;
import com.sixosix.gesmana.gestureApp.GestureAppActivity;
import com.sixosix.gesmana.hideApp.HideActivity;
import com.sixosix.gesmana.hideApp.MyConfirmPatternActivity;
import com.sixosix.gesmana.otherSetting.FloatingSettingActivity;
import com.sixosix.gesmana.slideMenu.SetActivity;
import com.sixosix.gesmana.slideMenu.ShareActivity;
import com.sixosix.gesmana.slideMenu.UserCenterActivity;

import static com.sixosix.gesmana.utils.FinalName.ALPHA;
import static com.sixosix.gesmana.utils.FinalName.BALL_PATH;
import static com.sixosix.gesmana.utils.FinalName.BLUR;
import static com.sixosix.gesmana.utils.FinalName.IMAGE_ID;
import static com.sixosix.gesmana.utils.FinalName.MOVE;
import static com.sixosix.gesmana.utils.FinalName.SIZE;
import static com.sixosix.gesmana.utils.FinalName.STICK;

/**
 * Created by oops on 17-12-21.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String SWITCH_NAME = "masterSwitch";
    private static final String LOCK_NAME = "lock";
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    private FloatingItem rlSwitch;
    private FloatingItem itemHide;
    private FloatingItem itemApp;
    private FloatingItem itemOther;
    private SwitchCompat switchCompat;
    private LinearLayout llSetting;
    private boolean isChecked;
    private boolean isLocked;
    private long firstClick;//连续两次按返回键退出程序
    private int imageId;


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("touovr", MODE_PRIVATE);
        editor = preferences.edit();
        initView();
        bindEvent();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                    createFloatBall();
                } else {
                    llSetting.setVisibility(View.GONE);
                    removeFloatBall();
                }
            }
        });
        itemHide.setOnSettingItemClick(new FloatingItem.OnSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent;
                boolean isOpen = preferences.getBoolean("hideSwitch", false);
                isLocked = preferences.getBoolean(LOCK_NAME, false);

                if (isOpen && isLocked) {
                    intent = new Intent(MainActivity.this, MyConfirmPatternActivity.class);
                } else
                    intent = new Intent(MainActivity.this, HideActivity.class);
                startActivity(intent);
            }
        });
        itemApp.setOnSettingItemClick(new FloatingItem.OnSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(MainActivity.this, GestureAppActivity.class);
                startActivity(intent);
            }
        });
        itemOther.setOnSettingItemClick(new FloatingItem.OnSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(MainActivity.this, FloatingSettingActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 初始化控件及相应状态
     */
    private void initView() {
        rlSwitch = findViewById(R.id.floating_switch);
        switchCompat = rlSwitch.findViewById(R.id.rightSwitch);
        llSetting = findViewById(R.id.floating_setting);
        itemHide = llSetting.findViewById(R.id.item_hide_settings);
        itemApp = llSetting.findViewById(R.id.item_app_settings);
        itemOther = llSetting.findViewById(R.id.item_other_settings);
        isChecked = preferences.getBoolean(SWITCH_NAME, true);
        imageId = preferences.getInt(IMAGE_ID, R.drawable.teaser10);
        switchCompat.setChecked(isChecked);
        if (isChecked) {
            llSetting.setVisibility(View.VISIBLE);
            createFloatBall();
        } else {
            llSetting.setVisibility(View.GONE);
            removeFloatBall();
        }

    }

    /**
     * 设置悬浮球属性
     *
     * @return
     */
    private Bundle getBallProperties() {
        Bundle bundle = new Bundle();
        String ballIconPath = preferences.getString(BALL_PATH, null);
        int size = preferences.getInt(SIZE, 100);
        int alpha = preferences.getInt(ALPHA, 150);
        boolean canMove = preferences.getBoolean(MOVE, false);
        boolean canStick = preferences.getBoolean(STICK, false);
        boolean canBlur = preferences.getBoolean(BLUR, true);
        bundle.putString(BALL_PATH, ballIconPath);
        bundle.putInt(SIZE, size);
        bundle.putInt(ALPHA, alpha);
        bundle.putInt(IMAGE_ID, imageId);
        bundle.putBoolean(MOVE, canMove);
        bundle.putBoolean(STICK, canStick);
        bundle.putBoolean(BLUR, canBlur);
        return bundle;
    }

    /**
     * 创建悬浮球
     */
    private void createFloatBall() {
        if (!BallWindowManager.isBallShowing()) {
            Intent intent = new Intent(MainActivity.this, BallService.class);
            Bundle bundle = getBallProperties();
            intent.putExtras(bundle);
            startService(intent);
        }
    }

    /**
     * 清除悬浮球
     */
    private void removeFloatBall() {
        if (BallWindowManager.isBallShowing())
            BallWindowManager.removeBallWindow(getApplicationContext());
    }

    /**
     * 连按两次返回键退出程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - firstClick) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstClick = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * Slide Menu
     */


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pj_user_center:
                Intent intent1 = new Intent();
                intent1.setClass(MainActivity.this, UserCenterActivity.class);
                startActivity(intent1);
                break;
            case R.id.pj_share:
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this, ShareActivity.class);
                startActivity(intent2);
                break;
            case R.id.pj_setting:
                Intent intent3 = new Intent();
                intent3.setClass(MainActivity.this, SetActivity.class);
                startActivity(intent3);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
