package com.ruikong.funcloud;

import com.squareup.okhttp.MediaType;

import java.io.IOException;
import java.util.Date;

import xiaofei.library.datastorage.annotation.ClassId;
import xiaofei.library.datastorage.annotation.ObjectId;

@ClassId("AudioRec")
public class AudioRec {

    @ObjectId
    public String mId;
    public long createTime;

    private String filePath;
    private long audioSecond;
    private boolean isUpload;

    public AudioRec(){
        createTime = new Date().getTime();
        mId = ""+hashCode()+createTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getAudioSecond() {
        return audioSecond;
    }

    public void setAudioSecond(long audioSecond) {
        this.audioSecond = audioSecond;
    }

    public boolean getIsUpload(){
        return isUpload;
    }

    public void setIsUpload(boolean isupload){
        this.isUpload = isupload;
    }


    public String getFileName(){
        String[] ss = filePath.split("/");

        return ss[ ss.length-1 ];
    }

    public String getTime(){
        int c = 0;
        int m = 0;
        int s = 0;

        if ( audioSecond>=3600 ){
            c = (int)Math.floor(audioSecond/3600);
            audioSecond-=c*3600;
        }

        if ( audioSecond>60 ){
            m = (int)Math.floor(audioSecond/60);
            audioSecond-=m*60;
        }

        s = (int)audioSecond;

        String time = "";
        if (c>0){
            time+=c+"小时";
        }
        if (m>0){
            time+=m+"分钟";
        }
        if (s>0){
            time+=s+"秒";
        }


        return time;
    }
}
