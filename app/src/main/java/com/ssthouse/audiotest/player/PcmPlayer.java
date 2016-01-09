package com.ssthouse.audiotest.player;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ssthouse.audiotest.Constant;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import timber.log.Timber;

/**
 * pcm录制文件的播放器
 * Created by ssthouse on 2016/1/8.
 */
public class PcmPlayer {

    private Context context;

    private AudioTrack audioTrack;

    private boolean isPlaying = false;

    static final int frequency = 16000;
    static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    int recBufSize, playBufSize;


    /**
     * 构造方法
     *
     * @param context
     */
    public PcmPlayer(Context context) {
        this.context = context;
    }

    public void start() {
        if (!isPlaying) {
            Timber.e("开始播放");
            Toast.makeText(context, "开始播放", Toast.LENGTH_SHORT).show();
            new PlayTask().execute();
        }
    }

    public void stop() {
        if (isPlaying) {
            Timber.e("停止录音");
            Toast.makeText(context, "停止录音", Toast.LENGTH_SHORT).show();
            isPlaying = false;
        }
    }

    /**
     * 播放音乐task
     */
    class PlayTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            isPlaying = true;
            int bufferSize = AudioTrack.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
            short[] buffer = new short[bufferSize / 4];
            try {
                //定义输入流，将音频写入到AudioTrack类中，实现播放
                DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(Constant.filePath)));
                //实例AudioTrack
                audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency, channelConfiguration, audioEncoding, bufferSize, AudioTrack.MODE_STREAM);
                //开始播放
                audioTrack.play();
                //由于AudioTrack播放的是流，所以，我们需要一边播放一边读取
                while (isPlaying && dis.available() > 0) {
                    int i = 0;
                    while (dis.available() > 0 && i < buffer.length) {
                        buffer[i] = dis.readShort();
                        i++;
                    }
                    //然后将数据写入到AudioTrack中
                    audioTrack.write(buffer, 0, buffer.length);

                }
                //播放结束
                audioTrack.stop();
                dis.close();
            } catch (Exception e) {
                Timber.e("something is wrong");
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            Toast.makeText(context, "播放暂停", Toast.LENGTH_SHORT).show();
            isPlaying = false;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

}
