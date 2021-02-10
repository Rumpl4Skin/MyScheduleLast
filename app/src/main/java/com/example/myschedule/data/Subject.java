package com.example.myschedule.data;

public class Subject {
    private  int id_subject;
    private  String time;
    private  String subjectName;
    private  String comm;
    private  String cab;
    private  int lab_count;
    private  int pract_count;

    public Subject() {
    }
    public Subject(Subject s) {
        this.time = s.getTime();
        this.subjectName = s.getSubjectName();
        this.cab = s.getCab();
    }
    public Subject(int id_subject, String time, String subjectName, String comm, String cab, int lab_count, int pract_count) {
        this.id_subject = id_subject;
        this.time = time;
        this.subjectName = subjectName;
        this.comm = comm;
        this.cab = cab;
        this.lab_count = lab_count;
        this.pract_count = pract_count;
    }

    public Subject(String time, String subjectName, String comm, String cab) {
        this.time = time;
        this.subjectName = subjectName;
        this.comm = comm;
        this.cab = cab;
    }


    public String getComm() {
        return comm;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }

    public String getCab() {
        return cab;
    }

    public void setCab(String cab) {
        this.cab = cab;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId_subject() {
        return id_subject;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getLab_count() {
        return lab_count;
    }

    public int getPract_count() {
        return pract_count;
    }

    public void setId_subject(int id_subject) {
        this.id_subject = id_subject;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setLab_count(int lab_count) {
        this.lab_count = lab_count;
    }

    public void setPract_count(int pract_count) {
        this.pract_count = pract_count;
    }
}
