package com.ssthouse.audiotest.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.ssthouse.audiotest.R;
import com.ssthouse.audiotest.player.MusicPlayer;
import com.ssthouse.audiotest.player.PcmPlayer;
import com.ssthouse.audiotest.recorder.PcmRecorder;
import com.ssthouse.audiotest.util.SDCardUtil;

import java.io.File;

/**
 * Created by ssthouse on 2016/1/8.
 */
public class PcmActivity extends AppCompatActivity {

    /**
     * 是否播放音乐
     */
    private Switch swPlayMusic;

    private Switch swPlayPcm;

    private Switch swAecEnable;

    private ArrayAdapter<String> arrayAdapter;

    /**
     * 录音按钮
     */
    private Button btnRecord;

    private PcmRecorder pcmRecorder;

    private PcmPlayer pcmPlayer;

    private MusicPlayer musicPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pcmRecorder = new PcmRecorder(this);
        pcmPlayer = new PcmPlayer(this);

        musicPlayer = new MusicPlayer(this);

        initView();
    }

    private void initView() {
        //
        btnRecord = (Button) findViewById(R.id.id_btn_record);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 2016/1/6 开启录音
                if (pcmRecorder.isRecording()) {
                    pcmRecorder.stop();
                } else {
                    pcmRecorder.start();
                }
            }
        });

        //播放录制好的pcm数据开关
        swPlayMusic = (Switch) findViewById(R.id.id_sw_play_music);
        swPlayMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    musicPlayer.startPlay();
                } else {
                    musicPlayer.pause();
                }
            }
        });

        swPlayPcm = (Switch) findViewById(R.id.id_sw_play_pcm);
        swPlayPcm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    pcmPlayer.start();
                }else{
                    pcmPlayer.stop();
                }
            }
        });

        swAecEnable = (Switch) findViewById(R.id.id_sw_aec_enable);
        swAecEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pcmRecorder.setAecEnable(true);
                }else{
                    pcmRecorder.setAecEnable(false);
                }
            }
        });

        //初始化文件列表
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.linear_item,  R.id.id_tv);
        new File(SDCardUtil.getSDCardPath()+"");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicPlayer.release();
        pcmRecorder.release();
    }
}
