package com.imooc.game2048.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.imooc.game2048.R;
import com.imooc.game2048.config.Config;

public class ConfigPreference extends Activity implements OnClickListener {

    private Button btnGameLines;

    private Button btnGoal;

    private Button btnBack;

    private Button btnDone;

    private String[] gameLinesList;

    private String[] gameGoalList;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_preference);
        initView();
    }

    private void initView() {
        btnGameLines = (Button) findViewById(R.id.btn_gamelines);
        btnGoal = (Button) findViewById(R.id.btn_goal);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnDone = (Button) findViewById(R.id.btn_done);
        btnGameLines.setText("" + Config.sp.getInt(Config.KEY_GameLines, 4));
        btnGoal.setText("" + Config.sp.getInt(Config.KEY_GameGoal, 2048));
        btnGameLines.setOnClickListener(this);
        btnGoal.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        gameLinesList = new String[]{"4", "5", "6"};
        gameGoalList = new String[]{"1024", "2048", "4096"};
    }

    private void saveConfig() {
        Editor editor = Config.sp.edit();
        editor.putInt(Config.KEY_GameLines, Integer.parseInt(btnGameLines.getText().toString()));
        editor.putInt(Config.KEY_GameGoal, Integer.parseInt(btnGoal.getText().toString()));
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gamelines:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("choose the lines of the game");
                builder.setItems(gameLinesList, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnGameLines.setText(gameLinesList[which]);
                    }
                });
                builder.create().show();
                break;
            case R.id.btn_goal:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("choose the goal of the game");
                builder.setItems(gameGoalList, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnGoal.setText(gameGoalList[which]);
                    }
                });
                builder.create().show();
                break;
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_done:
                saveConfig();
                setResult(RESULT_OK);
                this.finish();
                break;
            default:
                break;
        }
    }
}
