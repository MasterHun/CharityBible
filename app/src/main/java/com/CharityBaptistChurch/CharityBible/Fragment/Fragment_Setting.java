package com.CharityBaptistChurch.CharityBible.Fragment;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.TestLooperManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.CharityBaptistChurch.CharityBible.Activity.MainActivity;
import com.CharityBaptistChurch.CharityBible.Adapter.BibleDBAdapter;
import com.CharityBaptistChurch.CharityBible.Adapter.VerseRecyclerViewAdapter_Setting;
import com.CharityBaptistChurch.CharityBible.BibleReader;
import com.CharityBaptistChurch.CharityBible.FileDownload;
import com.CharityBaptistChurch.CharityBible.R;
import com.willowtreeapps.spruce.BuildConfig;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;


public class Fragment_Setting  extends Fragment implements View.OnClickListener{

    View m_view;
    SeekBar m_bartextsize;
    RecyclerView m_RecyclerView;
    Button m_btnFileDownload;

    TextView m_tvTextSize;

    String m_SavePath;
    String m_FileName;
    FileDownload m_fileDownload;
    //   VerseRecyclerViewAdapter mAdapter;
    FileDownload.DownloadThread dThread;

    public int m_nSize = 0;

    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

//    Button btn1, btn2, btn3, btn4, btn5;
//    ListView lv;


    private String m_strBibleVersion;
    private String m_strContexts;
    private String m_strChapter;
    private String m_strReplaceBibleVersion;
    private String m_strIsReplace;
    private String m_strFontSize;
    private String m_strIsBlackMode;
    private String m_strIsSleepMode;


    BibleDBAdapter dbAdapter;

    CheckedTextView ctBibleCompare;

    LinearLayout linearBible, linearCompareBible;
    TextView tvBible,tvBibleList,tvCompareBible, tvCompareBibleList;


    // 디비에서 데이터 받고 난 이후의 작업들
    @Override
    public void onStart() {
        super.onStart();


        m_tvTextSize.setText(m_strFontSize);
        m_bartextsize.setProgress(Integer.parseInt(m_strFontSize));

        // 역본 사용 체크가 되어있다면
        if(m_strIsReplace.equals("Y"))
            ctBibleCompare.setChecked(true);
        else
            ctBibleCompare.setChecked(false);

        if (ctBibleCompare.isChecked())
        {
            linearCompareBible.setClickable(true);
            tvCompareBible.setTextColor( ContextCompat.getColor(getContext(),R.color.colorBlack));
            tvCompareBibleList.setTextColor( ContextCompat.getColor(getContext(),R.color.colorGray3));

            dbAdapter.UpdateSettingIsReplace("Y");

        }else
        {
            linearCompareBible.setClickable(false);
            tvCompareBible.setTextColor( ContextCompat.getColor(getContext(),R.color.colorGray2));
            tvCompareBibleList.setTextColor( ContextCompat.getColor(getContext(),R.color.colorGray1));
            dbAdapter.UpdateSettingIsReplace("N");
        }



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        m_view = inflater.inflate(R.layout.fragment_setting, container, false);

        m_fileDownload = new FileDownload();

        m_RecyclerView = (RecyclerView) m_view.findViewById(R.id.rv);
        m_bartextsize = (SeekBar) m_view.findViewById(R.id.sbtextsize);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        m_RecyclerView.setLayoutManager(layoutManager);

        m_tvTextSize = (TextView) m_view.findViewById(R.id.tvTextSize);



        m_bartextsize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // 프로그래스 값이 변경 되었을때
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                VerseRecyclerViewAdapter_Setting s = (VerseRecyclerViewAdapter_Setting)m_RecyclerView.getAdapter();
                s.setFontSize(progress);
                m_tvTextSize.setText(Integer.toString(progress));
                s.notifyDataSetChanged();
                m_RecyclerView.setAdapter(s);

            }

            // 최초에 탭하여 드래그할때
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            // 탭하여 드래그 멈출때
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                Log.d("mhpark","Fragment_Setting ProgressBar Stop");
                dbAdapter.UpdateSettingFontSize(m_tvTextSize.getText().toString());

            }
        });

        init();


        int readCheck = checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeCheck = checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(readCheck != PackageManager.PERMISSION_GRANTED && writeCheck != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getContext(),"권한음슴",Toast.LENGTH_SHORT).show();
        }

        requestPermission();



        // 성경선택 및 역본선택
        tvBible = (TextView)m_view.findViewById(R.id.tvBible);
        tvBibleList = (TextView)m_view.findViewById(R.id.tvBibleList);
        tvCompareBible = (TextView)m_view.findViewById(R.id.tvCompareBible);
        tvCompareBibleList = (TextView)m_view.findViewById(R.id.tvCompareBibleList);

        linearBible = (LinearLayout) m_view.findViewById(R.id.linearBibleSelect);
        linearCompareBible = (LinearLayout) m_view.findViewById(R.id.linearCompareBibleSelect);

        ctBibleCompare = (CheckedTextView) m_view.findViewById(R.id.ctCompare);

        // 성경선택 기능 버튼 이벤트
        linearBible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        // 성경역본 비교 기능 버튼 이벤트
        linearCompareBible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // 성경 역본 사용 유무 체크박스
        ctBibleCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckedTextView view = (CheckedTextView) v;
                view.toggle();

                if (ctBibleCompare.isChecked())
                {
                    linearCompareBible.setClickable(true);
                    tvCompareBible.setTextColor( ContextCompat.getColor(getContext(),R.color.colorBlack));
                    tvCompareBibleList.setTextColor( ContextCompat.getColor(getContext(),R.color.colorGray3));

                    dbAdapter.UpdateSettingIsReplace("Y");

                }else
                {
                    linearCompareBible.setClickable(false);
                    tvCompareBible.setTextColor( ContextCompat.getColor(getContext(),R.color.colorGray2));
                    tvCompareBibleList.setTextColor( ContextCompat.getColor(getContext(),R.color.colorGray1));
                    dbAdapter.UpdateSettingIsReplace("N");
                }

            }
        });






//        btn1 = (Button)m_view.findViewById(R.id.btn1);
//        btn2 = (Button)m_view.findViewById(R.id.btn2);
//        btn3 = (Button)m_view.findViewById(R.id.btn3);
//        btn4 = (Button)m_view.findViewById(R.id.btn4);
//        btn5 = (Button)m_view.findViewById(R.id.btn5);
//        btn1.setOnClickListener(this);
//        btn2.setOnClickListener(this);
//        btn3.setOnClickListener(this);
//        btn4.setOnClickListener(this);
//        btn5.setOnClickListener(this);
//
//        lv = m_view.findViewById(R.id.lv1);

//        //파일 다운로드 버튼 클릭
//        m_btnFileDownload = (Button)m_view.findViewById(R.id.btn_filedownload);
//        m_btnFileDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                StartDownload();
//            }
//        });

        dbAdapter = new BibleDBAdapter(getContext());
        dbAdapter.open();

        getBibleData();




        return m_view;

    }

    private void getBibleData()
    {

        ArrayList<String> array = dbAdapter.SelectSettingDB();

        String strData = array.get(0);

        String strResult;
        int nStart = 0;
        int nEnd = strData.indexOf('|');

        int i = 0;

        while (nEnd != -1) {

            if( strData.length() < nEnd)
                break;

            strResult = strData.substring(nStart, nEnd);

            switch (i)
            {
                case 0:
                    m_strContexts = strResult;
                    break;
                case 1:
                    m_strChapter= strResult;
                    break;
                case 2:
                    m_strIsReplace = strResult;
                    break;
                case 3:
                    m_strBibleVersion= strResult;
                    break;
                case 4:
                    m_strReplaceBibleVersion = strResult;
                    break;
                case 5:
                    m_strFontSize = strResult;
                    break;
                case 6:
                    m_strIsBlackMode = strResult;
                    break;
                case 7:
                    m_strIsSleepMode = strResult;
                    break;
            }

            // 다음 시작포인트 위치 저장
            nStart = nEnd + 1;

            nEnd = strData.indexOf('|',nEnd + 1);


            i++;
        }

        String strLog = m_strContexts+","+m_strChapter+","+m_strIsReplace+","+m_strBibleVersion+","+m_strReplaceBibleVersion+","+m_strFontSize
                +","+m_strIsBlackMode+","+m_strIsSleepMode;
        Log.d("mhpark","Result:"+strLog);

    }

    private void StartDownload()
    {
        String url = "http://192.168.0.10/FileDownload/engnkjv.zip";
        //String url = "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2Fb4LL01%2Fbtqv809t5X9%2FVY5qybXsG3TKsXbIiSD1KK%2Fimg.jpg";
        new DownloadFileAsync(getContext()).execute(url,"1","1");

    }
    final int PERMISSIONS_REQUEST_CODE = 1;
    private void requestPermission() {
        boolean shouldProviceRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);//사용자가 이전에 거절한적이 있어도 true 반환

        if (shouldProviceRationale) {
            //앱에 필요한 권한이 없어서 권한 요청
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            //권한있을때.
            //오레오부터 꼭 권한체크내에서 파일 만들어줘야함
            makeDir();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //권한 허용 선택시
                    //오레오부터 꼭 권한체크내에서 파일 만들어줘야함
                    makeDir();
                } else {
                    //사용자가 권한 거절시
                    denialDialog();
                }
                return;
            }
        }
    }

    public void denialDialog() {
        new android.support.v7.app.AlertDialog.Builder(getActivity().getApplicationContext())
                .setTitle("알림")
                .setMessage("저장소 권한이 필요합니다. 환경 설정에서 저장소 권한을 허가해주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                                BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent); //확인버튼누르면 바로 어플리케이션 권한 설정 창으로 이동하도록
                    }
                })
                .create()
                .show();
    }

    public void makeDir() {
        String root = getActivity().getFilesDir().getAbsolutePath(); //내장에 만든다


        String directoryName = "Downloads";
        final File myDir = new File(root + "/" + directoryName);
        if (!myDir.exists()) {
            boolean wasSuccessful = myDir.mkdir();
            if (!wasSuccessful) {
                System.out.println("file: was not successful.");
            } else {
                System.out.println("file: 최초로 폴더만듦." + root + "/" + directoryName);
            }
        } else {
            System.out.println("file: " + root + "/" + directoryName +"already exists");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
//            case R.id.btn1:
//                dbAdapter.InsertSetting_DB("창세기","20","Y","korHKJV",
//                        "engnkjv","15","Y","Y");
//                dbAdapter.InsertUnderline_DB("창세기","01","20");
//                break;
//            case R.id.btn2:
//                dbAdapter.DeleteTable(BibleDBAdapter.getDatabaseTableSetting());
//                break;
//            case R.id.btn3:
//
//                dbAdapter.CreateTableAll();
//                break;
//            case R.id.btn4:
//
//                ArrayList<String> array = dbAdapter.SelectSettingDB();
//
//                ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,array);
//                lv.setAdapter(adapter);
//
//
//
//                break;
//            case R.id.btn5:
//                break;
        }
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

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

                publishProgress("max","100");
                Thread.sleep(100);
                URL url = new URL(params[0].toString());
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                String strPath = getActivity().getFilesDir().getAbsolutePath()+File.separator+"Downloads"+File.separator+"engnkjv.zip";
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(strPath);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    //publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    publishProgress("progress","" + (int) ((total * 100) / lenghtOfFile),"테스트중");
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

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


    void init() {


        final VerseRecyclerViewAdapter_Setting mAdapter;


        BibleReader bibleReader = new BibleReader();


        List<String> dataVerse = new ArrayList<String>();
        List<String> dataNumber = new ArrayList<String>();

        ArrayList<String[]> arrayBible = new ArrayList<>();
        Boolean bCompare = true;

        String strPath = getActivity().getFilesDir().getAbsolutePath()+File.separator+"Bibles"+File.separator;

        // 성경 비교기능을 사용 할 것인지
        if( bCompare)
            arrayBible =bibleReader.BibleParsing(strPath,"korHKJV","engnkjv","01","01");
        else
            arrayBible =bibleReader.BibleParsing(strPath,"korHKJV","01","01");


        if( arrayBible == null)
            return;

        for(int i=0; i < arrayBible.size(); i++)
        {
            String strData[] = arrayBible.get(i);
            dataNumber.add(strData[3]+" ");
            dataVerse.add(strData[4]);
        }

        if( bCompare)
            mAdapter = new VerseRecyclerViewAdapter_Setting(getContext(), m_RecyclerView,true);
        else
            mAdapter = new VerseRecyclerViewAdapter_Setting(getContext(), m_RecyclerView,false);
        m_RecyclerView.setAdapter(mAdapter);
        mAdapter.setData(dataNumber,dataVerse);


        mAdapter.setData(dataNumber, dataVerse);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public void onResume() {
        super.onResume();
    }
}


