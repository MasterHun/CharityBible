package com.CharityBaptistChurch.CharityBible;

import android.app.ProgressDialog;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//파일 다운로드 버튼 클릭
//        btn_pdfDownload = (Button)findViewById(R.id.btn_pdfDownload);
//        btn_pdfDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                File dir = new File(Save_Path);
//
//                // 폴더가 존재하지 않을 경우 폴더를 만듬
//                if (!dir.exists()) {
//                    dir.mkdir();
//                }
//
//                // 다운로드 폴더에 동일한 파일명이 존재하는지 확인해서 없으면 다운받고 있으면 해당 파일 실행시킴.
//                if (new File(Save_Path + "/" + File_Name).exists() == false) {
//                    progress = ProgressDialog.show(Siteinfo_detail.this, "","파일 다운로드중..");
//                    dThread = new DownloadThread(fileURL, Save_Path + "/" + File_Name);
//                    dThread.start();
//                } else {
//                    showDownloadFile();
//                }
//            }
//        });

public class FileDownload {
    String File_Name;
    String File_type;

    String fileURL; // URL
    String Save_Path;
    String Save_folder;

    ProgressDialog progress;
    DownloadThread dThread;

    public FileDownload() {
        File_Name = "kornkrv.cbk";
        File_type = "cbk";

        fileURL = "175.198.115.173:8000/test/kornkrv"; // URL
        Save_folder = "/charitybible/bible";

        // 다운로드 경로를 외장메모리 사용자 지정 폴더로 함.
        String ext = Environment.getExternalStorageState();

        if(ext.equals(Environment.DIRECTORY_DOWNLOADS)) {
            Save_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + Save_folder;
        }

    }


   public FileDownload(String a_strFileName, String a_strFileType, String a_strUrl) {
        File_Name = a_strFileName;
        File_type = a_strFileType;

        fileURL = a_strUrl; // URL
       if(Save_folder.isEmpty())
            Save_folder = "/charitybible/bible";

        // 다운로드 경로를 외장메모리 사용자 지정 폴더로 함.
        String ext = Environment.getExternalStorageState();

        if(ext.equals(Environment.DIRECTORY_DOWNLOADS)) {
            Save_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + Save_folder;
        }

    }

    // 다운로드 쓰레드로 돌림..
    public class DownloadThread extends Thread {
        String ServerUrl;
        String LocalPath;

        public DownloadThread(String serverPath, String localPath) {
            ServerUrl = serverPath;
            LocalPath = localPath;
          //  progress = ProgressDialog.show();
        }

        @Override
        public void run() {
            URL imgurl;
            int Read;

            try {

                imgurl = new URL(ServerUrl);
                HttpURLConnection conn = (HttpURLConnection) imgurl.openConnection();
                conn.connect();
                int len = conn.getContentLength();

                byte[] tmpByte = new byte[len];

                InputStream is = conn.getInputStream();
                File file = new File(LocalPath);
                FileOutputStream fos = new FileOutputStream(file);

                for (;;) {
                    Read = is.read(tmpByte);

                    if (Read <= 0) {
                        break;
                    }
                    fos.write(tmpByte, 0, Read);
                }

                is.close();
                fos.close();
                conn.disconnect();

            } catch (MalformedURLException e) {
                Log.e("ERROR1", e.getMessage());
            } catch (IOException e) {
                Log.e("ERROR2", e.getMessage());
                e.printStackTrace();
            }
            mAfterDown.sendEmptyMessage(0);
        }
    }

    Handler mAfterDown = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        //    progress.dismiss();

            // 파일 다운로드 종료 후 다운받은 파일을 실행시킨다.
         //   showDownloadFile();
        }
    };
}
