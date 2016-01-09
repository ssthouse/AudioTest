package com.ssthouse.audiotest.recorder;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.NoiseSuppressor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ssthouse.audiotest.Constant;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import timber.log.Timber;

/**
 * pcm的录音器
 * Created by ssthouse on 2016/1/8.
 */
public class PcmRecorder {

    private boolean isRecording = false;

    private Context context;

    private AudioRecord audioRecord;

    /**
     * 回应消除
     */
    private AcousticEchoCanceler aec;
    /**
     * 降噪器
     */
    private NoiseSuppressor noiseSuppressor;

    int recBufSize, playBufSize;


    private boolean isAecEnable = false;

    public PcmRecorder(Context context) {
        this.context = context;
    }

    /**
     * 初始化recorder
     */
    private void initRecorder() {
        int frequency = 16000;
        int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        recBufSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, frequency,
                AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, recBufSize);
        Timber.e("audioRecorder ID:\t" + audioRecord.getAudioSessionId());
    }

    /**
     * 初始化回应消除
     */
    private void initAec() {
        if (!isAecEnable) {
            Timber.e("不进行回声消除");
            return;
        }
        Timber.e("开启回声消除");
//        Timber.e("正在运行..");
//        if (AcousticEchoCanceler.isAvailable()) {
//            Timber.e("回应消除可用");
//        } else {
//            Timber.e("回应消除不能用..............");
//        }
        //初始化回应消除
        int sessionId = audioRecord.getAudioSessionId();
//        aec = AcousticEchoCanceler.create(sessionId);
//        aec.setControlStatusListener(new AudioEffect.OnControlStatusChangeListener() {
//            @Override
//            public void onControlStatusChange(AudioEffect effect, boolean controlGranted) {
//                Timber.e("has Control:\t" + controlGranted);
//            }
//        });
//        aec.setEnableStatusListener(new AudioEffect.OnEnableStatusChangeListener() {
//            @Override
//            public void onEnableStatusChange(AudioEffect effect, boolean enabled) {
//                Timber.e("aec enable:\t" + enabled);
//            }
//        });
//        aec.setEnabled(true);
//        Timber.e("set aec enable:\t" + aec.getEnabled());

        //初始化降噪
        noiseSuppressor = NoiseSuppressor.create(sessionId);
        noiseSuppressor.setControlStatusListener(new AudioEffect.OnControlStatusChangeListener() {
            @Override
            public void onControlStatusChange(AudioEffect effect, boolean controlGranted) {
                Timber.e("has Control:\t" + controlGranted);
            }
        });
        noiseSuppressor.setEnableStatusListener(new AudioEffect.OnEnableStatusChangeListener() {
            @Override
            public void onEnableStatusChange(AudioEffect effect, boolean enabled) {
                Timber.e("aec enable:\t" + enabled);
            }
        });
        noiseSuppressor.setEnabled(true);
        Timber.e("set noise delete enable:\t" + noiseSuppressor.getEnabled());
        Timber.e("降噪支持:\t" + NoiseSuppressor.isAvailable());
    }

    /**
     * 开始录音
     */
    public void start() {
        if (!isRecording) {

            //初始化recorder
            initRecorder();

            Timber.e("开始录音");
            Toast.makeText(context, "开始录音", Toast.LENGTH_SHORT).show();
            new RecordTask().execute();
        }
    }

    /**
     * 停止录音
     */
    public void stop() {
        if (isRecording) {
            Timber.e("结束录音");
            Toast.makeText(context, "结束录音", Toast.LENGTH_SHORT).show();
            isRecording = false;
        }
    }

    class RecordTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            Timber.e("开始doInBackground");
            isRecording = true;
            try {
                //开通输出流到指定的文件
                File audioFile = new File(Constant.filePath);
                DataOutputStream dos = new DataOutputStream(
                        new BufferedOutputStream(new FileOutputStream(audioFile)));
                //定义缓冲
                short[] buffer = new short[recBufSize];

                //// TODO: 2016/1/8
                initAec();

                //开始录制
                audioRecord.startRecording();

                //定义循环，根据isRecording的值来判断是否继续录制
                while (isRecording) {
                    //从bufferSize中读取字节，返回读取的short个数
                    //这里老是出现buffer overflow，不知道是什么原因，试了好几个值，都没用，TODO：待解决
                    int bufferReadResult = audioRecord.read(buffer, 0, buffer.length);
                    //循环将buffer中的音频数据写入到OutputStream中
                    for (int i = 0; i < bufferReadResult; i++) {
                        dos.writeShort(buffer[i]);
                    }
                }
                //录制结束
                audioRecord.stop();
                Timber.e("The DOS available:\t" + audioFile.length());
                dos.close();
            } catch (Exception e) {
                Timber.e("something is wrong");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Timber.e("录音结束");
        }
    }

    public void release() {
        if (aec != null) {
            aec.release();
        }
    }

    public boolean isRecording() {
        return isRecording;
    }


    public boolean isAecEnable() {
        return isAecEnable;
    }

    public void setAecEnable(boolean aecEnable) {
        isAecEnable = aecEnable;
    }
}
