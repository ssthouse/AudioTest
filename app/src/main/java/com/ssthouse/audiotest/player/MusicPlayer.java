package com.ssthouse.audiotest.player;

import android.content.Context;
import android.media.MediaPlayer;

import com.ssthouse.audiotest.R;
import com.ssthouse.audiotest.util.SDCardUtil;

import timber.log.Timber;

/**
 * 声音控制器
 * Created by ssthouse on 2016/1/7.
 */
public class MusicPlayer {

    private Context context;

    /**
     * 测试文件路径
     */
    private static final String filePath = SDCardUtil.getSDCardPath() + "新文件夹/speech2_8k_16bit_2ch.wav";

    /**
     * 声音控制器
     */
    private MediaPlayer mediaPlayer;

    /**
     * 构造方法
     *
     * @param context
     */
    public MusicPlayer(Context context) {
        this.context = context;

        //            File file = new File(filePath);
//            if(!file.exists()){
//                Timber.e("文件不存在!!!");
//            }

        //初始化
        mediaPlayer = MediaPlayer.create(context, R.raw.test);
//            mediaPlayer.setLooping(true);
//            mediaPlayer.prepare();

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
    }

    public void startPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            Timber.e("music is playing");
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            Timber.e("music pause");
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
