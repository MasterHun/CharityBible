package com.CharityBaptistChurch.CharityBible.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.CharityBaptistChurch.CharityBible.Activity.MainActivity;
import com.CharityBaptistChurch.CharityBible.Adapter.BibleDBAdapter;
import com.CharityBaptistChurch.CharityBible.Adapter.VerseRecyclerViewAdapter;
import com.CharityBaptistChurch.CharityBible.BibleReader;
import com.CharityBaptistChurch.CharityBible.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Fragment_ReadBible extends Fragment implements VerseRecyclerViewAdapter.OnListItemLongSelectedInterface, VerseRecyclerViewAdapter.OnListItemSelectedInterface{

    View m_view;

    public RecyclerView m_RecyclerView;

    private String m_strBibleVersion;
    private String m_strContexts;
    private String m_strChapter;
    private String m_strReplaceBibleVersion;
    private String m_strIsReplace;
    private String m_strFontSize;
    private String m_strIsBlackMode;
    private String m_strIsSleepMode;


    BibleDBAdapter dbAdapter;


    public Fragment_ReadBible()
    {
        m_strBibleVersion = "korHKJV";    // 어떤 종류의 성경인지    ex)
        m_strContexts = "창세기";          // 어떤 권인지            ex) 창세기, 요한복음
        m_strChapter = "01";              // 성경 몇번째 장인지      ex) 10장이면 10, 3장이면 03
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("hun","Fragment_ReadBible::OnStart()");

        // 성경이 존재하는지에 대한 체크
        if(isBibleChecker()) {
            if (!(m_strContexts.equals("") && m_strChapter.equals(""))) {
                init(m_strContexts, m_strChapter);
            }
        }
        else
        {
            Toast.makeText(getContext(),"찾으시는 성경이 없습니다.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        Log.d("hun", "Fragment_ReadBible::onCreateView()");

        m_view = inflater.inflate(R.layout.fragment_readbible, container, false);

        // 이놈 위치가 너무 애매하다.. 저번에는 여기 두면 에러나던데;;
        m_RecyclerView = (RecyclerView) m_view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        m_RecyclerView.setLayoutManager(layoutManager);

        // 최근 읽은 성경 DB조회
        getBibleData();

        String strChapter;
        String strContents;

        if(getActivity() != null) {
            strChapter = ((MainActivity) getActivity()).getBtnChapter();
            strContents = ((MainActivity) getActivity()).getBtnContents();

            if ( !(strChapter.equals("") || strContents.equals("")) ) {
                int nIndex = strChapter.indexOf("장");

                m_strChapter = strChapter.substring(0, nIndex);
                m_strContexts = strContents;
            }

        }



        return m_view;
    }

    /*
     * @class    : getBibleData
     * @data     : 2020/04/21
     * @lastedit : 2020/04/21
     * @author   : 박명훈
     * @brief    : 성경 설정관련된 정보들을 가지고 와서 값들을 파싱해서 전역함수로 저장.
     */
    private void getBibleData()
    {
        dbAdapter = new BibleDBAdapter(getContext());
        dbAdapter.open();

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
                    m_strBibleVersion = strResult;
                    break;
                case 4:
                    m_strReplaceBibleVersion = strResult;
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

        String strLog = m_strContexts+","+m_strChapter+","+m_strIsReplace+","+m_strBibleVersion+","+m_strReplaceBibleVersion+","+m_strFontSize
                +","+m_strIsBlackMode+","+m_strIsSleepMode;
        Log.d("mhpark","Result:"+strLog);

        if(getActivity() != null) {
            ((MainActivity) getActivity()).setChapter(m_strChapter);
            ((MainActivity) getActivity()).setContents(m_strContexts);
            ((MainActivity) getActivity()).setBtnChapter(m_strChapter);
            ((MainActivity) getActivity()).setBtnContents(m_strContexts);
        }

    }


    public boolean isBibleChecker() {

        if (getActivity() != null) {
            String strPath = getActivity().getFilesDir().getAbsolutePath() + File.separator + "Bibles" + File.separator;
            Log.d("hun ", "Fragment_ReadBible::isBibleChecker Path[" + strPath + "]");
            File file = new File(strPath);
            return file.isDirectory();
        }
        else
        {
            return false;
        }
    }


    /*
     * @ Func    : init
     * @ Param   : strContents 초기 설정할 성경권 받아옴       EX) 창세기
     *             strChapter  초기 설정할 성경 장을 받아옴    EX) 10
     * @ Since   : 2020.04.16
     * @ Last    : 2020.04.16
     * @ Author  : mhpark
     * @ Context : 성경 읽는 Fragment에서 초기화 해주기 위해서 호출하는 함수.
     */
    public void init(String strContents, String strChapter)
    {

        // 1. 성경 장 숫자 보정
        // - 9장 일때 9로 들어오면 09로 수정해서 전달함.
        if( Integer.parseInt(strChapter) < 10)
        {
            int nChapter = Integer.parseInt(strChapter);
            strChapter = "0"+nChapter;
        }

        // 2. 성경 읽어와주는 함수 생성
        BibleReader bibleReader = new BibleReader();

        // 3. 성경 권에 대한 인덱스 얻어오기.
        String[] strBibleContents = getResources().getStringArray(R.array.KOR);

        String strContentsIndex;    // 성경 권수 인덱스 위치
        int nBibleContents = 0;     // 성경 권수 인덱스 구하기 위한 카운트 값

        // 3-1. Array에서 구해온 배열을 가지고 성경권의 위치를 찾아낸다.
        for(int i = 0; i < strBibleContents.length; i ++) {

            if( strBibleContents[i].equals(strContents) ) {
                nBibleContents = i + 1 ;
                break;
            }
        }

        // 3-2. 찾은 인덱스를 무조건 두자리 숫자가 되게끔 보정해준다.
        if( nBibleContents < 10 )
            strContentsIndex = "0"+nBibleContents;
        else
            strContentsIndex = Integer.toString(nBibleContents);

        Log.d("mhpark", "init() "+strContentsIndex+"/"+strChapter);

        // getActivity() 는 Fragment가 Detach 되었을때 에러가 발생될 수 있어서 필터
        String strBiblesPath;
        if(getActivity() != null)
            strBiblesPath = getActivity().getFilesDir().getAbsolutePath() + File.separator + "Bibles" + File.separator;
        else
            return;


        ArrayList<String[]> arrayBible;     // 읽어온 성경 데이터를 담을 ArrayList

        // 4. 성경 비교기능을 사용 할 것인지
        if( m_strIsReplace.equals("Y"))
            arrayBible =bibleReader.BibleParsing(strBiblesPath, m_strBibleVersion, m_strReplaceBibleVersion, strContentsIndex, strChapter);
        else
            arrayBible =bibleReader.BibleParsing(strBiblesPath, m_strBibleVersion, strContentsIndex, strChapter);

        if(arrayBible == null) {
            Log.d("mhpark","init() BibleParsing Failed");
            return;
        }

        // 5. 받은 성경 데이터 정리
        List<String> dataVerse = new ArrayList<>();
        List<String> dataNumber = new ArrayList<>();
        for(int i=0; i < arrayBible.size(); i++)
        {
            String strData[] = arrayBible.get(i);
            dataNumber.add(strData[3]+" ");
            dataVerse.add(strData[4]);
        }

        // MediaPlayer가 생성되면 아래부분에 가리기 때문에 추가해줘야한다.
        dataVerse.add("");
        dataNumber.add("");
        dataVerse.add("");
        dataNumber.add("");
        dataVerse.add("");
        dataNumber.add("");

        final VerseRecyclerViewAdapter mAdapter;
        if( m_strIsReplace.equals("Y") )
            mAdapter = new VerseRecyclerViewAdapter(getContext(), m_RecyclerView, this,this,true);
        else
            mAdapter = new VerseRecyclerViewAdapter(getContext(), m_RecyclerView, this,this, false);
        m_RecyclerView.setAdapter(mAdapter);

        mAdapter.setFontSize(Integer.parseInt(m_strFontSize));

        mAdapter.setData(dataNumber,dataVerse);

        // * 2020/02/18 구분선을 추가하면 계속해서 늘어나는현상때문에 우선은 주석처리함
        // 기본 구분선 추가
    //    DividerItemDecoration dividerItemDecoration =
     //           new DividerItemDecoration(m_RecyclerView.getContext(),new LinearLayoutManager(getContext()).getOrientation());
      //  m_RecyclerView.addItemDecoration(dividerItemDecoration);
        // 아이템간 공백 추가
      //  RecyclerDecoration spaceDecoration = new RecyclerDecoration(20);
      //  mRecyclerView.addItemDecoration(spaceDecoration);

        mAdapter.notifyDataSetChanged();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

//    public void setVerse()
//    {
//     //  VerseListAdapter adapter;
//      //  adapter = new VerseListAdapter();
//        //adapter.addItem("1","2","아잉");
//
//  //      m_listview.setAdapter(adapter);
//
//    }


    @Override
    public void onItemLongSelected(View v, int position) {

        Toast.makeText(getActivity(), "길게 클릭!!",Toast.LENGTH_SHORT).show();

        VerseRecyclerViewAdapter.StdViewHolder viewHolder = (VerseRecyclerViewAdapter.StdViewHolder)m_RecyclerView.findViewHolderForAdapterPosition(position);
        //Toast.makeText(this, viewHolder.textVerse.getText().toString(), Toast.LENGTH_SHORT).show();
        String str = viewHolder.textVerse.getText().toString();

        Toast.makeText(getActivity(), "길게 클릭!!"+str,Toast.LENGTH_SHORT).show();
//        if(R.id.recyclerView == v.getId())
//        {
//            m_RecyclerView.
//        }
//
//        ClipboardManager clipboardManager = (ClipboardManager)getContext().getSystemService(getContext().CLIPBOARD_SERVICE);
//        ClipData clipData = ClipData.newPlainText("label",);

        Intent msg = new Intent(Intent.ACTION_SEND);

        msg.addCategory(Intent.CATEGORY_DEFAULT);

        msg.putExtra(Intent.EXTRA_SUBJECT, "주제");

        //msg.putExtra(Intent.EXTRA_TEXT, "내용");
        msg.putExtra(Intent.EXTRA_TEXT, str);

        msg.putExtra(Intent.EXTRA_TITLE, "제목");

        msg.setType("text/plain");

        startActivity(Intent.createChooser(msg, "공유"));

    }

    @Override
    public void onItemSelected(View v, int position) {

        VerseRecyclerViewAdapter.StdViewHolder viewHolder = (VerseRecyclerViewAdapter.StdViewHolder)m_RecyclerView.findViewHolderForAdapterPosition(position);
        //Toast.makeText(this, viewHolder.textVerse.getText().toString(), Toast.LENGTH_SHORT).show();
        String str = viewHolder.textVerse.getText().toString();

        Toast.makeText(getActivity(), "짧게 클릭!!:"+str,Toast.LENGTH_SHORT).show();
    }
}
