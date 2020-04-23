package com.CharityBaptistChurch.CharityBible.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.CharityBaptistChurch.CharityBible.Adapter.BibleDBAdapter;
import com.CharityBaptistChurch.CharityBible.Fragment.Fragment_ReadBible;
import com.CharityBaptistChurch.CharityBible.Fragment.Fragment_Search;
import com.CharityBaptistChurch.CharityBible.Fragment.Fragment_Setting;
import com.CharityBaptistChurch.CharityBible.Fragment.Fragment_Youtube;
import com.CharityBaptistChurch.CharityBible.MusicPlayer;
import com.CharityBaptistChurch.CharityBible.R;
import com.CharityBaptistChurch.CharityBible.Util;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


public class MainActivity extends AppCompatActivity implements Button.OnClickListener {



    private ImageButton ibtn1_bottom, ibtn2_bottom, ibtn8_bottom;
    private Button  btn3_bottom, btn4_bottom, btn5_bottom,btn6_bottom,btn7_bottom;
    private ImageButton ibtn_mp3_top, ibtn_moremenu_top, ibtnPrevious, ibtnPreview, ibtnPlay;
    private LinearLayout linear_Miniplayer, linear_top;
    private boolean bMusicbuttonflag;


    public BibleDBAdapter dbAdapter;



    private String m_strContents = "창세기";        // 현재 성경 전서
    private String m_strChapter = "1";              // 현재 성경 장
    public String m_strLang = "KOR";               // 현재 성경 언어

    private int m_nNowMaxChapter = 0;              // 현재 성경전서의 최대장수
    private int m_nNowIndexContents = 0;           // 현재 성경 전서의 인덱스번호


    static int m_nSelectItem = -1;

    FragmentManager fm;
    FragmentTransaction tran;

    MusicPlayer mp;
    boolean bPlayFlge = false;

    int nFragmentFlag = -1;      // 현재 띄워진 프래그먼트가 어떤 것인지 확인

    Fragment_ReadBible fragmentReadBible;

    @Override
    protected void onStart() {
        super.onStart();

        Log.i("MainActivity","OnStart()");

        fm = getSupportFragmentManager();
       tran = fm.beginTransaction();

    }




    public void setBtnContents(String a_strContents) {
        if(a_strContents.isEmpty())
            return;
        btn4_bottom.setText(a_strContents);

        if(dbAdapter != null)
            dbAdapter.UpdateSettingContents(a_strContents);

    }

    public void setBtnChapter(String a_strChapter) {
        if(a_strChapter.isEmpty())
            return;
        btn5_bottom.setText(a_strChapter+"장");

        if(dbAdapter != null)
            dbAdapter.UpdateSettingChapter(a_strChapter);
    }

    public String getBtnContents()
    {
        String strContents= btn4_bottom.getText().toString();
        if( !strContents.isEmpty() )
            return strContents;
        return "";
    }

    public String getBtnChapter()
    {
        String strChapter = btn5_bottom.getText().toString();
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

    void TopMenu()
    {
        switch(nFragmentFlag) {
            case 0: {

             //   Toast.makeText(getApplicationContext(),"Read",Toast.LENGTH_SHORT).show();
                btn3_bottom.setVisibility(View.INVISIBLE);
                btn7_bottom.setVisibility(View.INVISIBLE);

                linear_top.setVisibility(View.VISIBLE);
            }
            break;
            case 1: {
              //  Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_SHORT).show();
                linear_top.setVisibility(View.GONE);
                break;
            }
            case 2:
              //  Toast.makeText(getApplicationContext(),"Youtube",Toast.LENGTH_SHORT).show();
                linear_top.setVisibility(View.GONE);
                break;
            case 3:
             //   Toast.makeText(getApplicationContext(),"Setting",Toast.LENGTH_SHORT).show();
                linear_top.setVisibility(View.GONE);
                break;
            case 4:
                break;
            case 5:
                if(mp != null)
                {
                    mp.onPlay();

                    if(bPlayFlge)
                        ibtnPlay.setImageResource(R.drawable.ic_pause_black_24dp);
                    else
                        ibtnPlay.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }
                break;
        }
    }

    private Boolean DownloadChecker()
    {
        String strPath = getFilesDir().getAbsolutePath()+File.separator+"Bibles";

        File file = new File(strPath);

        File files[] = file.listFiles();

        if(files.length > 0)
        {
            return true;
        }else
        {

        }

        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DownloadChecker();

        ActionBar actionBar = getSupportActionBar();

        actionBar.hide();
        checkVerify();
        Log.i("MainActivity","OnCreate()");
        //   listviewVerse = (ListView) findViewById(R.id.listview1);

        ibtnPreview = (ImageButton) findViewById(R.id.ibtnPreview);
        ibtnPreview.setOnClickListener(this);
        ibtnPrevious = (ImageButton) findViewById(R.id.ibtnPrevious);
        ibtnPrevious.setOnClickListener(this);
        ibtnPlay = (ImageButton) findViewById(R.id.ibtnPlay);
        ibtnPlay.setOnClickListener(this);

        fragmentReadBible = (Fragment_ReadBible) getSupportFragmentManager().findFragmentById(R.id.frameLayout);

        sNavigationDrawer = findViewById(R.id.navigationDrawer);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("성경읽기(Read)",R.drawable.news_bg));
        menuItems.add(new MenuItem("구절검색(Search)",R.drawable.feed_bg));
        menuItems.add(new MenuItem("유튜브(Youtube)",R.drawable.message_bg));
        menuItems.add(new MenuItem("설정(Setting)",R.drawable.music_bg));
        sNavigationDrawer.setMenuItemList(menuItems);
        sNavigationDrawer.setAppbarTitleTV("성경읽기(Read)");
        //sNavigationDrawer.setAppbarTitleTextColor(R.color.colorRed);

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
                        nFragmentFlag = 0;
                        TopMenu();
                        break;
                    }
                    case 1:{
                    //    color1 = R.color.orange;
                        fragmentClass = Fragment_Search.class;
                        nFragmentFlag = 1;
                        TopMenu();
                        break;
                    }
                    case 2:{
                       // color1 = R.color.green;
                        fragmentClass = Fragment_Youtube.class;
                        nFragmentFlag = 2;
                        TopMenu();
                        break;
                    }
                    case 3:{
                      //  color1 = R.color.blue;
                        fragmentClass = Fragment_Setting.class;
                        nFragmentFlag = 3;
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
        linear_Miniplayer = (LinearLayout) findViewById(R.id.lineaMiniplayer);
        bMusicbuttonflag = false;

        linear_top = (LinearLayout) findViewById(R.id.lineartop);


        // *이전 페이지
        ibtn1_bottom = (ImageButton) findViewById(R.id.btn1_bottom);
        ibtn1_bottom.setOnClickListener(this);

        // *다음 페이지
        ibtn2_bottom = (ImageButton) findViewById(R.id.btn2_bottom);
        ibtn2_bottom.setOnClickListener(this);

        //
        btn3_bottom = (Button) findViewById(R.id.btn3_bottom);
        btn3_bottom.setOnClickListener(this);
        btn3_bottom.setVisibility(View.INVISIBLE);

        // *성경전서
        btn4_bottom = (Button)  findViewById(R.id.btn4_bottom);
        btn4_bottom.setOnClickListener(this);

        // *성경장수
        btn5_bottom = (Button) findViewById(R.id.btn5_bottom);
        btn5_bottom.setOnClickListener(this);

        // *성경종류
        btn6_bottom = (Button) findViewById(R.id.btn6_bottom);
        btn6_bottom.setOnClickListener(this);

        //
        btn7_bottom = (Button) findViewById(R.id.btn7_bottom);
        btn7_bottom.setOnClickListener(this);
        btn7_bottom.setVisibility(View.INVISIBLE);

        // *음성파일 재생
        ibtn8_bottom = (ImageButton) findViewById(R.id.btn8_bottom);
        ibtn8_bottom.setOnClickListener(this);

        // *미니플레이어를 숨김
        bMusicbuttonflag = false;
        linear_Miniplayer.setVisibility(View.INVISIBLE);



        dbAdapter = new BibleDBAdapter(this);
        dbAdapter.open();

        init();


    }
    @Override
    protected void onResume() {
        super.onResume();


        String strChapter;
        String strContents;
        int nChapter = 0;

        Log.i("MainActivity","OnResume()");


        Intent intent = getIntent();
        if( intent.getExtras() != null ) {
            Log.i("MainActivity","intent 값받아옴");
            strChapter = intent.getStringExtra("Chapter");      // 성경 장
            strContents = intent.getStringExtra("Contents");     // 성경 전서
            Log.i("MainActivity",strContents+":"+strChapter);

            if(!strChapter.isEmpty()) {
                nChapter = Integer.parseInt(strChapter);
            }

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

            fragmentReadBible = (Fragment_ReadBible) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
            fragmentReadBible.init(m_strContents,m_strChapter);
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
        Log.i("MainActivity","OnPause()");
    }

    // *성경 전서 인덱스, 성경 장수 값들 초기화
    private void init()
    {
        String[] strBibleContents = getResources().getStringArray(R.array.KOR);
        String[] strBibleMaxLen = getResources().getStringArray(R.array.KOR_LEN);

        for(int i = 0; i < strBibleContents.length; i ++) {

            // 현재 성경전서과 같은 인데스 위치 찾음
            if( true == strBibleContents[i].equals(m_strContents) ) {
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
                String key = data.getStringExtra("key");
                break;
            case 2:
                break;
        }

    }

    @Override
    public void onClick(View v) {

        fragmentReadBible = (Fragment_ReadBible) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        int nChapter = Integer.parseInt(m_strChapter);
        switch (v.getId())
        {

            case R.id.btn1_bottom:  // *이전 장
                if(nChapter > 1) {
                    m_strChapter = Integer.toString(nChapter - 1);
                    fragmentReadBible.init(m_strContents, m_strChapter);
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
                        fragmentReadBible.init(m_strContents, m_strChapter);

                        setBtnContents(m_strContents);
                        setBtnChapter(m_strChapter);
                    }else
                    {
                        Toast.makeText(getApplicationContext(),"첫 장입니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                getBibleList();
                break;
            case R.id.btn2_bottom:  // *다음 장

                if(nChapter < m_nNowMaxChapter) {
                    m_strChapter = Integer.toString(nChapter + 1);
                    fragmentReadBible.init(m_strContents, m_strChapter);

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

                        fragmentReadBible.init(m_strContents, m_strChapter);
                        init();
                        setBtnContents(m_strContents);
                        setBtnChapter(m_strChapter);
                    }else
                    {
                        Toast.makeText(getApplicationContext(),"마지막 장 입니다..",Toast.LENGTH_SHORT).show();
                    }

                }


                break;
            case R.id.btn3_bottom:

                break;
                //setFrag(2);
            case R.id.btn4_bottom: // *성경 전서

                Intent it_1 = new Intent(getApplicationContext(), BibleTabActivity.class);
                startActivity(it_1);
               // init();


                //setFrag(3);
                break;
            case R.id.btn5_bottom:  // *성경 장

                Intent it_2 = new Intent(getApplicationContext(), ChapterTableActivity.class);
                String strBible = "";
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

            case R.id.btn6_bottom:


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
                            btn6_bottom.setText(oItemsB[m_nSelectItem]);
                    }
                }).setCancelable(false).show();


                break;


            case R.id.btn8_bottom:
                if(bMusicbuttonflag) {
                    linear_Miniplayer.setVisibility(View.INVISIBLE);
                    bMusicbuttonflag = false;
                }
                else {
                    linear_Miniplayer.setVisibility(View.VISIBLE);
                    bMusicbuttonflag = true;
                }
                break;

//            case R.id.btn1_top:
//                Intent it_1 = new Intent(getApplicationContext(), BibleTabActivity.class);
//                startActivity(it_1);
//                break;
//            case R.id.btn2_top:
//                Intent it_2 = new Intent(getApplicationContext(), ChapterTableActivity.class);
//
//
//                String strBible = "";
//                int nBiblePosition = findBiblePosition(m_strContents, "KOR");
//                final String[] bible = getResources().getStringArray(R.array.KOR);
//                strBible = bible[nBiblePosition];
//                nBiblePosition =  Util.numOfChapters[nBiblePosition];
//
//                it_2.putExtra("bookPosition", nBiblePosition);
//                it_2.putExtra("bookName", strBible);
//
//                startActivity(it_2);
//                break;
//            case R.id.ibtn_mp3_top:
//                if(bMusicbuttonflag) {
//                    linear_Miniplayer.setVisibility(View.INVISIBLE);
//                    bMusicbuttonflag = false;
//
//
//                }
//                else {
//                    linear_Miniplayer.setVisibility(View.VISIBLE);
//                    bMusicbuttonflag = true;
//
//                    File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//                    String strDir = file.getAbsolutePath();
//
//                    String strPath  = strDir + "/bible_mp3";
//                    mp = new MusicPlayer(strPath);
//                    mp.initMusic();
//                }
//                break;
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

        String strPath = getFilesDir().getAbsolutePath() + File.separator + "Bibles" + File.separator;
        Log.d("hun ", "MainActivity::getBibleList Path[" + strPath + "]");
        File file = new File(strPath);
        File filelist[] = file.listFiles();

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

                }
                catch (ZipException e)
                {
                    e.printStackTrace();
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }

        return arrayList;

    }



    public void setFrag(int n){
        fm = getSupportFragmentManager();
        tran = fm.beginTransaction();

        Animation animation;

//        switch(n) {
//            case 0:     // *성경읽기
//                tran.replace(R.id.fragment, frag_readbible);
//                tran.commit();
//                actionBar.show();
//                break;
//            case 1:
//                animation= new AlphaAnimation(1,0);
//                animation.setDuration(1000);
//                tran.replace(R.id.fragment, frag_search);
//                tran.commit();
//                actionBar.hide();

//                btn1_top.setVisibility(View.INVISIBLE);
//                btn2_top.setVisibility(View.INVISIBLE);
//                btn3_top.setVisibility(View.INVISIBLE);
//                imgbtn_moremenu_top.setVisibility(View.INVISIBLE);
//                imgbtn_mp3_top.setVisibility(View.INVISIBLE);
//
//                btn1_top.setAnimation(animation);
//                btn2_top.setAnimation(animation);
//                btn3_top.setAnimation(animation);

//                break;
//            case 2:
//                //tran.replace(R.id.fragment, frag_readbible);
//                //tran.commit();
//                Toast.makeText(getApplicationContext(),"준비중인 기능입니다.",Toast.LENGTH_SHORT).show();
//                break;
//            case 3:
//                tran.replace(R.id.fragment, frag_youtube);
//                tran.commit();
//                actionBar.hide();
//                break;
//            case 4:
//                tran.replace(R.id.fragment, frag_setting);
//                tran.commit();
//                actionBar.hide();
//                break;

       // }

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
                return;
            }
        }
    }



}
