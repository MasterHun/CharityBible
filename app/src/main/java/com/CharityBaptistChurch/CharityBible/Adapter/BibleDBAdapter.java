package com.CharityBaptistChurch.CharityBible.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.textclassifier.SelectionEvent;

import java.util.ArrayList;

// 디비 헬퍼 클래스
public class BibleDBAdapter extends SQLiteOpenHelper {


    private static BibleDBAdapter bibleDBAdapter;

   // private static SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "CharityBible";
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_TABLE_SETTING = "my_setting";       // 1. 앱설정 관련된 Table
    public static final String DATABASE_TABLE_UNDERLINE = "my_underline";     // 2. 밑줄관리 Table
    //private static final String DATABASE_TABLE_READ_CHECK   = "my_read_check";    // 3. 읽었는지 유무 체크 Table


    public BibleDBAdapter(@Nullable Context context ) {

        super(context,DATABASE_NAME, null,DATABASE_VERSION);
    }

    public static BibleDBAdapter getInstance(Context context){
        if(bibleDBAdapter == null) {
            bibleDBAdapter = new BibleDBAdapter(context);


            SQLiteDatabase db = bibleDBAdapter.getWritableDatabase();

            db.execSQL(DATABASE_CREATE_UNDERLINE);
            db.execSQL(DATABASE_CREATE_SETTING);

        }



        return bibleDBAdapter;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //mDb = bibleDBAdapter.getWritableDatabase();
        Log.d("mhpark", "BibleDBHelper Create 호출");

        db.execSQL(DATABASE_CREATE_UNDERLINE);
        db.execSQL(DATABASE_CREATE_SETTING);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //mDb = bibleDBAdapter.getWritableDatabase();
    }

    public static String getDatabaseTableSetting() {
        return DATABASE_TABLE_SETTING;
    }

    public static String getDatabaseTableUnderline() {
        return DATABASE_TABLE_UNDERLINE;
    }

    // 1. 설정 디비
    private static final String DATABASE_CREATE_SETTING =
            "create table if not exists " + DATABASE_TABLE_SETTING + "("
                    + "_id integer primary key autoincrement,"
                    + "my_read_location_contents String not null,"       // 1.마지막 읽은 성경 전서 ex) 창세기10장 -> 창세기
                    + "my_read_location_chapter String not null,"        // 2.마지막 읽은 성경 장수 ex) 창세기10장 -> 10
                    + "isreplace String not null,"                       // 3.성경 비교 유무
                    + "version String not null,"                         // 4.현재성경 버전
                    + "replace_version String not null,"                 // 5.비교할 성경 버전
                    + "font_size String not null,"                       // 6.폰트 사이즈
                    + "isblack_theme String not null,"                   // 7.다크모드 사용 유무
                    + "issleep_mode String not null);";                  // 8.기기 잠들지 않게 하는 기능 사용 유무

    // 2. 밑줄 긋기 저장 디비
    private static final String DATABASE_CREATE_UNDERLINE =
            "create table if not exists " + DATABASE_TABLE_UNDERLINE + "("
                    + "_id integer primary key autoincrement, "       // 번호 기본키로 지정
                    + "book_id text not null, "                       // 책 번호 ex) 01(창세기), 02, 03
                    + "chapter_no text not null,"                     // 장 번호 ex) 10
                    + "verse_no text not null);";                     // 절 번호 ex) 2


    //    // 3. 나의 읽은 장 저장 디비
//    private static final String DATABASE_CREATE_READ_CHECK =
//            "create table if not exists "+ DATABASE_TABLE_READ_CHECK+"("
//                    + "_id integer primary key autoincrement, "
//                    + "book_id integer not null,"                               // 번호 기본키로 지정
//                    + "chapter_no integer not null,"                        // 책 번호 ex) 01(창세기), 02, 03
//                    + "check_table_id integer not null,"                        // 장 번호 ex) 10
//                    + "ver text not null, "
//                    + "check_datetime datetime default (datetime('now','localtime')));";





    public void CreateTableAll() {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL(DATABASE_CREATE_SETTING);
        db.execSQL(DATABASE_CREATE_UNDERLINE);
    }


    // 현재 데이터베이스 얻기
    public SQLiteDatabase getDB() {
        SQLiteDatabase db = getWritableDatabase();
        return db;
    }

    ;


    public void Insert_DB(String a_strSql) {

        SQLiteDatabase db = getWritableDatabase();

        try {
            db.execSQL(a_strSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * @ Func    : InsertSetting_DB
     * @ Param   : a_strReadLocate      현재읽는 위치
     *             a_strIsReplace       비교역본 사용유무
     *             a_strBibleVersion    현재 성경종류
     *             a_strReplaceVersion  역본 성경종류
     *             a_strFontSize        폰트 사이즈
     *             a_strIsBlackThem     어두운모드 사용여부
     *             a_strIsSleepMode     화면 잠들지않게
     * @ Since   : 2020.04.13
     * @ Last    : 2020.04.13
     * @ Author  : mhpark
     * @ Context : 세팅탭 관련 테이블 데이터 DB에 INSERT 하는 함수
     */
    public void InsertSetting_DB(String a_strContens,
                                        String a_strChapter,
                                        String a_strIsReplace,
                                        String a_strBibleVersion,
                                        String a_strReplaceVersion,
                                        String a_strFontSize,
                                        String a_strIsBlackThem,
                                        String a_strIsSleepMode) {
        SQLiteDatabase db = getWritableDatabase();

        if (a_strContens.isEmpty() || a_strChapter.isEmpty() || a_strIsReplace.isEmpty() || a_strBibleVersion.isEmpty()
                || a_strReplaceVersion.isEmpty() || a_strFontSize.isEmpty() || a_strIsSleepMode.isEmpty()) {
            Log.d("BibleDBAdapter", "InsertSetting_DB isEmpty! not date");
            return;
        }

        String strSql = "INSERT INTO " + DATABASE_TABLE_SETTING + "(my_read_location_contents, my_read_location_chapter, isreplace,version,replace_version,font_size,isblack_theme,issleep_mode) " +
                "Values('" + a_strContens + "','" + a_strChapter + "','" + a_strIsReplace + "','" + a_strBibleVersion + "','" + a_strReplaceVersion + "','" + a_strFontSize + "',"
                + "'" + a_strIsBlackThem + "','" + a_strIsSleepMode + "');";
        try {
            db.execSQL(strSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * @ Func    : InsertUnderline_DB
     * @ Param   : a_strBook        성경 몇권인지         EX)창세기
     *             a_strChapter     성경 몇장인지         EX)1
     *             a_strVerse       성경 몇절인지         EX)10
     * @ Since   : 2020.04.13
     * @ Last    : 2020.04.13
     * @ Author  : mhpark
     * @ Context : 밑줄 긋기 사용한 위치 표시
     */
    public void InsertUnderline_DB(String a_strBook,
                                   String a_strChapter,
                                   String a_strVerse) {

        SQLiteDatabase db = getWritableDatabase();
        if (a_strBook.isEmpty() || a_strChapter.isEmpty() || a_strVerse.isEmpty()) {
            Log.d("BibleDBAdapter", "InsertUnderline_DB isEmpty! not date");
            return;
        }

        String strSql = "INSERT INTO " + DATABASE_TABLE_UNDERLINE + "(book_id, chapter_no, verse_no)"
                + "Values('" + a_strBook + "','" + a_strChapter + "','" + a_strVerse + "');";
        try {
            db.execSQL(strSql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*
     * @ Func    : DeleteTable
     * @ Param   : a_strTableName      삭제할 테이블 이름
     * @ Since   : 2020.04.13
     * @ Last    : 2020.04.13
     * @ Author  : mhpark
     * @ Context : 전달받은 테이블 삭제
     */
    public void DeleteTable(String a_strTableName) {

        SQLiteDatabase db = getWritableDatabase();

            try {
                db.execSQL("drop table if exists " + a_strTableName + ";");
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    /*
     * @ Func    : DeleteTableAll
     * @ Param   : x
     * @ Since   : 2020.04.13
     * @ Last    : 2020.04.13
     * @ Author  : mhpark
     * @ Context : 존재하는 모든 테이블 삭제
     */
    public void DeleteTableAll() {

        SQLiteDatabase db = getWritableDatabase();
            try {
                db.execSQL("drop table if exists " + DATABASE_TABLE_SETTING + ";");
                db.execSQL("drop table if exists " + DATABASE_TABLE_UNDERLINE + ";");
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }


    /*
     * @ Func    : GetSettingDB
     * @ Param   : x
     * @ Since   : 2020.04.15
     * @ Last    : 2020.04.15
     * @ Author  : mhpark
     * @ Context : 설정테이블 조회함수
     */
    public ArrayList<String> SelectSettingDB() {
        ArrayList<String> array = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();

        String strSQL = "select my_read_location_contents, my_read_location_chapter ,isreplace,version,replace_version,font_size,isblack_theme,issleep_mode from " + DATABASE_TABLE_SETTING;

        try {
            Cursor cursor = db.rawQuery(strSQL, null);

            while (cursor.moveToNext()) {

                String str = cursor.getString(0) + "|" + cursor.getString(1) + "|" + cursor.getString(2) + "|" + cursor.getString(3)
                        + "|" + cursor.getString(4) + "|" + cursor.getString(5) + "|" + cursor.getString(6) + "|" + cursor.getString(7) + "|";
                array.add(str);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return array;
    }

    // 테이블 컬럼 갯수 조회
    public int GetTableCount(String a_strTableName) {

        SQLiteDatabase db = getWritableDatabase();
        //mDb = bibleDBAdapter.getWritableDatabase();
        if (a_strTableName == null )
            return -1;

        int nCount = -1;
        String strSql = "select * from " + a_strTableName;
        try {
            Cursor cursor = db.rawQuery(strSql, null);

            cursor.moveToFirst();
            nCount = cursor.getCount();

            db.close();

            cursor.close();
        } catch (SQLException e) {
            Log.v("에러", "Sql");

        }

        return nCount;
    }


    /*
     * @ Func    : UpdateSettingContents
     * @ Param   : a_strContents 수정할 성경권 받아옴  EX)창세기
     * @ Since   : 2020.04.15
     * @ Last    : 2020.04.15
     * @ Author  : mhpark
     * @ Context : 설정테이블에서 Contents 성경에 어떤 권을 보고있는지에 관한 데이터를 DB에서 얻어옴
     */
    public void UpdateSettingContents(String a_strContents) {

        SQLiteDatabase db = getWritableDatabase();

        // 항상 첫번째 라인것만 받아옴
        String strSQL = "update " + DATABASE_TABLE_SETTING + " set my_read_location_contents=" + "'" + a_strContents + "'" + " where _id ='" + "1'" + ";";

        try {
            db.execSQL(strSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * @ Func    : UpdateSettingChapter
     * @ Param   : a_strContents 수정할 성경권 받아옴  EX)창세기
     * @ Since   : 2020.04.15
     * @ Last    : 2020.04.15
     * @ Author  : mhpark
     * @ Context : 설정테이블에서 Contents 성경에 어떤 권을 보고있는지에 관한 데이터를 DB에서 얻어옴
     */
    public void UpdateSettingChapter(String a_strChapter) {

        SQLiteDatabase db = getWritableDatabase();

        // 항상 첫번째 라인것만 받아옴
        String strSQL = "update " + DATABASE_TABLE_SETTING + " set my_read_location_chapter=" + "'" + a_strChapter + "'" + " where _id ='" + "1'" + ";";

        try {
            db.execSQL(strSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * @ Func    : UpdateSettingIsReplace
     * @ Param   : a_strIsReplace 성경 비교할 것인지 ex) Y or N
     * @ Since   : 2020.04.15
     * @ Last    : 2020.04.15
     * @ Author  : mhpark
     * @ Context : 설정테이블에서 성경비교기능을 사용할 것인지에 관하여
     */
    public void UpdateSettingIsReplace(String a_strIsReplace) {

        SQLiteDatabase db = getWritableDatabase();

        // 항상 첫번째 라인것만 받아옴
        String strSQL = "update " + DATABASE_TABLE_SETTING + " set isreplace=" + "'" + a_strIsReplace + "'" + " where _id ='" + "1'" + ";";

        try {
            db.execSQL(strSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * @ Func    : UpdateSettingVersion
     * @ Param   : a_strContents 수정할 성경권 받아옴  EX)창세기
     * @ Since   : 2020.04.15
     * @ Last    : 2020.04.15
     * @ Author  : mhpark
     * @ Context : 설정테이블에서 Contents 성경에 어떤 권을 보고있는지에 관한 데이터를 DB에서 얻어옴
     */
    public void UpdateSettingVersion(String a_strVersion) {
        SQLiteDatabase db = getWritableDatabase();

        // 항상 첫번째 라인것만 받아옴
        String strSQL = "update " + DATABASE_TABLE_SETTING + " set version=" + "'" + a_strVersion + "'" + " where _id ='" + "1'" + ";";

        try {
            db.execSQL(strSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * @ Func    : UpdateSettingReplaceVersion
     * @ Param   : a_strContents 수정할 성경권 받아옴  EX)창세기
     * @ Since   : 2020.04.15
     * @ Last    : 2020.04.15
     * @ Author  : mhpark
     * @ Context : 설정테이블에서 Contents 성경에 어떤 권을 보고있는지에 관한 데이터를 DB에서 얻어옴
     */
    public void UpdateSettingReplaceVersion(String a_strReplaceVersion) {

        SQLiteDatabase db = getWritableDatabase();

        // 항상 첫번째 라인것만 받아옴
        String strSQL = "update " + DATABASE_TABLE_SETTING + " set replace_version=" + "'" + a_strReplaceVersion + "'" + " where _id ='" + "1'" + ";";

        try {
            db.execSQL(strSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * @ Func    : UpdateSettingFontSize
     * @ Param   : a_strContents
     * @ Since   : 2020.04.15
     * @ Last    : 2020.04.15
     * @ Author  : mhpark
     * @ Context : 설정테이블에서 Contents 성경에 어떤 권을 보고있는지에 관한 데이터를 DB에서 얻어옴
     */
    public void UpdateSettingFontSize(String a_strFontSize) {
        SQLiteDatabase db = getWritableDatabase();

        // 항상 첫번째 라인것만 받아옴
        String strSQL = "update " + DATABASE_TABLE_SETTING + " set font_size=" + "'" + a_strFontSize + "'" + " where _id ='" + "1'" + ";";

        try {
            db.execSQL(strSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * @ Func    : UpdateSettingContents
     * @ Param   : a_strContents 수정할 성경권 받아옴  EX)창세기
     * @ Since   : 2020.04.15
     * @ Last    : 2020.04.15
     * @ Author  : mhpark
     * @ Context : 설정테이블에서 Contents 성경에 어떤 권을 보고있는지에 관한 데이터를 DB에서 얻어옴
     */
    public void UpdateSettingIsBlackMode(String a_strIsBlackMode) {
        SQLiteDatabase db = getWritableDatabase();

        // 항상 첫번째 라인것만 받아옴
        String strSQL = "update " + DATABASE_TABLE_SETTING + " set isblack_theme=" + "'" + a_strIsBlackMode + "'" + " where _id ='" + "1'" + ";";

        try {
            db.execSQL(strSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * @ Func    : UpdateSettingContents
     * @ Param   : a_strContents 수정할 성경권 받아옴  EX)창세기
     * @ Since   : 2020.04.15
     * @ Last    : 2020.04.15
     * @ Author  : mhpark
     * @ Context : 설정테이블에서 Contents 성경에 어떤 권을 보고있는지에 관한 데이터를 DB에서 얻어옴
     */
    public void UpdateSettingIsSleepMode(String a_strIsSleepMode) {
        SQLiteDatabase db = getWritableDatabase();
        // 항상 첫번째 라인것만 받아옴
        String strSQL = "update " + DATABASE_TABLE_SETTING + " set issleep_mode=" + "'" + a_strIsSleepMode + "'" + " where _id ='" + "1'" + ";";

        try {
            db.execSQL(strSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 밑줄 그은 목록 얻어오는 함수
    public ArrayList<String[]> GetUnderline_DB() {
        ArrayList<String[]> array = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();

        String strSQL = "select * from " + DATABASE_TABLE_UNDERLINE;

        try {
            Cursor cursor = db.rawQuery(strSQL, null);

            while (cursor.moveToNext()) {
                String str[] = new String[3];

                //book_id
                str[0] = cursor.getString(0);
                //chapter_no
                str[1] = cursor.getString(1);
                //verse_no
                str[2] = cursor.getString(2);

                array.add(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return array;
    }

    public ArrayList<String[]> DeleteUnderline_DB() {
        ArrayList<String[]> array = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();


        String strSQL = "select * from " + DATABASE_TABLE_UNDERLINE;

        try {
            Cursor cursor = db.rawQuery(strSQL, null);

            while (cursor.moveToNext()) {
                String str[] = new String[3];

                //book_id
                str[0] = cursor.getString(0);
                //chapter_no
                str[1] = cursor.getString(1);
                //verse_no
                str[2] = cursor.getString(2);

                array.add(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return array;
    }
}
