package com.ssthouse.audiotest.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.ssthouse.audiotest.player.MusicPlayer;
import com.ssthouse.audiotest.R;
import com.ssthouse.audiotest.recorder.MediaRecordHelper;

public class MainActivity extends AppCompatActivity {

    private boolean isRecording = false;

    /**
     * 是否播放音乐
     */
    private Switch swPlayMusic;

    /**
     * 录音按钮
     */
    private Button btnRecord;

    private MusicPlayer mediaController;

    private MediaRecordHelper audioRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        audioRecorder = new MediaRecordHelper(this);

        mediaController = new MusicPlayer(this);

        initView();

    }

    private void initView() {
        //
        btnRecord = (Button) findViewById(R.id.id_btn_record);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 2016/1/6 开启录音
                if (isRecording) {
                    //停止录音
                    audioRecorder.stop();
                    Toast.makeText(MainActivity.this, "录音文件生成成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "开始录音", Toast.LENGTH_SHORT).show();
                    audioRecorder.start();
                    isRecording = true;
                }
            }
        });

        //播放音乐开关
        swPlayMusic = (Switch) findViewById(R.id.id_sw_play_music);
        swPlayMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mediaController.startPlay();
                } else {
                    mediaController.pause();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaController.release();
    }
}
