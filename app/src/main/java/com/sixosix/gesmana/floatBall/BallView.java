package com.sixosix.gesmana.floatBall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sixosix.gesmana.MainActivity;
import com.sixosix.gesmana.R;

import java.lang.reflect.Field;

public class BallView extends LinearLayout {
    private final static long LONG_PRESS_LIMIT = 600;
    private static final int WIDTH = 80; // TODO: change it
    private static final int HEIGHT = 80; // TODO: change it
    public static int ballWidth = WIDTH;
    public static int ballHeight = HEIGHT;
    private static int statusBarHeight;//状态栏高度
    private SharedPreferences preferences = MainActivity.preferences;
    private WindowManager manager;
    private WindowManager.LayoutParams ballParams;

    private float xInScreen;//手指在屏幕上位置的横坐标
    private float yInScreen;

    private float xInBall;//手指按下时在悬浮球上的位置的横坐标
    private float yInBall;

    private float scale;
    private int alpha;
    private int newAlpha;
    private boolean canStick;
    private boolean canMove;
    private boolean canBlur;
    private boolean isLongTouch;//判断是否长按
    private boolean isTouching;
    private long lastDownTime;
    private float touchSlop;
    private Vibrator vibrator;
    private LinearLayout layout;
    private ImageView imageView;
    private Animation animationAlpha;
    private Animation animationScale;
    private Context context;
    private boolean isOpen;

    public BallView(Context context) {
        this(context, 1);
    }

    public BallView(Context context, float scale) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.activity_ball, this);
        animationAlpha = AnimationUtils.loadAnimation(context, R.anim.blur);
        animationScale = AnimationUtils.loadAnimation(context, R.anim.scale);
        this.scale = scale;
        this.alpha = 200;
        initView(context);
        initTouch();
    }

    /**
     * 初始化控件
     *
     * @param context
     */

    private void initView(Context context) {
        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        layout = findViewById(R.id.ll_ball);
        imageView = findViewById(R.id.iv_ball);
        setBallScale(scale);
        setBallAlpha(alpha);
    }

    public void setImageID(int imageID) {
        imageView.setImageResource(imageID);
        setBallScale(scale);
    }

    /**
     * 设置悬浮球大小
     *
     * @param scale
     */
    public void setBallScale(float scale) {
        ballWidth = (int) (WIDTH * scale);
        ballHeight = (int) (HEIGHT * scale);
        imageView.setLayoutParams(new LayoutParams(ballWidth, ballHeight));
        layout.setLayoutParams(new LayoutParams(ballWidth, ballHeight));
    }

    /**
     * 设置悬浮球透明度
     *
     * @param alpha
     */
    public void setBallAlpha(int alpha) {
        newAlpha = alpha;
        imageView.setImageAlpha(alpha);
        if (!isTouching) {//自动虚化
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (canBlur)
                        if (newAlpha > 50) {
                            imageView.setImageAlpha(50);
//                            imageView.setAnimation(animationAlpha);
                        }
                }
            }, 3000);
        }
    }


    /**
     * 为悬浮球设置触摸事件
     */

    @SuppressLint("ClickableViewAccessibility")
    private void initTouch() {
        imageView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setBallAlpha(alpha);
                        isTouching = true;
                        imageView.setAnimation(animationScale);
                        lastDownTime = System.currentTimeMillis();
                        xInBall = motionEvent.getX();
                        yInBall = motionEvent.getY();
                        xInScreen = motionEvent.getRawX();
                        yInScreen = motionEvent.getRawY() - getStatusBarHeight();
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isLongTouch()) {
                                    isLongTouch = true;
                                    vibrator.vibrate(new long[]{0, 50}, -1);
                                }
                            }
                        }, LONG_PRESS_LIMIT);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!isLongTouch && isTouchSlop(motionEvent)) {
                            return true;
                        }
                        if (canMove && isLongTouch) {
                            xInScreen = motionEvent.getRawX();
                            yInScreen = motionEvent.getRawY() - getStatusBarHeight();
                            updateBallPosition();
                        } else {
                            // TODO:手势判断
                            // TODO: do we really need this?
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        isTouching = false;
                        if (isLongTouch) {
                            if (canStick) {
                                int width = manager.getDefaultDisplay().getWidth();
                                if (xInScreen > (width / 2))
                                    xInScreen = width;
                                else xInScreen = xInBall;
                                updateBallPosition();
                            }
                            isLongTouch = false;
                        } else {
                            isOpen = preferences.getBoolean("gestureSwitch", false);
                            // TODO: fix here, default value should be true
                            if (isOpen) {
                                Intent intent = new Intent(getContext(), RecogniseGesture.class);
                                intent.setAction(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getContext().startActivity(intent);
                            } else
                                Toast.makeText(getContext(), "请打开【手势应用】", Toast.LENGTH_SHORT).show();
                        }
                        setBallAlpha(newAlpha);

                    default:
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 判断是否属于长按行为
     *
     * @return
     */
    private boolean isLongTouch() {
        long time = System.currentTimeMillis();
        return isTouching && (time - lastDownTime) >= LONG_PRESS_LIMIT;
    }

    /**
     * 判断是否属于轻微滑动
     *
     * @param event
     * @return
     */
    private boolean isTouchSlop(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        return Math.abs(x - xInBall) < touchSlop && (Math.abs(y - yInBall) < touchSlop);
    }

    /**
     * 设置属性
     *
     * @param params
     */
    public void setBallParams(WindowManager.LayoutParams params) {
        this.ballParams = params;
    }

    /**
     * 更新悬浮球位置
     */
    private void updateBallPosition() {
        int x = (int) (xInScreen - xInBall);
        int y = (int) (yInScreen - yInBall);
        ballParams.x = x;
        ballParams.y = y;
        manager.updateViewLayout(this, ballParams);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                @SuppressLint("PrivateApi") Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (int) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    /**
     * 设置悬浮球可吸附
     *
     * @param canStick
     */
    public void setCanStick(boolean canStick) {
        this.canStick = canStick;
    }

    /**
     * 设置悬浮球可移动
     *
     * @param canMove
     */
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    /**
     * 设置悬浮球可虚化
     *
     * @param canBlur
     */
    public void setCanBlur(boolean canBlur) {
        this.canBlur = canBlur;
        setBallAlpha(newAlpha);
    }
}
