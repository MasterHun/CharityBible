package com.CharityBaptistChurch.CharityBible.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.CharityBaptistChurch.CharityBible.Fragment.Fragment_ReadBible;
import com.CharityBaptistChurch.CharityBible.R;
import com.CharityBaptistChurch.CharityBible.Util;

/*
 * Type : Activity
 * Contents : 성경 장을 선택하는 액티비티
 *  BibleTabActivity -> ChapterTableActivity
 *  Direct Click -> ChapterTableActivity
 * */
public class ChapterTableActivity extends AppCompatActivity {

    private String strBibleContents;
    private int nBibleChapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        return true;
    }

    TableLayout mTableLayout;

    // 연속클릭 방지코드
    private static final long MIN_CLICK_INTERVAL = 600;
    private long mLastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chaptertable);

        mTableLayout = (TableLayout) findViewById(R.id.table_chapter);
        mTableLayout.setShrinkAllColumns(true);

        // 연속클릭 방지코드
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                long currentClickTime = SystemClock.uptimeMillis();
                long elapsedTime = currentClickTime - mLastClickTime;
                mLastClickTime = currentClickTime;

                // 중복 클릭인 경우
                if (elapsedTime <= MIN_CLICK_INTERVAL) {
                    return;
                }

                // 사용자가 선택한 장수
                String strBibleSelectedChapter  = ((TextView) v).getText().toString();

                if( Integer.parseInt(strBibleSelectedChapter) < 9)
                {
                    strBibleSelectedChapter = "0" +strBibleSelectedChapter;
                }

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                int nBibleSelectedChapter = Integer.parseInt(strBibleSelectedChapter);

                if( nBibleSelectedChapter > 0 &&  strBibleContents != null ) {

                    Log.i("ChapterTable", "ChapterTableActivity onClickListener");
                    intent.putExtra("Chapter", strBibleSelectedChapter);
                    intent.putExtra("Contents", strBibleContents);
                    startActivity(intent);

                }
                //finish();
            }
        };

        Intent intent = getIntent();
        nBibleChapter = intent.getExtras().getInt("bookPosition");    // 각 성경마다 장수 ex) 창세기 == 50장
        strBibleContents= intent.getExtras().getString("bookName");   // 성경이 넘어온다. '창세기'


        // 성경저장
        Util.strBibleName = strBibleContents;

        final TextView tv = (TextView) findViewById(R.id.book_name);
        tv.setText(strBibleContents);

        // 8*8 타일형태로 장수 배열
        int nX = nBibleChapter / 8;
        int nNam = nBibleChapter % 8;

        // 타일형태로 성경 장수 구형
        int nCount = 1;
        for (int i = 0; i < nX; i++) {
            final TableRow tableRow = new TableRow(this);

            for (int j = 0; j < 8; j++) {
                final Button tb = new Button(this);
                tb.setOnClickListener(onClickListener);
                tb.setText("" + nCount);
                tb.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tableRow.addView(tb);
                int n = tb.getId();
                nCount++;
            }
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

            mTableLayout.addView(tableRow);
        }

        // 남은 장수 예외처리
        if (nNam > 0) {
            final TableRow tableRow = new TableRow(this);
            for (int i = 0; i < nNam; i++) {
                final Button tb = new Button(this);
                tb.setOnClickListener(onClickListener);
                tb.setText("" + nCount);
                tb.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tableRow.addView(tb);
                nCount++;
            }
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            mTableLayout.addView(tableRow);

        }
    }
}