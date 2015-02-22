package com.imooc.game2048.bean;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class GameItem extends FrameLayout {

    // Item显示数字
    private int cardShowNum;
    // Item显示颜色
    private int colorShow;
    // 数字Title
    private TextView tvNum;
    // 数字Title LayoutParams
    private LayoutParams params;

    public GameItem(Context context, int cardShowNum) {
        super(context);
        this.cardShowNum = cardShowNum;
        // 初始化Item
        initCardItem();
    }

    /**
     * 初始化Item
     *
     * @param context
     * @param cardShowNum
     */
    private void initCardItem() {
        // 设置背景色
        setBackgroundColor(Color.GRAY);
        tvNum = new TextView(getContext());
        setNum(cardShowNum);
        tvNum.setTextSize(30);
        tvNum.setGravity(Gravity.CENTER);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.setMargins(5, 5, 5, 5);
        addView(tvNum, params);
    }

    public View getItemView() {
        return tvNum;
    }

    public int getNum() {
        return cardShowNum;
    }

    public void setNum(int num) {
        this.cardShowNum = num;
        if (num == 0) {
            tvNum.setText("");
        } else {
            tvNum.setText("" + num);
        }
        // 设置背景颜色
        switch (num) {
            case 0:
                tvNum.setBackgroundColor(0x00000000);
                break;
            case 2:
                tvNum.setBackgroundColor(0xffeee4da);
                break;
            case 4:
                tvNum.setBackgroundColor(0xffede0c8);
                break;
            case 8:
                tvNum.setBackgroundColor(0xfff2b179);
                break;
            case 16:
                tvNum.setBackgroundColor(0xfff59563);
                break;
            case 32:
                tvNum.setBackgroundColor(0xfff67c5f);
                break;
            case 64:
                tvNum.setBackgroundColor(0xfff65e3b);
                break;
            case 128:
                tvNum.setBackgroundColor(0xffedcf72);
                break;
            case 256:
                tvNum.setBackgroundColor(0xffedcc61);
                break;
            case 512:
                tvNum.setBackgroundColor(0xffedc850);
                break;
            case 1024:
                tvNum.setBackgroundColor(0xffedc53f);
                break;
            case 2048:
                tvNum.setBackgroundColor(0xffedc22e);
                break;
            default:
                tvNum.setBackgroundColor(0xff3c3a32);
                break;
        }
    }
}
