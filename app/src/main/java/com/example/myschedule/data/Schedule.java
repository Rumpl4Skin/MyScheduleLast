package com.example.myschedule.data;

public class Schedule {
    private  int id_scheldule;
    private  String day;
    private  String groupName;

    private  Subject[] subjects;

    public Schedule(int id_scheldule, String day, String groupName, Subject[] subjects) {
        this.id_scheldule = id_scheldule;
        this.day = day;
        this.groupName = groupName;
        this.subjects = subjects;
    }


    public int getId_admins() {
        return id_scheldule;
    }

    public void setId_admins(int id_scheldule) {
        this.id_scheldule = id_scheldule;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Subject[] getSubjects() {
        return subjects;
    }

    public void setSubjects(Subject[] subjects) {
        this.subjects = subjects;
    }
}
