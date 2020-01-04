package com.sixosix.gesmana;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.SwitchCompat;


/**
 * Created by oops on 17-12-23.
 */

/**
 * 自定义布局
 */

public class FloatingItem extends RelativeLayout {
    private String mLeftText;
    private Drawable mLeftIcon;
    private Drawable mRightIcon;
    private int mTextColor;
    private float mRightTextSize;
    private int mRightTextColor;
    private View mView;
    private RelativeLayout mRootLayout;
    private TextView mTvLeftText;
    private TextView mTvRightText;
    private ImageView mIvLeftIcon;
    private int mLeftIconSize;
    private FrameLayout mRightLayout;
    private ImageView mIvRightIcon;
    private AppCompatCheckBox mRightIcon_check;
    private SwitchCompat mRightIcon_switch;
    private AppCompatSeekBar mRightIcon_seekBar;
    private int mRightStyle = 0;
    private boolean mChecked;
    private OnSettingItemClick onSettingItemClick;
    private OnSettingItemLongClick onSettingItemLongClick;

    public FloatingItem(Context context) {
        this(context, null);
    }

    public FloatingItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造函数
     * 绑定事件
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */

    public FloatingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        getCustomStyle(context, attrs);
        switchRightStyle(mRightStyle);
        mRootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOn();
            }
        });
        mRootLayout.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (null != onSettingItemLongClick)
                    onSettingItemLongClick.longClick();
                return true;
            }
        });

        mRightIcon_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onSettingItemClick != null) {
                    onSettingItemClick.click(isChecked);
                }
            }
        });
        mRightIcon_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onSettingItemClick != null) {
                    onSettingItemClick.click(isChecked);
                }
            }
        });
    }

    /**
     * 初始化控件
     */

    private void initView(Context context) {
        mView = View.inflate(context, R.layout.activity_floating_item, this);
        mRootLayout = mView.findViewById(R.id.rootLayout);
        mTvLeftText = mView.findViewById(R.id.tv_leftText);
        mTvRightText = mView.findViewById(R.id.tv_rightText);
        mIvLeftIcon = mView.findViewById(R.id.iv_leftIcon);
        mIvRightIcon = mView.findViewById(R.id.iv_rightIcon);
        mRightLayout = mView.findViewById(R.id.rightLayout);
        mRightIcon_check = mView.findViewById(R.id.rightCheck);
        mRightIcon_switch = mView.findViewById(R.id.rightSwitch);
        mRightIcon_seekBar = mView.findViewById(R.id.rightSeekBar);
    }

    /**
     * 获取自定义布局控件，并初始化
     *
     * @param context
     * @param attrs
     */

    private void getCustomStyle(Context context, AttributeSet attrs) {
        @SuppressLint("CustomViewStyleable") TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ItemView_leftText) {
                mLeftText = a.getString(attr);
                mTvLeftText.setText(mLeftText);
            } else if (attr == R.styleable.ItemView_leftIcon) {
                // 左侧图标
                mLeftIcon = a.getDrawable(attr);
                if (null != mLeftIcon) {
                    mIvLeftIcon.setImageDrawable(mLeftIcon);
                    mIvLeftIcon.setVisibility(VISIBLE);
                }
            } else if (attr == R.styleable.ItemView_leftIconSize) {
                mLeftIconSize = (int) a.getDimension(attr, 16);
                RelativeLayout.LayoutParams layoutParams = (LayoutParams) mIvLeftIcon.getLayoutParams();
                layoutParams.width = mLeftIconSize;
                layoutParams.height = mLeftIconSize;
                mIvLeftIcon.setLayoutParams(layoutParams);
            } else if (attr == R.styleable.ItemView_leftTextMarginLeft) {
                int leftMargin = (int) a.getDimension(attr, 8);
                RelativeLayout.LayoutParams layoutParams = (LayoutParams) mTvLeftText.getLayoutParams();
                layoutParams.leftMargin = leftMargin;
                mTvLeftText.setLayoutParams(layoutParams);
            } else if (attr == R.styleable.ItemView_rightIcon) {
                mRightIcon = a.getDrawable(attr);
                mIvRightIcon.setImageDrawable(mRightIcon);
            } else if (attr == R.styleable.ItemView_leftTextSize) {
                float textSize = a.getFloat(attr, 16);
                mTvLeftText.setTextSize(textSize);
            } else if (attr == R.styleable.ItemView_leftTextColor) {
                mTextColor = a.getColor(attr, Color.LTGRAY);
                mTvLeftText.setTextColor(mTextColor);
            } else if (attr == R.styleable.ItemView_rightStyle) {
                mRightStyle = a.getInt(attr, 0);
            } else if (attr == R.styleable.ItemView_rightText) {
                mTvRightText.setText(a.getString(attr));
            } else if (attr == R.styleable.ItemView_rightTextSize) {
                mRightTextSize = a.getFloat(attr, 14);
                mTvRightText.setTextSize(mRightTextSize);
            } else if (attr == R.styleable.ItemView_rightTextColor) {
                mRightTextColor = a.getColor(attr, Color.GRAY);
                mTvRightText.setTextColor(mRightTextColor);
            }
        }
        a.recycle();
    }

    /**
     * 切换自定义布局的右侧布局
     *
     * @param mRightStyle
     */
    private void switchRightStyle(int mRightStyle) {
        switch (mRightStyle) {
            case 0:
                //默认展示样式，只展示一个图标
                mIvRightIcon.setVisibility(View.VISIBLE);
                mRightIcon_check.setVisibility(View.GONE);
                mRightIcon_switch.setVisibility(View.GONE);
                mRightIcon_seekBar.setVisibility(View.GONE);
                break;
            case 1:
                //隐藏右侧图标
                mRightLayout.setVisibility(View.INVISIBLE);
                break;
            case 2:
                //显示选择框样式
                mIvRightIcon.setVisibility(View.GONE);
                mRightIcon_check.setVisibility(View.VISIBLE);
                mRightIcon_switch.setVisibility(View.GONE);
                mRightIcon_seekBar.setVisibility(View.GONE);
                break;
            case 3:
                //显示开关切换样式
                mIvRightIcon.setVisibility(View.GONE);
                mRightIcon_check.setVisibility(View.GONE);
                mRightIcon_switch.setVisibility(View.VISIBLE);
                mRightIcon_seekBar.setVisibility(View.GONE);
                break;
            case 4:
                mIvRightIcon.setVisibility(View.GONE);
                mRightIcon_check.setVisibility(View.GONE);
                mRightIcon_switch.setVisibility(View.GONE);
                mRightIcon_seekBar.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 处理单击事件
     */
    public void clickOn() {
        switch (mRightStyle) {
            case 0:
            case 1:
                if (null != onSettingItemClick) {
                    onSettingItemClick.click(mChecked);
                }
                break;
            case 2:
                //选择框切换选中状态
                mRightIcon_check.setChecked(!mRightIcon_check.isChecked());
                mChecked = mRightIcon_check.isChecked();
                break;
            case 3:
                //开关切换状态
                mRightIcon_switch.setChecked(!mRightIcon_switch.isChecked());
                mChecked = mRightIcon_check.isChecked();
                break;
            default:
                break;
        }
    }

    public void setOnSettingItemClick(OnSettingItemClick mOnSettingItemClick) {
        this.onSettingItemClick = mOnSettingItemClick;
    }

    public void setOnSettingItemLongClick(OnSettingItemLongClick onSettingItemLongClick) {
        this.onSettingItemLongClick = onSettingItemLongClick;
    }

    /**
     * 单击监听
     */
    public interface OnSettingItemClick {
        void click(boolean isChecked);
    }

    /**
     * 长按监听
     */
    public interface OnSettingItemLongClick {
        void longClick();
    }
}

