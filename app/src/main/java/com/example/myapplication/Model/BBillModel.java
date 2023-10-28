package com.example.myapplication.Model;

public class BBillModel {
    private String BName;
    private String BMob;
    private String BbPeriod;
    private String BTc;
    private String BBAmount;


    public BBillModel(String BName, String BMob, String bbPeriod, String BTc, String BBAmount) {
        this.BName = BName;
        this.BMob = BMob;
        BbPeriod = bbPeriod;
        this.BTc = BTc;
        this.BBAmount = BBAmount;
    }


    public String getBName() {
        return BName;
    }

    public void setBName(String BName) {
        this.BName = BName;
    }

    public String getBMob() {
        return BMob;
    }

    public void setBMob(String BMob) {
        this.BMob = BMob;
    }

    public String getBbPeriod() {
        return BbPeriod;
    }

    public void setBbPeriod(String bbPeriod) {
        BbPeriod = bbPeriod;
    }

    public String getBTc() {
        return BTc;
    }

    public void setBTc(String BTc) {
        this.BTc = BTc;
    }

    public String getBBAmount() {
        return BBAmount;
    }

    public void setBBAmount(String BBAmount) {
        this.BBAmount = BBAmount;
    }
}
