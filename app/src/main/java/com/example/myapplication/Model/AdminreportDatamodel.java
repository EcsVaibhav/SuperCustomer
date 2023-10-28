package com.example.myapplication.Model;

import java.util.Map;

public class AdminreportDatamodel {
    private String BussinessName;
    private String BussinessMob;
    private String status;
    private Map<String, Integer> monthTotalCounts;

    public String getBussinessName() {
        return BussinessName;
    }

    public void setBussinessName(String bussinessName) {
        BussinessName = bussinessName;
    }

    public String getBussinessMob() {
        return BussinessMob;
    }

    public void setBussinessMob(String bussinessMob) {
        BussinessMob = bussinessMob;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Integer> getMonthTotalCounts() {
        return monthTotalCounts;
    }

    public void setMonthTotalCounts(Map<String, Integer> monthTotalCounts) {
        this.monthTotalCounts = monthTotalCounts;
    }
}
