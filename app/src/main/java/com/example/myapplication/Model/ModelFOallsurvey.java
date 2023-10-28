package com.example.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ModelFOallsurvey implements Parcelable {

    private String S_ID;
    private String Title;
    private String Que1;
    private String Que2;
    private String Que3;
    private String Que4;
    private String Que5;
    private String Busername;
    private String DateTime;

    private String ExpDate;
    private String AttendCount;

    public ModelFOallsurvey(String s_ID, String title, String que1, String que2, String que3, String que4, String que5, String busername, String dateTime, String expDate, String attendCount) {
        S_ID = s_ID;
        Title = title;
        Que1 = que1;
        Que2 = que2;
        Que3 = que3;
        Que4 = que4;
        Que5 = que5;
        Busername = busername;
        DateTime = dateTime;
        ExpDate = expDate;
        AttendCount = attendCount;
    }


    protected ModelFOallsurvey(Parcel in) {
        S_ID = in.readString();
        Title = in.readString();
        Que1 = in.readString();
        Que2 = in.readString();
        Que3 = in.readString();
        Que4 = in.readString();
        Que5 = in.readString();
        Busername = in.readString();
        DateTime = in.readString();
        ExpDate = in.readString();
        AttendCount = in.readString();
    }

    public static final Creator<ModelFOallsurvey> CREATOR = new Creator<ModelFOallsurvey>() {
        @Override
        public ModelFOallsurvey createFromParcel(Parcel in) {
            return new ModelFOallsurvey(in);
        }

        @Override
        public ModelFOallsurvey[] newArray(int size) {
            return new ModelFOallsurvey[size];
        }
    };

    public String getS_ID() {
        return S_ID;
    }

    public void setS_ID(String s_ID) {
        S_ID = s_ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getQue1() {
        return Que1;
    }

    public void setQue1(String que1) {
        Que1 = que1;
    }

    public String getQue2() {
        return Que2;
    }

    public void setQue2(String que2) {
        Que2 = que2;
    }

    public String getQue3() {
        return Que3;
    }

    public void setQue3(String que3) {
        Que3 = que3;
    }

    public String getQue4() {
        return Que4;
    }

    public void setQue4(String que4) {
        Que4 = que4;
    }

    public String getQue5() {
        return Que5;
    }

    public void setQue5(String que5) {
        Que5 = que5;
    }

    public String getBusername() {
        return Busername;
    }

    public void setBusername(String busername) {
        Busername = busername;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getExpDate() {
        return ExpDate;
    }

    public void setExpDate(String expDate) {
        ExpDate = expDate;
    }

    public String getAttendCount() {
        return AttendCount;
    }

    public void setAttendCount(String attendCount) {
        AttendCount = attendCount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(S_ID);
        dest.writeString(Title);
        dest.writeString(Que1);
        dest.writeString(Que2);
        dest.writeString(Que3);
        dest.writeString(Que4);
        dest.writeString(Que5);
        dest.writeString(Busername);
        dest.writeString(DateTime);
        dest.writeString(ExpDate);
        dest.writeString(AttendCount);
    }
}
