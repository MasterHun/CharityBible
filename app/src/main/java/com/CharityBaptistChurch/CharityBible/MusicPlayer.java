package com.CharityBaptistChurch.CharityBible;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MusicPlayer {

    private MediaPlayer m_mp;
    private ArrayList<String> m_arrayMusic;     // 음악을 리스트로 가지고있음
    private int m_nIndex;                       // 현재 재생중인 인덱스음악
    private String m_strMusicPath;                // 음악의 절대경로
    private boolean bIsPlaying = false;
    private int m_nLength;                      // 일시정시 눌렀을경우에 현재 위치 저장


    public MusicPlayer(String a_strPath) {

        File f = new File(a_strPath);

        if (!f.exists()) {
            boolean bRet = f.mkdirs();
        }

        m_mp = new MediaPlayer();
        m_nIndex = 0;
        m_strMusicPath = a_strPath;
    }

    @Override
    public void finalize() {

        if (m_mp != null)
            m_mp.release();

        try {
            super.finalize();
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
    }

//
//    public int initMusic() {
//
//
//        if (m_strMusicPath.isEmpty())
//            return -1;
//        File f = new File(m_strMusicPath);
//
//        if (!f.exists()) {
//            // 폴더가 없을경우에는 리턴 -1
//            f.mkdirs();
//            return -1;
//        }
//
//        File[] files = f.listFiles();
//
//        m_arrayMusic = new ArrayList<String>();
//
//        m_arrayMusic.clear();
//        for (int i = 0; i < files.length; i++) {
//            if (!files[i].isHidden() && files[i].isFile())
//                m_arrayMusic.add(files[i].getName());
//        }
//
//        return 1;
//    }

    public void initMusic(String strFolder, String strBibleContent ,int nBibleChapter)
    {
        try {

            //String strPath = m_strMusicPath + File.separator + "korNIVSound" + "korNIV01.scbk";
            String strPath = m_strMusicPath + File.separator + strFolder+"Sound" +File.separator+ strFolder+strBibleContent +".scbk";

            bIsPlaying = true;
            File filesounds = new File(strPath);

            ZipFile zip = new ZipFile(filesounds);

            //String strFilename = "1.mp3";
            String strFilename = nBibleChapter + ".mp3";

            ZipEntry entry = zip.getEntry(strFilename);

            if (entry != null) {
                InputStream in = zip.getInputStream(entry);
                // see Note #3.
                File tempFile = File.createTempFile("_AUDIO_", ".wav");
                FileOutputStream out = new FileOutputStream(tempFile);
                IOUtils.copy(in, out);

                File f = tempFile;
                try {
                    if (f.exists()) {
//                                MediaPlayer mp = new MediaPlayer();
                        FileInputStream fis = new FileInputStream(f);
                        m_mp.setDataSource(fis.getFD());

                        m_mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp2) {
                                mp2.release();

                            };
                        });
                        // }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onPlay() {
        if (m_mp != null) {

            if( bIsPlaying )
            {
                m_mp.start();
                m_mp.seekTo(m_nLength);
            }

            if (!bIsPlaying) {                 // 재생
                try {
                    bIsPlaying = true;

//                    //strPath = m_strMusicPath + File.separator+"korNIV01.scbk";
//                    String strPath = m_strMusicPath + File.separator + "korNIV01.scbk";
//
//                    File filesounds = new File(strPath);
//
//                    ZipFile zip = new ZipFile(filesounds);
//
//                    String strFilename = "1.mp3";
//                    ZipEntry entry = zip.getEntry(strFilename);
//
//                    if (entry != null) {
//                        InputStream in = zip.getInputStream(entry);
//                        // see Note #3.
//                        File tempFile = File.createTempFile("_AUDIO_", ".wav");
//                        FileOutputStream out = new FileOutputStream(tempFile);
//                        IOUtils.copy(in, out);

  //                      File f = tempFile;
                        try {
//                            if (f.exists()) {
////                                MediaPlayer mp = new MediaPlayer();
//                                FileInputStream fis = new FileInputStream(f);
//                                m_mp.setDataSource(fis.getFD());
//
//                                //  if (mp.isPlaying() == false) {
                                m_mp.prepare();
                                //mp.setLooping(false);
                                m_mp.start();
                                // mp.stop();
                                //mp.release();

                                m_mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    public void onCompletion(MediaPlayer mp2) {
                                        mp2.release();

                                    }

                                    ;
                                });
//                                // }
//                            } else {
//
//                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return true;
    }

//    public boolean onPlay() {
//        if (m_mp != null) {
//
//            if( bIsPlaying )
//            {
//                m_mp.start();
//                m_mp.seekTo(m_nLength);
//            }
//
//            if (!bIsPlaying) {                 // 재생
//                try {
//                    bIsPlaying = true;
//
//                    //strPath = m_strMusicPath + File.separator+"korNIV01.scbk";
//                    String strPath = m_strMusicPath + File.separator + "korNIV01.scbk";
//
//                    File filesounds = new File(strPath);
//
//                    ZipFile zip = new ZipFile(filesounds);
//
//                    String strFilename = "1.mp3";
//                    ZipEntry entry = zip.getEntry(strFilename);
//
//                    if (entry != null) {
//                        InputStream in = zip.getInputStream(entry);
//                        // see Note #3.
//                        File tempFile = File.createTempFile("_AUDIO_", ".wav");
//                        FileOutputStream out = new FileOutputStream(tempFile);
//                        IOUtils.copy(in, out);
//
//                        File f = tempFile;
//                        try {
//                            if (f.exists()) {
////                                MediaPlayer mp = new MediaPlayer();
//                                FileInputStream fis = new FileInputStream(f);
//                                m_mp.setDataSource(fis.getFD());
//
//                                //  if (mp.isPlaying() == false) {
//                                m_mp.prepare();
//                                //mp.setLooping(false);
//                                m_mp.start();
//                                // mp.stop();
//                                //mp.release();
//
//                                m_mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                                    public void onCompletion(MediaPlayer mp2) {
//                                        mp2.release();
//
//                                    }
//
//                                    ;
//                                });
//                                // }
//                            } else {
//
//                            }
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//        return true;
//    }

    public void onStop() {
        if (m_mp != null) {

            if (m_mp.isPlaying()) {
                m_mp.stop();

            } else {
                Log.d("MusicPlayer", "Music is not playing, can not Stop");
            }
        }

    }

    public void onPause() {
        if(m_mp != null)
        {
            if (m_mp.isPlaying())    // 일시정지
            {
                Log.d("MusicPlayer", "music is playing!!!");
                m_mp.pause();
                m_nLength = m_mp.getCurrentPosition();
            }

        }
    }

    public boolean onPreview() {
        if (m_mp != null) {
            try {
                m_mp.stop();

                m_nIndex++;            // 다음곡으로 인덱스 증가
                if (m_nIndex > m_arrayMusic.size())
                    m_nIndex = 0;      // 마지막이라면 처음 곡으로 돌아오도록

                m_mp.reset();
                m_mp.setDataSource(m_arrayMusic.get(m_nIndex));
                m_mp.prepare();
                m_mp.start();

            } catch (IOException io) {
                io.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean onPrevious() {
        if (m_mp != null) {
            try {
                if(m_mp.isPlaying()) {
                    m_mp.stop();

                    m_nIndex--;
                    if (m_nIndex < 0)
                        m_nIndex = m_arrayMusic.size() - 1;

                    m_mp.reset();
                    m_mp.setDataSource(m_arrayMusic.get(m_nIndex));
                    m_mp.prepare();
                    m_mp.start();
                }
            } catch (IOException io) {
                io.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void clearMediaPlayer() {

        if (m_mp != null) {
            m_mp.stop();
            m_mp.release();
            m_mp = null;
        }
    }

    public void setSeekto(int nPosition)
    {
        if(m_mp != null && nPosition > -1)
        {
            m_mp.seekTo(nPosition);
        }
    }

    public int getDuration(){
        if(m_mp != null )
        {
            return m_mp.getDuration();
        }
        return -1;
    }

    public boolean IsPlaying()
    {
        if(m_mp != null)
        {
            return m_mp.isPlaying();
        }
        return false;
    }

    public int getCurrentPosition()
    {
        if(m_mp != null)
        {
            return m_mp.getCurrentPosition();
        }
        return -1;
    }

}
