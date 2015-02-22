package com.imooc.game2048.config;

import android.app.Application;
import android.content.SharedPreferences;

public class Config extends Application {

    /**
     * SP对象
     */
    public static SharedPreferences sp;

    /**
     * Game Goal
     */
    public static int GameGoal;

    /**
     * GameView行列数
     */
    public static int GameLines;

    /**
     * Item宽高
     */
    public static int ItemSize;

    /**
     * 记录分数
     */
    public static int Scroe = 0;

    public static String SP_HighScore = "SP_HighScore";

    public static String KEY_HighScore = "KEY_HighScore";

    public static String KEY_GameLines = "KEY_GameLines";

    public static String KEY_GameGoal = "KEY_GameGoal";

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences(SP_HighScore, 0);
        GameLines = sp.getInt(KEY_GameLines, 4);
        GameGoal = sp.getInt(KEY_GameGoal, 2048);
        ItemSize = 0;
    }
}
