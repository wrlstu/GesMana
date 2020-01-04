package com.sixosix.gesmana.floatBall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.sixosix.gesmana.utils.FinalName.ALPHA;
import static com.sixosix.gesmana.utils.FinalName.BLUR;
import static com.sixosix.gesmana.utils.FinalName.IMAGE_ID;
import static com.sixosix.gesmana.utils.FinalName.MOVE;
import static com.sixosix.gesmana.utils.FinalName.SIZE;
import static com.sixosix.gesmana.utils.FinalName.STICK;

/**
 * Created by oops on 17-12-26.
 */

public class BallWindowManager {
    private static BallView ballView;
    private static WindowManager.LayoutParams params;
    private static WindowManager manager;
    private static Bundle bundle;
    private static float size_percent = 1;
    private static int alpha;
    private static int imageId;
    private static boolean canMove = false;
    private static boolean canStick = false;
    private static boolean canBlur = false;

    public static void setBundle(Bundle bundle) {
        BallWindowManager.bundle = bundle;
        initProperties();
    }

    /**
     * 初始化悬浮球属性
     */
    private static void initProperties() {
        if (bundle != null) {
            size_percent = (float) (bundle.getInt(SIZE) / 100.0);
            alpha = bundle.getInt(ALPHA);
            imageId = bundle.getInt(IMAGE_ID);
            canMove = bundle.getBoolean(MOVE);
            canStick = bundle.getBoolean(STICK);
            canBlur = bundle.getBoolean(BLUR);
        }
    }

    /**
     * 创建新的悬浮球
     *
     * @param context
     */


    @SuppressLint("RtlHardcoded")
    public static void createBallWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (ballView == null) {
            ballView = new BallView(context, size_percent);
            setBallProperty();
            setImageID(imageId);
            if (params == null) {
                params = new WindowManager.LayoutParams();
                // If no permission, request it
                if (!highVersionPermissionCheck(context)) {
                    highVersionJump2PermissionActivity(context);
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    params.type = WindowManager.LayoutParams.TYPE_PHONE;
                }
                params.format = PixelFormat.RGBA_8888;
                params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                params.gravity = Gravity.LEFT | Gravity.TOP;
                params.width = BallView.ballWidth;
                params.height = BallView.ballHeight;
                params.x = 0;
                params.y = screenHeight / 3;
            }
            ballView.setBallParams(params);
            windowManager.addView(ballView, params);
            setAlphaPercent(alpha);
        }
    }

    private static boolean highVersionPermissionCheck(Context context) {
        try {
            Class clazz = Settings.class;
            Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
            return (Boolean) canDrawOverlays.invoke(null, context);
        } catch (Exception e) {
        }
        return false;
    }

    private static void highVersionJump2PermissionActivity(Context context) {
        try {
            Class clazz = Settings.class;
            Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
            Intent intent = new Intent(field.get(null).toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    /**
     * 移除悬浮球
     *
     * @param context
     */
    public static void removeBallWindow(Context context) {
        if (ballView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(ballView);
            ballView = null;
        }
    }

    /**
     * 设置悬浮球属性
     */
    private static void setBallProperty() {
        setBallProperty(canMove, canStick, canBlur);
    }

    private static void setBallProperty(boolean canMove, boolean canStick, boolean canBlur) {
        if (ballView != null) {
            ballView.setCanMove(canMove);
            ballView.setCanStick(canStick);
            ballView.setCanBlur(canBlur);
        }
    }

    public static void setImageID(int imageID) {
        ballView.setImageID(imageID);
    }

    public static void setSizePercent(int size) {
        size_percent = (float) (size / 100.0);
        ballView.setBallScale(size_percent);
        Log.i("Size", "" + size);
    }

    public static void setAlphaPercent(int alpha) {
        ballView.setBallAlpha(alpha);
    }

    public static void setCanMove(boolean move) {
        ballView.setCanMove(move);
    }

    public static void setCanStick(boolean stick) {
        ballView.setCanStick(stick);
    }

    public static void setCanBlur(boolean blur) {
        ballView.setCanBlur(blur);
    }


    /**
     * 判断悬浮球是否存在
     *
     * @return
     */
    public static boolean isBallShowing() {
        return ballView != null;
    }

    /**
     * 获取窗口管理器实例
     *
     * @param context
     * @return
     */
    private static WindowManager getWindowManager(Context context) {
        if (manager == null) {
            manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return manager;
    }


}
