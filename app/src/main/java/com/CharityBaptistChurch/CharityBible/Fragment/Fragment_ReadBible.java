package com.CharityBaptistChurch.CharityBible.Fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.CharityBaptistChurch.CharityBible.Adapter.VerseRecyclerViewAdapter;
import com.CharityBaptistChurch.CharityBible.BibleReader;
import com.CharityBaptistChurch.CharityBible.Items.ListViewItem;
import com.CharityBaptistChurch.CharityBible.R;
import com.CharityBaptistChurch.CharityBible.Adapter.VerseListAdapter;
import com.CharityBaptistChurch.CharityBible.RecyclerDecoration;

import java.util.ArrayList;
import java.util.List;

public class Fragment_ReadBible extends Fragment implements VerseRecyclerViewAdapter.OnListItemLongSelectedInterface, VerseRecyclerViewAdapter.OnListItemSelectedInterface{

    View m_view;
    public ListView m_listview;

    public RecyclerView mRecyclerView;

    private Boolean m_bFlag = true;



    //VerseListAdapter adapter;
    VerseRecyclerViewAdapter adapter;

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Test","Here is Fregment OnStart");

        String str1 = "받아왔다 : ";
        int nIndex;
        Bundle extra = this.getArguments();
        if( extra != null)
        {
            extra = getArguments();
            nIndex = extra.getInt("key");
            Toast.makeText(getActivity(), str1+nIndex,Toast.LENGTH_SHORT).show();
        }

    }

    private void init(LayoutInflater inflater ,ViewGroup container)
    {
        inflater.inflate(R.layout.fragment_readbible, container, false);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);


        m_view = inflater.inflate(R.layout.fragment_readbible,container,false);

        init(inflater);
//        mRecyclerView = (RecyclerView) m_view.findViewById(R.id.recyclerView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
//        mRecyclerView.setLayoutManager(layoutManager);
//
//        ArrayList<ListViewItem> list = new ArrayList<ListViewItem>();
//        list.add(new ListViewItem("10","10","테스트중입니다. --> 1",1));
//        list.add(new ListViewItem("10","11","테스트중입니다. --> 2",1));
//        ArrayList<String> test = new ArrayList<String>();
//
//        adapter = new VerseRecyclerViewAdapter(this.getContext(), mRecyclerView, this,this);
//        mRecyclerView.setAdapter(adapter);
//        adapter.setData(test);

//        ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
//        BibleReader bibleReader =  new BibleReader();

//        ArrayList<String[]> arrayList;
//        arrayList = bibleReader.BibleParsing("","01","10");
//
//        ArrayList<String> arrayString = new ArrayList<String>();
//        String strChapter = null;
//        String strSection = null ;
//        String strVerse = null ;
//        for(int i = 0; i < arrayList.size(); i++) {
//            for(int j = 0; j < arrayList.get(i).length; j++)
//            {
//                String str = arrayList.get(i)[j];
//          //      Log.v("Log:",str);
//                switch (j) {
//                    case 2:
//                        strChapter = str;
//                        break;
//                    case 3:
//                        strSection = str;
//                        break;
//                    case 4:
//                        strVerse = str;
//                        break;
//                }
//                arrayString.add(strChapter + strSection + strVerse+"");
//            }
//        //    adapter.addItem(strChapter,strSection,strVerse,0);
//            adapter.setData(arrayString);
//            strChapter = null;
//            strSection = null;
//            strVerse = null;
//        }

        // #테스트 성경구절 삽입
//        adapter.addItem("1","1","예수 그리스도의 종 바울은 사도로 부르심을 받아 [하나님]의 복음을 위해 구별되었는데 ",1);
//        adapter.addItem("1","1","예수 그리스도의 종 바울은 사도로 부르심을 받아 [하나님]의 복음을 위해 구별되었는데 ",0);
//        adapter.addItem("1","2","(이 복음은 그분께서 자신의 대언자들을 통하여 거룩한 성경 기록들에 미리 약속하신 것으로)",1);
//        adapter.addItem("1","2","(이 복음은 그분께서 자신의 대언자들을 통하여 거룩한 성경 기록들에 미리 약속하신 것으로)",0);
//        adapter.addItem("1","3","자신의 [아들] 예수 그리스도 우리 [주]에 관한 것이라. 그분께서는 육체로는 다윗의 씨에서 나셨고",1);
//        adapter.addItem("1","3","자신의 [아들] 예수 그리스도 우리 [주]에 관한 것이라. 그분께서는 육체로는 다윗의 씨에서 나셨고",0);
//        adapter.addItem("1","4","거룩함의 영으로는 죽은 자들로부터 부활하심으로써 [하나님]의 [아들]로 권능 있게 밝히 드러나셨느니라.",1);
//        adapter.addItem("1","4","거룩함의 영으로는 죽은 자들로부터 부활하심으로써 [하나님]의 [아들]로 권능 있게 밝히 드러나셨느니라.",0);
//        adapter.addItem("1","5","그분으로 말미암아 우리가 은혜와 사도직을 받아 그분의 이름을 위하여 모든 민족들 가운데서 믿음에 순종하게 하였나니",1);
//        adapter.addItem("1","5","그분으로 말미암아 우리가 은혜와 사도직을 받아 그분의 이름을 위하여 모든 민족들 가운데서 믿음에 순종하게 하였나니",0);
//        adapter.addItem("1","6","너희도 그들 가운데서 예수 그리스도의 부르심을 받았느니라.",1);
//        adapter.addItem("1","6","너희도 그들 가운데서 예수 그리스도의 부르심을 받았느니라.",0);
//        adapter.addItem("1","7","바울은, 로마에서 [하나님]께 사랑을 받고 성도로 부르심을 받은 모든 사람에게 편지하노니 [하나님] 우리 [아버지]와 [주] 예수 그리스도로부터 은혜와 평강이 너희에게 있기를 원하노라.",1);
//        adapter.addItem("1","7","바울은, 로마에서 [하나님]께 사랑을 받고 성도로 부르심을 받은 모든 사람에게 편지하노니 [하나님] 우리 [아버지]와 [주] 예수 그리스도로부터 은혜와 평강이 너희에게 있기를 원하노라.",0);
//        adapter.addItem("1","8","먼저 너희 모두로 인하여 예수 그리스도를 통해 나의 [하나님]께 감사하노니 이는 너희의 믿음이 온 세상에 두루 전하여졌기 때문이라.",1);
//        adapter.addItem("1","8","먼저 너희 모두로 인하여 예수 그리스도를 통해 나의 [하나님]께 감사하노니 이는 너희의 믿음이 온 세상에 두루 전하여졌기 때문이라.",0);
//        adapter.addItem("1","9","내가 그분의 [아들]의 복음 안에서 내 영으로 섬기는 [하나님]께서 내 증인이 되시거니와 내가 기도할 때에 언제나 너희에 관하여 끊임없이 말하며");
//        adapter.addItem("1","10","어찌하든지 이제라도 마침내 [하나님]의 뜻에 따라 순탄한 여정을 얻어 너희에게 가게 되기를 간구하노라.");
//        adapter.addItem("1","11","내가 너희를 간절히 보고자 함은 내가 너희에게 어떤 영적 선물을 나누어 주어 너희를 굳게 세우고자 함이니");
//        adapter.addItem("1","12","이것은 곧 너희와 나 사이의 공통된 믿음으로 말미암아 내가 너희와 함께 위로를 받고자 함이라.");
//        adapter.addItem("1","13","형제들아, 이제 나는 너희가 이것을 모르기를 원치 아니하노니 곧 내가 너희 가운데서도 다른 이방인들 가운데서처럼 어떤 열매를 얻기 위해 여러 번 너희에게 가고자 하였으나 (이제껏 막혔도다.)");
//        adapter.addItem("1","14","나는 그리스인이나 바바리인이나 지혜 있는 자나 지혜 없는 자에게 다 빚진 자니라.");
//        adapter.addItem("1","15","그러므로 내 안에 있는 분량대로 나는 또한 로마에 있는 너희에게 복음을 선포할 준비가 되어 있노라.");
//        adapter.addItem("1","16","내가 그리스도의 복음을 부끄러워하지 아니하노니 이는 그 복음이 믿는 모든 자를 구원에 이르게 하는 [하나님]의 권능이기 때문이라. 먼저는 유대인에게요 또한 그리스인에게로다.");
//        adapter.addItem("1","17","복음에는 [하나님]의 의가 믿음에서 믿음까지 계시되어 있나니 이것은 기록된바, 의인은 믿음으로 살리라, 함과 같으니라.");

        String str1;

        Log.i("Test","Here is Fregment OnCreateView");

//        Bundle extra = this.getArguments();
//        if( extra != null)
//        {
//            extra = getArguments();
//            str1 = extra.getString("Ch1");
//            Toast.makeText(getActivity(),str1,Toast.LENGTH_SHORT).show();
//        }

        return m_view;
    }

    private void init(LayoutInflater inflater)
    {
        mRecyclerView = (RecyclerView) m_view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        List<String> dataSet = new ArrayList<String>();

        int row = 1;
        for ( int i = 0; i < 10 ; i++) {
            dataSet.add("<" + row++ + ">" + "C/C++");
            dataSet.add("<" + row++ + ">" + "Java");
            dataSet.add("<" + row++ + ">" + "Kotlin");
            dataSet.add("<" + row++ + ">" + "Python");
            dataSet.add("<" + row++ + ">" + "Ruby");
        }

        final VerseRecyclerViewAdapter mAdapter = new VerseRecyclerViewAdapter(getContext(), mRecyclerView, this,this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(dataSet);

        // 기본 구분선 추가
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mRecyclerView.getContext(),new LinearLayoutManager(getContext()).getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        // 아이템간 공백 추가
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(20);
        mRecyclerView.addItemDecoration(spaceDecoration);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setVerse()
    {
     //  VerseListAdapter adapter;
      //  adapter = new VerseListAdapter();
        //adapter.addItem("1","2","아잉");

  //      m_listview.setAdapter(adapter);

    }


    @Override
    public void onItemLongSelected(View v, int position) {

    }

    @Override
    public void onItemSelected(View v, int position) {

    }
}
