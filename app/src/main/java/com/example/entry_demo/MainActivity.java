package com.example.entry_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.example.entry_demo.util.ViewHelper;
import com.example.entry_demo.view.EntryProgressBar;
import com.example.entry_demo.view.EntryView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ConstraintLayout btn_arrow_right,btn_arrow_left;
    private EntryProgressBar entry_progress_bar;
    private HorizontalScrollView horizontalScrollView;
    private Context context;
    private EntryView abs_1, cbs_2, ctbs_3, destination_4, gbs_5, home_fbs_6, lbs_7, nlbs_8, pbs_9, rbs_10, staycation_11, tbs_12, frbs_13;

    // 设置一个误差值，用于判断icon是否处于显示状态的一个边界误差值
    private int dp_6 = 6;

    //入口之间的间距
    int entry_margin_dp = 0;

    private int horizontalScrollView_width_dp = 0;
    //次入口的宽度
    private final int icon_width_dp = 60;
    //设置索引坐标，用于判断次入口是否处于显示状态
    private List<Integer> visible_index = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initView();
    }

    private void initView() {
        btn_arrow_left = findViewById(R.id.btn_arrow_left);
        btn_arrow_right = findViewById(R.id.btn_arrow_right);
        entry_progress_bar = findViewById(R.id.entry_progress_bar);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        abs_1 = findViewById(R.id.abs_1);
        cbs_2 = findViewById(R.id.cbs_2);
        ctbs_3 = findViewById(R.id.ctbs_3);
        destination_4 = findViewById(R.id.destination_4);
        gbs_5 = findViewById(R.id.gbs_5);
        home_fbs_6 = findViewById(R.id.home_fbs_6);
        lbs_7 = findViewById(R.id.lbs_7);
        nlbs_8 = findViewById(R.id.nlbs_8);
        pbs_9 = findViewById(R.id.pbs_9);
        rbs_10 = findViewById(R.id.rbs_10);
        staycation_11 = findViewById(R.id.staycation_11);
        tbs_12 = findViewById(R.id.tbs_12);
        frbs_13 = findViewById(R.id.frbs_13);

        btn_arrow_left.setOnClickListener(this);
        btn_arrow_right.setOnClickListener(this);

        addListenerToHorizontalView();
    }

    private void initEntry() {
        List<EntryView> entriesTemp = Arrays.asList(abs_1, cbs_2, ctbs_3, destination_4, gbs_5, home_fbs_6, lbs_7, nlbs_8, pbs_9, rbs_10, staycation_11, tbs_12, frbs_13);
        // 根据屏幕大小计算icon之间的间距
        int entrySize = entriesTemp.size();
        if (entrySize > 4) {
            btn_arrow_left.setVisibility(View.VISIBLE);
            // 之所以减去64dp，是因为右滑按钮占40dp，marginStar占24dp
            entry_margin_dp = (horizontalScrollView_width_dp - icon_width_dp * 4 - 64) / 4;
            // 设置图标总数
            entry_progress_bar.setTotalCount(entrySize);

            // 初始化索引坐标
            int index = 0;
            visible_index.add(0, 0);
            for (int i = 1; i <= entrySize - 4; i++) {
                // 注意，entrySize - 4 是因为默认每一页显示4个图标，当HorizontalView不可滑动时，这4个图标一定是处于可见状态的，所以不用加入到索引坐标中
                // 第一个和最后一个可见索引坐标为60dp，其他均为60dp + secondaryEntry_margin_dp
                if (i == 1 || i == entrySize - 4) {
                    index += icon_width_dp;
                } else {
                    index += (icon_width_dp + entry_margin_dp);
                }
                visible_index.add(i, index);
            }
        } else {
            // 之所以减去48dp，是因为当入口小于4个时，左右按钮隐藏，但marginStar和marginEnd各占24dp
            entry_margin_dp = (horizontalScrollView_width_dp - icon_width_dp * 4 -48) / 3;
            entry_progress_bar.setVisibility(View.GONE);
        }

        for (int i = 0; i < entriesTemp.size(); i ++) {
            EntryView entry = entriesTemp.get(i);
            int margin = entry_margin_dp;
            if (i < entrySize) {
                if (i == entrySize - 1) {
                    //设置最后一个入口的margin
                    margin = 24;
                }
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) entry.getLayoutParams();
                if (i == 0) {
                    params.setMarginStart(ViewHelper.dp2px(context, 24));
                }
                params.setMarginEnd(ViewHelper.dp2px(context, margin));
                entry.setLayoutParams(params);
            } else {
                entry.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //左滑按钮
            case R.id.btn_arrow_left:
                //校正icon坐标
                int correction_l = positionCorrection("left", ViewHelper.px2dip(context, horizontalScrollView.getScrollX()));
                //滑动距离
                int dx_l = ViewHelper.dp2px(context, (icon_width_dp + entry_margin_dp) * 4 + correction_l);
                //滑动动画
                smoothScrollAnimation(horizontalScrollView.getScrollX(), dx_l);
                break;

            //右滑按钮
            case R.id.btn_arrow_right:
                int correction_r = positionCorrection("right", ViewHelper.px2dip(context, horizontalScrollView.getScrollX()));
                int dx_r = -ViewHelper.dp2px(context, (icon_width_dp + entry_margin_dp) * 4 + correction_r);
                smoothScrollAnimation(horizontalScrollView.getScrollX(), dx_r);
                break;
        }
    }

    // 次入口坐标位置校正，用于处理当用户手动左右滑动后，再点击左右滑动按钮时的场景
    // 注意，如果当icon露出超过五分之四，则视为可见，此时往后滑动4个图标。 反之则不可见，此时往后滑动三个图标
    private int positionCorrection (String direction, int scrollX_dp) {
        int correction_dp = 0;
        //当次入口icon露出五分之四即为可见
        int judge_dp = 0;
        //当icon为第一个入口或者最后一个入口时，距离屏幕边缘的margin
        int head_dp = 0;
        if (direction.equals("left")) {
            for (int i = 0; i < visible_index.size(); i++) {
                if (i == 0) {
                    judge_dp = (int)(icon_width_dp * 0.2);
                    head_dp = 16;
                } else {
                    judge_dp = (int)((icon_width_dp + entry_margin_dp) * 0.2);
                    head_dp = 0;
                }
                if (i < visible_index.size() -1) {
                    if (scrollX_dp >= visible_index.get(i) - judge_dp && scrollX_dp <= visible_index.get(i+1) - judge_dp) {
                        correction_dp = visible_index.get(i) - scrollX_dp - head_dp;
                        return correction_dp - dp_6;
                    }
                }
            }
        } else if (direction.equals("right")) {
            for (int i = visible_index.size() - 1; i > 0; i--) {
                if (i == visible_index.size() - 1) {
                    judge_dp = (int)(icon_width_dp * 0.2);
                    head_dp = 16;
                } else {
                    judge_dp = (int)((icon_width_dp + entry_margin_dp) * 0.2);
                    head_dp = 0;
                }
                if (scrollX_dp >= visible_index.get(i-1) + judge_dp && scrollX_dp <= visible_index.get(i) + judge_dp) {
                    correction_dp = scrollX_dp - visible_index.get(i) - head_dp;
                    return correction_dp + dp_6;
                }
            }
        }
        return correction_dp;
    }

    // 点击次入口的左滑右滑按钮时的滑动动画
    private void smoothScrollAnimation(int startX, int dx) {
        ValueAnimator animator = ValueAnimator.ofInt(0, 1).setDuration(600);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animator.getAnimatedFraction();
                horizontalScrollView.smoothScrollTo(startX + (int) (dx * fraction), 0);
            }
        });
        animator.start();
    }

    // 给HorizontalScrollView 添加滑动监听
    private void addListenerToHorizontalView() {
        // 使用post方法是因为，在构建入口时，需要先获取View的宽度，然而只有当View进行到onLayout阶段后，才能得到准群的width，所以才调用view.post方法来保证获取到width是准确的
        horizontalScrollView.post(new Runnable() {
            @Override
            public void run() {
                horizontalScrollView_width_dp = ViewHelper.px2dip(context, horizontalScrollView.getWidth());
                initEntry();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            horizontalScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    float left_btn_alpha = 1.0f;
                    float right_btn_alpha = 1.0f;

                    // 获取可滑动范围
                    int scrollRange = 0;
                    int scroll_X = horizontalScrollView.getScrollX();
                    if (horizontalScrollView.getChildCount() > 0) {
                        View child = horizontalScrollView.getChildAt(0);
                        scrollRange = Math.max(0, child.getWidth() - (horizontalScrollView.getWidth() - (horizontalScrollView.getPaddingLeft() + horizontalScrollView.getPaddingRight())));
                    }

                    if (scrollRange > 0 ) {
                        // 同步更新底部蓝色进度条
                        entry_progress_bar.setProgress((float) scroll_X / scrollRange);

                        //设置左右按钮的渐变透明度
                        if (scroll_X >= scrollRange - ViewHelper.dp2px(context, 40)) {
                            left_btn_alpha = (float) (scrollRange - scroll_X) / ViewHelper.dp2px(context, 40);
                        } else if (scroll_X < ViewHelper.dp2px(context, 40)) {
                            right_btn_alpha = (float) scroll_X / ViewHelper.dp2px(context, 40);
                        }
                    }

                    if (horizontalScrollView.canScrollHorizontally(1) && scroll_X < scrollRange) {
                        //向右滚动
                        btn_arrow_left.setVisibility(View.VISIBLE);
                        btn_arrow_left.setAlpha(left_btn_alpha);
                    } else {
                        btn_arrow_left.setVisibility(View.GONE);
                    }

                    if (horizontalScrollView.canScrollHorizontally(-1)) {
                        //向左滚动
                        btn_arrow_right.setVisibility(View.VISIBLE);
                        btn_arrow_right.setAlpha(right_btn_alpha);
                    } else {
                        btn_arrow_right.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}