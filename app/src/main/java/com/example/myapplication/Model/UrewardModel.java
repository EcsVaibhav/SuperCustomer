package com.example.myapplication.Model;

public class UrewardModel {

   private String BMob,BName,Tsp,setP;

    public UrewardModel(String BMob, String BName, String tsp, String setP) {
        this.BMob = BMob;
        this.BName = BName;
        Tsp = tsp;
        this.setP = setP;
    }

    public String getBMob() {
        return BMob;
    }

    public void setBMob(String BMob) {
        this.BMob = BMob;
    }

    public String getBName() {
        return BName;
    }

    public void setBName(String BName) {
        this.BName = BName;
    }

    public String getTsp() {
        return Tsp;
    }

    public void setTsp(String tsp) {
        Tsp = tsp;
    }

    public String getSetP() {
        return setP;
    }

    public void setSetP(String setP) {
        this.setP = setP;
    }
}
