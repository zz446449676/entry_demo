package com.example.entry_demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.entry_demo.R;

public class EntryView extends ConstraintLayout implements View.OnClickListener{
    private Context context;
    // 展示文字
    private String text;

    // 图片资源
    private int img;

    private ImageView imgView;

    private TextView textView;

    private ConstraintLayout entry_view;

    public EntryView(@NonNull Context context) {
        this(context, null);
    }

    public EntryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EntryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.enrty_view, this);
        imgView = this.findViewById(R.id.imgView);
        textView = this.findViewById(R.id.textView);
        entry_view = this.findViewById(R.id.entry_view);

        // 获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EntryView);
        text = ta.getString(R.styleable.EntryView_entryText);
        img = ta.getResourceId(R.styleable.EntryView_img, R.drawable.common_file_icon_default);

        Glide.with(this).load(img).into(imgView);
        textView.setText(text);

        entry_view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(context,"您点击了 " + text + " 入口", Toast.LENGTH_SHORT).show();
    }
}
