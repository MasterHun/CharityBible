package com.CharityBaptistChurch.CharityBible.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
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
import com.CharityBaptistChurch.CharityBible.R;


public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    private ListView listviewVerse;
    private Button btn1_top, btn2_top, btn3_top;
    private Button btn1_bottom, btn2_bottom, btn3_bottom, btn4_bottom, btn5_bottom;
    private ImageButton imgbtn_mp3_top, imgbtn_moremenu_top;
    private LinearLayout linear_bottom;
    private boolean bMusicbuttonflag;

    public Fragment_ReadBible fr = new Fragment_ReadBible();
  //  FragmentManager fm = getSupportFragmentManager();

    Fragment_ReadBible frag_readbible;
    Fragment_Search frag_search;
    Fragment_Youtube frag_youtube;
    Fragment_Setting frag_setting;

    FragmentManager fm;
    FragmentTransaction tran;

    ActionBar actionBar;

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

        imgbtn_mp3_top = (ImageButton) findViewById(R.id.imgbtn_mp3_top);
        imgbtn_mp3_top.setOnClickListener(this);
        imgbtn_moremenu_top = (ImageButton) findViewById(R.id.imgbtn_moremenu_top);
        btn1_top = (Button) findViewById(R.id.btn1_top);
        btn1_top.setOnClickListener(this);
        btn1_top.setText("레위기▼");
        btn2_top = (Button) findViewById(R.id.btn2_top);
        btn2_top.setOnClickListener(this);
        btn2_top.setText("1장▼");
        btn3_top = (Button) findViewById(R.id.btn3_top);
        btn3_top.setOnClickListener(this);
        btn3_top.setText("KORHKJV▼");

        //        return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();

        int nChapter = 0 ;
        String strSection = null;
        Log.i("Test_Debuge","OnResume");

        Intent intent = getIntent();
        if( intent.getExtras() != null ) {
            Log.i("Test_Debuge","intent 값받아옴");
         //   nChapter = intent.getint("Chapter");
            strSection = intent.getStringExtra("Section");
            Log.i("Test_Debuge",strSection+":"+nChapter);
        }
        if( nChapter > 0)
        {
            Log.i("Test_Debuge","찍힘 Here");
            Toast.makeText(getApplicationContext(), strSection,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkVerify();
     //   listviewVerse = (ListView) findViewById(R.id.listview1);

        linear_bottom = (LinearLayout) findViewById(R.id.linearbottom);
        bMusicbuttonflag = false;

        btn1_bottom = (Button) findViewById(R.id.btn1_bottom);
        btn1_bottom.setOnClickListener(this);
        btn1_bottom.setText("성경읽기");

        btn2_bottom = (Button) findViewById(R.id.btn2_bottom);
        btn2_bottom.setOnClickListener(this);
        btn2_bottom.setText("검색");

        btn3_bottom = (Button) findViewById(R.id.btn3_bottom);
        btn3_bottom.setOnClickListener(this);
        btn3_bottom.setText("부록");

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


//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();
//        transaction.replace(R.id.fragment, fr);
//        transaction.commit();


//        VerseListAdapter adapter;
//        adapter = new VerseListAdapter();

//        listviewVerse.setAdapter(adapter);

//        ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.listfragment);
//        listFragment.setListAdapter(adapter);
//
//        // #테스트 성경구절 삽입
//        adapter.addItem("1  ","예수 그리스도의 종 바울은 사도로 부르심을 받아 [하나님]의 복음을 위해 구별되었는데 ");
//        adapter.addItem("2  ","(이 복음은 그분께서 자신의 대언자들을 통하여 거룩한 성경 기록들에 미리 약속하신 것으로)");
//        adapter.addItem("3  ","자신의 [아들] 예수 그리스도 우리 [주]에 관한 것이라. 그분께서는 육체로는 다윗의 씨에서 나셨고");
//        adapter.addItem("4  ","거룩함의 영으로는 죽은 자들로부터 부활하심으로써 [하나님]의 [아들]로 권능 있게 밝히 드러나셨느니라.");
//        adapter.addItem("5  ","그분으로 말미암아 우리가 은혜와 사도직을 받아 그분의 이름을 위하여 모든 민족들 가운데서 믿음에 순종하게 하였나니");
//        adapter.addItem("6  ","너희도 그들 가운데서 예수 그리스도의 부르심을 받았느니라.");
//        adapter.addItem("7  ","바울은, 로마에서 [하나님]께 사랑을 받고 성도로 부르심을 받은 모든 사람에게 편지하노니 [하나님] 우리 [아버지]와 [주] 예수 그리스도로부터 은혜와 평강이 너희에게 있기를 원하노라.");
//        adapter.addItem("8  ","먼저 너희 모두로 인하여 예수 그리스도를 통해 나의 [하나님]께 감사하노니 이는 너희의 믿음이 온 세상에 두루 전하여졌기 때문이라.");
//        adapter.addItem("9  ","내가 그분의 [아들]의 복음 안에서 내 영으로 섬기는 [하나님]께서 내 증인이 되시거니와 내가 기도할 때에 언제나 너희에 관하여 끊임없이 말하며");
//        adapter.addItem("10  ","어찌하든지 이제라도 마침내 [하나님]의 뜻에 따라 순탄한 여정을 얻어 너희에게 가게 되기를 간구하노라.");
//        adapter.addItem("11  ","내가 너희를 간절히 보고자 함은 내가 너희에게 어떤 영적 선물을 나누어 주어 너희를 굳게 세우고자 함이니");
//        adapter.addItem("12  ","이것은 곧 너희와 나 사이의 공통된 믿음으로 말미암아 내가 너희와 함께 위로를 받고자 함이라.");
//        adapter.addItem("13  ","형제들아, 이제 나는 너희가 이것을 모르기를 원치 아니하노니 곧 내가 너희 가운데서도 다른 이방인들 가운데서처럼 어떤 열매를 얻기 위해 여러 번 너희에게 가고자 하였으나 (이제껏 막혔도다.)");
//        adapter.addItem("14  ","나는 그리스인이나 바바리인이나 지혜 있는 자나 지혜 없는 자에게 다 빚진 자니라.");
//        adapter.addItem("15  ","그러므로 내 안에 있는 분량대로 나는 또한 로마에 있는 너희에게 복음을 선포할 준비가 되어 있노라.");
//        adapter.addItem("16  ","내가 그리스도의 복음을 부끄러워하지 아니하노니 이는 그 복음이 믿는 모든 자를 구원에 이르게 하는 [하나님]의 권능이기 때문이라. 먼저는 유대인에게요 또한 그리스인에게로다.");
//        adapter.addItem("17  ","복음에는 [하나님]의 의가 믿음에서 믿음까지 계시되어 있나니 이것은 기록된바, 의인은 믿음으로 살리라, 함과 같으니라.");


    }
    private void init()
    {

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
            case R.id.btn3_bottom:
                setFrag(2);
                break;
            case R.id.btn4_bottom:
                setFrag(3);
                break;
            case R.id.btn5_bottom:
                setFrag(4);
                break;
            case R.id.btn1_top:
                Intent it = new Intent(getApplicationContext(), BibleTabActivity.class);
                startActivity(it);
                break;
            case R.id.btn2_top:
                //Intent it = new Intent(getApplicationContext(), );
                break;
            case R.id.imgbtn_mp3_top:
                if(bMusicbuttonflag) {
                    linear_bottom.setVisibility(View.INVISIBLE);
                    bMusicbuttonflag = false;
                }
                else {
                    linear_bottom.setVisibility(View.VISIBLE);
                    bMusicbuttonflag = true;
                }

                break;
        }

    }

    public void setFrag(int n){
        fm = getSupportFragmentManager();
        tran = fm.beginTransaction();

        Animation animation;

        switch(n) {
            case 0:
//                animation= new AlphaAnimation(0,1);
 //               animation.setDuration(1000);
                tran.replace(R.id.fragment, frag_readbible);
                tran.commit();
//                btn1_top.setVisibility(View.VISIBLE);
//                btn2_top.setVisibility(View.VISIBLE);
//                btn3_top.setVisibility(View.VISIBLE);
//                imgbtn_moremenu_top.setVisibility(View.VISIBLE);
//                imgbtn_mp3_top.setVisibility(View.VISIBLE);
//                btn1_top.setAnimation(animation);
//                btn2_top.setAnimation(animation);
//                btn3_top.setAnimation(animation);
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
            case 2:
                //tran.replace(R.id.fragment, frag_readbible);
                //tran.commit();
                Toast.makeText(getApplicationContext(),"준비중인 기능입니다.",Toast.LENGTH_SHORT).show();
                break;
            case 3:
                tran.replace(R.id.fragment, frag_youtube);
                tran.commit();
                break;
            case 4:
                tran.replace(R.id.fragment, frag_setting);
                tran.commit();
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
