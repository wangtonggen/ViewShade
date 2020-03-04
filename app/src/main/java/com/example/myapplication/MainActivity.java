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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout ll_content;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private RelativeLayout rl_content;
    private AppCompatTextView tv_hint;

    AppCompatButton btn_show;
    AppCompatButton btn_hide;
    AppCompatButton btn_show3;

    private View beforeView;//原来显示的位置
    private View afterView;//点击后的显示位置

    private Handler handler;
    private Runnable runnable;
    private int count = 4;

    private View view;
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
        rl_content = findViewById(R.id.rl_content);
        tv_hint = findViewById(R.id.tv_hint);

        btn_show = findViewById(R.id.btn_show);
        btn_hide = findViewById(R.id.btn_hide);
        btn_show3 = findViewById(R.id.btn_show3);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        btn_show.setOnClickListener(this);
        btn_hide.setOnClickListener(this);
        btn_show3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show://显示一个
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.GONE);
                tv3.setVisibility(View.GONE);
                break;
            case R.id.btn_hide://显示两个
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.GONE);
//                dismissShadeAnimation();
                break;
            case R.id.btn_show3://显示三个
                tv1.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
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
        }
    }

    /**
     * 是否显示
     *
     * @return true 未显示 false显示
     */
    private boolean isNotShow() {
        return view == null || view.getVisibility() == View.INVISIBLE;
    }

    private void isShowVisibility(final View beforeView) {
        view = LayoutInflater.from(this).inflate(R.layout.view_layout,null,false);
        view.setVisibility(View.VISIBLE);
        rl_content.addView(view);

        Log.e("tv4",findViewById(R.id.tv_4).getLeft()+"---");
        Log.e("tv5",findViewById(R.id.tv_5).getLeft()+"---");
        this.beforeView = beforeView;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.alignWithParent = true;
//        ((ConstraintLayout)this.beforeView.getParent()).getLayoutParams().
        layoutParams.leftMargin = this.beforeView.getLeft() + ll_content.getLeft();
        layoutParams.rightMargin = this.beforeView.getRight() + ll_content.getRight();
        layoutParams.topMargin = this.beforeView.getTop()+findViewById(R.id.ll_bottom).getTop();
        layoutParams.bottomMargin = ll_content.getBottom();
        layoutParams.width = this.beforeView.getWidth()+4;
//        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            layoutParams.height = beforeView.getHeight()+4;
        view.setLayoutParams(layoutParams);
        showShadeAnimation(view);
    }

    private void showShadeAnimation(final View view) {
        //动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1.0f);//
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1.0f);//
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1.0f);//

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                Log.e("cancel","取消了");
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                Log.e("start","start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.e("end","end");
                //
                count = 3;//重置时间
                handler.postDelayed(runnable, 1000);
            }
        });
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);
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
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);//
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f);//
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);//

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (view != null){
                    view.setVisibility(View.INVISIBLE);
                    rl_content.removeView(view);
                }
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
        Log.e("tata", translationX + "---" + translationY);
        //动画
        handler.removeCallbacks(runnable);//停止倒计时
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, translationX);//
        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, translationY);//
        ValueAnimator widthUpdateTranslation = ValueAnimator.ofInt(view.getWidth(), afterView.getWidth(), afterView.getWidth());
        widthUpdateTranslation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.getLayoutParams().width = (Integer) animation.getAnimatedValue();
                view.requestLayout();
                Log.e("bef", view.getWidth() + "---" + animation.getAnimatedValue());
            }
        });

        ValueAnimator heightUpdateTranslation = ValueAnimator.ofInt(view.getHeight(), afterView.getHeight());
        heightUpdateTranslation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                view.requestLayout();
            }
        });
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                startUpdateWidthAndHeight();
                //开始倒计时
                count = 3;//重置时间
                handler.postDelayed(runnable, 1000);
            }
        });
        animationSet.setInterpolator(new SpringInterpolator(1f));
        animationSet.playTogether(translationXAnimator, translationYAnimator, widthUpdateTranslation,heightUpdateTranslation);
//        animationSet.playTogether(translationXAnimator, translationYAnimator);
        animationSet.setDuration(500);
        animationSet.start();
    }
}
