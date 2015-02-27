package com.imooc.game2048.bean;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.imooc.game2048.config.Config;

public class GameItem extends FrameLayout {

    // Item显示数字
    private int mCardShowNum;
    // Item显示颜色
    private int mColorShow;
    // 数字Title
    private TextView mTvNum;
    // 数字Title LayoutParams
    private LayoutParams mParams;

    public GameItem(Context context, int mCardShowNum) {
        super(context);
        this.mCardShowNum = mCardShowNum;
        // 初始化Item
        initCardItem();
    }

    /**
     * 初始化Item
     */
    private void initCardItem() {
        // 设置面板背景色，是由Frame拼起来的
        setBackgroundColor(Color.GRAY);
        mTvNum = new TextView(getContext());
        setNum(mCardShowNum);
        // 修改5X5时字体太大
        int gameLines = Config.mSp.getInt(Config.KEY_GAME_LINES, 4);
        if (gameLines == 4) {
            mTvNum.setTextSize(35);
        } else if (gameLines == 5) {
            mTvNum.setTextSize(25);
        } else {
            mTvNum.setTextSize(20);
        }
        TextPaint tp = mTvNum.getPaint();
        tp.setFakeBoldText(true);
        mTvNum.setGravity(Gravity.CENTER);
        mParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mParams.setMargins(5, 5, 5, 5);
        addView(mTvNum, mParams);
    }

    public View getItemView() {
        return mTvNum;
    }

    public int getNum() {
        return mCardShowNum;
    }

    public void setNum(int num) {
        this.mCardShowNum = num;
        if (num == 0) {
            mTvNum.setText("");
        } else {
            mTvNum.setText("" + num);
        }
        // 设置背景颜色
        switch (num) {
            case 0:
                mTvNum.setBackgroundColor(0x00000000);
                break;
            case 2:
                mTvNum.setBackgroundColor(0xffeee4da);
                break;
            case 4:
                mTvNum.setBackgroundColor(0xffede0c8);
                break;
            case 8:
                mTvNum.setBackgroundColor(0xfff2b179);
                break;
            case 16:
                mTvNum.setBackgroundColor(0xfff59563);
                break;
            case 32:
                mTvNum.setBackgroundColor(0xfff67c5f);
                break;
            case 64:
                mTvNum.setBackgroundColor(0xfff65e3b);
                break;
            case 128:
                mTvNum.setBackgroundColor(0xffedcf72);
                break;
            case 256:
                mTvNum.setBackgroundColor(0xffedcc61);
                break;
            case 512:
                mTvNum.setBackgroundColor(0xffedc850);
                break;
            case 1024:
                mTvNum.setBackgroundColor(0xffedc53f);
                break;
            case 2048:
                mTvNum.setBackgroundColor(0xffedc22e);
                break;
            default:
                mTvNum.setBackgroundColor(0xff3c3a32);
                break;
        }
    }
}
