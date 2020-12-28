package com.example.hydro.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;

public class LogoProgressBar extends ProgressBar {

    public void start(Context context) {
        RotateAnimation animation = new RotateAnimation(
                0, 360,
                0.5f,
                0.5f
        );
        animation.setInterpolator(new LinearInterpolator());
        this.startAnimation(animation);
    }

    public LogoProgressBar(Context context) {
        super(context);
    }

    public LogoProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LogoProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LogoProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
