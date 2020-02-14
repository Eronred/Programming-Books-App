package com.deveu.copus.app.Datas;

public class Users {

    private String isPro;
    private String userid;
    private String userimage;
    private String userinfo;
    private String username;

    public Users() {
    }

    public Users(String isPro, String userid, String userimage, String userinfo, String username) {
        this.isPro = isPro;
        this.userid = userid;
        this.userimage = userimage;
        this.userinfo = userinfo;
        this.username = username;
    }

    public String getIsPro() {
        return isPro;
    }

    public void setIsPro(String isPro) {
        this.isPro = isPro;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
