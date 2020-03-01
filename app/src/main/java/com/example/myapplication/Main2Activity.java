package com.example.myapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import java.util.Random;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private FrameLayout fl_content;
    private AppCompatTextView tv_hint;

    AppCompatButton btn_show;
    AppCompatButton btn_hide;

    private View beforeView;//原来显示的位置
    private View afterView;//点击后的显示位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        fl_content = findViewById(R.id.fl_content);
        tv_hint = findViewById(R.id.tv_hint);

        btn_show = findViewById(R.id.btn_show);
        btn_hide = findViewById(R.id.btn_hide);

//        Blurry.with(this).radius(20).sampling(2).onto(fl_content);

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
                if (fl_content.getVisibility() == View.INVISIBLE) {
                    //显示并出现动画
                    fl_content.setVisibility(View.VISIBLE);
                    int random = new Random().nextInt(3);
                    View view;
                    switch (random) {
                        case 0:
                            view = tv1;
                            break;
                        case 1:
                            view = tv2;
                            break;
                        case 2:
                            view = tv3;
                            break;
                        default:
                            view = tv1;
                            break;
                    }
//                    view = tv1;
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fl_content.getLayoutParams();
                    layoutParams.leftMargin = (int) view.getX();
                    layoutParams.topMargin = (int) view.getY();
//                    int midXFl = (int) (fl_content.getX() + fl_content.getWidth() / 2);
//                    int midYFl = (int) (fl_content.getY() + fl_content.getHeight() / 2);
//                    int midXView = (int) (view.getX() + view.getWidth() / 2);
//                    int midYView = (int) (view.getY() + view.getHeight() / 2);
//                    int midX = midXView - midXFl;
//                    int midY = midYView - midYFl;
//                    layoutParams.width = view.getWidth();
//                    layoutParams.height = view.getHeight();
//                    layoutParams.leftMargin =(int) fl_content.getX()+midX;
//                    layoutParams.topMargin = (int) fl_content.getY()+midY;
                    //算出中心点
                    fl_content.setLayoutParams(layoutParams);
                    beforeView = view;

                    showShadeAnimation();
                }
                break;
            case R.id.btn_hide:
                dismissShadeAnimation();
                break;
            case R.id.tv_1:
                afterView = tv1;
//                springAnimation(tv1.getX(),tv1.getY());
                translationShadeAnimation(afterView.getX() - beforeView.getX(), afterView.getY() - beforeView.getY());
//                translationShadeAnimation((afterView.getX()+afterView.getWidth()/2.0f) - (beforeView.getX()+beforeView.getWidth()/2.0f), afterView.getY() - beforeView.getY());
                break;
            case R.id.tv_2:
                afterView = tv2;
                Log.e("tv_2",afterView.getX()+afterView.getWidth()/2.0f+"---"+(beforeView.getX()+beforeView.getWidth()/2.0f));
//                springAnimation(tv2.getX(),tv2.getY());
                translationShadeAnimation(afterView.getX() - beforeView.getX(), afterView.getY() - beforeView.getY());
//                translationShadeAnimation((afterView.getX()+afterView.getWidth()/2.0f) - (beforeView.getX()+beforeView.getWidth()/2.0f), afterView.getY() - beforeView.getY());
                break;
            case R.id.tv_3:
                afterView = tv3;
//                springAnimation(tv3.getX(),tv3.getY());
                translationShadeAnimation(afterView.getX() - beforeView.getX(), afterView.getY() - beforeView.getY());
//                translationShadeAnimation((afterView.getX()+afterView.getWidth()/2.0f) - (fl_content.getX()+fl_content.getWidth()/2.0f), afterView.getY() - beforeView.getY());
                break;
            case R.id.fl_content:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fl_content.getLayoutParams();
                Log.e("222",layoutParams.leftMargin+"---"+layoutParams.topMargin);
                Log.e("222",fl_content.getX()+"---"+fl_content.getWidth()/2);
                Toast.makeText(this, fl_content.getX() + fl_content.getWidth() / 2 + "---" + fl_content.getY() + fl_content.getHeight() / 2, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showShadeAnimation() {
        //动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(fl_content, "alpha", 0f, 0f, 1f);//
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(fl_content, "scaleY", 0f, 0f, 1f);//
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(fl_content, "scaleX", 0f, 0f, 1f);//
        ValueAnimator widthUpdateTranslation = ValueAnimator.ofInt(fl_content.getWidth(), beforeView.getWidth());
        widthUpdateTranslation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.e("hahah", animation.getAnimatedValue().toString());
                fl_content.getLayoutParams().width = (Integer) animation.getAnimatedValue();
                fl_content.requestLayout();
            }
        });

        ValueAnimator heightUpdateTranslation = ValueAnimator.ofInt(fl_content.getHeight(), beforeView.getHeight());
        heightUpdateTranslation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.e("hahah111", animation.getAnimatedValue().toString());
                fl_content.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                fl_content.requestLayout();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(alphaAnimator,scaleXAnimator,scaleYAnimator,widthUpdateTranslation,heightUpdateTranslation);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    /**
     * 消失动画
     */
    private void dismissShadeAnimation() {
        //动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(fl_content, "alpha", 1f, 1f, 0f);//
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(fl_content, "scaleY", 1f, 1f, 0f);//
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(fl_content, "scaleX", 1f, 1f, 0f);//

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(alphaAnimator,scaleXAnimator,scaleYAnimator);
        animatorSet.setDuration(300);
        animatorSet.start();

        fl_content.setVisibility(View.INVISIBLE);

    }

    /**
     * 位移动画
     *
     * @param translationX x方向移动的距离
     * @param translationY Y方向移动距离
     */
    private void translationShadeAnimation(float translationX, float translationY) {
        Log.e("tag", translationX + "---" + translationY);
        //动画
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(fl_content, View.TRANSLATION_X, translationX);//
        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(fl_content, View.TRANSLATION_Y, translationY);//
        ValueAnimator widthUpdateTranslation = ValueAnimator.ofInt(fl_content.getWidth(), afterView.getWidth());
        widthUpdateTranslation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fl_content.getLayoutParams().width = (Integer) animation.getAnimatedValue();
                fl_content.requestLayout();
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
        animationSet.setInterpolator(new SpringInterpolator(1f));
        animationSet.playTogether(translationXAnimator, translationYAnimator, widthUpdateTranslation,heightUpdateTranslation);
        animationSet.setDuration(500);
        animationSet.start();
    }

    private void springAnimation(float finalPositionX, float finalPositionY) {
        SpringAnimation springAnimationBikeX = SpringAnimationUtils.createSpringAnimation(fl_content, SpringAnimation.TRANSLATION_X, finalPositionX, SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        SpringAnimation springAnimationBikeY = SpringAnimationUtils.createSpringAnimation(fl_content, SpringAnimation.TRANSLATION_Y, finalPositionY, SpringForce.STIFFNESS_LOW, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnimationBikeX.start();
        springAnimationBikeY.start();
    }
}
