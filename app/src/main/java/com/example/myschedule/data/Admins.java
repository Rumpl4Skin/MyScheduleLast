package com.example.myschedule.data;

import com.example.myschedule.DbHelper;
import com.example.myschedule.data.model.LoggedInUser;

public class Admins {
    private  int id_admins;
    private  String fio;
    private  String diljn;
    private  String img;
    public Admins() {
    }
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

    public void clear(Admins[] mas){
        for(int i=0;i<mas.length;i++)
        {
            mas[i].setDiljn(null);
            mas[i].setFio(null);
            mas[i].setImg(null);
            mas[i].setId_admins(0);
        }


}

    public void setId_admins(int id_admins) {
        this.id_admins = id_admins;
    }
}
