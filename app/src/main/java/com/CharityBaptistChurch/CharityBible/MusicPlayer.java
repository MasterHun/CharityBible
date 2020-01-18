package com.CharityBaptistChurch.CharityBible;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer {

    MediaPlayer m_mp;
    ArrayList<String> m_arrayMusic;     // 음악을 리스트로 가지고있음
    int m_nIndex;                       // 현재 재생중인 음악
    String m_strMusicPath;                // 음악의 절대경로

    SeekBar m_sk;


    public MusicPlayer(String a_strPath) {
        m_mp = new MediaPlayer();
        m_nIndex = 0;
        m_strMusicPath = a_strPath;
    }

    @Override
    public void finalize() {

        if(m_mp != null)
            m_mp.release();

        try {
            super.finalize();
        }catch (Throwable tr)
        {
            tr.printStackTrace();
        }
    }


    public int initMusic() {


        if(m_strMusicPath.isEmpty())
            return -1;
        File f = new File(m_strMusicPath);

        if (!f.exists()) {
            // 폴더가 없을경우에는 리턴 -1
            f.mkdirs();
            return -1;
        }

        File[] files = f.listFiles();

        m_arrayMusic = new ArrayList<String>();

        m_arrayMusic.clear();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isHidden() && files[i].isFile())
                m_arrayMusic.add(files[i].getName());
        }

//        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,arrayMusic);
//
//        lv.setAdapter(adapter);

        return 1;
    }

    public boolean onPlay()
    {
        if(m_mp.isPlaying())    // 일시정지
        {
            Log.i("MusicPlayer","music is playing!!!");
            m_mp.pause();

        }else {                 // 재생
            String strPath = m_strMusicPath + "/" + m_arrayMusic.get(m_nIndex);     // 재생할 파일의 경로 생성

            try {
                m_mp.reset();
                m_mp.setDataSource(strPath);
                m_mp.prepare();
                m_mp.start();
            } catch (IOException io) {
                io.printStackTrace();
            }

        }
        return true;
    }

    public boolean onStop()
    {
        if(m_mp.isPlaying())
        {
            m_mp.stop();

        }else{
            Log.i("MusicPlayer","Music is not playing, can not Stop");
        }
        return true;
    }

    public boolean onPreview()
    {
        if(m_mp != null)
        {
            try {
                m_mp.stop();

                m_nIndex++;            // 다음곡으로 인덱스 증가
                if(m_nIndex > m_arrayMusic.size())
                    m_nIndex = 0;      // 마지막이라면 처음 곡으로 돌아오도록

                m_mp.reset();
                m_mp.setDataSource(m_arrayMusic.get(m_nIndex));
                m_mp.prepare();
                m_mp.start();

            }catch (IOException io)
            {
                io.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean onPrevious()
    {
        if(m_mp != null)
        {
            try {
                m_mp.stop();

                m_nIndex--;
                if(m_nIndex < 0 )
                    m_nIndex = m_arrayMusic.size() - 1;

                m_mp.reset();
                m_mp.setDataSource(m_arrayMusic.get(m_nIndex));
                m_mp.prepare();
                m_mp.start();

            }catch (IOException io)
            {
                io.printStackTrace();
                return false;
            }
        }
        return true;
    }


}
