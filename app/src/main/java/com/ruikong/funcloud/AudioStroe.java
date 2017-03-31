package com.ruikong.funcloud;
import android.content.Context;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import xiaofei.library.datastorage.DataStorageFactory;
import xiaofei.library.datastorage.IDataStorage;

/**
 * Created by ruikong on 2017/3/30.
 */

public class AudioStroe {
    ArrayList<AudioRec> mAudioRecList = new ArrayList<AudioRec>();
    IDataStorage mDataStorage;
    static AudioStroe audioInstance = null;

    private AudioStroe(){}

    public void setContext(Context app){
        initStorage(app);
    }

    private void initStorage(Context app){
        mDataStorage = DataStorageFactory.getInstance(
                app,
                DataStorageFactory.TYPE_DATABASE);

        syncDatas();
    }

    static public AudioStroe getInstance(){
        if (audioInstance==null){
            audioInstance = new AudioStroe();
        }
        return audioInstance;
    }

    public void syncDatas(){
        ArrayList<AudioRec> list = (ArrayList<AudioRec>) mDataStorage.loadAll(AudioRec.class, new Comparator<AudioRec>() {
            @Override
            public int compare(AudioRec audioRec, AudioRec t1) {
                return audioRec.createTime>t1.createTime?1:0;
            }
        });
        mAudioRecList.clear();
        mAudioRecList.addAll(list);
    }

    public void persistentData(){
        mDataStorage.storeOrUpdate(mAudioRecList);
    }

    public void add(String filepath, long second){
        AudioRec audio = new AudioRec();
        audio.setFilePath( filepath );
        audio.setAudioSecond( second );
        audio.setIsUpload( false );

        mAudioRecList.add(audio);

        mDataStorage.storeOrUpdate(audio);
    }

    public boolean del(int index){
        return del(mAudioRecList.get(index));
    }

    public boolean del(AudioRec rec){

        boolean ret = false;

        do {
            File file = new File( rec.getFilePath() );
            file.delete();

            mAudioRecList.remove(rec);

            mDataStorage.delete(AudioRec.class, rec.mId);

            ret = true;
        }while (false);

        return ret;

    }


    public ArrayList<AudioRec> getmAudioRecList() {
        return mAudioRecList;
    }
}
