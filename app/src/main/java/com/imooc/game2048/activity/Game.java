package com.imooc.game2048.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.imooc.game2048.R;
import com.imooc.game2048.anim.AnimationLayer;
import com.imooc.game2048.config.Config;
import com.imooc.game2048.view.GameView;

public class Game extends Activity implements OnClickListener {

    // Activity的引用
    private static Game mGame;
    // 记录分数
    private TextView tvScore;
    // 历史记录分数
    private TextView tvHighScore;
    private int highScore;
    // 目标分数
    private TextView tvGoal;
    private int goal;
    // 重新开始按钮
    private Button btnRestart;
    // 撤销按钮
    private Button btnRevert;
    // 选项按钮
    private Button btnOptions;
    // 游戏面板
    private GameView gameView;
    // 动画层
    private AnimationLayer animLayer;

    public Game() {
        mGame = this;
    }

    /**
     * 获取当前Activity的引用
     *
     * @return
     */
    public static Game getGameActivity() {
        return mGame;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化View
        initView();
        animLayer = new AnimationLayer(this);
        gameView = new GameView(this);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fff);
        frameLayout.addView(animLayer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        frameLayout.addView(gameView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 获取当前动画层引用
     *
     * @return
     */
    public AnimationLayer getAnimationLayer() {
        return animLayer;
    }

    /**
     * 初始化View
     */
    private void initView() {
        tvScore = (TextView) findViewById(R.id.scroe);
        tvGoal = (TextView) findViewById(R.id.tv_Goal);
        tvHighScore = (TextView) findViewById(R.id.record);
        btnRestart = (Button) findViewById(R.id.btn_restart);
        btnRevert = (Button) findViewById(R.id.btn_revert);
        btnOptions = (Button) findViewById(R.id.btn_option);
        btnRestart.setOnClickListener(this);
        btnRevert.setOnClickListener(this);
        btnOptions.setOnClickListener(this);
        highScore = Config.sp.getInt(Config.KEY_HighScore, 0);
        goal = Config.sp.getInt(Config.KEY_GameGoal, 2048);
        tvHighScore.setText("" + highScore);
        tvGoal.setText("" + goal);
        tvScore.setText("0");
        setScore(0, 0);
    }

    /**
     * 修改得分
     *
     * @param score
     * @param flag  0 : score 1 : high score
     */
    public void setScore(int score, int flag) {
        switch (flag) {
            case 0:
                tvScore.setText("" + score);
                break;
            case 1:
                tvHighScore.setText("" + score);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_restart:
                gameView.startGame();
                setScore(0, 0);
                break;
            case R.id.btn_revert:
                gameView.revertGame();
                break;
            case R.id.btn_option:
                Intent intent = new Intent(Game.this, ConfigPreference.class);
                startActivityForResult(intent, 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            goal = Config.sp.getInt(Config.KEY_GameGoal, 2048);
            tvGoal.setText("" + goal);
            getHighScore();
            gameView.startGame();
        }
    }

    /**
     * 获取最高记录
     */
    private void getHighScore() {
        int score = Config.sp.getInt(Config.KEY_HighScore, 0);
        setScore(score, 1);
    }
}
