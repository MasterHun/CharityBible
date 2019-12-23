package com.CharityBaptistChurch.CharityBible.Activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.CharityBaptistChurch.CharityBible.R;
import com.CharityBaptistChurch.CharityBible.Util;

/*
* Type : Activity
* Contents : 성경을 선택하는 액티비티
* */
public class BibleTabActivity extends AppCompatActivity implements View.OnClickListener {

    private final int TABLE_BUTTON_WIDTH = 7;


    // 간략하게 보기 탭
    private Button mTextSimpleTestament;
    private ListView mListSimpleTestament;

    // 리스트로 보기 탭
    private Button mTextListTestament;
    private TableLayout mTableLayout;

    private ViewFlipper mFlipper;

    boolean mIsSelected = false;

    // 두번클릭 방지를 위한 코드
    private static final long MIN_CLICK_INTERVAL=600;
    private long mLastClickTime;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bibletab);

        mTextSimpleTestament = (Button) findViewById(R.id.simple_testament);
        mListSimpleTestament = (ListView) findViewById(R.id.list);

        mTextListTestament = (Button) findViewById(R.id.list_testament);
        mTableLayout = (TableLayout) findViewById(R.id.table);
        mTableLayout.setPadding(50, 20, 50, 0);

        mFlipper = (ViewFlipper) findViewById(R.id.flipper);
        mListSimpleTestament.setOnItemClickListener(listener);

        mListSimpleTestament.setTextFilterEnabled(true);

        // 초기 버튼 색상 지정
        setButtonStyle();

        // 성서 클릭이벤트 처리
        mTextSimpleTestament.setOnClickListener(this);
        mTextListTestament.setOnClickListener(this);

        // 성경 간략하게 보기
        String[] orginalAcms = getResources().getStringArray(R.array.KOR_ACM);

        mTableLayout = (TableLayout) findViewById(R.id.table);
        mTableLayout.setShrinkAllColumns(true);

        // 버튼의 균일한 크기를 지정하기
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int nWidth = dm.widthPixels;
        int nHeight = dm.heightPixels;


        int nCount = 0;

        // 테이블 형태로 된 버튼 목록 생성
        while (nCount < orginalAcms.length) {

            final TableRow tableRow = new TableRow(this);

            while (nCount < orginalAcms.length) {
                final Button btn = new Button(this);
                btn.setOnClickListener(new Button.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {

                        long currentClickTime = SystemClock.uptimeMillis();
                        long elapsedTime = currentClickTime - mLastClickTime;
                        mLastClickTime = currentClickTime;

                        // 중복 클릭인 경우
                        if (elapsedTime <= MIN_CLICK_INTERVAL) {
                            return;
                        }

                        Toast.makeText(getApplicationContext(),btn.getText(),Toast.LENGTH_LONG).show();
                        String strBible = (String) btn.getText();
                        int nBiblePosition = findBiblePosition(strBible, "KOR_ACM");
                        final String[] bible = getResources().getStringArray(R.array.KOR);
                        strBible = bible[nBiblePosition];
                        nBiblePosition =  Util.numOfChapters[nBiblePosition];

                        Intent i = new Intent(getApplicationContext(), ChapterTableActivity.class);

                        i.putExtra("bookPosition", nBiblePosition);
                        i.putExtra("bookName", strBible);

                        startActivity(i);

                    }
                });
                btn.setText(orginalAcms[nCount]);
                btn.setTextSize(12);
                btn.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tableRow.addView(btn);

                if ((nCount + 1) % TABLE_BUTTON_WIDTH == 0 && nCount + 1 < orginalAcms.length) {
                    nCount++;
                    break;
                }
                nCount++;
            }
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            mTableLayout.addView(tableRow);
        }

        // ※※ 꾹 눌렀을 경우 발생되는 이벤트 <-- 처리해야한다.
        //   registerForContextMenu(mListOldTestament);
        //    registerForContextMenu(mListNewTestament);



    }


    @Override
    public void onClick(View v) {
        Log.v("TabActivity", "OnClick!!");
        switch (v.getId()) {
            // 간략하게 보기 선택
            case R.id.simple_testament: {
                //Toast.makeText(getApplicationContext(), "신약", Toast.LENGTH_LONG).show();
                if (mFlipper.getDisplayedChild() == 0) {
                    mIsSelected = true;
                    mFlipper.showNext();
                    setButtonStyle();
                }
                break;
            }
            // 리스트로 보기 선택
            case R.id.list_testament: {  // 구약을 선택했을때
                //Toast.makeText(getApplicationContext(), "구약", Toast.LENGTH_LONG).show();
                if (mFlipper.getDisplayedChild() == 1) {
                    mIsSelected = false;
                    mFlipper.showNext();
                    setButtonStyle();
                }
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 성경 리스트로보기
        // 성경은 구약 39권 신약 27권 구성
        Log.v("TabActivity", "OnCreate");

        String[] orginal = getResources().getStringArray(R.array.KOR);

        // ListView에 성경 넣기
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orginal);
        mListSimpleTestament.setAdapter(adapter);
    }

    // 리스트 하나하나 클릭하였을때 발생되는 이벤트
    AdapterView.OnItemClickListener     listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {

            long currentClickTime = SystemClock.uptimeMillis();
            long elapsedTime = currentClickTime - mLastClickTime;
            mLastClickTime = currentClickTime;

            // 중복 클릭인 경우
            if (elapsedTime <= MIN_CLICK_INTERVAL) {
                return;
            }

            Intent i = new Intent(getBaseContext(), ChapterTableActivity.class);

            int nidx = findBiblePosition(mListSimpleTestament.getItemAtPosition(position).toString(), "KOR");
            int nPos = Util.numOfChapters[nidx];
            i.putExtra("bookPosition", nPos);
           i.putExtra("bookName", mListSimpleTestament.getItemAtPosition(position).toString());
            startActivity(i);
        }
    };

    // @@Function
    // 버튼 클릭할때마다 색상이 변경되게.
    void setButtonStyle() {
        if (mIsSelected) {
            mTextSimpleTestament.setTypeface(mTextSimpleTestament.getTypeface(), Typeface.BOLD);
            mTextSimpleTestament.setTextColor(Color.WHITE);
          //  mTextSimpleTestament.setBackgroundColor(Color.BLACK);
            mTextListTestament.setTypeface(null, Typeface.NORMAL);
            mTextListTestament.setTextColor(Color.BLACK);
           // mTextListTestament.setBackgroundColor(Color.DKGRAY);
        } else {
            mTextListTestament.setTypeface(mTextListTestament.getTypeface(), Typeface.BOLD);
            mTextListTestament.setTextColor(Color.WHITE);
           // mTextListTestament.setBackgroundColor(Color.BLACK);
            mTextSimpleTestament.setTypeface(null, Typeface.NORMAL);
            mTextSimpleTestament.setTextColor(Color.BLACK);
           // mTextSimpleTestament.setBackgroundColor(Color.DKGRAY);

        }
    }


    // @@Function
    // 성경 배열로 정리
    // - 필요한 목록은 추가로 입력하면됨.
    // - 시간되면 유틸쪽으로 옮겨도 될거같아보임
    int findBiblePosition(String a_strBibleName, String a_strBibleSelecter)
    {
        switch(a_strBibleSelecter)
        {
            case "KOR":
            {
                final String[] bible = getResources().getStringArray(R.array.KOR);

                for(int i =0; i < bible.length; i++) {
                    if(bible[i].equals(a_strBibleName))
                    {
                        return i;
                    }
                }
            }
            case "KOR_ACM":
            {
                final String[] bible_ACM = getResources().getStringArray(R.array.KOR_ACM);

                for(int i =0; i < bible_ACM.length; i++) {
                    if(bible_ACM[i].equals(a_strBibleName))
                    {
                        return i;
                    }
                }
            }
        }




        return -1;
    }
}