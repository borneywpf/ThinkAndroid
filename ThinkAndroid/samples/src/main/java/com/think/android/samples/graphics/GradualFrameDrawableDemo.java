package com.think.android.samples.graphics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.think.android.samples.R;

/**
 * Created by borney on 9/23/16.
 */
public class GradualFrameDrawableDemo extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradualframedrawable_demo);
    }

    public void onClickImageView(View v) {
        Animation a = new AlphaAnimation(1.0f, 0.0f);
        a.setDuration(1000);
        v.startAnimation(a);
    }
}
