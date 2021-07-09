package com.CharityBaptistChurch.CharityBible.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import com.CharityBaptistChurch.CharityBible.Activity.MainActivity;
import com.CharityBaptistChurch.CharityBible.Adapter.BibleDBAdapter;
import com.CharityBaptistChurch.CharityBible.Adapter.VerseRecyclerViewAdapter;
import com.CharityBaptistChurch.CharityBible.BibleReader;
import com.CharityBaptistChurch.CharityBible.DBQueryData;
import com.CharityBaptistChurch.CharityBible.JsonBible;
import com.CharityBaptistChurch.CharityBible.R;
import com.CharityBaptistChurch.CharityBible.Util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fragment_ReadBible extends Fragment implements VerseRecyclerViewAdapter.OnListItemLongSelectedInterface, VerseRecyclerViewAdapter.OnListItemSelectedInterface{

    View m_view;

    public RecyclerView m_RecyclerView;

    private ArrayList<Integer> arrayClickVerse;

    BibleDBAdapter dbAdapter;

    Context mContext;
    Activity mActivity;

    public Fragment_ReadBible()
    {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        if (context instanceof Activity)
            mActivity = (Activity) context;
    }


    @Override
    public void onStart() {
        super.onStart();

        Log.d("Fragment_ReadBible","onStart()");

        // 성경이 존재하는지에 대한 체크
        if(isBibleChecker()) {
            if (!(DBQueryData.getInstance().getContents().equals("") && DBQueryData.getInstance().getChapter().equals(""))) {
                init(DBQueryData.getInstance().getContents(), DBQueryData.getInstance().getChapter());
            }
        }
        else
        {
            Toast.makeText(getContext(),"찾으시는 성경이 없습니다.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        dbAdapter = BibleDBAdapter.getInstance(getContext());


        arrayClickVerse = new ArrayList<>();

        Log.d("Fragment_ReadBible", "onCreateView()");

        m_view = inflater.inflate(R.layout.fragment_readbible, container, false);

        // 이놈 위치가 너무 애매하다.. 저번에는 여기 두면 에러나던데;;
        m_RecyclerView = m_view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        m_RecyclerView.setLayoutManager(layoutManager);

        // 최근 읽은 성경 DB조회
        getBibleData();

        return m_view;
    }

    /*
     * @class    : getBibleData
     * @data     : 2020/04/21
     * @lastedit : 2020/04/21
     * @author   : 박명훈
     * @brief    : 성경 설정관련된 정보들을 가지고 와서 값들을 파싱해서 전역함수로 저장.
     */
    private void getBibleData()
    {
        Log.d("Fragment_ReadBible","getBibleData()");

        ArrayList<String> array = dbAdapter.SelectSettingDB();

        DBQueryData.getInstance().initData(array.get(0));
        JsonBibleUpdate();

        if(getActivity() != null) {
            ((MainActivity) getActivity()).setBtnChapter(DBQueryData.getInstance().getChapter());
            ((MainActivity) getActivity()).setBtnContents(DBQueryData.getInstance().getContents());
        }

    }


    public boolean isBibleChecker() {

        if (getActivity() != null) {
            String strPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+ Util.m_strDirectory + File.separator;
            Log.d("Fragment_ReadBible ", "isBibleChecker() >> Path[" + strPath + "]");
            File file = new File(strPath);
            return file.isDirectory();
        }
        else
        {
            return false;
        }
    }


    /*
     * @ Func    : init
     * @ Param   : strContents 초기 설정할 성경권 받아옴       EX) 창세기
     *             strChapter  초기 설정할 성경 장을 받아옴    EX) 10
     * @ Since   : 2020.04.16
     * @ Last    : 2020.04.16
     * @ Author  : mhpark
     * @ Context : 성경 읽는 Fragment에서 초기화 해주기 위해서 호출하는 함수.
     */
    public void init(String strContents, String strChapter)
    {
        Log.d("Fragment_ReadBible", "init() >>");
        boolean bJson = true;

        if(!bJson) {

            // 1. 성경 장 숫자 보정
            // - 9장 일때 9로 들어오면 09로 수정해서 전달함.
            if (Integer.parseInt(strChapter) < 10) {
                int nChapter = Integer.parseInt(strChapter);
                strChapter = "0" + nChapter;
            }

            // 2. 성경 읽어와주는 함수 생성
            BibleReader bibleReader = new BibleReader();

            // 3. 성경 권에 대한 인덱스 얻어오기.
            String[] strBibleContents = getResources().getStringArray(R.array.KOR);

            String strContentsIndex;    // 성경 권수 인덱스 위치
            int nBibleContents = 0;     // 성경 권수 인덱스 구하기 위한 카운트 값

            // 3-1. Array에서 구해온 배열을 가지고 성경권의 위치를 찾아낸다.
            for (int i = 0; i < strBibleContents.length; i++) {

                if (strBibleContents[i].equals(strContents)) {
                    nBibleContents = i + 1;
                    break;
                }
            }

            // 3-2. 찾은 인덱스를 무조건 두자리 숫자가 되게끔 보정해준다.
            if (nBibleContents < 10)
                strContentsIndex = "0" + nBibleContents;
            else
                strContentsIndex = Integer.toString(nBibleContents);

            Log.d("Fragment_ReadBible", "init() >> " + strContentsIndex + "/" + strChapter);

            // getActivity() 는 Fragment가 Detach 되었을때 에러가 발생될 수 있어서 필터
            String strBiblesPath;
            if (getActivity() != null)
                strBiblesPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Util.m_strDirectory + File.separator;
            else
                return;

            ArrayList<String[]> arrayBible;     // 읽어온 성경 데이터를 담을 ArrayList

            // 4. 성경 비교기능을 사용 할 것인지
            if (DBQueryData.getInstance().getIsReplace().equals("Y"))
                arrayBible = bibleReader.BibleParsing(strBiblesPath, DBQueryData.getInstance().getBibleVersion(), DBQueryData.getInstance().getBibleVersionReplace(), strContentsIndex, strChapter);
            else
                arrayBible = bibleReader.BibleParsing(strBiblesPath, DBQueryData.getInstance().getBibleVersion(), strContentsIndex, strChapter);

            if (arrayBible == null) {
                Log.d("Fragment_ReadBible", "init() >> BibleParsing Failed");
                return;
            }

            // 5. 받은 성경 데이터 정리
            List<String> dataVerse = new ArrayList<>();
            List<String> dataNumber = new ArrayList<>();

            for (int i = 0; i < arrayBible.size(); i++) {
                String[] strData = arrayBible.get(i);
                dataNumber.add(strData[3] + " ");
                dataVerse.add(strData[4]);
            }

            // MediaPlayer가 생성되면 아래부분에 가리기 때문에 추가해줘야한다.
            dataVerse.add("");
            dataNumber.add("");
            dataVerse.add("");
            dataNumber.add("");
            dataVerse.add("");
            dataNumber.add("");

            final VerseRecyclerViewAdapter mAdapter;
            if ( DBQueryData.getInstance().getIsReplace().equals("Y"))
                mAdapter = new VerseRecyclerViewAdapter(getContext(), m_RecyclerView, this, this, true);
            else
                mAdapter = new VerseRecyclerViewAdapter(getContext(), m_RecyclerView, this, this, false);
            m_RecyclerView.setAdapter(mAdapter);

            mAdapter.setFontSize(Integer.parseInt(DBQueryData.getInstance().getFontSize()));

            mAdapter.setData(dataNumber, dataVerse);

            mAdapter.notifyDataSetChanged();


            // 변경된 데이터 디비 업데이트
            dbAdapter.UpdateSettingChapter(strChapter);
            dbAdapter.UpdateSettingContents(strContents);
            getBibleData();
        }else{

            /// Json 방식으로 변경한 이유는 서버 없이 사용하기위해서 변경함

            // 변경된 데이터 디비 업데이트
            dbAdapter.UpdateSettingChapter(strChapter);
            dbAdapter.UpdateSettingContents(strContents);
            getBibleData();

            /// 성경 권에 대한 인덱스 얻어오기.
            String[] strBibleContents = getResources().getStringArray(R.array.KOR);

            int nBibleContents = 0;     // 성경 권수 인덱스 구하기 위한 카운트 값

            // Array에서 구해온 배열을 가지고 성경권의 위치를 찾아낸다.
            for (int i = 0; i < strBibleContents.length; i++) {
                if (strBibleContents[i].equals(strContents)) {
                    nBibleContents = i;
                    break;
                }
            }

            ArrayList<String[]> arrayJsonBible = new ArrayList();
            try {
                if(DBQueryData.getInstance().getIsReplace().equals("Y")) {

                    String line = JsonBible.getInstance().getBibleJsonData();

                    String lineReplace = JsonBible.getInstance().getBibleJsonDataReplace();

                    JSONObject jsonObject = new JSONObject(line);
                    JSONObject jsonObjectReplace = new JSONObject(lineReplace);


                    String[] sBibleArray = getResources().getStringArray(R.array.KOR_ACM);

                    int i = 1;
                    strChapter = Integer.toString(Integer.parseInt(strChapter));
                    while (true) {
                        String str = sBibleArray[nBibleContents] + strChapter + ":" + i;
                        String strLine = jsonObject.getString(str);

                        String strReplace = sBibleArray[nBibleContents] + strChapter + ":" + i;
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
                        //jsonObject.getString("계10:1");
                    }
                }else
                {
                    String line = JsonBible.getInstance().getBibleJsonData();
                    JSONObject jsonObject = new JSONObject(line);
                    String[] sBibleArray = getResources().getStringArray(R.array.KOR_ACM);

                    int i = 1;
                    while (true) {
                        String str = sBibleArray[nBibleContents] + strChapter + ":" + i;
                        String strLine = jsonObject.getString(str);

                        if(strLine.isEmpty())
                            break;

                        if( i < 10) {
                            arrayJsonBible.add(new String[]{"0" + i + " ", strLine});
                        }else
                        {
                            arrayJsonBible.add(new String[]{ Integer.toString(i), strLine});
                        }
                        i++;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            // 5. 받은 성경 데이터 정리
            List<String> dataVerse  = new ArrayList<>();
            List<String> dataNumber = new ArrayList<>();

            for (int i = 0; i < arrayJsonBible.size(); i++) {

                if (DBQueryData.getInstance().getIsReplace().equals("Y")) {
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

            // MediaPlayer가 생성되면 아래부분에 가리기 때문에 추가해줘야한다.
            dataVerse.add(" ");
            dataNumber.add(" ");
            dataVerse.add(" ");
            dataNumber.add(" ");
            dataVerse.add(" ");
            dataNumber.add(" ");

            final VerseRecyclerViewAdapter mAdapter;
            if (DBQueryData.getInstance().getIsReplace().equals("Y"))
                mAdapter = new VerseRecyclerViewAdapter(getContext(), m_RecyclerView, this, this, true);
            else
                mAdapter = new VerseRecyclerViewAdapter(getContext(), m_RecyclerView, this, this, false);
            m_RecyclerView.setAdapter(mAdapter);

            mAdapter.setFontSize(Integer.parseInt(DBQueryData.getInstance().getFontSize()));
            mAdapter.setData(dataNumber, dataVerse);
            mAdapter.notifyDataSetChanged();


        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemLongSelected(View v, int position) {

        StringBuilder sData= new StringBuilder();

        int nSize = arrayClickVerse.size();
        Collections.sort(arrayClickVerse); /// 오름차순 정렬


        if(nSize > 0)
        {

            String[] strKor = getResources().getStringArray(R.array.versions_kor_new);
            String[] strEng = getResources().getStringArray(R.array.versions_eng_new);



            if (DBQueryData.getInstance().getIsReplace().equals("Y")) {

                String sBibleVersion = "";
                String sBibleVersionReplace = "";
                int nIndex = 0;
                for(String strBible : strEng)
                {
                    if(strBible.equals(DBQueryData.getInstance().getBibleVersion()))
                    {
                        sBibleVersion = strKor[nIndex];
                    }

                    if(strBible.equals(DBQueryData.getInstance().getBibleVersionReplace()))
                    {
                        sBibleVersionReplace = strKor[nIndex];

                    }

                    nIndex++;
                }

                sData.append(sBibleVersion).append("\n");
                sData.append(sBibleVersionReplace).append("\n");

                for (int i = 0; i < nSize; i = i + 2) {
                    sData.append(DBQueryData.getInstance().getContents()).append(" ").append(DBQueryData.getInstance().getChapter());
                    int nPos = arrayClickVerse.get(i);
                    VerseRecyclerViewAdapter.StdViewHolder vhReplaceOne = (VerseRecyclerViewAdapter.StdViewHolder) m_RecyclerView.findViewHolderForAdapterPosition(nPos);
                    VerseRecyclerViewAdapter.StdViewHolder vhReplaceTwo = (VerseRecyclerViewAdapter.StdViewHolder) m_RecyclerView.findViewHolderForAdapterPosition(nPos + 1);

                    if (vhReplaceOne != null && vhReplaceTwo != null) {
                        int nNumberOne = Integer.parseInt(vhReplaceOne.textNumber.getText().toString().trim());
                        int nNumberTwo = Integer.parseInt(vhReplaceTwo.textNumber.getText().toString().trim());

                        if (nNumberOne == nNumberTwo) {
                            sData.append(":").append(nNumberOne).append("\n");
                            sData.append(vhReplaceOne.textVerse.getText().toString()).append("\n").append(vhReplaceTwo.textVerse.getText().toString());
                        } else if (nNumberOne < nNumberTwo) {
                            sData.append(":").append(nNumberOne).append("\n");
                            VerseRecyclerViewAdapter.StdViewHolder vhReplaceTemp = (VerseRecyclerViewAdapter.StdViewHolder) m_RecyclerView.findViewHolderForAdapterPosition(nPos - 1);

                            if (vhReplaceTemp != null)
                                sData.append(vhReplaceTemp.textVerse.getText().toString()).append("\n").append(vhReplaceOne.textVerse.getText().toString());
                        }
                    }
                    if (i + 1 < nSize)
                        sData.append("\n");
                }
            } else {
                String sBibleVersion = "";
                int nIndex = 0;
                for(String strBible : strEng)
                {
                    if(strBible.equals(DBQueryData.getInstance().getBibleVersion()))
                    {
                        sBibleVersion = strKor[nIndex];
                        break;
                    }

                    nIndex++;
                }
                sData.append(sBibleVersion).append("\n");

                for (int i = 0; i < nSize; i++) {
                    sData.append(DBQueryData.getInstance().getContents()).append(" ").append(DBQueryData.getInstance().getChapter());
                    int nPos = arrayClickVerse.get(i);
                    VerseRecyclerViewAdapter.StdViewHolder vhReplace = (VerseRecyclerViewAdapter.StdViewHolder) m_RecyclerView.findViewHolderForAdapterPosition(nPos);

                    if (vhReplace != null) {
                        int nNumberOne = Integer.parseInt(vhReplace.textNumber.getText().toString().trim());

                        if (nNumberOne > 0) {
                            sData.append(":").append(nNumberOne).append("\n");
                            sData.append(vhReplace.textVerse.getText().toString());
                        }
                    }
                    if (i + 1 < nSize)
                        sData.append("\n");
                }

            }

        }

        Intent msg = new Intent(Intent.ACTION_SEND);

        msg.addCategory(Intent.CATEGORY_DEFAULT);

        msg.putExtra(Intent.EXTRA_SUBJECT, "");

        msg.putExtra(Intent.EXTRA_TEXT, sData.toString());

        msg.putExtra(Intent.EXTRA_TITLE, "");

        msg.setType("text/plain");

        startActivity(Intent.createChooser(msg, "공유"));

    }

    @Override
    public void onItemSelected(View v, int position) {

        VerseRecyclerViewAdapter.StdViewHolder viewHolder = (VerseRecyclerViewAdapter.StdViewHolder)m_RecyclerView.findViewHolderForAdapterPosition(position);
        if( viewHolder != null)
        {
            int nSize = arrayClickVerse.size();
            if(nSize > 0) {
                for (int i = 0; i < nSize; i++) {

                    if (arrayClickVerse.get(i) == position) {
                        arrayClickVerse.remove(i);
                        break;
                    }

                    if( i + 1 == nSize ) {
                        arrayClickVerse.add(position);
                        break;
                    }
                }
            }else
            {
                arrayClickVerse.add(position);
            }
        }
    }

    // 권한관련
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    @TargetApi(Build.VERSION_CODES.M)
    public void checkVerify() {
        if (
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            //https://academy.realm.io/kr/posts/android-marshmellow-permission/
            // 권한 요청 거절 이후의 앱권한 획득 순서
            // 사용자가 권한 요청을 한번 거절하면 메서든 반환값이 true가 된다.
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                if(Build.VERSION.SDK_INT>22) {
                    requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }

            } else {
                if(Build.VERSION.SDK_INT>22) {
                    // 최초로 권한을 요청하는 경우 ( 첫 실행 )
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
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
                    Toast.makeText(getContext(), "동의하지 않으시면 앱을 사용하는데 문제가 발생됩니다.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
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
