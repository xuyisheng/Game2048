package com.imooc.game2048.anim;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.imooc.game2048.bean.GameItem;

public class AnimationLayer extends FrameLayout {

    public AnimationLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationLayer(Context context) {
        super(context);
    }

    public void animCreate(GameItem target) {
        ScaleAnimation sa = new ScaleAnimation(0.1f, 1, 0.1f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(100);
        target.setAnimation(null);
        target.getItemView().startAnimation(sa);
    }
}
