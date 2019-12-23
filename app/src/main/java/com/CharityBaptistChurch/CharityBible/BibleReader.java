package com.CharityBaptistChurch.CharityBible;

import android.Manifest;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;

public class BibleReader {

    public static String m_strBibleVersion = "korhkjv";     // 성경 버전

    // 성경버전, 성경전서, 장수
    public ArrayList<String[]>  BibleParsing(String a_strBibleVersion, String a_strChapter, String a_strSection) {
        if (a_strBibleVersion == null)
            return null;

        a_strBibleVersion = m_strBibleVersion;
        //ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        String strSearchFile = a_strBibleVersion+a_strChapter+"_"+a_strSection+".lfb";
        String strVerse;
        String strTemp;

        String strBibleNo;
        String strBible;
        String strChapter;
        String strVerseNo;
        String strMediaFile;


        String strPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + a_strBibleVersion;

        // 성경 경로안의 파일목록 생성
        File file = new File(strPath);

        File files[] = file.listFiles();
//        File files[] = file.listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File pathname) {
//                return pathname.getName().toLowerCase(Locale.US).endsWith("lfb");
//            }
//        });

    //    if( files.length == 0 )
      //      return;

        ArrayList<String> arrayPath = new ArrayList<String>();
        String strPathFilter;
        for(int i = 0; files.length > i; i++)
        {
            strPathFilter = files[i].getPath();
            if(0 <= strPathFilter.toUpperCase().indexOf(strSearchFile.toUpperCase()))
            {
                arrayPath.add(strPathFilter);
                break;
            }
        }


        // 성경 '장' 의 경로를 얻어온다.
        String strPathPasing;

        // 파싱할때 자리수 관리하기 위한 변수.
        int nStart = 0;
        int nEnd = 0;

        ArrayList<String[]> arrayList = new ArrayList<String[]>();      // 성경 파싱한 정보들이 장 별로 축척된다.
        if (0 < arrayPath.size()) {
             // 성경 한장을 읽어들인다.
//            strPathPasing = "/storage/emulated/0/Download/korhkjv/korHKJV01_01.lfb";
         //   strPathPasing = files[i].getPath();
            strPathPasing = arrayPath.get(0);
            //Log.v("pmh AA path:",strPathPasing);

            File f = new File(strPathPasing);

            try {
                FileReader fr = new FileReader(f);
                BufferedReader bufReader = new BufferedReader(fr);

                String line = "";
                while ((line = bufReader.readLine()) != null) {
                    strBibleNo = null;
                    strBible = null;
                    strChapter = null;
                    strVerse = null;
                    strMediaFile = null;
                    nEnd = 0;
                    nStart = 0;

                    //  Log.v("BibleDownload BB line:",line);

                    // 파싱 진행

                    nEnd = line.indexOf(":");       // "01창 10:" 까지 구해와진다.
                    nEnd = line.indexOf(" ", nEnd);
                    strTemp = line.substring(nStart, nEnd); // "01창 10:1" 까지 구해와졌다.

                    // 1. 구절획득
                    nStart = nEnd + 1;  // +1 을 해줘야 절 index가 된다.
                    strVerse = line.substring(nStart, line.length());  // 구절을 얻어온다.

                    // 2. 성경과 번호 획득
                    nStart = 0;
                    nEnd = strTemp.indexOf(" ");    // "01창 "까지 구해와진다.
                    //   nBibleNo = Integer.parseInt(strTemp.substring(nStart, 2));   // 01
                    strBibleNo = strTemp.substring(nStart, 2);   // 01
                    strBible = strTemp.substring(2, nEnd);   // 창

                    // 3. 장 획득
                    nStart = nEnd + 1;
                    nEnd = strTemp.indexOf(":");
                    //   nChapter = Integer.parseInt(strTemp.substring(nStart ,nEnd-1)); // 10
                    strChapter = strTemp.substring(nStart, nEnd); // 10

                    if (Integer.parseInt(strChapter) < 10)
                        strChapter = "0" + strChapter;

                    // 4. 절 획득
                    nStart = nEnd + 1;
                    strVerseNo = strTemp.substring(nStart, strTemp.length()); // 1 을 구해온다.

                    if (Integer.parseInt(strVerseNo) < 10)
                        strVerseNo = "0" + strVerseNo;


                    // 5. 미디어파일 이름만들기 korhkjv01_01_01.mp3 ~ korhkjv66_22_21.mp3
                    strMediaFile = m_strBibleVersion + strBibleNo + "_" + strChapter + "_" + strVerseNo + ".mp3";

                    arrayList.add(new String[]{strBibleNo, strBible, strChapter, strVerseNo, strVerse, strMediaFile});
                }
                bufReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
        {
            return null;
        }
        return arrayList;
    }
}
