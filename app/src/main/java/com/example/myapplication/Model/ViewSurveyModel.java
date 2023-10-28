package com.example.myapplication.Model;

public class ViewSurveyModel {
    private String que;
    private String YP,NP,MaybeP;

    public ViewSurveyModel(String que, String YP, String NP, String maybeP) {
        this.que = que;
        this.YP = YP;
        this.NP = NP;
        MaybeP = maybeP;
    }

    public String getQue() {
        return que;
    }

    public void setQue(String que) {
        this.que = que;
    }

    public String getYP() {
        return YP;
    }

    public void setYP(String YP) {
        this.YP = YP;
    }

    public String getNP() {
        return NP;
    }

    public void setNP(String NP) {
        this.NP = NP;
    }

    public String getMaybeP() {
        return MaybeP;
    }

    public void setMaybeP(String maybeP) {
        MaybeP = maybeP;
    }
}
