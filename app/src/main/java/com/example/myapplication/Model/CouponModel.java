package com.example.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CouponModel implements Parcelable {

    private int Id;
    String AOP,SM,O,OA,MPO,APO,VT,D,Mobile;

    public CouponModel() {
    }

    public CouponModel(int id, String AOP, String SM, String o, String OA, String MPO, String APO, String VT, String d, String mobile) {
        Id = id;
        this.AOP = AOP;
        this.SM = SM;
        O = o;
        this.OA = OA;
        this.MPO = MPO;
        this.APO = APO;
        this.VT = VT;
        D = d;
        Mobile = mobile;
    }

    protected CouponModel(Parcel in) {
        Id = in.readInt();
        AOP = in.readString();
        SM = in.readString();
        O = in.readString();
        OA = in.readString();
        MPO = in.readString();
        APO = in.readString();
        VT = in.readString();
        D = in.readString();
        Mobile = in.readString();
    }

    public static final Creator<CouponModel> CREATOR = new Creator<CouponModel>() {
        @Override
        public CouponModel createFromParcel(Parcel in) {
            return new CouponModel(in);
        }

        @Override
        public CouponModel[] newArray(int size) {
            return new CouponModel[size];
        }
    };

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(AOP);
        dest.writeString(SM);
        dest.writeString(O);
        dest.writeString(OA);
        dest.writeString(MPO);
        dest.writeString(APO);
        dest.writeString(VT);
        dest.writeString(D);
        dest.writeString(Mobile);
    }
}
