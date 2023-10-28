package com.example.myapplication.Model;

public class AllCustomerModel {
    String CId;
    String Name,Mobile,Count,Lvisit;

    public AllCustomerModel(String CId, String name, String mobile, String count, String lvisit) {
        this.CId = CId;
        Name = name;
        Mobile = mobile;
        Count = count;
        Lvisit = lvisit;
    }

    public String getCId() {
        return CId;
    }

    public String getName() {
        return Name;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getCount() {
        return Count;
    }

    public String getLvisit() {
        return Lvisit;
    }
}
