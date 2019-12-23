package com.CharityBaptistChurch.CharityBible;

import java.util.ArrayList;
import java.util.HashMap;

public class Util {
    // 성경 [장] 수 분류
    public static final int[] numOfChapters ={
            50,40,27,36,34,24,21,4,31,24,   // 10
            22,25,29,36,10,13,10,42,150,31, // 10
            12,8,66,52,5,48,12,14,3,9,      // 10
            1,4,7,3,3,3,2,14,4,             // 9
            28,16,24,21,28,16,16,13,6,6,    // 10
            4,4,5,3,6,4,3,1,13,5,           // 10
            5,3,5,1,1,1,22};                // 15

    // 성경 종류 ( 5개만 지원되도록 할것 )
    public static final String[] strBibleBooksName ={
            "korHKJV",      // 킹제임스 흠정역성경
            "korHKJVM",     // 킹제임스 흠정역성경 마제스터판
            "engKJV",       // 영어 킹제임스 흠정역성경
            "korHRV",       // 개역 한글
            "korKTV" };     // 바른성경  (한국성경공회)

    public static final String[] strBibleDBName ={
            "BIBLE_korHKJV",      // 킹제임스 흠정역성경
            "BIBLE_korHKJVM",     // 킹제임스 흠정역성경 마제스터판
            "BIBLE_engKJV",       // 영어 킹제임스 흠정역성경
            "BIBLE_korHRV",       // 개역 한글
            "BIBLE_korKTV" };     // 바른성경  (한국성경공회)

    // 현재 어플리케이션에서 사용된 메인 성경 버전 ex) korHKJV, korHKJVM, engKJV, korHRV, korKTV
    public static String strBibleBookVersion = "korhkjv";

    // 성경 인덱스
    public static int nBookBibleVersion;

    // 현재 어플리케이션에서 보고있는 성경
    public static String strBibleName;
    public static String nBibleName;

    // 현재 어플리케이션에서 보고있는 성경 장
    public static String strBibleChapter;

    // 현재 어플리케이션에서 보고있는 성경 절
    public static String strBibleVerse;


    // 성경의 권수
    public static final int nBibleCount = 66;

    // 성경의 최대 장수
    public static final int nMaxBibleChapter = 150; // 시편은 최대 150편이다.

    // 성경의 절수 제한
    public  static final int nMaxBibleVerse = 200; // 시편 119편이 제일 길것이다. 200이 안된다. 176편
    private static int kor;

    // 다운로드 받은 성경 체크
    public static ArrayList<HashMap<String,String>> m_hsCurrentBibles = new ArrayList<HashMap<String,String>>();

    // 다운로드 가능한 성경 체크
    public static ArrayList<HashMap<String,String>> m_hsDownLoadBibles = new ArrayList<HashMap<String,String>>();




    // 현재 성경책 얻어오기
    // 있으면 번호 넘겨주고 없으면 -1 리턴해준다.
    static int getBookBibleName(String a_strBookName)
    {
        if(!a_strBookName.equals(""))
        {
            for(int i = 0; i < strBibleBooksName.length; i++)
            {
                if(strBibleBooksName[i].equals(a_strBookName))
                    return i;
            }
        }
        return -1;
    }
    // 현재 성경책 변경하기
    static boolean setBookBibleName(String a_strBookName)
    {
        if(!a_strBookName.equals(""))
        {
            strBibleBookVersion = a_strBookName;
        }
        return false;
    }



}
