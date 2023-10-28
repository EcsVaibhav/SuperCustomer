package com.example.myapplication.Model;

public class USubmitSurveyModel {
    private String Que;
    private String QueID;

    private String SurveyID;
    private String BussinessNO;

    private String UMobNo;

    public USubmitSurveyModel(String que, String queID, String surveyID, String bussinessNO, String UMobNo) {
        Que = que;
        QueID = queID;
        SurveyID = surveyID;
        BussinessNO = bussinessNO;
        this.UMobNo = UMobNo;
    }

    public String getQue() {
        return Que;
    }

    public void setQue(String que) {
        Que = que;
    }

    public String getQueID() {
        return QueID;
    }

    public void setQueID(String queID) {
        QueID = queID;
    }

    public String getSurveyID() {
        return SurveyID;
    }

    public void setSurveyID(String surveyID) {
        SurveyID = surveyID;
    }

    public String getBussinessNO() {
        return BussinessNO;
    }

    public void setBussinessNO(String bussinessNO) {
        BussinessNO = bussinessNO;
    }

    public String getUMobNo() {
        return UMobNo;
    }

    public void setUMobNo(String UMobNo) {
        this.UMobNo = UMobNo;
    }
}
