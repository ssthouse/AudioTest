package com.ssthouse.audiotest;

import android.content.Context;
import android.media.MediaPlayer;

import com.ssthouse.audiotest.util.SDCardUtil;

import java.io.IOException;

import timber.log.Timber;

/**
 * 声音控制器
 * Created by ssthouse on 2016/1/7.
 */
public class MediaController {

    private Context context;

    /**
     * 测试文件路径
     */
    private static final String filePath = SDCardUtil.getSDCardPath() + "虚拟现实/speech2_8k_16bit_2ch.wav";

    /**
     * 声音控制器
     */
    private MediaPlayer mediaPlayer;

    /**
     * 构造方法
     *
     * @param context
     */
    public MediaController(Context context) {
        this.context = context;

        try {
            //初始化
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();

            //设置监听器
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Timber.e("complete");
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Timber.e("error");
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
