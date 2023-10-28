package com.example.myapplication.Model;

public class UserOffersModel {

    private String id;
    private String COUNT ;
    private String Name ;
    private String Username ;
    private String Offer_1Line,Offer_2Line,Offer_3Line;

    private String fullOffer;

    public UserOffersModel(String id, String COUNT, String name, String username, String offer_1Line, String offer_2Line, String offer_3Line, String fullOffer) {
        this.id = id;
        this.COUNT = COUNT;
        Name = name;
        Username = username;
        Offer_1Line = offer_1Line;
        Offer_2Line = offer_2Line;
        Offer_3Line = offer_3Line;
        this.fullOffer = fullOffer;
    }

    public String getFullOffer() {
        return fullOffer;
    }

    public void setFullOffer(String fullOffer) {
        this.fullOffer = fullOffer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(String COUNT) {
        this.COUNT = COUNT;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getOffer_1Line() {
        return Offer_1Line;
    }

    public void setOffer_1Line(String offer_1Line) {
        Offer_1Line = offer_1Line;
    }

    public String getOffer_2Line() {
        return Offer_2Line;
    }

    public void setOffer_2Line(String offer_2Line) {
        Offer_2Line = offer_2Line;
    }

    public String getOffer_3Line() {
        return Offer_3Line;
    }

    public void setOffer_3Line(String offer_3Line) {
        Offer_3Line = offer_3Line;
    }
}
