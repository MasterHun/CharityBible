package com.CharityBaptistChurch.CharityBible.Fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
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

import com.CharityBaptistChurch.CharityBible.Activity.MainActivity;
import com.CharityBaptistChurch.CharityBible.Adapter.VerseRecyclerViewAdapter;
import com.CharityBaptistChurch.CharityBible.BibleReader;
import com.CharityBaptistChurch.CharityBible.Items.ListViewItem;
import com.CharityBaptistChurch.CharityBible.R;
import com.CharityBaptistChurch.CharityBible.Adapter.VerseListAdapter;
import com.CharityBaptistChurch.CharityBible.RecyclerDecoration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Fragment_ReadBible extends Fragment implements VerseRecyclerViewAdapter.OnListItemLongSelectedInterface, VerseRecyclerViewAdapter.OnListItemSelectedInterface{

    View m_view;
    public ListView m_listview;

    public RecyclerView mRecyclerView;

    private Boolean m_bFlag = true;

    public String m_strContexts = "";
    public String m_strChapter = "";



    //VerseListAdapter adapter;
    VerseRecyclerViewAdapter adapter;

    @Override
    public void onStart() {
        super.onStart();
        Log.i("ReadBible","OnStart()");
    }

//    private void init(LayoutInflater inflater ,ViewGroup container)
//    {
//        inflater.inflate(R.layout.fragment_readbible, container, false);
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        Log.i("Readbible","onCreateView");

        m_view = inflater.inflate(R.layout.fragment_readbible,container,false);

        if(m_strContexts.equals("") || m_strChapter.equals(""))
        {
            m_strContexts = "창세기";
            m_strChapter = "01";
        }

        init(m_strContexts,m_strChapter);

        return m_view;
    }

    public void init(String strContents, String strChapter)
    {

        m_strContexts = strContents;
        m_strChapter = strChapter;

        mRecyclerView = (RecyclerView) m_view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        List<String> dataVerse = new ArrayList<String>();
        List<String> dataNumber = new ArrayList<String>();

        BibleReader bibleReader = new BibleReader();

        String[] strBibleContents = getResources().getStringArray(R.array.KOR);
        String strBible = "";

        int nBibleContents = 0;
        for(int i = 0; i < strBibleContents.length; i ++) {

            if( true == strBibleContents[i].equals(strContents) ) {
                nBibleContents = i + 1 ;
                break;
            }
        }

        if( nBibleContents < 9 )
            strBible = "0"+nBibleContents;
        else
            strBible = Integer.toString(nBibleContents);


        Log.i("Readbible", strBible+"/"+strChapter);
        ArrayList<String[]> arrayBIble =bibleReader.BibleParsing("korHKJV",strBible,strChapter);

        for(int i=0; i < arrayBIble.size(); i++)
        {
            String strData[] = arrayBIble.get(i);
            dataNumber.add(strData[3]+" ");
            dataVerse.add(strData[4]);
        }

        dataVerse.add("");
        dataNumber.add("");
        dataVerse.add("");
        dataNumber.add("");
        dataVerse.add("");
        dataNumber.add("");


        final VerseRecyclerViewAdapter mAdapter = new VerseRecyclerViewAdapter(getContext(), mRecyclerView, this,this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(dataNumber,dataVerse);

        // 기본 구분선 추가
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mRecyclerView.getContext(),new LinearLayoutManager(getContext()).getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        // 아이템간 공백 추가
      //  RecyclerDecoration spaceDecoration = new RecyclerDecoration(20);
      //  mRecyclerView.addItemDecoration(spaceDecoration);



    }

    boolean BibleParsing(String strVerse){

        String strChapter = "";


        return true;
    }

    File GetBibleFileList(File a_file){


        return a_file;
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
