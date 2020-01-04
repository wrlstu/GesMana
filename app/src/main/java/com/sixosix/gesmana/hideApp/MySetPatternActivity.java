package com.sixosix.gesmana.hideApp;

import android.content.Intent;
import android.content.SharedPreferences;


import com.sixosix.gesmana.MainActivity;

import java.util.List;

import me.zhanghai.android.patternlock.PatternUtils;
import me.zhanghai.android.patternlock.PatternView;
import me.zhanghai.android.patternlock.SetPatternActivity;

/**
 * Created by oops on 17-12-24.
 */

public class MySetPatternActivity extends SetPatternActivity {
    private SharedPreferences.Editor editor = MainActivity.editor;

    @Override
    protected void onSetPattern(List<PatternView.Cell> pattern) {
        String patternSha1 = PatternUtils.patternToSha1String(pattern);
        editor.putString("pattern", patternSha1);
        editor.putBoolean("lock", true);
        editor.putString("define_hint", "【已设置】");
        editor.commit();
        Intent intent = new Intent();
        intent.putExtra("result", "【已设置】");
        MySetPatternActivity.this.setResult(RESULT_OK, intent);
        MySetPatternActivity.this.finish();
    }

}
