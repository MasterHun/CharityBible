package com.CharityBaptistChurch.CharityBible.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.CharityBaptistChurch.CharityBible.Activity.BibleTabActivity;
import com.CharityBaptistChurch.CharityBible.Fragment.Fragment_ReadBible;
import com.CharityBaptistChurch.CharityBible.Fragment.Fragment_Search;
import com.CharityBaptistChurch.CharityBible.Fragment.Fragment_Setting;
import com.CharityBaptistChurch.CharityBible.Fragment.Fragment_Youtube;
import com.CharityBaptistChurch.CharityBible.MusicPlayer;
import com.CharityBaptistChurch.CharityBible.R;
import com.CharityBaptistChurch.CharityBible.Util;

import java.io.File;


public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    private ListView listviewVerse;
    private Button btn1_top, btn2_top, btn3_top;
    private Button btn1_bottom, btn2_bottom, btn3_bottom, btn4_bottom, btn5_bottom;
    private ImageButton ibtn_mp3_top, ibtn_moremenu_top, ibtnPrevious, ibtnPreview, ibtnPlay;
    private LinearLayout linear_Miniplayer;
    private boolean bMusicbuttonflag;

    public String m_strContents = "창세기";
    public String m_strChapter = "1";

    public Fragment_ReadBible fr = new Fragment_ReadBible();
  //  FragmentManager fm = getSupportFragmentManager();

    Fragment_ReadBible frag_readbible;
    Fragment_Search frag_search;
    Fragment_Youtube frag_youtube;
    Fragment_Setting frag_setting;

    FragmentManager fm;
    FragmentTransaction tran;

    ActionBar actionBar;

    MusicPlayer mp;
    boolean bPlayFlge = false;
    int i = 0;
    // #Action bar 커스텀
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.custom_actionbar, null);

        actionBar.setCustomView(customView);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.argb(255,255,255,255)));

        Toolbar parent = (Toolbar) customView.getParent();

        // action bar 공백 없애주기
        parent.setContentInsetsAbsolute(0,0);

        ibtn_mp3_top = (ImageButton) findViewById(R.id.ibtn_mp3_top);
        ibtn_mp3_top.setOnClickListener(this);
        ibtn_moremenu_top = (ImageButton) findViewById(R.id.ibtn_moremenu_top);
        btn1_top = (Button) findViewById(R.id.btn1_top);
        btn1_top.setOnClickListener(this);
        btn1_top.setText(m_strContents+"▼");
        btn2_top = (Button) findViewById(R.id.btn2_top);
        btn2_top.setOnClickListener(this);
        btn2_top.setText(m_strChapter+"장▼");
        btn3_top = (Button) findViewById(R.id.btn3_top);
        btn3_top.setOnClickListener(this);
        btn3_top.setText("KORHKJV▼");

        ibtnPreview = (ImageButton) findViewById(R.id.ibtnPreview);
        ibtnPreview.setOnClickListener(this);
        ibtnPrevious = (ImageButton) findViewById(R.id.ibtnPrevious);
        ibtnPrevious.setOnClickListener(this);
        ibtnPlay = (ImageButton) findViewById(R.id.ibtnPlay);
        ibtnPlay.setOnClickListener(this);

        //        return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i("MainActivity","OnStart()");

        fm = getSupportFragmentManager();
        tran = fm.beginTransaction();
        tran.replace(R.id.fragment, frag_readbible);
        tran.commit();
        // actionBar.show();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkVerify();
        Log.i("MainActivity","OnCreate()");
        //   listviewVerse = (ListView) findViewById(R.id.listview1);

        linear_Miniplayer = (LinearLayout) findViewById(R.id.lineaMiniplayer);
        bMusicbuttonflag = false;

        btn1_bottom = (Button) findViewById(R.id.btn1_bottom);
        btn1_bottom.setOnClickListener(this);
        btn1_bottom.setText("성경읽기");

        btn2_bottom = (Button) findViewById(R.id.btn2_bottom);
        btn2_bottom.setOnClickListener(this);
        btn2_bottom.setText("검색");

//        btn3_bottom = (Button) findViewById(R.id.btn3_bottom);
//        btn3_bottom.setOnClickListener(this);
//        btn3_bottom.setText("부록");

        btn4_bottom = (Button) findViewById(R.id.btn4_bottom);
        btn4_bottom.setOnClickListener(this);
        btn4_bottom.setText("유튜브");

        btn5_bottom = (Button) findViewById(R.id.btn5_bottom);
        btn5_bottom.setOnClickListener(this);
        btn5_bottom.setText("설정");

        frag_readbible = new Fragment_ReadBible();
        frag_search = new Fragment_Search();
        frag_youtube = new Fragment_Youtube();
        frag_setting = new Fragment_Setting();


        // *미니플레이어를 숨김
        bMusicbuttonflag = false;
        linear_Miniplayer.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void onResume() {
        super.onResume();

        String strChapter = "" ;
        String strContents = "";
        int nChapter = 0;

        Log.i("MainActivity","OnResume()");

        Intent intent = getIntent();
        if( intent.getExtras() != null ) {
            Log.i("MainActivity","intent 값받아옴");
            strChapter = intent.getStringExtra("Chapter");      // 성경 장
            strContents = intent.getStringExtra("Contents");     // 성경 전서
            Log.i("MainActivity",strContents+":"+strChapter);

            if(!strChapter.isEmpty())
                nChapter = Integer.parseInt(strChapter);

            if( nChapter > 0)
            {
                m_strChapter = strChapter;
                Log.i("MainActivity","찍힘 Here");
                Toast.makeText(getApplicationContext(), strContents,Toast.LENGTH_SHORT).show();
            }

            if(strContents != null)
            {
                m_strContents = strContents;
            }
               Fragment_ReadBible fragmentReadBible = (Fragment_ReadBible) getSupportFragmentManager().findFragmentById(R.id.fragment);
               fragmentReadBible.init(m_strContents,m_strChapter);
        }




    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MainActivity","OnPause()");
    }

    private void init()
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode)
        {
            case 1:
                String key = data.getStringExtra("key");
                break;
            case 2:
                break;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {

            case R.id.btn1_bottom:
                setFrag(0);
                break;
            case R.id.btn2_bottom:
                setFrag(1);
                break;
//            case R.id.btn3_bottom:
//                setFrag(2);
//                break;
            case R.id.btn4_bottom:
                setFrag(3);
                break;
            case R.id.btn5_bottom:
                setFrag(4);
                break;
            case R.id.btn1_top:
                Intent it_1 = new Intent(getApplicationContext(), BibleTabActivity.class);
                startActivity(it_1);
                break;
            case R.id.btn2_top:
                Intent it_2 = new Intent(getApplicationContext(), ChapterTableActivity.class);


                String strBible = "";
                int nBiblePosition = findBiblePosition(m_strContents, "KOR");
                final String[] bible = getResources().getStringArray(R.array.KOR);
                strBible = bible[nBiblePosition];
                nBiblePosition =  Util.numOfChapters[nBiblePosition];

                it_2.putExtra("bookPosition", nBiblePosition);
                it_2.putExtra("bookName", strBible);

                startActivity(it_2);
                break;
            case R.id.ibtn_mp3_top:
                if(bMusicbuttonflag) {
                    linear_Miniplayer.setVisibility(View.INVISIBLE);
                    bMusicbuttonflag = false;


                }
                else {
                    linear_Miniplayer.setVisibility(View.VISIBLE);
                    bMusicbuttonflag = true;

                    File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    String strDir = file.getAbsolutePath();

                    String strPath  = strDir + "/bible_mp3";
                    mp = new MusicPlayer(strPath);
                    mp.initMusic();
                }
                break;
            case R.id.ibtnPreview:
                if(mp != null)
                {
                    mp.onPreview();
                }
                break;
            case R.id.ibtnPlay:
                if(mp != null)
                {
                    mp.onPlay();

                    if(bPlayFlge)
                        ibtnPlay.setImageResource(R.drawable.ic_pause_black_24dp);
                    else
                        ibtnPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }
                break;
            case R.id.ibtnPrevious:
                if(mp != null)
                {
                    mp.onPrevious();
                }
                break;

        }

    }

    // @@Function
    // 성경 배열로 정리
    // - 필요한 목록은 추가로 입력하면됨.
    // - 시간되면 유틸쪽으로 옮겨도 될거같아보임
    int findBiblePosition(String a_strBibleName, String a_strBibleSelecter) {
        switch (a_strBibleSelecter) {
            case "KOR": {
                final String[] bible = getResources().getStringArray(R.array.KOR);

                for (int i = 0; i < bible.length; i++) {
                    if (bible[i].equals(a_strBibleName)) {
                        return i;
                    }
                }
            }
            case "KOR_ACM": {
                final String[] bible_ACM = getResources().getStringArray(R.array.KOR_ACM);

                for (int i = 0; i < bible_ACM.length; i++) {
                    if (bible_ACM[i].equals(a_strBibleName)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public void setFrag(int n){
        fm = getSupportFragmentManager();
        tran = fm.beginTransaction();

        Animation animation;

        switch(n) {
            case 0:     // *성경읽기
                tran.replace(R.id.fragment, frag_readbible);
                tran.commit();
                actionBar.show();
                break;
            case 1:
//                animation= new AlphaAnimation(1,0);
//                animation.setDuration(1000);
                tran.replace(R.id.fragment, frag_search);
                tran.commit();
                actionBar.hide();

//                btn1_top.setVisibility(View.INVISIBLE);
//                btn2_top.setVisibility(View.INVISIBLE);
//                btn3_top.setVisibility(View.INVISIBLE);
//                imgbtn_moremenu_top.setVisibility(View.INVISIBLE);
//                imgbtn_mp3_top.setVisibility(View.INVISIBLE);
//
//                btn1_top.setAnimation(animation);
//                btn2_top.setAnimation(animation);
//                btn3_top.setAnimation(animation);

                break;
//            case 2:
//                //tran.replace(R.id.fragment, frag_readbible);
//                //tran.commit();
//                Toast.makeText(getApplicationContext(),"준비중인 기능입니다.",Toast.LENGTH_SHORT).show();
//                break;
            case 3:
                tran.replace(R.id.fragment, frag_youtube);
                tran.commit();
                actionBar.hide();
                break;
            case 4:
                tran.replace(R.id.fragment, frag_setting);
                tran.commit();
                actionBar.hide();
                break;

        }

    }

    // 권한관련
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    @TargetApi(Build.VERSION_CODES.M)
    public void checkVerify() {
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            //https://academy.realm.io/kr/posts/android-marshmellow-permission/
            // 권한 요청 거절 이후의 앱권한 획득 순서
            // 사용자가 권한 요청을 한번 거절하면 메서든 반환값이 true가 된다.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            } else {
                // 최초로 권한을 요청하는 경우 ( 첫 실행 )
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {
            // 사용 권한이 있음을 확인한 경우.
        }

    }

    // 권한관련해서 클릭을 하였을경우 발생되는 콜백함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 동의버튼 선택
                } else {
                    // 권한 동의안함 버튼 선택
                    Toast.makeText(this, "동의하지 않으시면 앱을 사용하는데 문제가 발생됩니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }

}
