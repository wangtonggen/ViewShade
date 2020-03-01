package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fivehundredpx.android.blur.BlurringView;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_content;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private FrameLayout fl_content;
    private AppCompatTextView tv_hint;

    AppCompatButton btn_show;
    AppCompatButton btn_hide;

    private View beforeView;//原来显示的位置
    private View afterView;//点击后的显示位置

    private Handler handler;
    private Runnable runnable;
    private int count = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (count <= 0) {
                    dismissShadeAnimation();
                    return;
                }
                count--;
                handler.postDelayed(this, 1000);
            }
        };
        ll_content = findViewById(R.id.ll_content);
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        fl_content = findViewById(R.id.fl_content);
        tv_hint = findViewById(R.id.tv_hint);

        btn_show = findViewById(R.id.btn_show);
        btn_hide = findViewById(R.id.btn_hide);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        fl_content.setOnClickListener(this);
        btn_show.setOnClickListener(this);
        btn_hide.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show:
                if (isNotShow()) {
                    isShowVisibility(btn_show);
                }
                break;
            case R.id.btn_hide:
                dismissShadeAnimation();
                break;
            case R.id.tv_1:
                if (isNotShow()) {
                    isShowVisibility(tv1);
                } else {
                    afterView = tv1;
                    translationShadeAnimation(afterView.getX() - beforeView.getX(), afterView.getY() - beforeView.getY());
                }
                break;
            case R.id.tv_2:
                if (isNotShow()) {
                    isShowVisibility(tv2);
                } else {
                    afterView = tv2;
                    translationShadeAnimation(afterView.getX() - beforeView.getX(), afterView.getY() - beforeView.getY());
                }
                break;
            case R.id.tv_3:
                if (isNotShow()) {
                    isShowVisibility(tv3);
                } else {
                    afterView = tv3;
                    translationShadeAnimation(afterView.getX() - beforeView.getX(), afterView.getY() - beforeView.getY());
                }
                break;
            case R.id.fl_content:
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fl_content.getLayoutParams();
                Toast.makeText(this, fl_content.getX() + fl_content.getWidth() / 2 + "---" + fl_content.getY() + fl_content.getHeight() / 2, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 是否显示
     *
     * @return true 未显示 false显示
     */
    private boolean isNotShow() {
        return fl_content.getVisibility() == View.INVISIBLE;
    }

    private void isShowVisibility(final View beforeView) {
        if (this.beforeView != null) {//已经存在beforeView
            //先执行位移动画 位移动画执行完在执行显示动画 afterView.getX() - beforeView.getX(), afterView.getY() - beforeView.getY()
            ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(fl_content, View.TRANSLATION_X, beforeView.getX() - this.beforeView.getX());//
            ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(fl_content, View.TRANSLATION_Y, beforeView.getY() - this.beforeView.getY());//
            ValueAnimator widthUpdateTranslation = ValueAnimator.ofInt(fl_content.getWidth(), beforeView.getWidth());
            widthUpdateTranslation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    fl_content.getLayoutParams().width = (Integer) animation.getAnimatedValue();
                    fl_content.requestLayout();
                }
            });

            ValueAnimator heightUpdateTranslation = ValueAnimator.ofInt(fl_content.getHeight(), beforeView.getHeight());
            heightUpdateTranslation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    fl_content.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                    fl_content.requestLayout();
                }
            });
            AnimatorSet animationSet = new AnimatorSet();
            animationSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    fl_content.setVisibility(View.VISIBLE);
                    showShadeAnimation(beforeView);
                }
            });
            animationSet.setInterpolator(new SpringInterpolator(1f));
            animationSet.playTogether(translationXAnimator, translationYAnimator, widthUpdateTranslation);
            animationSet.setDuration(100);
            animationSet.start();
        } else {
            //显示并出现动画
            this.beforeView = beforeView;
            fl_content.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fl_content.getLayoutParams();
            layoutParams.leftMargin = (int) this.beforeView.getX()+(int) ll_content.getX();
//            layoutParams.leftMargin = (int) this.beforeView.getX();
            layoutParams.topMargin = (int) this.beforeView.getY();
            layoutParams.width = this.beforeView.getWidth();
//            layoutParams.height = beforeView.getHeight();
            fl_content.setLayoutParams(layoutParams);
            showShadeAnimation(beforeView);
        }
    }

    private void showShadeAnimation(View view) {
        //动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(fl_content, "alpha", 0f, 0f, 1.0f);//
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(fl_content, "scaleY", 0f, 0f, 1.1f);//
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(fl_content, "scaleX", 0f, 0f, 1.1f);//
        ValueAnimator widthUpdateTranslation = ValueAnimator.ofInt(fl_content.getWidth(), view.getWidth());
        widthUpdateTranslation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fl_content.getLayoutParams().width = (Integer) animation.getAnimatedValue();
                fl_content.requestLayout();
            }
        });

        ValueAnimator heightUpdateTranslation = ValueAnimator.ofInt(fl_content.getHeight(), beforeView.getHeight());
        heightUpdateTranslation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fl_content.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                fl_content.requestLayout();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                count = 3;//重置时间
                handler.postDelayed(runnable, 1000);
            }
        });
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator,widthUpdateTranslation,heightUpdateTranslation);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    /**
     * 消失动画
     */
    private void dismissShadeAnimation() {
        if (isNotShow()) {//未显示 直接return
            return;
        }
        //动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(fl_content, "alpha", 1f, 0f);//
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(fl_content, "scaleY", 1f, 0f);//
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(fl_content, "scaleX", 1f, 0f);//

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                fl_content.setVisibility(View.INVISIBLE);
            }
        });
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    /**
     * 位移动画
     *
     * @param translationX x方向移动的距离
     * @param translationY Y方向移动距离
     */
    private void translationShadeAnimation(float translationX, float translationY) {
        //动画
        handler.removeCallbacks(runnable);//停止倒计时
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(fl_content, View.TRANSLATION_X, translationX);//
        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(fl_content, View.TRANSLATION_Y, translationY);//
        ValueAnimator widthUpdateTranslation = ValueAnimator.ofInt(fl_content.getWidth(), afterView.getWidth());
        Log.e("eeee",fl_content.getWidth()+"---"+afterView.getWidth());
        widthUpdateTranslation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fl_content.getLayoutParams().width = (Integer) animation.getAnimatedValue();
                fl_content.requestLayout();
                Log.e("bef",fl_content.getWidth()+"---"+animation.getAnimatedValue());
            }
        });

        ValueAnimator heightUpdateTranslation = ValueAnimator.ofInt(fl_content.getHeight(), afterView.getHeight());
        heightUpdateTranslation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fl_content.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                fl_content.requestLayout();
            }
        });
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.e("afterView",fl_content.getX()+"---"+fl_content.getWidth()+"---"+afterView.getWidth());
                //开始倒计时
                count = 3;//重置时间
                handler.postDelayed(runnable, 1000);
            }
        });
        animationSet.setInterpolator(new SpringInterpolator(1f));
        animationSet.playTogether(translationXAnimator, translationYAnimator, widthUpdateTranslation, heightUpdateTranslation);
        animationSet.setDuration(500);
        animationSet.start();
    }
}
