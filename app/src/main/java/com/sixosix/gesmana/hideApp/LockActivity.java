package com.sixosix.gesmana.hideApp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sixosix.gesmana.MainActivity;

public class LockActivity extends AppCompatActivity implements OnConfirmPatternResultListener {
    private static final String CONFIRM_PATTERN_STARTED = "confirm_pattern_started";
    private static final String SHOULD_START_ACTIVITY = "should_start_activity";
    public static final int REQUEST_CODE_CONFIRM_PATTERN = 1024;
    public static final String PATTERN = "pattern";
    private boolean mConfirmPatternStarted = false;
    private boolean mShouldStartActivity = false;
    private SharedPreferences preferences = MainActivity.preferences;
    private SharedPreferences.Editor editor = MainActivity.editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mConfirmPatternStarted = savedInstanceState.getBoolean(CONFIRM_PATTERN_STARTED);
            mShouldStartActivity = savedInstanceState.getBoolean(SHOULD_START_ACTIVITY);
        } else
            mShouldStartActivity = !hasPattern();
        if (!mConfirmPatternStarted) {
            confirmPattern();
            mConfirmPatternStarted = true;
        }
    }

    /**
     * 判断手势图案是否存在
     *
     * @return
     */
    private boolean hasPattern() {
        return !TextUtils.isEmpty(preferences.getString(PATTERN, null));
    }

    /**
     * 验证手势图案正确性
     */
    private void confirmPattern() {
        if (hasPattern()) {
            this.startActivityForResult(new Intent(LockActivity.this,
                    MyConfirmPatternActivity.class), REQUEST_CODE_CONFIRM_PATTERN);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mShouldStartActivity) {
            this.startActivityForResult(new Intent(LockActivity.this,
                    HideActivity.class), REQUEST_CODE_CONFIRM_PATTERN);
            mShouldStartActivity = false;
        }
    }

    /**
     * 保存状态
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(CONFIRM_PATTERN_STARTED, mConfirmPatternStarted);
        outState.putBoolean(SHOULD_START_ACTIVITY, mShouldStartActivity);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (checkConfirmPatternResult(this, requestCode, resultCode)) {
            Toast.makeText(LockActivity.this, "hhh", Toast.LENGTH_LONG).show();
//            startActivity(new Intent(LockActivity.this, HideActivity.class));
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 验证手势图案
     *
     * @param activity
     * @param requestCode
     * @param resultCode
     * @param <ActivityType>
     * @return
     */
    public static <ActivityType extends Activity & OnConfirmPatternResultListener> boolean
    checkConfirmPatternResult(ActivityType activity, int requestCode, int resultCode) {
        if (requestCode == REQUEST_CODE_CONFIRM_PATTERN) {
            activity.onConfirmPatternResult(resultCode == Activity.RESULT_OK);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onConfirmPatternResult(boolean successful) {
        if (successful) {
            mShouldStartActivity = true;
        } else finish();
    }
}

interface OnConfirmPatternResultListener {
    void onConfirmPatternResult(boolean successful);
}
