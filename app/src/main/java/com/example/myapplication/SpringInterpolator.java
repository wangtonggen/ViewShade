package com.example.myapplication;

import android.view.animation.Interpolator;

/**
 * author:wtg
 * time:2020/2/29
 * desc:
 */
public class SpringInterpolator implements Interpolator {
    //弹性因数 它的值越大，它回弹效果越慢。让我们来看看效果吧：
    private float factor;

    public SpringInterpolator(float factor) {
        this.factor = factor;
    }

    @Override
    public float getInterpolation(float input) {
        return (float) (Math.pow(2, -10 * input) * Math.sin((input - factor * 4) * (2 * Math.PI) / factor) + 1);
    }
}
