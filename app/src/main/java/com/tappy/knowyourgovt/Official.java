package com.tappy.knowyourgovt;

import java.io.Serializable;
import java.util.HashMap;


public class Official implements Serializable {

    String oName, oDesig, party, adl1, adl2, city,state, eMail, webSite , zipCode, phone;
    String urls, photoUrls;
    HashMap<String,String> channels;


    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getimgUrls() {
        return photoUrls;
    }

    public void setimgUrls(String photoUrls) {
        this.photoUrls = photoUrls;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


    public static int getCtr() {
        return ctr;
    }

    public static void setCtr(int ctr) {
        Official.ctr = ctr;
    }

    private static int ctr =1;

    public Official(){}


    public String getoName() {
        return oName;
    }

    public void setoName(String officialName) {
        this.oName = officialName;
    }

    public String getoDesig() {
        return oDesig;
    }

    public void setoffDesig(String officialDesignation) {
        this.oDesig = officialDesignation;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getadl1() {
        return adl1;
    }

    public void setadl1(String adl1) {
        this.adl1 = adl1;
    }

    public String getadl2() {
        return adl2;
    }

    public void setadl2(String adl1) {
        this.adl2 = adl1;
    }

    public String getCity() {
        return city;
    }

    public HashMap<String, String> getchannel() {
        return channels;
    }

    public void setchannel(HashMap<String, String> channels) {
        this.channels = channels;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getstate() {
        return state;
    }

    public void setstate(String state) {
        this.state = state;
    }

    public String getEmail() {
        return eMail;
    }

    public void setEmail(String eMail) {
        this.eMail = eMail;
    }

    public String getweb() {
        return webSite;
    }

    public void setweb(String webSite) {
        this.webSite = webSite;
    }
}
