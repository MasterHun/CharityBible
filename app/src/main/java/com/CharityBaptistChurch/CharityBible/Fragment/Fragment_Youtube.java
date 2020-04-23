package com.CharityBaptistChurch.CharityBible.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.CharityBaptistChurch.CharityBible.Activity.MainActivity;
import com.CharityBaptistChurch.CharityBible.Adapter.YoutubeListAdapter;
import com.CharityBaptistChurch.CharityBible.R;
import com.gc.materialdesign.widgets.ProgressDialog;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Fragment_Youtube extends Fragment {

    YouTubePlayerView m_youtubeview;
    View m_view;
    ListView m_listView;
    YoutubeListAdapter m_youtubeListAdapter;

    List<String> listTitle;
    List<String> listPublishedAt;
    List<String> listVideoid;

    LinearLayout m_linearYoutube;

    int nTimer = 0;

    ProgressDialog dialog;
    int m_nPos_dialog=0;
    //static final String[] LIST_MENU = {"LIST1", "LIST2", "LIST3"} ;

    Timer timer;
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

        m_listView = (ListView) m_view.findViewById(R.id.lvList);

        m_linearYoutube = (LinearLayout) m_view.findViewById(R.id.linearYoutube);


        m_youtubeListAdapter = new YoutubeListAdapter();
        m_listView.setAdapter(m_youtubeListAdapter);


        YoutubeParshing youtubeParshing = new YoutubeParshing();
        youtubeParshing.execute();



//        TimerTask tt = new TimerTask() {
//            @Override
//            public void run() {
//
//                ((MainActivity) getContext()).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        if(dialog != null)
//                        {
//                            return ;
//                        }
//
//                        if (m_youtubeListAdapter != null) {
//
//                            Log.i("onResume", "notify");
//
//                            if (!(listTitle == null || !(listTitle.size() > 0 && listPublishedAt.size() > 0 && listVideoid.size() > 0))) {
//                                for (int i = 0; i < listTitle.size(); i++) {
//                                    m_youtubeListAdapter.additem(listTitle.get(i), "", listVideoid.get(i), listPublishedAt.get(i));
//                                }
//                            }
//
//
//                            m_youtubeListAdapter.notifyDataSetChanged();
//
//                            // 타이머 한번만 동작하게 하기 위해서 추가가
//                           if(nTimer > 0)
//                                timer.cancel();
//                            nTimer++;
//
//                        }
//                    }
//                });
//
//            }
//        };
//
//        timer = new Timer();
//        timer.schedule(tt,0,3000);

        return m_view;
    }

    public void OnCheck() {
        if (dialog != null) {
            return;
        }

        if (m_youtubeListAdapter != null) {

            if (!(listTitle == null || !(listTitle.size() > 0 && listPublishedAt.size() > 0 && listVideoid.size() > 0))) {
                for (int i = 0; i < listTitle.size(); i++) {
                    m_youtubeListAdapter.additem(listTitle.get(i), "", listVideoid.get(i), listPublishedAt.get(i));
                }
            }
            m_youtubeListAdapter.notifyDataSetChanged();
        }

    }


    //첫번재 데이터 타입은 doInBackground() 메소드의 파라미터 타입을 지정

    //두번재 파라미터의 타입은 onProgressUpdate() 메소드의 파라미터 타입을 지정

    //세번째 파라미터의 타입은 onPostExecute() 메소드의 파라미터 타입을 지정
    class YoutubeParshing extends AsyncTask<Integer, Integer, Boolean>{

        String result = "";
   //     ArrayList<String> listTitle = new ArrayList<String>();
    //    ArrayList<String> listPublishedAt = new ArrayList<String>();
   //     ArrayList<String> listVideoid = new ArrayList<String>();
        public YoutubeParshing() {
            super();
        }



        // 초기화 단계
        @Override
        protected void onPreExecute() {

            listTitle = new ArrayList<String>();
            listPublishedAt = new ArrayList<String>();
            listVideoid = new ArrayList<String>();

            dialog = new ProgressDialog(getContext(),"유튜브 영상 얻어오는중");
            dialog.setCanceledOnTouchOutside(false);    // 프로그래스바 작동할때 바깥부분 클릭 금지
            dialog.show();
            super.onPreExecute();

        }


        protected Boolean doInBackground(Integer... Integers)
        {




                try {
//                    URL url = new URL("https://www.googleapis.com/youtube/v3/search?part=snippet&q=ENTER SEARCH WORD &maxResults=20&key=ENTER YOUR API KEY HERE");
                    String strA = "https://www.googleapis.com/youtube/v3/channels?part=contentDetails&forUsername=LoveChurch1611&key=AIzaSyAGqpbHB3Dbke3gxgEOGYuLSAnSb7Q32Fo";
                    String strB = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=Hi&maxResults=20&key=AIzaSyAGqpbHB3Dbke3gxgEOGYuLSAnSb7Q32Fo";
                    String strC = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=UU-qEqMeTaSnmy3kdoN-rF4w&key=AIzaSyAGqpbHB3Dbke3gxgEOGYuLSAnSb7Q32Fo";

                    URL url = new URL(strC);

                    URLConnection con = url.openConnection();
                    InputStream is = con.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);

                    while (true) {
                        String data = reader.readLine();
                        if (data == null) break;
                        result += data;
                    }
                    Log.e("MainActivity", result);
                    JSONObject obj = new JSONObject(result);
                    JSONArray arr = (JSONArray) obj.get("items");

                    int i = 0;
                    for (i = 0; i < arr.length(); i++) {
                        JSONObject item = (JSONObject) arr.get(i);
                        JSONObject snippet = (JSONObject) item.get("snippet");
                        String title = (String) snippet.get("title");
                        String publishedAt = (String) snippet.get("publishedAt");

                        JSONObject resourceId = snippet.optJSONObject("resourceId");
                        String videoid = (String) resourceId.get("videoId");

                        listTitle.add(title);
                        Log.i("Fragment[" + i + "]", listTitle.get(i));
                        listPublishedAt.add(publishedAt);
                        listVideoid.add(videoid);

                        m_nPos_dialog = i;
                        publishProgress(m_nPos_dialog);

                    }

                    m_nPos_dialog = i;
                    publishProgress(m_nPos_dialog);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }


            return true;
        }

        // doInBackground() 메소드 종료 후 자동으로 호출되는 콜백
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            dialog.dismiss();
            dialog = null;

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



    };



    class YoutubeTask extends AsyncTask<Integer, Integer, Integer>
    {
        public YoutubeTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            return null;
        }
    }

}
