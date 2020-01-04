package com.sixosix.gesmana.hideApp;

import android.content.SharedPreferences;

import com.sixosix.gesmana.MainActivity;

import java.util.List;

import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;
import me.zhanghai.android.patternlock.SetPatternActivity;

/**
 * Created by oops on 17-12-24.
 */

public class MyResetPatternActivity extends SetPatternActivity {
    private SharedPreferences.Editor editor = MainActivity.editor;
    @Override
    protected void onSetPattern(List<PatternView.Cell> pattern) {
        String patternSha1 = PatternUtils.patternToSha1String(pattern);
        editor.putString("pattern", patternSha1);
        editor.commit();
        finish();
    }
}
