package com.ruikong.funcloud;

/**
 * Created by ruikong on 2017/3/30.
 */

import java.io.File;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class RecordPlayer {

    private static MediaPlayer mediaPlayer;
    private Context mcontext;

    public RecordPlayer(Context context) {
        this.mcontext = context;
    }

    public void playRecordFile(File file) {
        if (file.exists() && file != null) {
            if (mediaPlayer == null) {
                Uri uri = Uri.fromFile(file);
                mediaPlayer = MediaPlayer.create(mcontext, uri);
            }
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer paramMediaPlayer) {
                    // TODO Auto-generated method stub
                    Toast.makeText(mcontext,
                            "确定",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void pausePalyer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();

        }

    }

    public void stopPalyer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
    }
}
