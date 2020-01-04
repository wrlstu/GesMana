package com.sixosix.gesmana.floatBall;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sixosix.gesmana.R;
import com.sixosix.gesmana.utils.BaseActivity;
import com.sixosix.gesmana.utils.FinalName;

import java.io.File;
import java.util.ArrayList;

public class RecogniseGesture extends BaseActivity {
    public static String APP_PACKAGE_NAME = null;
    private GestureOverlayView gestureOverlayView;
    private GestureLibrary library;

    /**
     * 启动薄荷App
     *
     * @param context
     */
    private static void launchapp(Context context) {
        // 判断是否安装过App，否则去市场下载
        if (isAppInstalled(context, APP_PACKAGE_NAME)) {
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(APP_PACKAGE_NAME);
            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        } else {
            goToMarket(context, APP_PACKAGE_NAME);
        }
    }

    /**
     * 检测某个应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    private static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 去市场下载页面
     */
    private static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (Exception e) {
        }
    }

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognise);
        String gesturePath = getFilesDir().getPath() + File.separator + FinalName.GESTURE_PATH;
        library = GestureLibraries.fromFile(gesturePath);
        if (!library.load()) {
            Toast.makeText(RecogniseGesture.this, "手势文件装载失败！", Toast.LENGTH_SHORT).show();
        }

        gestureOverlayView = findViewById(R.id.gesture_recognise);
        gestureOverlayView.addOnGesturePerformedListener(
                new GestureOverlayView.OnGesturePerformedListener() {
                    @Override
                    public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
                        ArrayList<Prediction> predictions = library.recognize(gesture);
                        //只有相似度大于2.0的手势才会被输出
                        String appName = null;
                        double score = 2;
                        for (Prediction prediction : predictions) {
                            if (prediction.score > score) {
                                appName = prediction.name;
                                score = prediction.score;
                            }
                        }
                        if (appName != null && !"".equals(appName)) {
                            APP_PACKAGE_NAME = appName;
                            launchapp(RecogniseGesture.this);
                        } else {
                            Toast.makeText(RecogniseGesture.this, "无法找到匹配手势", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        gestureOverlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void back(View view) {
        finish();
    }
}
