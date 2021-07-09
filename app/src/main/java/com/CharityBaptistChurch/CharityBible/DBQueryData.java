package com.CharityBaptistChurch.CharityBible;

import android.content.Context;
import android.util.Log;

import com.CharityBaptistChurch.CharityBible.Activity.MainActivity;
import com.CharityBaptistChurch.CharityBible.Adapter.BibleDBAdapter;


import java.util.ArrayList;

public class DBQueryData {

    private static DBQueryData ourInstance = new DBQueryData();


    private String sBibleVersion;
    private String sContents;
    private String sChapter;
    private String sBibleVersionReplace;
    private String sIsReplace;
    private String sFontSize;
    private String sIsBlackMode;
    private String sIsSleepMode;


    public DBQueryData() {
    }

    public static DBQueryData getInstance()
    {
        return ourInstance;
    }

    public void initData(String sDB)
    {

        String strResult;
        int nStart = 0;
        int nEnd = sDB.indexOf('|');

        int i = 0;

        while (nEnd != -1) {

            if( sDB.length() < nEnd)
                break;

            strResult = sDB.substring(nStart, nEnd);

            switch (i)
            {
                case 0:
                    sContents = strResult;
                    break;
                case 1:
                    sChapter= strResult;
                    break;
                case 2:
                    sIsReplace = strResult;
                    break;
                case 3:
                    sBibleVersion = strResult;
                    break;
                case 4:
                    sBibleVersionReplace = strResult;
                    break;
                case 5:
                    sFontSize = strResult;
                    break;
                case 6:
                    sIsBlackMode = strResult;
                    break;
                case 7:
                    sIsSleepMode = strResult;
                    break;
            }
            // 다음 시작포인트 위치 저장
            nStart = nEnd + 1;

            nEnd = sDB.indexOf('|',nEnd + 1);

            i++;
        }
    }

    public void setBibleVersion(String sBibleVersion) {
        this.sBibleVersion = sBibleVersion;
    }

    public void setContents(String sContexts) {

        this.sContents = sContexts;
    }

    public void setChapter(String sChapter) {

        this.sChapter = sChapter;
    }

    public void setBibleVersionReplace(String sBibleVersionReplace) {
        this.sBibleVersionReplace = sBibleVersionReplace;
    }

    public void setIsReplace(String sIsReplace) {
        this.sIsReplace = sIsReplace;
    }

    public void setFontSize(String sFontSize) {
        this.sFontSize = sFontSize;
    }

    public void setsIsBlackMode(String sIsBlackMode) {
        this.sIsBlackMode = sIsBlackMode;
    }

    public void setsIsSleepMode(String sIsSleepMode) {
        this.sIsSleepMode = sIsSleepMode;
    }

    public String getBibleVersion() {
        return sBibleVersion;
    }

    public String getContents() {
        return sContents;
    }

    public String getChapter() {
        return sChapter;
    }

    public String getBibleVersionReplace() {
        return sBibleVersionReplace;
    }

    public String getIsReplace() {
        return sIsReplace;
    }

    public String getFontSize() {
        return sFontSize;
    }

    public String getIsBlackMode() {
        return sIsBlackMode;
    }

    public String getIsSleepMode() {
        return sIsSleepMode;
    }


}
