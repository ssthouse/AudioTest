package com.ssthouse.audiotest.player;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.ssthouse.audiotest.util.SDCardUtil;

import java.io.FileInputStream;

import timber.log.Timber;

/**
 * 背景声音播放器(需要消除的声音)
 * Created by ssthouse on 2016/1/7.
 */
public class MusicPlayer {

    /**
     * 测试文件路径
     */
    public static  final String file = SDCardUtil.getSDCardPath()+"虚拟现实/speech2_8k_16bit_2ch.wav";

    private final static String tag = "22";
    static byte[] buffer = null;
    AudioTrack at = null;
    int pcmlen = 0;

    private Context context;

    public MusicPlayer(Context context) {
        this.context = context;

        initAudioTracker();
    }

    private void initAudioTracker() {
        try {
            FileInputStream fis = new FileInputStream(file);
            buffer = new byte[1024 * 1024 * 2];//2M
            int len = fis.read(buffer);
            Timber.e("fis len=" + len);
            Timber.e("0:" + (char) buffer[0]);
            pcmlen = 0;
            pcmlen += buffer[0x2b];
            pcmlen = pcmlen * 256 + buffer[0x2a];
            pcmlen = pcmlen * 256 + buffer[0x29];
            pcmlen = pcmlen * 256 + buffer[0x28];

            int channel = buffer[0x17];
            channel = channel * 256 + buffer[0x16];

            int bits = buffer[0x23];
            bits = bits * 256 + buffer[0x22];
            Timber.e("pcmlen=" + pcmlen + ",channel=" + channel + ",bits=" + bits);
            at = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                    channel,
                    AudioFormat.ENCODING_PCM_16BIT,
                    pcmlen,
                    AudioTrack.MODE_STATIC);
            at.write(buffer, 0x2C, pcmlen);
            Timber.e("write 1...");
            Timber.e("play 1...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放
     */
    public void play(){
        if(at == null){
            return;
        }
        at.play();
    }

}
