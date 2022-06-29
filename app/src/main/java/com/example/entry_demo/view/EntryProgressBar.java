package com.example.entry_demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.entry_demo.R;
import com.example.entry_demo.util.ViewHelper;

/**
 * 次入口的蓝色小滚动条
 */
public class EntryProgressBar extends LinearLayout {
    View progress_view;
    private float currentX = 0;
    //每一页展示的icon个数
    private float display_count = 4;
    //icon的总个数
    private float total_count = 5;
    //进度条组件总宽度32dp
    private float width_dp = 32;
    //蓝色进度的宽度dp
    private float blue_width_dp = 20;

    public EntryProgressBar(Context context) {
        this(context, null);
    }

    public EntryProgressBar(Context context,@Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // 引入布局
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.entry_progress_bar_layout, this);
        progress_view = this.findViewById(R.id.progress_view);
        setBlueWidth();
    }

    //设置蓝色进度的width
    private void setBlueWidth() {
        blue_width_dp = (display_count / total_count) * width_dp;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewHelper.dp2px(getContext(),blue_width_dp), LayoutParams.MATCH_PARENT);
        progress_view.setLayoutParams(params);
    }

    // 设置总的入口个数
    public void setTotalCount(int total_count) {
        this.total_count = total_count;
        setBlueWidth();
    }

    // 更新进度
    public void setProgress(float percent_progress) {
        float progress = 0;
        progress = ViewHelper.dp2px(getContext(), width_dp - blue_width_dp) * percent_progress;
        // 开启动画
        TranslateAnimation animation = new TranslateAnimation(currentX, progress, 0, 0);
        animation.setDuration(200);
        // 设置动画过后不复位
        animation.setFillEnabled(true);
        animation.setFillAfter(true);

        progress_view.startAnimation(animation);
        currentX = progress;
    }
}
