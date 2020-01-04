package com.sixosix.gesmana.hideApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.sixosix.gesmana.MainActivity;

import java.util.List;

import me.zhanghai.android.patternlock.ConfirmPatternActivity;
import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;

public class MyConfirmPatternActivity extends ConfirmPatternActivity {
    private SharedPreferences preferences = MainActivity.preferences;

    @Override
    protected boolean isStealthModeEnabled() {
        return !preferences.getBoolean("pattern_visible", true);
    }

    @Override
    protected boolean isPatternCorrect(List<PatternView.Cell> pattern) {
        String patternSha1 = preferences.getString("pattern", null);
        boolean result = TextUtils.equals(PatternUtils.patternToSha1String(pattern), patternSha1);
        if (result) {
            startActivity(new Intent(MyConfirmPatternActivity.this, HideActivity.class));
        }
        return result;
    }

    @Override
    protected void onForgotPassword() {
        startActivity(new Intent(this, MyResetPatternActivity.class));
        super.onForgotPassword();
    }
}
