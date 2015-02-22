package com.imooc.game2048.view;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.Toast;

import com.imooc.game2048.activity.Game;
import com.imooc.game2048.bean.GameItem;
import com.imooc.game2048.config.Config;

import java.util.ArrayList;
import java.util.List;

public class GameView extends GridLayout implements OnTouchListener {

    // GameView对应矩阵
    private GameItem[][] gameMatrix;
    // 空格List
    private List<Point> blanks;
    // 矩阵行列数
    private int gameLines;
    // 记录坐标
    private int startX, startY, endX, endY;
    // 辅助数组
    private List<Integer> calList;
    private int keyItemNum = -1;
    // 历史记录数组
    private int[][] gameMatrixHistory;
    // 历史记录分数
    private int scoreHistory;
    // 最高记录
    private int highScore;

    public GameView(Context context) {
        super(context);
        initGameMatrix();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameMatrix();
    }

    public void startGame() {
        initGameMatrix();
        initGameView(Config.ItemSize);
    }

    private void initGameView(int cardSize) {
        removeAllViews();
        GameItem card;
        for (int i = 0; i < gameLines; i++) {
            for (int j = 0; j < gameLines; j++) {
                card = new GameItem(getContext(), 0);
                addView(card, cardSize, cardSize);
                // 初始化GameMatrix全部为0 空格List为所有
                gameMatrix[i][j] = card;
                blanks.add(new Point(i, j));
            }
        }
        // 添加随机数字
        addRandomNum();
        addRandomNum();
    }

    /**
     * 撤销上次移动
     */
    public void revertGame() {
        if (gameMatrixHistory.length != 0) {
            Game.getGameActivity().setScore(scoreHistory, 0);
            Config.Scroe = scoreHistory;
            for (int i = 0; i < gameLines; i++) {
                for (int j = 0; j < gameLines; j++) {
                    gameMatrix[i][j].setNum(gameMatrixHistory[i][j]);
                }
            }
        }
    }

    /**
     * 添加随机数字
     */
    private void addRandomNum() {
        getBlanks();
        if (blanks.size() > 0) {
            int randomNum = (int) (Math.random() * blanks.size());
            Point randomPoint = blanks.get(randomNum);
            gameMatrix[randomPoint.x][randomPoint.y].setNum(Math.random() > 0.2d ? 2 : 4);
            Game.getGameActivity().getAnimationLayer().animcreate(gameMatrix[randomPoint.x][randomPoint.y]);
        }
    }

    /**
     * 获取空格Item数组
     */
    private void getBlanks() {
        blanks.clear();
        for (int i = 0; i < gameLines; i++) {
            for (int j = 0; j < gameLines; j++) {
                if (gameMatrix[i][j].getNum() == 0) {
                    blanks.add(new Point(i, j));
                }
            }
        }
    }

    /**
     * 初始化View
     */
    private void initGameMatrix() {
        // 初始化矩阵
        removeAllViews();
        scoreHistory = 0;
        Config.Scroe = 0;
        Config.GameLines = Config.sp.getInt(Config.KEY_GameLines, 4);
        gameLines = Config.GameLines;
        gameMatrix = new GameItem[gameLines][gameLines];
        gameMatrixHistory = new int[gameLines][gameLines];
        calList = new ArrayList<Integer>();
        blanks = new ArrayList<Point>();
        highScore = Config.sp.getInt(Config.KEY_HighScore, 0);
        setColumnCount(gameLines);
        setRowCount(gameLines);
        setOnTouchListener(this);
        // 初始化View参数
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        Config.ItemSize = metrics.widthPixels / Config.GameLines;
        initGameView(Config.ItemSize);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                saveHistoryMatrix();
                startX = (int) event.getX();
                startY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                endX = (int) event.getX();
                endY = (int) event.getY();
                judgeDirection(endX - startX, endY - startY);
                if (isMoved()) {
                    addRandomNum();
                    // 修改显示分数
                    Game.getGameActivity().setScore(Config.Scroe, 0);
                }
                int result = checkCompleted();
                if (result == 0) {
                    if (Config.Scroe > highScore) {
                        Editor editor = Config.sp.edit();
                        editor.putInt(Config.KEY_HighScore, Config.Scroe);
                        editor.commit();
                    }
                    Toast.makeText(getContext(), "lose", Toast.LENGTH_LONG).show();
                    Config.Scroe = 0;
                } else if (result == 2) {
                    Toast.makeText(getContext(), "success", Toast.LENGTH_LONG).show();
                    Config.Scroe = 0;
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 保存历史记录
     */
    private void saveHistoryMatrix() {
        scoreHistory = Config.Scroe;
        for (int i = 0; i < gameLines; i++) {
            for (int j = 0; j < gameLines; j++) {
                gameMatrixHistory[i][j] = gameMatrix[i][j].getNum();
            }
        }
    }

    /**
     * 根据偏移量判断移动方向
     *
     * @param offsetX
     * @param offsetY
     */
    private void judgeDirection(int offsetX, int offsetY) {
        if (Math.abs(offsetX) > Math.abs(offsetY)) {
            if (offsetX > 10) {
                swipeRight();
            } else {
                swipeLeft();
            }
        } else {
            if (offsetY > 10) {
                swipeDown();
            } else {
                swipeUp();
            }
        }
    }

    /**
     * 判断是否结束
     *
     * @return 0:结束 1:正常 2:成功
     */
    private int checkCompleted() {
        getBlanks();
        if (blanks.size() == 0) {
            for (int i = 0; i < gameLines; i++) {
                for (int j = 0; j < gameLines; j++) {
                    if (j < gameLines - 1) {
                        if (gameMatrix[i][j].getNum() == gameMatrix[i][j + 1].getNum()) {
                            return 1;
                        }
                    }
                    if (i < gameLines - 1) {
                        if (gameMatrix[i][j].getNum() == gameMatrix[i + 1][j].getNum()) {
                            return 1;
                        }
                    }
                }
            }
            return 0;
        }
        for (int i = 0; i < gameLines; i++) {
            for (int j = 0; j < gameLines; j++) {
                if (gameMatrix[i][j].getNum() == 2048) {
                    return 2;
                }
            }
        }
        return 1;
    }

    /**
     * 判断是否移动过(是否需要新增Item)
     *
     * @return
     */
    private boolean isMoved() {
        for (int i = 0; i < gameLines; i++) {
            for (int j = 0; j < gameLines; j++) {
                if (gameMatrixHistory[i][j] != gameMatrix[i][j].getNum()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 滑动事件：上
     */
    private void swipeUp() {
        for (int i = 0; i < gameLines; i++) {
            for (int j = 0; j < gameLines; j++) {
                int currentNum = gameMatrix[j][i].getNum();
                if (currentNum != 0) {
                    if (keyItemNum == -1) {
                        keyItemNum = currentNum;
                    } else {
                        if (keyItemNum == currentNum) {
                            calList.add(keyItemNum * 2);
                            Config.Scroe += keyItemNum * 2;
                            keyItemNum = -1;
                        } else {
                            calList.add(keyItemNum);
                            keyItemNum = currentNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (keyItemNum != -1) {
                calList.add(keyItemNum);
            }
            // 改变Item值
            for (int j = 0; j < calList.size(); j++) {
                gameMatrix[j][i].setNum(calList.get(j));
            }
            for (int m = calList.size(); m < gameLines; m++) {
                gameMatrix[m][i].setNum(0);
            }
            // 重置行参数
            keyItemNum = -1;
            calList.clear();
        }
    }

    /**
     * 滑动事件：下
     */
    private void swipeDown() {
        for (int i = gameLines - 1; i >= 0; i--) {
            for (int j = gameLines - 1; j >= 0; j--) {
                int currentNum = gameMatrix[j][i].getNum();
                if (currentNum != 0) {
                    if (keyItemNum == -1) {
                        keyItemNum = currentNum;
                    } else {
                        if (keyItemNum == currentNum) {
                            calList.add(keyItemNum * 2);
                            Config.Scroe += keyItemNum * 2;
                            keyItemNum = -1;
                        } else {
                            calList.add(keyItemNum);
                            keyItemNum = currentNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (keyItemNum != -1) {
                calList.add(keyItemNum);
            }
            // 改变Item值
            for (int j = 0; j < gameLines - calList.size(); j++) {
                gameMatrix[j][i].setNum(0);
            }
            int index = calList.size() - 1;
            for (int m = gameLines - calList.size(); m < gameLines; m++) {
                gameMatrix[m][i].setNum(calList.get(index));
                index--;
            }
            // 重置行参数
            keyItemNum = -1;
            calList.clear();
            index = 0;
        }
    }

    /**
     * 滑动事件：左
     */
    private void swipeLeft() {
        for (int i = 0; i < gameLines; i++) {
            for (int j = 0; j < gameLines; j++) {
                int currentNum = gameMatrix[i][j].getNum();
                if (currentNum != 0) {
                    if (keyItemNum == -1) {
                        keyItemNum = currentNum;
                    } else {
                        if (keyItemNum == currentNum) {
                            calList.add(keyItemNum * 2);
                            Config.Scroe += keyItemNum * 2;
                            keyItemNum = -1;
                        } else {
                            calList.add(keyItemNum);
                            keyItemNum = currentNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (keyItemNum != -1) {
                calList.add(keyItemNum);
            }
            // 改变Item值
            for (int j = 0; j < calList.size(); j++) {
                gameMatrix[i][j].setNum(calList.get(j));
            }
            for (int m = calList.size(); m < gameLines; m++) {
                gameMatrix[i][m].setNum(0);
            }
            // 重置行参数
            keyItemNum = -1;
            calList.clear();
        }
    }

    /**
     * 滑动事件：右
     */
    private void swipeRight() {
        for (int i = gameLines - 1; i >= 0; i--) {
            for (int j = gameLines - 1; j >= 0; j--) {
                int currentNum = gameMatrix[i][j].getNum();
                if (currentNum != 0) {
                    if (keyItemNum == -1) {
                        keyItemNum = currentNum;
                    } else {
                        if (keyItemNum == currentNum) {
                            calList.add(keyItemNum * 2);
                            Config.Scroe += keyItemNum * 2;
                            keyItemNum = -1;
                        } else {
                            calList.add(keyItemNum);
                            keyItemNum = currentNum;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (keyItemNum != -1) {
                calList.add(keyItemNum);
            }
            // 改变Item值
            for (int j = 0; j < gameLines - calList.size(); j++) {
                gameMatrix[i][j].setNum(0);
            }
            int index = calList.size() - 1;
            for (int m = gameLines - calList.size(); m < gameLines; m++) {
                gameMatrix[i][m].setNum(calList.get(index));
                index--;
            }
            // 重置行参数
            keyItemNum = -1;
            calList.clear();
            index = 0;
        }
    }
}
