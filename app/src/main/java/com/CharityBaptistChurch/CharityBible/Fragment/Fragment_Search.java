package com.CharityBaptistChurch.CharityBible.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.CharityBaptistChurch.CharityBible.Adapter.BibleDBAdapter;
import com.CharityBaptistChurch.CharityBible.Adapter.VerseRecyclerViewAdapter;
import com.CharityBaptistChurch.CharityBible.R;
import com.CharityBaptistChurch.CharityBible.Util;
import com.gc.materialdesign.widgets.ProgressDialog;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Fragment_Search extends Fragment implements VerseRecyclerViewAdapter.OnListItemLongSelectedInterface, VerseRecyclerViewAdapter.OnListItemSelectedInterface{

    View m_View;
    RecyclerView m_list;
    Button m_btnSearch;
    EditText m_editSearch;
    TextView m_txNewCnt, m_txOldCnt, m_txTotalCnt;


    private String m_strBibleVersion;
    private String m_strContexts;
    private String m_strChapter;
    private String m_strCompareBibleVersion;
    private String m_strIsReplace;
    private String m_strFontSize;
    private String m_strIsBlackMode;
    private String m_strIsSleepMode;

    private  BibleDBAdapter dbAdapter;

    VerseRecyclerViewAdapter m_Adapter;


    List<ArrayList<String>> m_arrayList;

    SearchText m_searchtext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        m_View = inflater.inflate(R.layout.fragment_search, container, false);
        //  m_list = (RecyclerView) m_View.findViewById(R.id.listSearch);
        m_btnSearch = m_View.findViewById(R.id.btnSearch);
        m_editSearch = m_View.findViewById(R.id.editSearch);


        m_txNewCnt = m_View.findViewById(R.id.txNewCnt);
        m_txOldCnt = m_View.findViewById(R.id.txOldCnt);
        m_txTotalCnt = m_View.findViewById(R.id.txTotalCnt);


        dbAdapter = new BibleDBAdapter(getContext());
        dbAdapter.open();

        m_btnSearch.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                String strText = m_editSearch.getText().toString();


                if(strText.length() < 2)
                {
                    show("2글자 이상만 입력 가능합니다.");
                    m_editSearch.setText("");
                }else
                {

                    InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    mInputMethodManager.hideSoftInputFromWindow(m_editSearch.getWindowToken(), 0);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    m_list.setLayoutManager(layoutManager);

                    boolean bIsReplace = false;
                    if(m_strIsReplace.equals("Y"))
                        bIsReplace = true;

                    m_Adapter = new VerseRecyclerViewAdapter(getContext(), m_list,Fragment_Search.this,Fragment_Search.this, bIsReplace);


                   // m_searchtext = new SearchText(strText);
                    m_searchtext = new SearchText(strText);
                    m_searchtext.execute(strText);



                }
            }
        });

        m_list = m_View.findViewById(R.id.listSearch);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        m_list.setLayoutManager(layoutManager);

        getBibleData();

        //return super.onCreateView(inflater, container, savedInstanceState);
        return m_View;
    }


    @Override
    public void onItemLongSelected(View v, int position) {

    }

    @Override
    public void onItemSelected(View v, int position) {

    }

//    private List<ArrayList<String>> SearchText_(String strText) {
//
////        if(Util.isExternalStorageReadable())
////         return null;
//
//        String strVerse;
//        String strTemp;
//
//        String strBibleNo;
//        String strBible;
//        String strChapter;
//        String strVerseNo;
//
//        String strPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator + Util.m_strDirectory + File.separator + m_strBibleVersion+".cbk";
//
//        // 성경 경로안의 파일목록 생성
//        File file = new File(strPath);
//
//        // 읽은 성경 파일들 목록 배열 형태로 저장
//        File files[] = file.listFiles();
//
//        // 파싱할때 자리수 관리하기 위한 변수.
//        int nStart = 0;
//        int nEnd = 0;
//
//
//        List<ArrayList<String>> arrayList = new ArrayList<>();      // 성경 파싱한 정보들이 장 별로 축척된다.
//
//
//        ArrayList<String> listVerse = new ArrayList<>();
//        ArrayList<String> listNumber = new ArrayList<>();
//        ArrayList<String> listNewCount = new ArrayList<>();
//        ArrayList<String> listOldCount = new ArrayList<>();
//        int nNewCnt = 0;
//        int nOldCnt = 0;
//
//        // 전체 성경 검색하기
//        for (int i = 0; files.length > i; i++) {
//            File f = new File(files[i].getPath());
//
//            try {
//                FileReader fr = new FileReader(f);
//                BufferedReader br = new BufferedReader(fr);
//
//                String line ;
//                while ((line = br.readLine()) != null) {
//                    nStart = 0;
//
//                    //  Log.v("BibleDownload BB line:",line);
//
//                    // 파싱 진행
//
//                    nEnd = line.indexOf(":");       // "01창 10:" 까지 구해와진다.
//                    nEnd = line.indexOf(" ", nEnd);
//                    strTemp = line.substring(nStart, nEnd); // "01창 10:1" 까지 구해와졌다.
//
//                    // 1. 구절획득
//                    nStart = nEnd + 1;  // +1 을 해줘야 절 index가 된다.
//                    strVerse = line.substring(nStart);  // 구절을 얻어온다.
//
//                    // 2. 성경과 번호 획득
//                    nStart = 0;
//                    nEnd = strTemp.indexOf(" ");    // "01창 "까지 구해와진다.
//                    //   nBibleNo = Integer.parseInt(strTemp.substring(nStart, 2));   // 01
//                    strBibleNo = strTemp.substring(nStart, 2);   // 01
//                    strBible = strTemp.substring(2, nEnd);   // 창
//
//
//                    // 3. 장 획득
//                    nStart = nEnd + 1;
//                    nEnd = strTemp.indexOf(":");
//                    //   nChapter = Integer.parseInt(strTemp.substring(nStart ,nEnd-1)); // 10
//                    strChapter = strTemp.substring(nStart, nEnd); // 10
//
//                    if (Integer.parseInt(strChapter) < 10)
//                        strChapter = "0" + strChapter;
//
//                    // 4. 절 획득
//                    nStart = nEnd + 1;
//                    strVerseNo = strTemp.substring(nStart); // 1 을 구해온다.
//
//                    if (Integer.parseInt(strVerseNo) < 10)
//                        strVerseNo = "0" + strVerseNo;
//
//                    if (strVerse.indexOf(strText) != -1) {
//
//                        listVerse.add(strVerse);
//                        listNumber.add(strBible + " " + strChapter + ":" + strVerseNo+" ");
//
//
//                        if (Integer.parseInt(strBibleNo) < 40) {
//                            nOldCnt++;
//                        } else if (Integer.parseInt(strBibleNo) > 39) {
//                            nNewCnt++;
//                        }
//                        //   arrayList.add(new String[]{strBibleNo, strBible, strChapter, strVerseNo, strVerse});
//                    }
//                }
//                br.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        listOldCount.add(Integer.toString(nOldCnt));
//        listNewCount.add(Integer.toString(nNewCnt));
//
//        arrayList.add(listVerse);
//        arrayList.add(listNumber);
//        arrayList.add(listOldCount);
//        arrayList.add(listNewCount);
//        return arrayList;
//
//    }

    class SearchText extends AsyncTask<String, Integer, Integer>
    {

        ProgressDialog dialog;
        String m_strText;
        SearchText() {
            super();
        }

        SearchText(String strText) {
            super();
            m_strText = strText;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            dialog.dismiss();
            dialog = null;


            if(m_arrayList.size() > 0 ) {
                Log.d("OnCreateVIew", "data:" + m_arrayList.get(0).size());
                if (m_arrayList.get(0).size() > 0) {
                    Log.d("OnCreateVIew", "Viewing Search Data" + m_arrayList.get(0).size());

                    // 0 : 성경절 내용
                    // 1 : 성경절 번호
                    // 2 : 구약성경에서 카운트 개수
                    // 3 : 신약성경에서 카운트 개수
                    m_Adapter.setData(m_arrayList.get(1), m_arrayList.get(0));

                    String strOldCnt = m_arrayList.get(2).get(0) + "번";
                    String strNewCnt = m_arrayList.get(3).get(0) + "번";
                    String strAllCnt = m_arrayList.get(0).size() + "번";
                    m_txOldCnt.setText(strOldCnt);
                    m_txNewCnt.setText(strNewCnt);
                    m_txTotalCnt.setText(strAllCnt);
                } else {
                    m_arrayList.get(0).add("  ");
                    m_arrayList.get(1).add(" \"" + m_strText + "\"에 대한 찾고자 하는 값이 없습니다.");
                    m_Adapter.setData(m_arrayList.get(0), m_arrayList.get(1));

                    m_txOldCnt.setText("n번");
                    m_txNewCnt.setText("n번");
                    m_txTotalCnt.setText("n번");

                    show("찾고자 하는 결과값이 없습니다 ㅠㅠ");
                }
                m_list.setAdapter(m_Adapter);
            }


        }

        @Override
        protected void onPreExecute() {


            dialog = new ProgressDialog(getContext(),"\""+m_strText+"\""+"단어 찾는중..");
            dialog.setCanceledOnTouchOutside(false);    // 프로그래스바 작동할때 바깥부분 클릭 금지

            dialog.show();

            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(String... strings) {


            String strPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator + Util.m_strDirectory + File.separator + m_strBibleVersion+".cbk";

            // 성경 경로안의 파일목록 생성
            File file = new File(strPath);

            m_arrayList = new ArrayList<>();
           // List<ArrayList<String>> arrayList = new ArrayList<>();      // 성경 파싱한 정보들이 장 별로 축척된다.

            ArrayList<String> listVerse = new ArrayList<>();
            ArrayList<String> listNumber = new ArrayList<>();
            ArrayList<String> listNewCount = new ArrayList<>();
            ArrayList<String> listOldCount = new ArrayList<>();

            int nNewCnt = 0;
            int nOldCnt = 0;

            try {
                ZipFile zip = new ZipFile(file, ZipFile.OPEN_READ);

                Enumeration<? extends ZipEntry> entries = zip.entries();


                // 파싱할때 자리수 관리하기 위한 변수.
                int nStart = 0;
                int nEnd = 0;

                int nIndex =0;

                while (entries.hasMoreElements()) {
                    nIndex++;
                    String strVerse;
                    String strTemp;

                    String strBibleNo;
                    String strBible;
                    String strChapter;
                    String strVerseNo;

                    ZipEntry entry = entries.nextElement();

                    InputStream input = zip.getInputStream(entry);

                    Scanner scan = new Scanner(input);

                    while (scan.hasNext()) {
                        String line = scan.nextLine();
                        nStart = 0;

                        //  Log.v("BibleDownload BB line:",line);
                        // 파싱 진행

                        nEnd = line.indexOf(":");       // "01창 10:" 까지 구해와진다.
                        nEnd = line.indexOf(" ", nEnd);
                        strTemp = line.substring(nStart, nEnd); // "01창 10:1" 까지 구해와졌다.

                        // 1. 구절획득
                        nStart = nEnd + 1;  // +1 을 해줘야 절 index가 된다.
                        strVerse = line.substring(nStart);  // 구절을 얻어온다.

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
                        strVerseNo = strTemp.substring(nStart); // 1 을 구해온다.

                        if (Integer.parseInt(strVerseNo) < 10)
                            strVerseNo = "0" + strVerseNo;

                        // 5. 찾고자하는 단어가 있다면 리스트에 추
                        if (strVerse.contains(strings[0])) {

                            listVerse.add(strVerse);
                            listNumber.add(strBible + " " + strChapter + ":" + strVerseNo + " ");

                            if (Integer.parseInt(strBibleNo) < 40) {
                                nOldCnt++;
                            } else if (Integer.parseInt(strBibleNo) > 39) {
                                nNewCnt++;
                            }
                        }
                    }
                    publishProgress(nIndex);
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }

            listOldCount.add(Integer.toString(nOldCnt));
            listNewCount.add(Integer.toString(nNewCnt));

            m_arrayList.add(listVerse);
            m_arrayList.add(listNumber);
            m_arrayList.add(listOldCount);
            m_arrayList.add(listNewCount);

            //return arrayList;
            return null;
        }
    }
    private List<ArrayList<String>> SearchText(String strText) {

        // 파일 접근가능 여부 체크
//        if(Util.isExternalStorageReadable())
//            return null;

        String strPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator + Util.m_strDirectory + File.separator + m_strBibleVersion+".cbk";

        // 성경 경로안의 파일목록 생성
        File file = new File(strPath);

        List<ArrayList<String>> arrayList = new ArrayList<>();      // 성경 파싱한 정보들이 장 별로 축척된다.

        ArrayList<String> listVerse = new ArrayList<>();
        ArrayList<String> listNumber = new ArrayList<>();
        ArrayList<String> listNewCount = new ArrayList<>();
        ArrayList<String> listOldCount = new ArrayList<>();

        int nNewCnt = 0;
        int nOldCnt = 0;

        try {
            ZipFile zip = new ZipFile(file, ZipFile.OPEN_READ);

            Enumeration<? extends ZipEntry> entries = zip.entries();


            // 파싱할때 자리수 관리하기 위한 변수.
            int nStart = 0;
            int nEnd = 0;

            while (entries.hasMoreElements()) {

                String strVerse;
                String strTemp;

                String strBibleNo;
                String strBible;
                String strChapter;
                String strVerseNo;

                ZipEntry entry = entries.nextElement();

                InputStream input = zip.getInputStream(entry);

                Scanner scan = new Scanner(input);

                while (scan.hasNext()) {
                    String line = scan.nextLine();
                    nStart = 0;

                    //  Log.v("BibleDownload BB line:",line);
                    // 파싱 진행

                    nEnd = line.indexOf(":");       // "01창 10:" 까지 구해와진다.
                    nEnd = line.indexOf(" ", nEnd);
                    strTemp = line.substring(nStart, nEnd); // "01창 10:1" 까지 구해와졌다.

                    // 1. 구절획득
                    nStart = nEnd + 1;  // +1 을 해줘야 절 index가 된다.
                    strVerse = line.substring(nStart);  // 구절을 얻어온다.

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
                    strVerseNo = strTemp.substring(nStart); // 1 을 구해온다.

                    if (Integer.parseInt(strVerseNo) < 10)
                        strVerseNo = "0" + strVerseNo;

                    // 5. 찾고자하는 단어가 있다면 리스트에 추
                    if (strVerse.contains(strText)) {

                        listVerse.add(strVerse);
                        listNumber.add(strBible + " " + strChapter + ":" + strVerseNo + " ");

                        if (Integer.parseInt(strBibleNo) < 40) {
                            nOldCnt++;
                        } else if (Integer.parseInt(strBibleNo) > 39) {
                            nNewCnt++;
                        }
                    }
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        listOldCount.add(Integer.toString(nOldCnt));
        listNewCount.add(Integer.toString(nNewCnt));

        arrayList.add(listVerse);
        arrayList.add(listNumber);
        arrayList.add(listOldCount);
        arrayList.add(listNewCount);

        return arrayList;

    }

    private void getBibleData()
    {

        ArrayList<String> array = dbAdapter.SelectSettingDB();

        String strData = array.get(0);

        String strResult;
        int nStart = 0;
        int nEnd = strData.indexOf('|');

        int i = 0;

        while (nEnd != -1) {

            if( strData.length() < nEnd)
                break;

            strResult = strData.substring(nStart, nEnd);

            switch (i)
            {
                case 0:
                    m_strContexts = strResult;
                    break;
                case 1:
                    m_strChapter= strResult;
                    break;
                case 2:
                    m_strIsReplace = strResult;
                    break;
                case 3:
                    m_strBibleVersion= strResult;
                    break;
                case 4:
                    m_strCompareBibleVersion = strResult;
                    break;
                case 5:
                    m_strFontSize = strResult;
                    break;
                case 6:
                    m_strIsBlackMode = strResult;
                    break;
                case 7:
                    m_strIsSleepMode = strResult;
                    break;
            }

            // 다음 시작포인트 위치 저장
            nStart = nEnd + 1;

            nEnd = strData.indexOf('|',nEnd + 1);


            i++;
        }

        String strLog = m_strContexts+","+m_strChapter+","+m_strIsReplace+","+m_strBibleVersion+","+m_strCompareBibleVersion+","+m_strFontSize
                +","+m_strIsBlackMode+","+m_strIsSleepMode;
        Log.d("mhpark","Result:"+strLog);

    }


    void show(String str)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
   //    builder.setTitle("AlertDialog Title");
        builder.setMessage(str);
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      //  Toast.makeText(getContext(),"예를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
//        builder.setNegativeButton("아니오",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
//                    }
//                });
        builder.show();
    }

}
