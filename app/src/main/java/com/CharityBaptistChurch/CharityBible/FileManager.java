package com.CharityBaptistChurch.CharityBible;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class FileManager{

    /*
    * @ Func    : getFileList
    * @ Param   : a_strPath 검색하고자 하는 절대 경로
    *             a_nOption 검색하려는 모드 (0:확장자 없이 이름만, 1:확장자 있고 이름만, 2:확장자 있고 절대경로)
    * @ Since   : 2020.04.12
    * @ Last    : 2020.04.13
    * @ Author  : mhpark
    * @ Context : 해당함수는 해당 경로에 있는 폴더들을 리스트형태로 반환해준다.
     */
    public ArrayList<String> getFileList(String a_strPath, int a_nOption)
    {
        ArrayList<String> arrayBible;

        if( a_strPath.isEmpty() )
        {
            return null;
        }

        arrayBible = new ArrayList<>();

        File file = new File(a_strPath);
        File files[] = file.listFiles();

        String strName_source;
        String strName_dest;
        switch (a_nOption)
        {
            case 0:

                // 확장자 없이

                for(int i = 0; i < files.length; i++) {
                    strName_source = files[i].getName();
                    int nIndex = strName_source.indexOf('.');
                    strName_dest = strName_source.substring(0,nIndex);

                    arrayBible.add(strName_dest);
                }
                break;
            case 1:

                // 확장자 있게

                for(int i = 0; i < files.length; i++) {
                    strName_source = files[i].getName();

                    arrayBible.add(strName_source);
                }
                break;
            case 2:

                // 확장자 있고 절대경로

                for(int i = 0; i < files.length; i++) {
                    strName_source = files[i].getAbsolutePath();

                    arrayBible.add(strName_source);
                }

                break;
        }

        if( arrayBible.isEmpty())
        {
            return null;
        }else {
            return arrayBible;
        }
    }

    /*
     * @ Func    : isFile
     * @ Param   : a_strPath 검색하고자 하는 파일의 절대 경로
     * @ Since   : 2020.04.12
     * @ Last    : 2020.04.13
     * @ Author  : mhpark
     * @ Context : 전달받은 경로에 대한 파일의 유무를 확인해주는 함수
     */
    public boolean isFile(String a_strPath)
    {
        if( a_strPath.isEmpty())
            return false;

        File file = new File(a_strPath);
        if(file.isFile())
            return true;

        return false;
    }

    /*
     * @ Func    : deleteFile
     * @ Param   : a_strPath 삭제 하는 파일,폴더의 절대 경로
     * @ Since   : 2020.04.12
     * @ Last    : 2020.04.13
     * @ Author  : mhpark
     * @ Context : 삭제할 파일 경로를 받아서 파일,폴더 를 삭제한다.
     */
    public boolean deleteFile(String a_strPath)
    {
        if( a_strPath.isEmpty()) {
            Log.d("hun","File is Empty");
            return false;
        }

        File file = new File(a_strPath);
        if(file.isFile()) {
            file.delete();
            Log.d("hun","this is file delete success");
        }
        if(file.isDirectory()) {
            file.delete();
            Log.d("hun","this is file directory success");
        }

        return true;
    }

}

