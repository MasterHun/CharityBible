package com.CharityBaptistChurch.CharityBible.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.CharityBaptistChurch.CharityBible.Adapter.YoutubeListAdapter;
import com.CharityBaptistChurch.CharityBible.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class Fragment_Youtube extends Fragment {

    YouTubePlayerView m_youtubeview;
    View m_view;
    ListView m_listView;

    //static final String[] LIST_MENU = {"LIST1", "LIST2", "LIST3"} ;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.v("Youtube_onCreateView","CreateView");
        m_view =  inflater.inflate(R.layout.fragment_youtube, container,false );

        m_listView = (ListView) m_view.findViewById(R.id.lvList);

        YoutubeListAdapter youtubeListAdapter = new YoutubeListAdapter();
        m_listView.setAdapter(youtubeListAdapter);

        youtubeListAdapter.additem("새벽기도, 통성기도, 왜 안하는가_03 : 정동수 목사, 사랑침례교회, 킹제임스 흠정역 성경, 설교 말씀","Context: Text","oryGmmUtOdw","2015.6.26");
        youtubeListAdapter.additem("서구 근대 사회의 근간인 십계명의 영적 의미와 정치 사회 경제적 의미_성경과 정치 19 : 정동수 목사, 사랑침례교회, 킹제임스 성경 (2019. 9.20)", "Context: Text","EfHmXNY_SWs","2015.6.26");
        youtubeListAdapter.additem("조국 등 무능과 위선의 386 운동권 정부의 실태와 대한민국의 비극 그리고 희망_양상훈 사설 인용 정동수 목사","Context: Text","lfJ_dHmgES0","2015.6.26");
        youtubeListAdapter.additem("제목","Context: Text","-39qGxrPOIU","2019.9.26");
        youtubeListAdapter.additem("제목","Context: Text","F4tuW2Twtc4", "2019.9.26");
        youtubeListAdapter.additem("제목","Context: Text","K7TEFtSk-i4","2019.9.26");
        youtubeListAdapter.additem("제목","Context: Text","djtv92FSlok","2018.9.26");
        youtubeListAdapter.additem("제목","Context: Text","RKwx1KVf3yM","2015.6.26");
        youtubeListAdapter.additem("제목","Context: Text","G8P5TacN0t8","2015.6.26");

        return m_view;
        //return super.onCreateView(inflater, container, savedInstanceState);

    }
}
