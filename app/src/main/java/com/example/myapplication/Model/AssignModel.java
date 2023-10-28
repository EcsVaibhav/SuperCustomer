package com.example.myapplication.Model;

public class AssignModel {
    int Id;
    String BusinessName, BusinessAddress, BusinessMobile1, BusinessMobile2, Status, Role;

    public AssignModel() {
    }

    public AssignModel(int id,String businessName, String businessAddress, String businessMobile1, String businessMobile2, String status, String role) {

        BusinessName = businessName;
        BusinessAddress = businessAddress;
        BusinessMobile1 = businessMobile1;
        BusinessMobile2 = businessMobile2;
        Status = status;
        Role = role;
        Id=id;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getBusinessName() {
        return BusinessName;
    }

    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }

    public String getBusinessAddress() {
        return BusinessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        BusinessAddress = businessAddress;
    }

    public String getBusinessMobile1() {
        return BusinessMobile1;
    }

    public void setBusinessMobile1(String businessMobile1) {
        BusinessMobile1 = businessMobile1;
    }

    public String getBusinessMobile2() {
        return BusinessMobile2;
    }

    public void setBusinessMobile2(String businessMobile2) {
        BusinessMobile2 = businessMobile2;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}