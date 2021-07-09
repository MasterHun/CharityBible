package com.CharityBaptistChurch.CharityBible.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.CharityBaptistChurch.CharityBible.Adapter.OnYotubeItemClick;
import com.CharityBaptistChurch.CharityBible.Adapter.YoutubeListAdapter;
import com.CharityBaptistChurch.CharityBible.R;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.ProgressDialog;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;


public class Fragment_Youtube extends Fragment implements View.OnClickListener, OnYotubeItemClick  {

    YouTubePlayerView m_youtubeview;
    View m_view;
    ListView m_listView;
    YoutubeListAdapter m_youtubeListAdapter;

    List<String> m_listTitle;           // Youtube 영상 제목
    List<String> m_listPublishedAt;     // Youtube 업로드 날짜
    List<String> m_listVideoid;         // Youtube 영상 고유 아이디

    LinearLayout m_LLYoutube;           //


    int nTimer = 0;

    Button m_Btn_Main, m_Btn_Sub;

    ProgressDialog dialogYoutube;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //YoutubeParshing();

        nTimer = 0;
        Log.v("Youtube_onCreateView", "CreateView");
        m_view = inflater.inflate(R.layout.fragment_youtube, container, false);

        m_listView = m_view.findViewById(R.id.lvList);

        m_LLYoutube = m_view.findViewById(R.id.linearYoutube);


        m_youtubeListAdapter = new YoutubeListAdapter(this);
        m_listView.setAdapter(m_youtubeListAdapter);



        m_Btn_Main = m_view.findViewById(R.id.BTN_main);
        m_Btn_Main.setOnClickListener(this);

        m_Btn_Sub = m_view.findViewById(R.id.BTN_sub);
        m_Btn_Sub.setOnClickListener(this);


        String strUrl = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet,contentDetails&maxResults=50&playlistId=UU-qEqMeTaSnmy3kdoN-rF4w&key=AIzaSyAGqpbHB3Dbke3gxgEOGYuLSAnSb7Q32Fo";
        YoutubeParshing youtubeParshing = new YoutubeParshing();
        youtubeParshing.execute(strUrl);

        return m_view;
    }

    public void OnCheck() {
        if (dialogYoutube != null) {
            return;
        }

        m_youtubeListAdapter = null;
        m_youtubeListAdapter = new YoutubeListAdapter(this);
        m_listView.setAdapter(m_youtubeListAdapter);

        if (!(m_listTitle == null || !(m_listTitle.size() > 0 && m_listPublishedAt.size() > 0 && m_listVideoid.size() > 0))) {
            for (int i = 0; i < m_listTitle.size(); i++) {
                m_youtubeListAdapter.additem(m_listTitle.get(i), "", m_listVideoid.get(i), m_listPublishedAt.get(i));
            }
        }
        m_youtubeListAdapter.notifyDataSetChanged();


    }

    @Override
    public void onClick(View v) {

        YoutubeParshing youtubeParshing = new YoutubeParshing();
        String strUrl;

        switch (v.getId()) {
            case R.id.BTN_main:

                strUrl = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet,contentDetails&maxResults=50&playlistId=UU-qEqMeTaSnmy3kdoN-rF4w&key=AIzaSyAGqpbHB3Dbke3gxgEOGYuLSAnSb7Q32Fo";
                youtubeParshing.execute(strUrl);

                break;

            case R.id.BTN_sub:

                strUrl = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet,contentDetails&maxResults=50&playlistId=PL4P6SBDceLgFGaOSkipbSojlUYAxAXwku&key=AIzaSyAGqpbHB3Dbke3gxgEOGYuLSAnSb7Q32Fo";
                youtubeParshing.execute(strUrl);
                break;
            default:
                break;
        }
    }

    @Override
    public void OnClickItem(final String a_strID) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        dialog.setTitle("Youtube");
        dialog.setMessage("유튜브를 여시겠습니까?");
        dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://www.youtube.com/watch?v="+a_strID);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        dialog.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

       dialog.show();

    }

    //첫번재 데이터 타입은 doInBackground() 메소드의 파라미터 타입을 지정

    //두번재 파라미터의 타입은 onProgressUpdate() 메소드의 파라미터 타입을 지정

    //세번째 파라미터의 타입은 onPostExecute() 메소드의 파라미터 타입을 지정
    class YoutubeParshing extends AsyncTask<String, Integer, Boolean> {

        String result = "";

        YoutubeParshing() {
            super();
        }


        // 초기화 단계
        @Override
        protected void onPreExecute() {

            m_listTitle = new ArrayList<>();
            m_listPublishedAt = new ArrayList<>();
            m_listVideoid = new ArrayList<>();

            dialogYoutube = new ProgressDialog(getContext(), "유튜브 영상 얻어오는중");
            dialogYoutube.setCanceledOnTouchOutside(false);    // 프로그래스바 작동할때 바깥부분 클릭 금지
            dialogYoutube.show();
            super.onPreExecute();

        }


        protected Boolean doInBackground(String... Strings) {
            try {
//                    URL url = new URL("https://www.googleapis.com/youtube/v3/search?part=snippet&q=ENTER SEARCH WORD &maxResults=20&key=ENTER YOUR API KEY HERE");
                String strA = "https://www.googleapis.com/youtube/v3/channels?part=contentDetails&forUsername=LoveChurch1611&key=AIzaSyAGqpbHB3Dbke3gxgEOGYuLSAnSb7Q32Fo";
                String strB = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=Hi&maxResults=20&key=AIzaSyAGqpbHB3Dbke3gxgEOGYuLSAnSb7Q32Fo";


                // 메인설
                String strC = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=UU-qEqMeTaSnmy3kdoN-rF4w&key=AIzaSyAGqpbHB3Dbke3gxgEOGYuLSAnSb7Q32Fo";

                // 필수설교
                String strD = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=PL4P6SBDceLgFGaOSkipbSojlUYAxAXwku&key=AIzaSyAGqpbHB3Dbke3gxgEOGYuLSAnSb7Q32Fo";

                URL url = new URL(Strings[0]);

                URLConnection con = url.openConnection();
                InputStream is = con.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);

                // Byte 단위로 받아와서 데이터 저장
                while (true) {
                    String data = reader.readLine();
                    if (data == null) break;
                    result += data;
                }
                Log.d("Fragment_youtube", result);
                JSONObject obj = new JSONObject(result);
                JSONArray arr = (JSONArray) obj.get("items");

                int i = 0;
                for (i = 0; i < arr.length(); i++) {
                    JSONObject item = (JSONObject) arr.get(i);
                    JSONObject snippet = (JSONObject) item.get("snippet");
                    String title = (String) snippet.get("title");
                    //   String publishedAt = (String) snippet.get("publishedAt");

                    JSONObject resourceId = snippet.optJSONObject("resourceId");
                    String videoid = (String) resourceId.get("videoId");

                    JSONObject contentDetails = (JSONObject) item.get("contentDetails");
                    String videoPublishedAt = (String) contentDetails.get("videoPublishedAt");


                    if(title.length()>40)
                    {
                        String sTitle;
                        sTitle = title.substring(0,40);
                        sTitle += "...";
                        m_listTitle.add(sTitle);

                    }else
                        m_listTitle.add(title);
                    Log.d("Fragment[" + i + "]", m_listTitle.get(i));

                    // ex) 2019-01-28T10:29:24Z
                    int nIndex = videoPublishedAt.indexOf('T');
                    int nLen = videoPublishedAt.length();
                    String sVideoDate = videoPublishedAt.substring(0, nIndex);
                    sVideoDate += " "+videoPublishedAt.substring(nIndex+1,nLen-1);

                    m_listPublishedAt.add(sVideoDate);
                    m_listVideoid.add(videoid);


                    //  m_nPos_dialog = i;
                    //    publishProgress(m_nPos_dialog);

                }

                //  m_nPos_dialog = i;
                //   publishProgress(m_nPos_dialog);


            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return false;
            }


            return true;
        }

        // doInBackground() 메소드 종료 후 자동으로 호출되는 콜백
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(dialogYoutube != null)
                dialogYoutube.dismiss();
            dialogYoutube = null;

            OnCheck();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


    }
}
