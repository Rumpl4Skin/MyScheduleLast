package com.example.myschedule.data;

import com.example.myschedule.DbHelper;

public class Admins {
    private  int id_admins;
    private  String fio;
    private  String diljn;
    private  String img;

    public Admins(int id_admins, String fio, String diljn, String img) {
        this.id_admins = id_admins;
        this.fio = fio;
        this.diljn = diljn;
        this.img = img;
    }
    public Admins(Admins admin) {
        this.id_admins = admin.getId_admins();
        this.fio = admin.getFio();
        this.diljn = admin.getDiljn();
        this.img = admin.getImg();
    }

    public int getId_admins() {
        return id_admins;
    }

    public String getFio() {
        return fio;
    }

    public String getDiljn() {
        return diljn;
    }

    public String getImg() {
        return img;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public void setDiljn(String diljn) {
        this.diljn = diljn;
    }

    public void setImg(String img) {
        this.img = img;
    }


}
