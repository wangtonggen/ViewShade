package com.example.myapplication;

import android.view.View;

import androidx.annotation.FloatRange;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

/**
 * author: wtg
 * date:2020/2/11 0011
 * desc: 弹簧动画的工具类
 */
public class SpringAnimationUtils {

    /**
     * 创建弹簧动画的类
     * @param view 执行动画的view
     * @param property 动画作用的属性
     * @param finalPosition 动画结束的位置
     * @param stiffness 硬度
     * @param dampingRatio 阻尼
     * @return SpringAnimation
     */
    public static SpringAnimation createSpringAnimation(View view, DynamicAnimation.ViewProperty property, float finalPosition, @FloatRange(from = 0.0) Float stiffness, @FloatRange(from = 0.0) Float dampingRatio){
        //创建弹性动画类SpringAnimation
        SpringAnimation animation = new SpringAnimation(view, property);
        SpringForce spring = new SpringForce(finalPosition);
        spring.setStiffness(stiffness);
        spring.setDampingRatio(dampingRatio);
        //关联弹性特质
        animation.setSpring(spring);
        return animation;
    }
}
