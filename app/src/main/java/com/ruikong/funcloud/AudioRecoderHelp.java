package com.ruikong.funcloud;

/**
 * Created by ruikong on 2017/3/30.
 */

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.FileHandler;

import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AudioRecoderHelp {

    private MediaRecorder mediaRecorder;
    private File recordFile;
    private RecordPlayer player;
    private ArrayList<String> recList = new ArrayList<String>();


    public AudioRecoderHelp(Context context){
        player = new RecordPlayer(context);
    }

    private void startRecording() {

        mediaRecorder = new MediaRecorder();
        Date date = new Date();

        recordFile = new File("/mnt/sdcard", "rec-"+date.getTime()+".aac");

        if (recordFile.exists()) {
            recordFile.delete();
        }

        //设置参数
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(recordFile.getAbsolutePath());

        try {

            mediaRecorder.prepare();
            mediaRecorder.start();

        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (recordFile != null) {
            mediaRecorder.stop();
            mediaRecorder.release();

            recList.add(recordFile.getAbsolutePath());
        }
    }

    public ArrayList<String> getRecList() {
        return recList;
    }

    public void play(int index)
    {
        String file = this.recList.get(index);
        playRecording( new File(file) );
    }

    private void playRecording(File file) {
        player.playRecordFile(file);
    }

    private void pauseplayer() {
        player.pausePalyer();
    }

    private void stopplayer() {
        // TODO Auto-generated method stub
        player.stopPalyer();
    }

}
