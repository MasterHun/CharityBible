package com.CharityBaptistChurch.CharityBible.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.CharityBaptistChurch.CharityBible.Adapter.BibleDBAdapter;
import com.CharityBaptistChurch.CharityBible.Adapter.VerseRecyclerViewAdapter;
import com.CharityBaptistChurch.CharityBible.Adapter.VerseRecyclerViewAdapter_Setting;
import com.CharityBaptistChurch.CharityBible.BibleReader;
import com.CharityBaptistChurch.CharityBible.DBQueryData;
import com.CharityBaptistChurch.CharityBible.FileDownload;
import com.CharityBaptistChurch.CharityBible.JsonBible;
import com.CharityBaptistChurch.CharityBible.R;
import com.CharityBaptistChurch.CharityBible.Util;
import com.willowtreeapps.spruce.BuildConfig;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;


public class Fragment_Setting  extends Fragment implements View.OnClickListener{


    BibleDBAdapter bibleDBAdapter;
    TextView m_tvTextSize;

    //   VerseRecyclerViewAdapter mAdapter;
    FileDownload.DownloadThread dThread;

    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    View m_view;
    SeekBar m_bartextsize;
    RecyclerView m_RecyclerView;

    CheckedTextView ctBibleCompare;

    LinearLayout linearBible, linearCompareBible;
    TextView tvBible,tvBibleList,tvCompareBible, tvCompareBibleList;

    private static int m_nSelectBible = -1;
    private static int m_nSelectCompareBible = -1;
    private static int m_nSelectDownload = -1;
    private static int m_nSelectDelete = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        bibleDBAdapter = BibleDBAdapter.getInstance(getContext());
        m_view = inflater.inflate(R.layout.fragment_setting, container, false);

        m_RecyclerView = m_view.findViewById(R.id.rv);
        m_bartextsize = m_view.findViewById(R.id.sbtextsize);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        m_RecyclerView.setLayoutManager(layoutManager);

        m_tvTextSize = m_view.findViewById(R.id.tvTextSize);



        m_bartextsize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // 프로그래스 값이 변경 되었을때
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                VerseRecyclerViewAdapter_Setting adapter = (VerseRecyclerViewAdapter_Setting)m_RecyclerView.getAdapter();
                if( progress >= 10)
                {

                    assert adapter != null;
                    adapter.setFontSize(progress);
                    m_tvTextSize.setText(Integer.toString(progress));
                    adapter.notifyDataSetChanged();
                    m_RecyclerView.setAdapter(adapter);
                }else
                {
                    assert adapter != null;
                    adapter.setFontSize(progress);
                    m_tvTextSize.setText(Integer.toString(progress));
                    adapter.notifyDataSetChanged();
                    m_RecyclerView.setAdapter(adapter);

                    seekBar.setProgress(10);
                }


            }

            // 최초에 탭하여 드래그할때
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            // 탭하여 드래그 멈출때
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                Log.d("Fragment_Setting","onStopTrackingTouch ");

                bibleDBAdapter.UpdateSettingFontSize(m_tvTextSize.getText().toString());

            }
        });

        int readCheck = 0;
        int writeCheck = 0;
        if(getActivity() != null) {
            readCheck = checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
            writeCheck = checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(readCheck != PackageManager.PERMISSION_GRANTED && writeCheck != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(getContext(),"권한음슴",Toast.LENGTH_SHORT).show();
        }

     //   requestPermission();



        // 성경선택 및 역본선택
        tvBible = m_view.findViewById(R.id.tvBible);
        tvBibleList = m_view.findViewById(R.id.tvBibleList);
        tvCompareBible = m_view.findViewById(R.id.tvCompareBible);
        tvCompareBibleList = m_view.findViewById(R.id.tvCompareBibleList);

        linearBible =  m_view.findViewById(R.id.linearBibleSelect);
        linearCompareBible = m_view.findViewById(R.id.linearCompareBibleSelect);

        ctBibleCompare = m_view.findViewById(R.id.ctCompare);

        // 성경선택 기능 버튼 이벤트
        linearBible.setOnClickListener(this);
        // 성경역본 비교 기능 버튼 이벤트
        linearCompareBible.setOnClickListener(this);
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

                    DBQueryData.getInstance().setIsReplace("Y");
                    bibleDBAdapter.UpdateSettingIsReplace("Y");

                }else
                {
                    linearCompareBible.setClickable(false);
                    tvCompareBible.setTextColor( ContextCompat.getColor(getContext(),R.color.colorGray2));
                    tvCompareBibleList.setTextColor( ContextCompat.getColor(getContext(),R.color.colorGray1));
                    DBQueryData.getInstance().setIsReplace("N");
                    bibleDBAdapter.UpdateSettingIsReplace("N");
                }
                refreshBibleListView();

            }
        });

        // 역본 사용 체크가 되어있다면
        if(DBQueryData.getInstance().getIsReplace().equals("Y"))
            ctBibleCompare.setChecked(true);
        else
            ctBibleCompare.setChecked(false);


        // 성경선택, 역본성경 선택 초기화 진행
        if(DBQueryData.getInstance().getBibleVersion() != null && DBQueryData.getInstance().getBibleVersionReplace() != null)
        {
            ArrayList<ArrayList<String>> arrayListBibles = getBibleList();
            ArrayList<String> arrayList_KOR = arrayListBibles.get(0);
            ArrayList<String> arrayList_ENG = arrayListBibles.get(1);

            if( arrayList_ENG.size() > 0 || arrayList_KOR.size() > 0) {
                int nIndex_A = 0;
                int nIndex_B = 0;
                for (int i = 0; i < arrayList_ENG.size(); i++) {
                    if (arrayList_ENG.get(i).equalsIgnoreCase(DBQueryData.getInstance().getBibleVersion())) {
                        nIndex_A = i;
                        break;
                    }
                }

                for (int i = 0; i < arrayList_ENG.size(); i++) {
                    if (arrayList_ENG.get(i).equalsIgnoreCase(DBQueryData.getInstance().getBibleVersionReplace())) {
                        nIndex_B = i;
                        break;
                    }
                }

                tvBibleList.setText(arrayList_KOR.get(nIndex_A));
                tvCompareBibleList.setText(arrayList_KOR.get(nIndex_B));
            }
        }
        return m_view;

    }

    // 디비에서 데이터 받고 난 이후의 작업들
    @Override
    public void onStart() {
        super.onStart();

        JsonBibleUpdate();
        refreshBibleListView();
        m_tvTextSize.setText(DBQueryData.getInstance().getFontSize());
        m_bartextsize.setProgress(Integer.parseInt(DBQueryData.getInstance().getFontSize()));

        // 역본 사용 체크가 되어있다면
        if(DBQueryData.getInstance().getIsReplace().equals("Y"))
            ctBibleCompare.setChecked(true);
        else
            ctBibleCompare.setChecked(false);

        if (ctBibleCompare.isChecked())
        {
            linearCompareBible.setClickable(true);
            tvCompareBible.setTextColor( ContextCompat.getColor(getContext(),R.color.colorBlack));
            tvCompareBibleList.setTextColor( ContextCompat.getColor(getContext(),R.color.colorGray3));

            DBQueryData.getInstance().setIsReplace("Y");

        }else
        {
            linearCompareBible.setClickable(false);
            tvCompareBible.setTextColor( ContextCompat.getColor(getContext(),R.color.colorGray2));
            tvCompareBibleList.setTextColor( ContextCompat.getColor(getContext(),R.color.colorGray1));

            DBQueryData.getInstance().setIsReplace("N");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //권한 허용 선택시
                //오레오부터 꼭 권한체크내에서 파일 만들어줘야함
                makeDir();
            } else {
                //사용자가 권한 거절시
                denialDialog();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            // 성경선택 탭
            case R.id.linearBibleSelect:

                ArrayList<ArrayList<String>> arrayListBibles = getBibleList();
                ArrayList<String> arrayList_KOR = arrayListBibles.get(0);
                ArrayList<String> arrayList_ENG = arrayListBibles.get(1);

                if( arrayList_ENG.size() > 0 || arrayList_KOR.size() > 0) {


                    for (int i = 0; i < arrayList_ENG.size(); i++) {
                        if (arrayList_ENG.get(i).equalsIgnoreCase(DBQueryData.getInstance().getBibleVersion())) {
                            m_nSelectBible = i;
                            break;
                        }
                    }

                    final CharSequence[] oItemsKor = arrayList_KOR.toArray(new CharSequence[0]);
                    final CharSequence[] oItemsEng = arrayList_ENG.toArray(new CharSequence[0]);


                    AlertDialog.Builder oDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()), android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

                    oDialog.setTitle("읽을 성경을 선택해주세요").setSingleChoiceItems(oItemsKor, m_nSelectBible, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            m_nSelectBible = which;
                        }
                    }).setNeutralButton("선택", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (m_nSelectBible >= 0) {
                                String strMsg = oItemsKor[m_nSelectBible] + "";

                                tvBibleList.setText(strMsg);

                                DBQueryData.getInstance().setBibleVersion(oItemsEng[m_nSelectBible] + "");
                                bibleDBAdapter.UpdateSettingVersion(oItemsEng[m_nSelectBible] + "");

                                JsonBibleUpdate();
                                refreshBibleListView();
                            }
                        }
                    }).setCancelable(false).show();
                }
                break;
            case R.id.linearCompareBibleSelect:

                ArrayList<ArrayList<String>> arrayListBibles_02 = getBibleList();
                ArrayList<String> arrayList_KOR_02 = arrayListBibles_02.get(0);
                ArrayList<String> arrayList_ENG_02 = arrayListBibles_02.get(1);

                if( arrayList_ENG_02.size() > 0 || arrayList_KOR_02.size() > 0) {

                    for (int i = 0; i < arrayList_ENG_02.size(); i++) {
                        if (arrayList_ENG_02.get(i).equalsIgnoreCase(DBQueryData.getInstance().getBibleVersionReplace())) {
                            m_nSelectCompareBible = i;
                            break;
                        }
                    }

                    final CharSequence[] oItemsKor_02 = arrayList_KOR_02.toArray(new CharSequence[arrayList_KOR_02.size()]);
                    final CharSequence[] oItemsEng_02 = arrayList_ENG_02.toArray(new CharSequence[arrayList_ENG_02.size()]);


                    AlertDialog.Builder oDialog_02 = new AlertDialog.Builder(Objects.requireNonNull(getContext()), android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

                    oDialog_02.setTitle("비교할 성경을 선택해주세요").setSingleChoiceItems(oItemsKor_02, m_nSelectCompareBible, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            m_nSelectCompareBible = which;
                        }
                    }).setNeutralButton("선택", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (m_nSelectCompareBible >= 0) {
                                String strMsg = oItemsKor_02[m_nSelectCompareBible] + "";

                                tvCompareBibleList.setText(strMsg);

                                DBQueryData.getInstance().setBibleVersionReplace(oItemsEng_02[m_nSelectCompareBible] + "");
                                bibleDBAdapter.UpdateSettingReplaceVersion(oItemsEng_02[m_nSelectCompareBible] + "");

                                JsonBibleUpdate();
                                refreshBibleListView();
                            }
                        }
                    }).setCancelable(false).show();
                }
                break;

        }
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

        String root = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+ Util.m_strDirectory+File.separator;

        final File myDir = new File(root);
        if (!myDir.exists()) {
            boolean wasSuccessful = myDir.mkdir();
            if (!wasSuccessful) {
                System.out.println("file: was not successful.");
            } else {
                System.out.println("file: 최초로 폴더만듦." + root + "/" + Util.m_strDirectory);
            }
        } else {
            System.out.println("file: " + root + "/" + Util.m_strDirectory +"already exists");
        }
    }



    private ArrayList<ArrayList<String>> getBibleList()
    {
        ArrayList<ArrayList<String>> arrayList = new ArrayList<>();
        ArrayList<String> arrayListCheck_ENG = new ArrayList<>();
        ArrayList<String> arrayListCheck_KOR = new ArrayList<>();

        String []strKor = getResources().getStringArray(R.array.versions_kor_new);
        String []strEng = getResources().getStringArray(R.array.versions_eng_new);
        AssetManager am = getResources().getAssets();


            for (int i = 0; i<strEng.length; i++) {
                try {
                    InputStream inputStream;
                    inputStream = am.open(strEng[i] + ".json");
                    arrayListCheck_KOR.add(strKor[i]);
                    arrayListCheck_ENG.add(strEng[i]);
                    inputStream.close();
                }catch (Exception io )
                {
                    io.printStackTrace();
                }

            }
        arrayList.add(arrayListCheck_KOR);
        arrayList.add(arrayListCheck_ENG);

        return arrayList;
    }


    private void StartDownload()
    {
        String url = Util.m_strServerIP+"/FileDownload/engnkjv.zip";
        new DownloadFileAsync(getContext()).execute(url,"1","1");

    }

    static class DownloadFileAsync extends AsyncTask<String, String, String> {

        private ProgressDialog mDlg;
        private Context mContext;

        DownloadFileAsync(Context context) {
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

                String strPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+Util.m_strDirectory+File.separator+"engnkjv.cbk";
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(strPath);

                byte[] data = new byte[1024];

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

            } catch (InterruptedException | IOException e) {
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


    void refreshBibleListView() {

        ArrayList<String[]> arrayJsonBible = new ArrayList();
        try {

            if(DBQueryData.getInstance().getIsReplace().equals("Y") && ctBibleCompare.isChecked() ) {

                String line = JsonBible.getInstance().getBibleJsonData();

                String lineReplace = JsonBible.getInstance().getBibleJsonDataReplace();

                JSONObject jsonObject = new JSONObject(line);
                JSONObject jsonObjectReplace = new JSONObject(lineReplace);

                String[] sBibleArray = getResources().getStringArray(R.array.KOR_ACM);

                int i = 1;
                while (true) {
                    String str = sBibleArray[0] + 1 + ":" + i;
                    String strLine = jsonObject.getString(str);

                    String strReplace = sBibleArray[0] + 1 + ":" + i;
                    String strLineReplace = jsonObjectReplace.getString(strReplace);

                    if(strLine.isEmpty() || strLineReplace.isEmpty())
                        break;


                    if( i < 10) {
                        arrayJsonBible.add(new String[]{"0" + i + " ", strLine});
                        arrayJsonBible.add(new String[]{"0" + i + " ", strLineReplace});
                    }else
                    {
                        arrayJsonBible.add(new String[]{ i +" ", strLine});
                        arrayJsonBible.add(new String[]{ i +" ", strLineReplace});
                    }

                    i++;
                }
            }
            else {
                String line = JsonBible.getInstance().getBibleJsonData();
                JSONObject jsonObject = new JSONObject(line);
                String[] sBibleArray = getResources().getStringArray(R.array.KOR_ACM);

                int i = 1;
                while (true) {
                    String str = sBibleArray[0] + 1 + ":" + i;
                    String strLine = jsonObject.getString(str);

                    if (strLine.isEmpty())
                        break;

                    if (i < 10) {
                        arrayJsonBible.add(new String[]{"0" + i + " ", strLine});
                    } else {
                        arrayJsonBible.add(new String[]{Integer.toString(i), strLine});
                    }
                    i++;
                }
            }
        }catch (Exception io)
        {
            io.printStackTrace();
        }

        final VerseRecyclerViewAdapter_Setting mAdapter;

        List<String> dataVerse  = new ArrayList<>();
        List<String> dataNumber = new ArrayList<>();

        for (int i = 0; i < arrayJsonBible.size(); i++) {

            if (DBQueryData.getInstance().getIsReplace().equals("Y") && ctBibleCompare.isChecked()) {
                String[] strData = arrayJsonBible.get(i);
                dataNumber.add(strData[0]);
                dataVerse.add(strData[1]);
            }else
            {
                String[] strData = arrayJsonBible.get(i);
                int nNumber = i + 1;
                if (nNumber < 10)
                    dataNumber.add("0" + nNumber + " ");
                else
                    dataNumber.add(i + 1 + " ");
                dataVerse.add(strData[1]);
            }
        }

        if (DBQueryData.getInstance().getIsReplace().equals("Y") && ctBibleCompare.isChecked())
            mAdapter = new VerseRecyclerViewAdapter_Setting(getContext(), m_RecyclerView, true);
        else
            mAdapter = new VerseRecyclerViewAdapter_Setting(getContext(), m_RecyclerView, false);
        m_RecyclerView.setAdapter(mAdapter);

        mAdapter.setFontSize(Integer.parseInt(DBQueryData.getInstance().getFontSize()) );
        mAdapter.setData(dataNumber,dataVerse);
        mAdapter.notifyDataSetChanged();

    }


    public void JsonBibleUpdate(){

        AssetManager am = getResources().getAssets();
        InputStream inputStream = null;
        InputStream inputStreamReplace = null;
        try {
            inputStream = am.open(DBQueryData.getInstance().getBibleVersion() + ".json");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            JsonBible.getInstance().setBibleJsonData(line);



            inputStreamReplace = am.open(DBQueryData.getInstance().getBibleVersionReplace() + ".json");
            InputStreamReader inputStreamReaderReplace = new InputStreamReader(inputStreamReplace);
            BufferedReader readerReplace = new BufferedReader(inputStreamReaderReplace);
            String lineReplace = readerReplace.readLine();
            JsonBible.getInstance().setBibleJsonDataReplace(lineReplace);


        } catch (Exception e) {
            e.printStackTrace();
        }

        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (inputStreamReplace != null) {
            try {
                inputStreamReplace.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


