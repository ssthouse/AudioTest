package com.ssthouse.audiotest.recorder;

import android.content.Context;

import com.ssthouse.audiotest.util.SDCardUtil;

import java.io.IOException;

/**
 * 录音控制类
 * Created by ssthouse on 2016/1/7.
 */
public class MediaRecordHelper {

    private Context context;

    /**
     * 录音器
     */
    private android.media.MediaRecorder recorder;

    /**
     * 构造方法
     *
     * @param context
     */
    public MediaRecordHelper(Context context) {
        this.context = context;
    }


    /**
     * 启动
     */
    public void start() {
        recorder = new android.media.MediaRecorder();// new出MediaRecorder对象
        recorder.setAudioSource(android.media.MediaRecorder.AudioSource.MIC);
        // 设置MediaRecorder的音频源为麦克风
        recorder.setOutputFormat(android.media.MediaRecorder.OutputFormat.AMR_NB);
        // 设置MediaRecorder录制的音频格式
        recorder.setAudioEncoder(android.media.MediaRecorder.AudioEncoder.AMR_NB);
        // 设置MediaRecorder录制音频的编码为amr.
        recorder.setOutputFile(SDCardUtil.getSDCardPath()+"audioTest.wav");
        // 设置录制好的音频文件保存路径
        try {
            recorder.prepare();// 准备录制
            recorder.start();// 开始录制
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 终止
     */
    public void stop(){
        recorder.stop();// 停止刻录
        // recorder.reset(); // 重新启动MediaRecorder.
        recorder.release(); // 刻录完成一定要释放资源
    }
}