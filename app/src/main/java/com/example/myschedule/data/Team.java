package com.example.myschedule.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Team implements Parcelable
{
    private int n_subject,n_group;
    private String week_day,duration,audit,subject_name,group_name;


    public Team(int n_subject, int n_group, String week_day, String duration, String audit, String subject_name, String group_name) {
        this.n_subject = n_subject;
        this.n_group = n_group;
        this.week_day = week_day;
        this.duration = duration;
        this.audit = audit;
        this.subject_name = subject_name;
        this.group_name = group_name;
    }

    // Methods which makes our class parcelable
    public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };
    private Team(Parcel in) {
        n_subject = in.readInt();
        n_group = in.readInt();
        week_day = in.readString();
        duration = in.readString();
        audit = in.readString();
        subject_name = in.readString();
        group_name = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(n_subject);
        parcel.writeInt(n_group);
        parcel.writeString(week_day);
        parcel.writeString(duration);
        parcel.writeString(audit);
        parcel.writeString(subject_name);
        parcel.writeString(group_name);
    }

    public int getN_subject() {
        return n_subject;
    }

    public int getN_group() {
        return n_group;
    }

    public String getWeek_day() {
        return week_day;
    }

    public String getDuration() {
        return duration;
    }

    public String getAudit() {
        return audit;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public static Creator<Team> getCREATOR() {
        return CREATOR;
    }

    public void setN_subject(int n_subject) {
        this.n_subject = n_subject;
    }

    public void setN_group(int n_group) {
        this.n_group = n_group;
    }

    public void setWeek_day(String week_day) {
        this.week_day = week_day;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setAudit(String audit) {
        this.audit = audit;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
