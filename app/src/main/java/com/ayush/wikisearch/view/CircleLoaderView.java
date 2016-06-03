package com.ayush.wikisearch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ayush.wikisearch.R;

/**
 * Created by ayushkedia on 03/06/16.
 */
public class CircleLoaderView extends RelativeLayout {

    private ImageView circle;
    Animation animation;

    public CircleLoaderView(Context context) {
        super(context);
        initLayout();
    }

    public CircleLoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public CircleLoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
    }

    public void initLayout() {
        inflate(getContext(), R.layout.loading_animation_layout, this);
        circle = (ImageView) findViewById(R.id.circle_icon);
        initAnimations();
        showProgressView(null);
    }

    public void showProgressView(Context context) {
        this.clearAnimation();
        this.setVisibility(VISIBLE);
        this.setEnabled(true);
        startRotateAnimation();
    }

    private void initAnimations() {
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
    }

    private void startRotateAnimation() {
        circle.startAnimation(animation);
    }

}
