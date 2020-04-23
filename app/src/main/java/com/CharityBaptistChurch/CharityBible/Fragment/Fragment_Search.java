package com.CharityBaptistChurch.CharityBible.Fragment;

import android.content.Context;
import android.content.DialogInterface;
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

import com.CharityBaptistChurch.CharityBible.Adapter.VerseRecyclerViewAdapter;
import com.CharityBaptistChurch.CharityBible.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Search extends Fragment implements VerseRecyclerViewAdapter.OnListItemLongSelectedInterface, VerseRecyclerViewAdapter.OnListItemSelectedInterface{

    View m_View;
    RecyclerView m_list;
    Button m_btnSearch;
    EditText m_editSearch;
    TextView m_txNewCnt, m_txOldCnt, m_txTotalCnt;


    VerseRecyclerViewAdapter m_Adapter;
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

//        String strChapter;
//        String strContents;
//        if (getActivity() != null) {
//            strChapter = ((MainActivity) getActivity()).getBtnChapter();
//            strContents = ((MainActivity) getActivity()).getBtnContents();
//        }


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

                    m_Adapter = new VerseRecyclerViewAdapter(getContext(), m_list,Fragment_Search.this,Fragment_Search.this);


                    List<ArrayList> listseach = SearchText(strText);
                    Log.d("OnCreateVIew","data:"+listseach.get(0).size());
                    if(listseach.get(0).size() > 0)
                    {
                        Log.d("OnCreateVIew","Viewing Search Data"+listseach.get(0).size());


                        m_Adapter.setData(listseach.get(1),listseach.get(0));

                        m_txOldCnt.setText(listseach.get(2).get(0).toString()+"번");
                        m_txNewCnt.setText(listseach.get(3).get(0).toString()+"번");
                        m_txTotalCnt.setText(listseach.get(0).size()+"번");
                    }else
                    {
                        listseach.get(0).add("  ");
                        listseach.get(1).add(" \""+strText+"\"에 대한 찾고자 하는 값이 없습니다.");
                        m_Adapter.setData(listseach.get(0),listseach.get(1));

                        m_txOldCnt.setText("n번");
                        m_txNewCnt.setText("n번");
                        m_txTotalCnt.setText("n번");

                        show("찾고자 하는 결과값이 없습니다 ㅠㅠ");
                    }
                    m_list.setAdapter(m_Adapter);
                }
            }
        });

        m_list = m_View.findViewById(R.id.listSearch);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        m_list.setLayoutManager(layoutManager);

        //return super.onCreateView(inflater, container, savedInstanceState);
        return m_View;
    }


    @Override
    public void onItemLongSelected(View v, int position) {

    }

    @Override
    public void onItemSelected(View v, int position) {

    }

    private List<ArrayList> SearchText(String strText) {
        String strVerse;
        String strTemp;

        String strBibleNo;
        String strBible;
        String strChapter;
        String strVerseNo;

        String strPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "korhkjv";

        // 성경 경로안의 파일목록 생성
        File file = new File(strPath);

        // 읽은 성경 파일들 목록 배열 형태로 저장
        File files[] = file.listFiles();

        // 파싱할때 자리수 관리하기 위한 변수.
        int nStart = 0;
        int nEnd = 0;


        List<ArrayList> arrayList = new ArrayList<>();      // 성경 파싱한 정보들이 장 별로 축척된다.


        ArrayList<String> listVerse = new ArrayList<>();
        ArrayList<String> listNumber = new ArrayList<>();
        ArrayList<String> listNewCount = new ArrayList<>();
        ArrayList<String> listOldCount = new ArrayList<>();
        int nNewCnt = 0;
        int nOldCnt = 0;

        // 전체 성경 검색하기
        for (int i = 0; files.length > i; i++) {
            File f = new File(files[i].getPath());

            try {
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);

                String line = "";
                while ((line = br.readLine()) != null) {
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

                    if (strVerse.indexOf(strText) != -1) {

                        listVerse.add(strVerse);
                        listNumber.add(strBible + " " + strChapter + ":" + strVerseNo);


                        if (Integer.parseInt(strBibleNo) < 40) {
                            nOldCnt++;
                        } else if (Integer.parseInt(strBibleNo) > 39) {
                            nNewCnt++;
                        }
                        //   arrayList.add(new String[]{strBibleNo, strBible, strChapter, strVerseNo, strVerse});
                    }
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        listOldCount.add(Integer.toString(nOldCnt));
        listNewCount.add(Integer.toString(nNewCnt));

        arrayList.add(listVerse);
        arrayList.add(listNumber);
        arrayList.add(listOldCount);
        arrayList.add(listNewCount);
        return arrayList;

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
