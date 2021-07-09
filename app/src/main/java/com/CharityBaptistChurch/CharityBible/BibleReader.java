package com.CharityBaptistChurch.CharityBible;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class BibleReader {

    public static String m_strBibleVersionA = "korHKJV";     // 성경 버전
    public static String m_strBibleVersionB = "engNKJV";        // 성경버전

    public BibleReader()
    {
        m_strBibleVersionA = "korHKJV";
        m_strBibleVersionB = "engNKJV";
    }

    public BibleReader(String a_strBibleVersionA)
    {
        m_strBibleVersionA = a_strBibleVersionA;
    }
    public BibleReader(String a_strBibleVersionA, String a_strBibleVersionB)
    {
        m_strBibleVersionA = a_strBibleVersionA;
        m_strBibleVersionB = a_strBibleVersionB;
    }

    // 성경버전, 성경전서, 장수
    //a_strBiblePath 은 풀패스로 넘어온다.
    public ArrayList<String[]>  BibleParsingText(String a_strBiblePath, String a_strBibleVersion,  String a_strContentsIndex, String a_strChapter)
    {
        if (a_strBibleVersion == null)
            return null;

        m_strBibleVersionA = a_strBibleVersion;
        //ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        String strSearchFile = a_strBibleVersion+a_strContentsIndex+"_"+a_strChapter+".lfb";
        String strVerse;
        String strTemp;

        String strBibleNo;
        String strBible;
        String strChapter;
        String strVerseNo;
        String strMediaFile;


        //String strPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + a_strBibleVersion;
        String strPath = a_strBiblePath + a_strBibleVersion;

        // 성경 경로안의 파일목록 생성
        File file = new File(strPath);

        if(!file.isDirectory())
            return null;


        File files[] = file.listFiles();

        // 찾고자 하는 장의 경로 얻어오기
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
                    strMediaFile = m_strBibleVersionA + strBibleNo + "_" + strChapter + "_" + strVerseNo + ".mp3";

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

    // 성경 두개 역본 보기위한 함수
    public ArrayList<String[]>  BibleParsingText(String a_strBiblePath, String a_strBibleVersionA, String a_strBibleVersionB, String a_strContentsIndex, String a_strChapter)
    {
        if (a_strBibleVersionA == null || a_strBibleVersionB == null)
            return null;


         m_strBibleVersionA = a_strBibleVersionA;
         m_strBibleVersionB = a_strBibleVersionB;

        //ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        String strSearchFileA = a_strBibleVersionA+a_strContentsIndex+"_"+a_strChapter+".lfb";
        String strSearchFileB = a_strBibleVersionB+a_strContentsIndex+"_"+a_strChapter+".lfb";

        String strVerseA, strVerseB;            // 구절
        String strTempA, strTempB;              //
        String strBibleNoA, strBibleNoB;        //
        String strBibleA, strBibleB;            //
        String strChapterA, strChapterB;        // 장 번호
        String strVerseNoA, strVerseNoB;        // 절 번호
        String strMediaFileA, strMediaFileB;    // mp3

    //    String strPathA = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + a_strBibleVersionA;
    //    String strPathB = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + a_strBibleVersionB;

        String strPathA = a_strBiblePath + a_strBibleVersionA;
        String strPathB = a_strBiblePath + a_strBibleVersionB;

        // 성경 경로안의 파일목록 생성
        File fileA = new File(strPathA);

        if(!fileA.isDirectory())
            return null;


        File filesA[] = fileA.listFiles();

        // 찾고자 하는 장의 경로 얻어오기
        ArrayList<String> arrayPathA = new ArrayList<String>();
        String strPathFilterA;
        for(int i = 0; filesA.length > i; i++)
        {
            strPathFilterA = filesA[i].getPath();
            if(0 <= strPathFilterA.toUpperCase().indexOf(strSearchFileA.toUpperCase()))
            {
                arrayPathA.add(strPathFilterA);
                break;
            }
        }

        // 성경 경로안의 파일목록 생성
        File fileB = new File(strPathB);

        if(!fileB.isDirectory())
            return null;

        File filesB[] = fileB.listFiles();

        // 찾고자 하는 장의 경로 얻어오기
        ArrayList<String> arrayPathB = new ArrayList<String>();
        String strPathFilterB;
        for(int i = 0; filesB.length > i; i++)
        {
            strPathFilterB = filesB[i].getPath();
            if(0 <= strPathFilterB.toUpperCase().indexOf(strSearchFileB.toUpperCase()))
            {
                arrayPathB.add(strPathFilterB);
                break;
            }
        }


        // 성경 '장' 의 경로를 얻어온다.
        String strPathPasingA;
        String strPathPasingB;

        // 파싱할때 자리수 관리하기 위한 변수.
        int nStart = 0;
        int nEnd = 0;

        ArrayList<String[]> arrayList = new ArrayList<>();      // 성경 파싱한 정보들이 장 별로 축척된다.
        if (0 < arrayPathA.size() || 0 < arrayPathB.size()) {
            // 성경 한장을 읽어들인다.
//            strPathPasing = "/storage/emulated/0/Download/korhkjv/korHKJV01_01.lfb";
            //   strPathPasing = files[i].getPath();
            strPathPasingA = arrayPathA.get(0);
            strPathPasingB = arrayPathB.get(0);

            File fA = new File(strPathPasingA);
            File fB = new File(strPathPasingB);

            try {
                FileReader frA = new FileReader(fA);
                FileReader frB = new FileReader(fB);
                BufferedReader bufReaderA = new BufferedReader(frA);
                BufferedReader bufReaderB = new BufferedReader(frB);

                String lineA = "";
                String lineB = "";

                // 비교한 값을 저장할 데이터
                String strVerse1 = "";
                String strVerse2 = "";
                String strVerseNumber1 = "";
                String strVerseNumber2 = "";


                while ( true ) {

                    lineA = bufReaderA.readLine();
                    lineB = bufReaderB.readLine();

                    Boolean nFlagA = false;
                    Boolean nFlagB = false;

                    //  Log.v("BibleDownload BB line:",line);

                    // 파싱 진행


                    // 첫번째 성경 읽기
                    if(lineA != null) {

                        nEnd = 0;
                        nStart = 0;

                        strBibleNoA = null;
                        strBibleA = null;
                        strChapterA = null;
                        strVerseA = null;
                        strMediaFileA = null;

                        nEnd = lineA.indexOf(":");       // "01창 10:" 까지 구해와진다.
                        nEnd = lineA.indexOf(" ", nEnd);
                        strTempA = lineA.substring(nStart, nEnd); // "01창 10:1" 까지 구해와졌다.

                        // 1. 구절획득
                        nStart = nEnd + 1;  // +1 을 해줘야 절 index가 된다.
                        strVerseA = lineA.substring(nStart, lineA.length());  // 구절을 얻어온다.

                        // 2. 성경과 번호 획득
                        nStart = 0;
                        nEnd = strTempA.indexOf(" ");    // "01창 "까지 구해와진다.
                        //   nBibleNo = Integer.parseInt(strTemp.substring(nStart, 2));   // 01
                        strBibleNoA = strTempA.substring(nStart, 2);   // 01
                        strBibleA = strTempA.substring(2, nEnd);   // 창

                        // 3. 장 획득
                        nStart = nEnd + 1;
                        nEnd = strTempA.indexOf(":");
                        //   nChapter = Integer.parseInt(strTemp.substring(nStart ,nEnd-1)); // 10
                        strChapterA = strTempA.substring(nStart, nEnd); // 10

                        if (Integer.parseInt(strChapterA) < 10)
                            strChapterA = "0" + strChapterA;

                        // 4. 절 획득
                        nStart = nEnd + 1;
                        strVerseNoA = strTempA.substring(nStart, strTempA.length()); // 1 을 구해온다.

                        if (Integer.parseInt(strVerseNoA) < 10)
                            strVerseNoA = "0" + strVerseNoA;


                        // 5. 미디어파일 이름만들기 korhkjv01_01_01.mp3 ~ korhkjv66_22_21.mp3
                        strMediaFileA = m_strBibleVersionA + strBibleNoA + "_" + strChapterA + "_" + strVerseNoA + ".mp3";

                        arrayList.add(new String[]{strBibleNoA, strBibleA, strChapterA, strVerseNoA, strVerseA, strMediaFileA});
                    }else
                    {
                        nFlagA = true;
                    }

                    // 두번째 성경 읽기
                    if(lineB != null) {
                        nEnd = 0;
                        nStart = 0;

                        strBibleNoB = null;
                        strBibleB = null;
                        strChapterB = null;
                        strVerseB = null;
                        strMediaFileB = null;

                        nEnd = lineB.indexOf(":");       // "01창 10:" 까지 구해와진다.
                        nEnd = lineB.indexOf(" ", nEnd);
                        strTempB = lineB.substring(nStart, nEnd); // "01창 10:1" 까지 구해와졌다.

                        // 1. 구절획득
                        nStart = nEnd + 1;  // +1 을 해줘야 절 index가 된다.
                        strVerseB = lineB.substring(nStart, lineB.length());  // 구절을 얻어온다.

                        // 2. 성경과 번호 획득
                        nStart = 0;
                        nEnd = strTempB.indexOf(" ");    // "01창 "까지 구해와진다.
                        //   nBibleNo = Integer.parseInt(strTemp.substring(nStart, 2));   // 01
                        strBibleNoB = strTempB.substring(nStart, 2);   // 01
                        strBibleB = strTempB.substring(2, nEnd);   // 창

                        // 3. 장 획득
                        nStart = nEnd + 1;
                        nEnd = strTempB.indexOf(":");
                        //   nChapter = Integer.parseInt(strTemp.substring(nStart ,nEnd-1)); // 10
                        strChapterB = strTempB.substring(nStart, nEnd); // 10

                        if (Integer.parseInt(strChapterB) < 10)
                            strChapterB = "0" + strChapterB;

                        // 4. 절 획득
                        nStart = nEnd + 1;
                        strVerseNoB = strTempB.substring(nStart, strTempB.length()); // 1 을 구해온다.

                        if (Integer.parseInt(strVerseNoB) < 10)
                            strVerseNoB = "0" + strVerseNoB;


                        // 5. 미디어파일 이름만들기 korhkjv01_01_01.mp3 ~ korhkjv66_22_21.mp3
                        strMediaFileB = m_strBibleVersionA + strBibleNoB + "_" + strChapterB + "_" + strVerseNoB + ".mp3";

                        arrayList.add(new String[]{strBibleNoB, strBibleB, strChapterB, strVerseNoB, strVerseB, strMediaFileB});
                    }else
                    {
                        nFlagB = true;
                    }


                    if( nFlagA && nFlagB)
                        break;

                    // 둘 중 한개는 더이상 읽을 부분이 없음
                    if(nFlagA|| nFlagB )
                    {
                        if(strVerse1.length() > 0 && strVerseNumber1.length() > 0)
                        {
                            arrayList.add(new String[]{"없음", "없음", "없음", strVerseNumber1, strVerse1, "없음"});
                        }else if(strVerse2.length() > 0 && strVerseNumber2.length() > 0)
                        {
                            arrayList.add(new String[]{"없음", "없음", "없음", strVerseNumber2, strVerse2, "없음"});
                        }

                    }

                }
                bufReaderA.close();
                bufReaderB.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
        {
            return null;
        }
        return arrayList;
    }


    /*
     * @ Func    : BibleParsing()
     * @ Param   : a_strBiblePath        성경 파일 경로
     *             a_strBibleVersion     성경 버전정보    ex) korhkjv
     *             a_strContensIndex     성경 장 번호     ex) 1
     *             a_strChapter          성경
     * @ Since   : 2020.04.22
     * @ Last    : 2020.04.24
     * @ Author  : mhpark
     * @ Context : 성경데이터 압축파일에서 읽어와서 처리, 한장만 읽는 함수
     */
    public ArrayList<String[]> BibleParsing(String a_strBiblePath, String a_strBibleVersion, String a_strContentsIndex, String a_strChapter) {
        if (a_strBibleVersion == null)
            return null;

        // 성경 종류 저장
        m_strBibleVersionA = a_strBibleVersion;


        String strSearchFile = a_strBibleVersion + a_strContentsIndex + "_" + a_strChapter+".lfb";      // 해당 경로에서 검색하고자하는 파일명   EX) korHKJV01_01.lfb

        String strPath = a_strBiblePath + a_strBibleVersion + ".cbk";    // 성경파일이 들어있는 절대경로

        // 1. 경로에있는 압축파일 읽어들인다.
        File zipfile = new File(strPath);

        ArrayList<String[]> arrayList = new ArrayList<>();   // 파싱한 성경데이터 저장한 리스트

        try{

            // 2. 압축된 성경파일 읽기위해서 ZipFile 라이브러리 사용
            ZipFile zip = new ZipFile(zipfile, ZipFile.OPEN_READ);

            // 2-1. zip 파일안에 있는 목록의 파일에 접근.
            ZipEntry entry = zip.getEntry(strSearchFile);

            if(entry == null)
                return null;

            // 2-2. 선택된 목록의 파일을 읽어온다.
            InputStream input = zip.getInputStream(entry);

            // 2-3. inputStream 으로부터의 데이터를 스캔한다.
            Scanner sc = new Scanner(input);

            // 3. 얻어온 데이터를 한라인씩 읽어온다.
            while (sc.hasNext()) {

                // 3-1. 한라인 얻어옴
                String line = sc.nextLine();


                String strBibleNo;      // *성경의 번호      EX) 출애굽기일 경우에 성경의 두번째에 위치하기 때문에 -> 02
                String strBible;        // *성경의 이름      EX) 창세기 일경우에 -> 창
                String strChapter;      // *성경의 장수      EX) 9장일때 -> 09
                String strVerse;        // *성경의 구절      EX) 하나님이 세상을 이처럼...
                String strVerseNo;      // *성경구절의 위치   EX) 10절일경우 -> 10
                String strMediaFile;
                String strTemp;         // 데이터를 담아가면서 사용할 더미


                // 파싱할때 자리수 관리하기 위한 변수.
                int nStart = 0;
                int nEnd;


                // 성경데이터 파싱 Start
                nEnd = line.indexOf(":");       // "01창 10:" 까지 구해와진다.
                nEnd = line.indexOf(" ", nEnd);
                strTemp = line.substring(nStart, nEnd); // "01창 10:1" 까지 구해와졌다.

                // 3-2. 구절획득
                nStart = nEnd + 1;  // +1 을 해줘야 절 index가 된다.
                strVerse = line.substring(nStart);  // 구절을 얻어온다.

                // 3-3. 성경과 번호 획득
                nStart = 0;
                nEnd = strTemp.indexOf(" ");    // "01창 "까지 구해와진다.
                //   nBibleNo = Integer.parseInt(strTemp.substring(nStart, 2));   // 01
                strBibleNo = strTemp.substring(nStart, 2);   // 01
                strBible = strTemp.substring(2, nEnd);   // 창

                // 3-4. 장 획득
                nStart = nEnd + 1;
                nEnd = strTemp.indexOf(":");
                //   nChapter = Integer.parseInt(strTemp.substring(nStart ,nEnd-1)); // 10
                strChapter = strTemp.substring(nStart, nEnd); // 10

                if (Integer.parseInt(strChapter) < 10)
                    strChapter = "0" + strChapter;

                // 3-5. 절 획득
                nStart = nEnd + 1;
                strVerseNo = strTemp.substring(nStart); // 1 을 구해온다.

                if (Integer.parseInt(strVerseNo) < 10)
                    strVerseNo = "0" + strVerseNo;


                // 3-6. 미디어파일 이름만들기 korhkjv01_01_01.mp3 ~ korhkjv66_22_21.mp3
                strMediaFile = m_strBibleVersionA + strBibleNo + "_" + strChapter + "_" + strVerseNo + ".mp3";

                arrayList.add(new String[]{strBibleNo, strBible, strChapter, strVerseNo, strVerse, strMediaFile});
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    /*
     * @ Func    : BibleParsing()
     * @ Param   : a_strBiblePath        성경 파일 경로
     *             a_strBibleVersion     성경 버전정보    ex) korhkjv
     *             a_strContensIndex     성경 장 번호     ex) 1
     *             a_strChapter          성경
     * @ Since   : 2020.04.22
     * @ Last    : 2020.04.24
     * @ Author  : mhpark
     * @ Context : 성경데이터 압축파일에서 읽어와서 처리, 역본읽는 함수
     */
    public ArrayList<String[]>  BibleParsing(String a_strBiblePath, String a_strBibleVersionA, String a_strBibleVersionB, String a_strContentsIndex, String a_strChapter) {

        if (a_strBibleVersionA == null || a_strBibleVersionB == null)
            return null;

        // 성경 종류 저장
        m_strBibleVersionA = a_strBibleVersionA;
        m_strBibleVersionB = a_strBibleVersionB;

        String strSearchFileA = a_strBibleVersionA+a_strContentsIndex+"_"+a_strChapter+".lfb";
        String strSearchFileB = a_strBibleVersionB+a_strContentsIndex+"_"+a_strChapter+".lfb";

        String strPathA = a_strBiblePath + a_strBibleVersionA+".cbk";
        String strPathB = a_strBiblePath + a_strBibleVersionB+".cbk";

        // 1. 경로에있는 압축파일 읽어들인다.
        File zipfileA = new File(strPathA);
        File zipfileB = new File(strPathB);

        if( !(zipfileA.isFile() && zipfileB.isFile()) )
        {
            Log.d("BibleParshing 1","File Not Access");
            return null;
        }

        ArrayList<String[]> arrayList = new ArrayList<>();   // 파싱한 성경데이터 저장한 리스트

        try{

            // 2. 압축된 성경파일 읽기위해서 ZipFile 라이브러리 사용
            ZipFile zipA = new ZipFile(zipfileA, ZipFile.OPEN_READ);
            ZipFile zipB = new ZipFile(zipfileB, ZipFile.OPEN_READ);

            // 2-1. zip 파일안에 있는 목록의 파일에 접근.
            ZipEntry entryA = zipA.getEntry(strSearchFileA);
            ZipEntry entryB = zipB.getEntry(strSearchFileB);

            // 2-2. 선택된 목록의 파일을 읽어온다.
            InputStream inputA = zipA.getInputStream(entryA);
            InputStream inputB = zipB.getInputStream(entryB);

            // 2-3. inputStream 으로부터의 데이터를 스캔한다.
            Scanner scA = new Scanner(inputA);
            Scanner scB = new Scanner(inputB);

            // 3. 얻어온 데이터를 한라인씩 읽어온다.
            while ( scA.hasNext() && scB.hasNext() ) {

                // 3-1. 한라인 얻어옴
                String lineA = scA.nextLine();
                String lineB = scB.nextLine();

                String strBibleNoA, strBibleNoB;      // *성경의 번호      EX) 출애굽기일 경우에 성경의 두번째에 위치하기 때문에 -> 02
                String strBibleA, strBibleB;          // *성경의 이름      EX) 창세기 일경우에 -> 창
                String strChapterA, strChapterB;      // *성경의 장수      EX) 9장일때 -> 09
                String strVerseA, strVerseB;          // *성경의 구절      EX) 하나님이 세상을 이처럼...
                String strVerseNoA = "0", strVerseNoB = "0";      // *성경구절의 위치   EX) 10절일경우 -> 10
                String strMediaFileA, strMediaFileB;  // 데이터를 담아가면서 사용할 더미
                String strTempA, strTempB;


                // 파싱할때 자리수 관리하기 위한 변수.
                int nStart;
                int nEnd;

                Boolean nFlagA = false;
                Boolean nFlagB = false;

                // 파싱 진행

                // 첫번째 성경 읽기
                if(lineA != null) {
                    nStart = 0;


                    nEnd = lineA.indexOf(":");       // "01창 10:" 까지 구해와진다.
                    nEnd = lineA.indexOf(" ", nEnd);
                    strTempA = lineA.substring(nStart, nEnd); // "01창 10:1" 까지 구해와졌다.

                    // 1. 구절획득
                    nStart = nEnd + 1;  // +1 을 해줘야 절 index가 된다.
                    strVerseA = lineA.substring(nStart);  // 구절을 얻어온다.

                    // 2. 성경과 번호 획득
                    nStart = 0;
                    nEnd = strTempA.indexOf(" ");    // "01창 "까지 구해와진다.
                    //   nBibleNo = Integer.parseInt(strTemp.substring(nStart, 2));   // 01
                    strBibleNoA = strTempA.substring(nStart, 2);   // 01
                    strBibleA = strTempA.substring(2, nEnd);   // 창

                    // 3. 장 획득
                    nStart = nEnd + 1;
                    nEnd = strTempA.indexOf(":");
                    //   nChapter = Integer.parseInt(strTemp.substring(nStart ,nEnd-1)); // 10
                    strChapterA = strTempA.substring(nStart, nEnd); // 10

                    if (Integer.parseInt(strChapterA) < 10)
                        strChapterA = "0" + strChapterA;

                    // 4. 절 획득
                    nStart = nEnd + 1;
                    strVerseNoA = strTempA.substring(nStart); // 1 을 구해온다.

                    if (Integer.parseInt(strVerseNoA) < 10)
                        strVerseNoA = "0" + strVerseNoA;


                    // 5. 미디어파일 이름만들기 korhkjv01_01_01.mp3 ~ korhkjv66_22_21.mp3
                    strMediaFileA = m_strBibleVersionA + strBibleNoA + "_" + strChapterA + "_" + strVerseNoA + ".mp3";

                    arrayList.add(new String[]{strBibleNoA, strBibleA, strChapterA, strVerseNoA, strVerseA, strMediaFileA});
                }else
                {
                    nFlagA = true;
                }

                // 두번째 성경 읽기
                if(lineB != null) {
                    nStart = 0;

                    nEnd = lineB.indexOf(":");       // "01창 10:" 까지 구해와진다.
                    nEnd = lineB.indexOf(" ", nEnd);
                    strTempB = lineB.substring(nStart, nEnd); // "01창 10:1" 까지 구해와졌다.

                    // 1. 구절획득
                    nStart = nEnd + 1;  // +1 을 해줘야 절 index가 된다.
                    strVerseB = lineB.substring(nStart);  // 구절을 얻어온다.

                    // 2. 성경과 번호 획득
                    nStart = 0;
                    nEnd = strTempB.indexOf(" ");    // "01창 "까지 구해와진다.
                    //   nBibleNo = Integer.parseInt(strTemp.substring(nStart, 2));   // 01
                    strBibleNoB = strTempB.substring(nStart, 2);   // 01
                    strBibleB = strTempB.substring(2, nEnd);   // 창

                    // 3. 장 획득
                    nStart = nEnd + 1;
                    nEnd = strTempB.indexOf(":");
                    //   nChapter = Integer.parseInt(strTemp.substring(nStart ,nEnd-1)); // 10
                    strChapterB = strTempB.substring(nStart, nEnd); // 10

                    if (Integer.parseInt(strChapterB) < 10)
                        strChapterB = "0" + strChapterB;

                    // 4. 절 획득
                    nStart = nEnd + 1;
                    strVerseNoB = strTempB.substring(nStart); // 1 을 구해온다.

                    if (Integer.parseInt(strVerseNoB) < 10)
                        strVerseNoB = "0" + strVerseNoB;


                    // 5. 미디어파일 이름만들기 korhkjv01_01_01.mp3 ~ korhkjv66_22_21.mp3
                    strMediaFileB = m_strBibleVersionA + strBibleNoB + "_" + strChapterB + "_" + strVerseNoB + ".mp3";

                    arrayList.add(new String[]{strBibleNoB, strBibleB, strChapterB, strVerseNoB, strVerseB, strMediaFileB});
                }else
                {
                    nFlagB = true;
                }


                if( nFlagA && nFlagB)
                    break;

                // 둘 중 한개는 더이상 읽을 부분이 없음
                if(nFlagA)
                    arrayList.add(new String[]{"없음", "없음", "없음", strVerseNoB, "(없음)", "없음"});
                else if(nFlagB)
                    arrayList.add(new String[]{"없음", "없음", "없음", strVerseNoA, "(없음)", "없음"});

            }

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return arrayList;

    }


    /*
     * @ Func    : BibleJsonParsing()
     * @ Param   : a_strBiblePath        성경 파일 경로
     *             a_strBibleVersion     성경 버전정보    ex) korhkjv
     *             a_strContensIndex     성경 장 번호     ex) 1
     *             a_strChapter          성경
     * @ Since   : 2021.06.29
     * @ Last    : 2021.04.24
     * @ Author  : mhpark
     * @ Context : assets에 있는 성경 json파일 읽어와서 처리, 역본읽는 함수
     */
    public ArrayList<String[]>  BibleJsonParsing(String a_strBiblePath, String a_strBibleVersionA, String a_strBibleVersionB, String a_strContentsIndex, String a_strChapter)
    {

        if (a_strBibleVersionA == null || a_strBibleVersionB == null)
            return null;

        // 성경 종류 저장
        m_strBibleVersionA = a_strBibleVersionA;
        m_strBibleVersionB = a_strBibleVersionB;

        String strSearchFileA = a_strBibleVersionA+a_strContentsIndex+"_"+a_strChapter+".lfb";
        String strSearchFileB = a_strBibleVersionB+a_strContentsIndex+"_"+a_strChapter+".lfb";

        String strPathA = a_strBiblePath + a_strBibleVersionA+".cbk";
        String strPathB = a_strBiblePath + a_strBibleVersionB+".cbk";

        // 1. 경로에있는 압축파일 읽어들인다.
        File zipfileA = new File(strPathA);
        File zipfileB = new File(strPathB);

        if( !(zipfileA.isFile() && zipfileB.isFile()) )
        {
            Log.d("BibleParshing 1","File Not Access");
            return null;
        }

        ArrayList<String[]> arrayList = new ArrayList<>();   // 파싱한 성경데이터 저장한 리스트

       // arrayList.add(new String[]{strBibleNoB, strBibleB, strChapterB, strVerseNoB, strVerseB, strMediaFileB});


        return null;
    }


}