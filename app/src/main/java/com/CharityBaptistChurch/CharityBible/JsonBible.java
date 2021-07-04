package com.CharityBaptistChurch.CharityBible;

public class JsonBible {
    private static final JsonBible ourInstance = new JsonBible();
    private static String sBibleJsonData = "";
    private static String sBibleJsonDataReplace = "";

    public static JsonBible getInstance()
    {
        return ourInstance;
    }

    private  JsonBible(){

    }

    public void setBibleJsonData(String sJson) {
        sBibleJsonData = sJson;
    }

    public String getBibleJsonData(){
        return sBibleJsonData;
    }

    public void setBibleJsonDataReplace(String sJson) {
        sBibleJsonDataReplace = sJson;
    }

    public String getBibleJsonDataReplace(){
        return sBibleJsonDataReplace;
    }
}
