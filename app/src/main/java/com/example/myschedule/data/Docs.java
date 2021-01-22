package com.example.myschedule.data;

public class Docs {
    private  int id_doc;
    private  String name;
    private  String img;

    public Docs(int id_doc, String name, String img) {
        this.id_doc=id_doc;
        this.name = name;
        this.img = img;
    }

    public Docs(Docs docs) {
        this.id_doc = docs.getId_doc();
        this.name = docs.getName();
        this.img = docs.getImg();
    }

    public int getId_doc() {
        return id_doc;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public void setId_doc(int id_doc) {
        this.id_doc = id_doc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
