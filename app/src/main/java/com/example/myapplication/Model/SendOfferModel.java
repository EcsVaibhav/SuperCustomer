package com.example.myapplication.Model;

public class SendOfferModel {
    int Id;
            String Name, Mobile, Username, COUNT,AOP, SM, O, OA, MPO, APO, VT, D;
    private boolean isSelected;
    public SendOfferModel() {
    }

    public SendOfferModel(int id, String name, String mobile, String username, String COUNT, String AOP, String SM, String o, String OA, String MPO, String APO, String VT, String d) {
        Id = id;
        Name = name;
        Mobile = mobile;
        Username = username;
        this.COUNT = COUNT;
        this.AOP = AOP;
        this.SM = SM;
        O = o;
        this.OA = OA;
        this.MPO = MPO;
        this.APO = APO;
        this.VT = VT;
        D = d;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(String COUNT) {
        this.COUNT = COUNT;
    }

    public String getAOP() {
        return AOP;
    }

    public void setAOP(String AOP) {
        this.AOP = AOP;
    }

    public String getSM() {
        return SM;
    }

    public void setSM(String SM) {
        this.SM = SM;
    }

    public String getO() {
        return O;
    }

    public void setO(String o) {
        O = o;
    }

    public String getOA() {
        return OA;
    }

    public void setOA(String OA) {
        this.OA = OA;
    }

    public String getMPO() {
        return MPO;
    }

    public void setMPO(String MPO) {
        this.MPO = MPO;
    }

    public String getAPO() {
        return APO;
    }

    public void setAPO(String APO) {
        this.APO = APO;
    }

    public String getVT() {
        return VT;
    }

    public void setVT(String VT) {
        this.VT = VT;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }
}
