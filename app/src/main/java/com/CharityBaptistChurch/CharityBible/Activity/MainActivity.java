package com.CharityBaptistChurch.CharityBible.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.IpSecManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.CharityBaptistChurch.CharityBible.Adapter.BibleDBAdapter;
import com.CharityBaptistChurch.CharityBible.Fragment.Fragment_ReadBible;
import com.CharityBaptistChurch.CharityBible.Fragment.Fragment_Search;
import com.CharityBaptistChurch.CharityBible.Fragment.Fragment_Setting;
import com.CharityBaptistChurch.CharityBible.Fragment.Fragment_Youtube;
import com.CharityBaptistChurch.CharityBible.MusicPlayer;
import com.CharityBaptistChurch.CharityBible.R;
import com.CharityBaptistChurch.CharityBible.Util;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class MainActivity extends AppCompatActivity implements Button.OnClickListener {


    // *레이아웃 관련 선언
    private Button m_Btn_Contents, m_Btn_Chapter, m_Btn_SelectBible;
    private ImageButton iBtn_Play;
    private ImageButton m_ImgBtn_Sound;
    private FloatingActionButton m_FABtn_NextChapter;
    private FloatingActionButton m_FABtn_PreviousChapter;

    private FrameLayout m_FL_FATBtn;
    private LinearLayout m_Linear_Miniplayer, m_Linear_top;

    private SeekBar m_SB;
    private TextView m_TV_PlayTime, m_TV_TotalPlayTime;

    Fragment_ReadBible m_fragmentReadBible;

    private String m_strBibleVersion;           //
    private String m_strContexts;
    private String m_strChapter;
    private String m_strCompareBibleVersion;
    private String m_strIsReplace;
    private String m_strFontSize;
    private String m_strIsBlackMode;
    private String m_strIsSleepMode;

    public BibleDBAdapter dbAdapter;

    // *변수선언
    private String m_strContents = "창세기";         // 현재 성경 전서
   // private String m_strChapter = "1";             // 현재 성경 장
    private String m_strLang = "KOR";              // 현재 성경 언어
    private int m_nNowMaxChapter = 0;              // 현재 성경전서의 최대장수
    private int m_nNowIndexContents = 0;           // 현재 성경 전서의 인덱스번호
    static int m_nSelectItem = -1;
    int m_nFragmentFlag = -1;                      // 현재 띄워진 프래그먼트가 어떤 것인지 확인
    private boolean m_bSound;                      // 음악 플레이어 사용을 위한 Flag ( True : 보임 / False : 숨김 )
    private boolean m_bPlay = false;
    static boolean m_bLoadingImg = false;

    MusicPlayer m_mp;


    /*
     * @ Func    : onCreate()
     * @ Param   : ~~~
     * @ Since   : 2020.06.18
     * @ Last    : 2020.06.18
     * @ Author  : mhpark
     * @ Context : Activity가 떳을때 가장 먼저 호출되는 함수.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("TestLog","MainActivity-OnCreate()");
        Log.d("MainActivity","OnCreate()");

        // *로딩이미지 호출

        if(!m_bLoadingImg) {
            Intent intent = new Intent(this, LoadingActivity.class);
            startActivity(intent);
            m_bLoadingImg = true;
        }
        // *액션바 숨기기
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        // *권한 체크하기
        checkVerify();

        // *다운로드 체크
        DownLoadChecker();

        ImageButton iBtn_NextSound = findViewById(R.id.IB_next);        // 다음 음성파일 재생 변수선언
        iBtn_NextSound.setOnClickListener(this);

        ImageButton iBtn_PreviousSound = findViewById(R.id.IB_previous);   // 이전 음성파일 재생 변수선언
        iBtn_PreviousSound.setOnClickListener(this);

        iBtn_Play = findViewById(R.id.IB_play);
        iBtn_Play.setOnClickListener(this);

        m_fragmentReadBible = (Fragment_ReadBible) getSupportFragmentManager().findFragmentById(R.id.frameLayout);

        //*sNavigationDrawer 선언 및 사
        sNavigationDrawer = findViewById(R.id.navigationDrawer);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("성경읽기(Read)",R.drawable.news_bg));
        menuItems.add(new MenuItem("구절검색(Search)",R.drawable.feed_bg));
        menuItems.add(new MenuItem("유튜브(Youtube)",R.drawable.message_bg));
        menuItems.add(new MenuItem("설정(Setting)",R.drawable.music_bg));
        //menuItems.add(new MenuItem("설정(Setting)",R.drawable.music_bg));
        sNavigationDrawer.setMenuItemList(menuItems);
        sNavigationDrawer.setAppbarTitleTV("성경읽기(Read)");

        fragmentClass =  Fragment_ReadBible.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();
        }


        sNavigationDrawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(int position) {
                System.out.println("Position "+position);

                switch (position){
                    case 0:{
                        //   color1 = R.color.red;
                        fragmentClass = Fragment_ReadBible.class;
                        m_nFragmentFlag = 0;
                        TopMenu();
                        break;
                    }
                    case 1:{
                        //    color1 = R.color.orange;
                        fragmentClass = Fragment_Search.class;
                        m_nFragmentFlag = 1;
                        TopMenu();
                        break;
                    }
                    case 2:{
                        // color1 = R.color.green;
                        fragmentClass = Fragment_Youtube.class;
                        m_nFragmentFlag = 2;
                        TopMenu();
                        break;
                    }
                    case 3:{
                        //  color1 = R.color.blue;
                        fragmentClass = Fragment_Setting.class;
                        m_nFragmentFlag = 3;
                        TopMenu();
                        break;
                    }

                }
                sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {

                    @Override
                    public void onDrawerOpened() {

                    }

                    @Override
                    public void onDrawerOpening(){

                    }

                    @Override
                    public void onDrawerClosing(){
                        System.out.println("Drawer closed");

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (fragment != null) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();

                        }
                    }

                    @Override
                    public void onDrawerClosed() {

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        System.out.println("State "+newState);
                    }
                });
            }
        });

        /////////////

        m_Linear_top = findViewById(R.id.LL_Top);
        m_FL_FATBtn = findViewById(R.id.FL_FatButton);

        // *이전 페이지
        m_FABtn_PreviousChapter = findViewById(R.id.FAT_Previous);   // Variable
        m_FABtn_PreviousChapter.setOnClickListener(this);

        // *다음 페이지
        m_FABtn_NextChapter = findViewById(R.id.FAT_Next);           // Variable
        m_FABtn_NextChapter.setOnClickListener(this);

        // *성경전서
        m_Btn_Contents = findViewById(R.id.BTN_contents);

        m_Btn_Contents.setOnClickListener(this);

        // *성경장수
        m_Btn_Chapter = findViewById(R.id.BTN_chapter);
        m_Btn_Chapter.setOnClickListener(this);

        // *성경종류
        m_Btn_SelectBible = findViewById(R.id.BTN_selectbible);
        m_Btn_SelectBible.setOnClickListener(this);

        // *음성파일 재생
        m_ImgBtn_Sound = findViewById(R.id.IB_sound);
        m_ImgBtn_Sound.setOnClickListener(this);

        // *미니플레이어를 숨김
        m_Linear_Miniplayer = findViewById(R.id.lineaMiniplayer);
        m_Linear_Miniplayer.setVisibility(View.INVISIBLE);
        m_bSound = false;

        m_TV_PlayTime = findViewById(R.id.TV_playtime);
        m_TV_PlayTime.setText("00:00");

        m_TV_TotalPlayTime = findViewById(R.id.TV_totalplaytime);
        m_TV_TotalPlayTime.setText("00:00");

        // 재생
        m_SB = findViewById(R.id.SB_playbar);
        m_SB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // 드래그 하는 중에 발생됨
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    m_mp.setSeekto(progress);
                }

                int m = progress / 60000;
                int s = (progress % 60000) / 1000;
                String strTime = String.format("%02d:%02d",m,s);
                m_TV_PlayTime.setText(strTime);

            }
            // 최초 탭하고 드래그 시작할때
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            // 드래그 멈출
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        dbAdapter = new BibleDBAdapter(this);
        dbAdapter.open();

        ArrayList<String> array = dbAdapter.SelectSettingDB();
        if( 0 == array.size())
        {
            dbAdapter.InsertSetting_DB("창세기", "01", "Y", "korHKJV",
                    "engnkjv", "15", "Y", "Y");
            dbAdapter.InsertUnderline_DB("창세기", "01", "20");
        }

        init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TestLog","MainActivity-onStart");
        Log.d("MainActivity","OnStart()");

    //    fm = getSupportFragmentManager();
     //   fragtran = fm.beginTransaction();
     //   fragtran.commit();

    }




    public void setBtnContents(String a_strContents) {
        if(a_strContents.isEmpty())
            return;
        m_Btn_Contents.setText(a_strContents);

        if(dbAdapter != null)
            dbAdapter.UpdateSettingContents(a_strContents);

    }

    public void setBtnChapter(String a_strChapter) {
        if(a_strChapter.isEmpty())
            return;
        m_Btn_Chapter.setText(a_strChapter+"장");

        if(dbAdapter != null)
            dbAdapter.UpdateSettingChapter(a_strChapter);
    }

    public String getBtnContents()
    {
        String strContents = m_Btn_Contents.getText().toString();
        if( !strContents.isEmpty() )
            return strContents;
        return "";
    }

    public String getBtnChapter()
    {
        String strChapter = m_Btn_Chapter.getText().toString();
        if( !strChapter.isEmpty() )
            return strChapter;
        return "";
    }

    public void setContents(String m_strContents) {
        this.m_strContents = m_strContents;
    }

    public void setChapter(String m_strChapter) {
        this.m_strChapter = m_strChapter;
    }

    SNavigationDrawer sNavigationDrawer;
    Class fragmentClass;
    public static Fragment fragment;


    /*
     * @ Func    : TopMenu()
     * @ Param   : x
     * @ Since   : 2020.06.07
     * @ Last    : 2020.06.07
     * @ Author  : mhpark
     * @ Context : 상단 네이게이션바에 있는 오브젝트들 보여줄 것들 필터하는 함수.
     */
    void TopMenu()
    {
        switch(m_nFragmentFlag) {
            case 0: {
                m_Linear_top.setVisibility(View.VISIBLE);
                m_Linear_Miniplayer.setVisibility(View.INVISIBLE);
                m_FL_FATBtn.setVisibility(View.VISIBLE);
            }
            break;
            case 1: {
                m_Linear_top.setVisibility(View.GONE);
                m_Linear_Miniplayer.setVisibility(View.INVISIBLE);
                m_FL_FATBtn.setVisibility(View.INVISIBLE);
                break;
            }
            case 2:
            case 3:
            case 4:
                m_Linear_top.setVisibility(View.GONE);
                m_Linear_Miniplayer.setVisibility(View.INVISIBLE);
                m_FL_FATBtn.setVisibility(View.INVISIBLE);
                break;

            //            case 5:
//                if(mp_sound != null)
//                {
//                    mp_sound.onPlay();
//
//                    if(bPlay)
//                        iBtn_Play.setImageResource(R.drawable.ic_pause_black_24dp);
//                    else
//                        iBtn_Play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
//                }
//                break;
        }
    }

    private Boolean DownloadChecker()
    {
        //String strPath = getFilesDir().getAbsolutePath()+File.separator+"Downloads"+File.separator;
        String strPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+Util.m_strDirectory+File.separator;
        File file = new File(strPath);

        if(!file.exists()) {
            return file.mkdirs();
        }

        if(file.isDirectory())
        {
            Log.d("DownloadChecker","폴더가 존재한다.");
        }else
        {
            Log.d("DownloadChecker","폴더가 없음");
       }

        File[] files = file.listFiles();

        if(files == null)
            return false;

        return files.length > 0;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("TestLog","MainActivity-OnResume()");

        String strChapter;
        String strContents;
        int nChapter = 0;

        Log.d("MainActivity","OnResume()");


        Intent intent = getIntent();
        if( intent.getExtras() != null ) {
            Log.d("MainActivity","intent 값받아옴");
            strChapter = intent.getStringExtra("Chapter");      // 성경 장
            strContents = intent.getStringExtra("Contents");     // 성경 전서
            Log.d("MainActivity",strContents+":"+strChapter);

            if( strChapter != null) {
                nChapter = Integer.parseInt(strChapter);
            }

            if( nChapter > 0)
            {
                m_strChapter = strChapter;
                Log.d("MainActivity","찍힘 Here");
                Toast.makeText(getApplicationContext(), strContents,Toast.LENGTH_SHORT).show();
            }

            if(strContents != null)
            {
                m_strContents = strContents;
            }

            m_fragmentReadBible = (Fragment_ReadBible) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
            assert m_fragmentReadBible != null;
            m_fragmentReadBible.init(m_strContents,m_strChapter);
            setBtnContents(m_strContents);
            setBtnChapter(m_strChapter);
            init();


            getIntent().removeExtra("Contents");
            getIntent().removeExtra("Chapter");
        }

    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TestLog","MainActivity-OnPause()");
        Log.d("MainActivity","OnPause()");
    }

    // *성경 전서 인덱스, 성경 장수 값들 초기화
    private void init()
    {


        // 다운로드된 파일이 있는지 체크
        DownloadChecker();

        String[] strBibleContents = getResources().getStringArray(R.array.KOR);
        String[] strBibleMaxLen = getResources().getStringArray(R.array.KOR_LEN);

        for(int i = 0; i < strBibleContents.length; i ++) {

            // 현재 성경전서과 같은 인데스 위치 찾음
            if(strBibleContents[i].equals(m_strContents)) {
                m_nNowIndexContents = i;
                m_nNowMaxChapter = Integer.parseInt(strBibleMaxLen[i]);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode)
        {
            case 1:
                assert data != null;
                String key = data.getStringExtra("key");
                break;
            case 2:
                break;
        }

    }


    @Override
    public void onClick(View v) {

        m_fragmentReadBible = (Fragment_ReadBible) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        int nChapter = Integer.parseInt(m_strChapter);
        switch (v.getId())
        {

            case R.id.FAT_Previous:  // *이전 장
                if(nChapter > 1) {
                    m_strChapter = Integer.toString(nChapter - 1);
                    m_fragmentReadBible.init(m_strContents, m_strChapter);
                    init();
                    setBtnContents(m_strContents);
                    setBtnChapter(m_strChapter);
                }else
                {
                    if (m_nNowIndexContents > 0) {
                        m_nNowIndexContents--;
                        String[] strBibleContents = getResources().getStringArray(R.array.KOR);

                        // 이전 성경전서로 이동
                        m_strContents = strBibleContents[m_nNowIndexContents];
                        init();
                        m_strChapter = Integer.toString(m_nNowMaxChapter);
                        m_fragmentReadBible.init(m_strContents, m_strChapter);

                        setBtnContents(m_strContents);
                        setBtnChapter(m_strChapter);
                    }else
                    {
                        Toast.makeText(getApplicationContext(),"첫 장입니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                //getBibleList();
                break;
            case R.id.FAT_Next:  // *다음 장

                if(nChapter < m_nNowMaxChapter) {
                    m_strChapter = Integer.toString(nChapter + 1);
                    m_fragmentReadBible.init(m_strContents, m_strChapter);

                    init();
                    setBtnContents(m_strContents);
                    setBtnChapter(m_strChapter);

                }else
                {
                    // 다음 성경전서 찾기
                    if( m_nNowIndexContents < 65) {
                        m_nNowIndexContents++;
                        String[] strBibleContents = getResources().getStringArray(R.array.KOR);
                        m_strContents = strBibleContents[m_nNowIndexContents];
                        m_strChapter = "1";

                        m_fragmentReadBible.init(m_strContents, m_strChapter);
                        init();
                        setBtnContents(m_strContents);
                        setBtnChapter(m_strChapter);
                    }else
                    {
                        Toast.makeText(getApplicationContext(),"마지막 장 입니다..",Toast.LENGTH_SHORT).show();
                    }

                }


                break;
            case R.id.BTN_contents: // *성경 전서

                Intent it_1 = new Intent(getApplicationContext(), BibleTabActivity.class);
                startActivity(it_1);
               // init();


                //setFrag(3);
                break;
            case R.id.BTN_chapter:  // *성경 장

                Intent it_2 = new Intent(getApplicationContext(), ChapterTableActivity.class);
                String strBible;
                int nBiblePosition = findBiblePosition(m_strContents, "KOR");
                final String[] bible = getResources().getStringArray(R.array.KOR);
                strBible = bible[nBiblePosition];
                nBiblePosition =  Util.numOfChapters[nBiblePosition];

                it_2.putExtra("bookPosition", nBiblePosition);
                it_2.putExtra("bookName", strBible);
                startActivity(it_2);
             //   init();

                //setFrag(4);
                break;

            case R.id.BTN_selectbible:


                final CharSequence[] oItemsA = { "킹제임스 흠정역(HKJV)", "개역개정(NKRV)","쉬운성경(Easy)" };
                final CharSequence[] oItemsB = { "HKJV", "NKRV","Easy" };


                AlertDialog.Builder oDialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);


                oDialog.setTitle("읽을 성경을 선택해주세요").setSingleChoiceItems(oItemsA, m_nSelectItem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_nSelectItem = which;
                    }
                }).setNeutralButton("선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(m_nSelectItem > 0)
                            m_Btn_SelectBible.setText(oItemsB[m_nSelectItem]);
                    }
                }).setCancelable(false).show();


                break;


            case R.id.IB_sound:

                if(m_bSound) {    // 사운드 버튼 Off

                    m_Linear_Miniplayer.setVisibility(View.INVISIBLE);
                    m_bSound = false;

                    m_ImgBtn_Sound.setSelected(false);

                    m_FABtn_NextChapter.animate().translationY(0).start();
                    m_FABtn_PreviousChapter.animate().translationY(0).start();

                    if(m_mp != null) {
                        m_mp.clearMediaPlayer();
                        m_mp = null;
                    }
                }
                else {          // 사운드 버튼 On
                    m_Linear_Miniplayer.setVisibility(View.VISIBLE);
                    m_bSound = true;

                    m_ImgBtn_Sound.setSelected(true);

                    m_FABtn_NextChapter.animate().translationY(-180).start();
                    m_FABtn_PreviousChapter.animate().translationY(-180).start();

                    String strSoundPath = Environment.getExternalStorageDirectory()+File.separator+Util.m_strDirectory+File.separator+"BibleSound";
                    if(m_mp == null) {
                        m_mp = new MusicPlayer(strSoundPath);
                        m_mp.initMusic(
                                m_fragmentReadBible.getM_strBibleVersion(),
                                m_fragmentReadBible.getM_strContexts(),
                                Integer.parseInt(m_fragmentReadBible.getM_strChapter()) );
                    }
                }
                break;
            case R.id.IB_next:
                if(m_mp != null)
                {
                    m_mp.onPreview();
                    m_SB.setMax(m_mp.getDuration());

                    int nData = m_mp.getDuration();
                    int m = nData / 60000;
                    int s = (nData % 60000) / 1000;
                    String strTime = String.format("%02d:%02d",m,s);
                    m_TV_TotalPlayTime.setText(strTime);
                }
                break;
            case R.id.IB_play:
                if(m_mp != null)
                {
                    m_bPlay = !m_bPlay;

                    if(m_bPlay) { // 재생중일때
                        iBtn_Play.setImageResource(R.drawable.ic_pause_black_24dp);
                        m_mp.onPlay();
                        m_SB.setMax(m_mp.getDuration());

                        int nData = m_mp.getDuration();
                        int m = nData / 60000;
                        int s = (nData % 60000) / 1000;
                        String strTime = String.format("%02d:%02d",m,s);
                        m_TV_TotalPlayTime.setText(strTime);

                        Thread();
                    }
                    else { // 재생중 아닐때
                        iBtn_Play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        m_mp.onPause();
                    }
                }
                break;
            case R.id.IB_previous:
                if(m_mp != null)
                {
                    m_mp.onPrevious();
                    m_SB.setMax(m_mp.getDuration());

                    int nData = m_mp.getDuration();
                    int m = nData / 60000;
                    int s = (nData % 60000) / 1000;
                    String strTime = String.format("%02d:%02d",m,s);
                    m_TV_TotalPlayTime.setText(strTime);
                }
                break;

        }

    }

    public void Thread(){
        Runnable task = new Runnable(){
            @Override
            public void run() {

                while(m_mp.IsPlaying())
                {
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    m_SB.setProgress(m_mp.getCurrentPosition());
                }

            }
        };

        Thread thread = new Thread(task);
        thread.start();
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

    /*
     * @ Func    : getBibleList()
     * @ Param   : x
     * @ Since   : 2020.04.22
     * @ Last    : 2020.04.22
     * @ Author  : mhpark
     * @ Context : 성경 경로에있는 성경 리스트 얻어오기
     */
    public ArrayList<String> getBibleList() {

        ArrayList<String> arrayList = new ArrayList<>();

        //String strPath = getFilesDir().getAbsolutePath() + File.separator + "Bibles" + File.separator;
        String strPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+Util.m_strDirectory+File.separator;
        Log.d("hun ", "MainActivity::getBibleList Path[" + strPath + "]");
        File file = new File(strPath);
        File[] filelist = file.listFiles();

        assert filelist != null;
        for(int i = 0; i < filelist.length; i++)
        {
            if( filelist[i].getName().indexOf(".cbk") > 0 )
            {

                arrayList.add(filelist[i].getName());

                try {

                    String strName = filelist[i].getName();
                    int nIndex = strName.indexOf('.');
                    strName = strName.substring(0,nIndex);


                    ZipFile zip = new ZipFile(filelist[i].getAbsoluteFile(), ZipFile.OPEN_READ);

                    int nSize = zip.size();

                    String strFile = strName+"01_01"+".lfb";
                    ZipEntry entry = zip.getEntry(strFile);
                    InputStream inputStream = zip.getInputStream(entry);

                    Scanner sc = new Scanner(inputStream);
                    while(sc.hasNext())
                    {
                        arrayList.add(sc.nextLine());
                    }



                    zip.close();

                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }

        return arrayList;

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

                if(Build.VERSION.SDK_INT>22) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }

            } else {
                if(Build.VERSION.SDK_INT>22) {
                    // 최초로 권한을 요청하는 경우 ( 첫 실행 )
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
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
            }
        }
    }


    private void DownLoadChecker()
    {
        ArrayList<String> arrayBible = getBibleList();
        if(arrayBible.size() == 0) {

                //String url = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2Fb4LL01%2Fbtqv809t5X9%2FVY5qybXsG3TKsXbIiSD1KK%2Fimg.jpg";
            DownloadFileAsync df = new DownloadFileAsync(this);
            df.execute("","1","1");
        }
    }

    static class DownloadFileAsync extends AsyncTask<String, String, String> {

        private ProgressDialog mDlg;
        private Context mContext;

        public DownloadFileAsync(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            mDlg = new ProgressDialog(mContext);
            mDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDlg.setMessage("Start");
            mDlg.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            int count = 0;

            try {


                ArrayList<String> array = new ArrayList<>();
                array.add("http://175.198.115.173:8000/FileDownload/engnkjv.cbk");
                array.add("http://175.198.115.173:8000/FileDownload/korHKJV.cbk");
                array.add("http://175.198.115.173:8000/FileDownload/kornkrv.cbk");

                for(int i = 0; i < array.size(); i++) {
                    publishProgress("max", "100");
                    Thread.sleep(100);
                    URL url = new URL(array.get(i));
                    URLConnection conexion = url.openConnection();
                    conexion.connect();

                    int lenghtOfFile = conexion.getContentLength();
                    Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                    int nIndex = array.get(i).indexOf(".cbk");
                    nIndex = array.get(i).lastIndexOf("/", nIndex);
                    String strName = array.get(i).substring(nIndex+1);

                    String strPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Util.m_strDirectory + File.separator + strName;
                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream(strPath);

                    byte[] data = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        //publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                        publishProgress("progress", "" + (int) ((total * 100) / lenghtOfFile), "테스트중");
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                }

                // 작업이 진행되면서 호출하며 화면의 업그레이드를 담당하게 된다
                //publishProgress("progress", 1, "Task " + 1 + " number");

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

            // 수행이 끝나고 리턴하는 값은 다음에 수행될 onProgressUpdate 의 파라미터가 된다
            return null;
        }


        @Override
        protected void onProgressUpdate(String... progress) {
            if (progress[0].equals("progress")) {
                mDlg.setProgress(Integer.parseInt(progress[1]));
                mDlg.setMessage(progress[2]);
            } else if (progress[0].equals("max")) {
                mDlg.setMax(Integer.parseInt(progress[1])); // 최대값
            }
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String unused) {
            mDlg.dismiss();
            //Toast.makeText(mContext, Integer.toString(result) + " total sum",
            //Toast.LENGTH_SHORT).show();
        }
    }



}
