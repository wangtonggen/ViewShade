package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv1;
    private TextView tv2;
    private FrameLayout fl_content;
    private AppCompatTextView tv_hint;

    AppCompatButton btn_show;
    AppCompatButton btn_hide;

    private int[] tv_location1 = new int[2];
    private int[] tv_location2 = new int[2];
    private int[] fl_or_location = new int[2];//指示器原来的位置
    private int[] fl_content_location = new int[2];//指示器更新后的位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        fl_content = findViewById(R.id.fl_content);
        tv_hint = findViewById(R.id.tv_hint);
        tv1.getLocationOnScreen(tv_location1);
        tv2.getLocationOnScreen(tv_location2);

        btn_show = findViewById(R.id.btn_show);
        btn_hide = findViewById(R.id.btn_hide);

//        Blurry.with(this).radius(20).sampling(2).onto(fl_content);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
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
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fl_content.getLayoutParams();
                    layoutParams.leftMargin = (int) tv2.getX();
                    layoutParams.topMargin = (int) tv2.getY();
                    fl_content.setLayoutParams(layoutParams);
                    showShadeAnimation();
                }
                showShadeAnimation();
                break;
            case R.id.btn_hide:
                dismissShadeAnimation();
                break;
            case R.id.tv_1:
                fl_content.getLocationOnScreen(fl_content_location);
                translationShadeAnimation(0, -240, 0, 0);
                break;
            case R.id.tv_2:
                fl_content.getLocationOnScreen(fl_or_location);
                Log.e("fl_content1", fl_content.getX() + "---" + fl_content.getY() + "---" + tv1.getX() + "---" + tv1.getY());
                translationShadeAnimation(-240, 0, 0, 0);
                break;
        }
    }

    private void showShadeAnimation() {
        //动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(fl_content, "alpha", 0f, 0f, 1f);//
        alphaAnimator.setDuration(300);
        alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimator.start();

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(fl_content, "scaleY", 0f, 0f, 1f);//
        scaleYAnimator.setDuration(300);
        scaleYAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimator.start();

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(fl_content, "scaleX", 0f, 0f, 1f);//
        scaleXAnimator.setDuration(300);
        scaleXAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimator.start();
    }

    /**
     * 消失动画
     */
    private void dismissShadeAnimation() {
        //动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(fl_content, "alpha", 1f, 1f, 0f);//
        alphaAnimator.setDuration(300);
        alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimator.start();

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(fl_content, "scaleY", 1f, 1f, 0f);//
        scaleYAnimator.setDuration(300);
        scaleYAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimator.start();

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(fl_content, "scaleX", 1f, 1f, 0f);//
        scaleXAnimator.setDuration(300);
        scaleXAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimator.start();
    }

    /**
     * 位移动画
     *
     * @param currentX     当前x位置
     * @param translationX x方向移动的距离
     * @param currentY     当前X位置
     * @param translationY Y方向移动距离
     */
    private void translationShadeAnimation(float currentX, float translationX, float currentY, float translationY) {
        //TODO 可能需要使用TranslateAnimation 动画 去除
        Log.e("translation", currentX + "---" + translationX + "---" + currentY + "---" + translationY);
        Log.e("translation22", fl_content.getX() + "---" + fl_content.getY());
        //动画
        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(fl_content, "translationX", currentX, translationX);//
        translationXAnimator.setDuration(1000);
        translationXAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.e("translation33", fl_content.getX() + "---" + fl_content.getY());
            }
        });
        translationXAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        translationXAnimator.start();

        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(fl_content, "translationY", currentY, translationY);//
        translationYAnimator.setDuration(1000);
        translationYAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        translationYAnimator.start();
    }
}
