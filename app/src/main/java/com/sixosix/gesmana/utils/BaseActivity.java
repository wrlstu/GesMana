package com.sixosix.gesmana.utils;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    private GestureDetector detector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e1.getRawY() - e2.getRawY()) > 100) {//不是合法的手势
                    return true;
                }
                if (Math.abs(velocityX) < 150) {//速度太慢
                    return true;
                }

                if ((e2.getRawX() - e1.getRawX()) > 200) {// 表示 向右滑动表示上一页
                    back(null);
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    /**
     * 右滑返回上一页
     *
     * @param view
     */
    public abstract void back(View view);

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
