package com.example.myschedule.ui.admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myschedule.data.model.LoggedInUser;

import java.util.ArrayList;

public class AdminViewModel extends ViewModel {

    private MutableLiveData<Integer> userId;
    private MutableLiveData<String> fio;
    private MutableLiveData<String> mail;
    private MutableLiveData<String> password;
    private MutableLiveData<Integer> groupId;

    public AdminViewModel() {
        //userId = new MutableLiveData<>();
    }
public void setAllUsers(ArrayList<LoggedInUser> users){

    userId.setValue(users.get(0).getIdUser());
    fio.setValue(users.get(0).getFIO());
    mail.setValue(users.get(0).getMail());
    password .setValue(users.get(0).getPassword());
    groupId.setValue(users.get(0).getIdGroup());
}
    public MutableLiveData<Integer> getUserId() {
        return userId;
    }

    public MutableLiveData<String> getFio() {
        return fio;
    }

    public MutableLiveData<String> getMail() {
        return mail;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public MutableLiveData<Integer> getGroupId() {
        return groupId;
    }

    public void setUserId(MutableLiveData<Integer> userId) {
        this.userId = userId;
    }

    public void setFio(MutableLiveData<String> fio) {
        this.fio = fio;
    }

    public void setMail(MutableLiveData<String> mail) {
        this.mail = mail;
    }

    public void setPassword(MutableLiveData<String> password) {
        this.password = password;
    }

    public void setGroupId(MutableLiveData<Integer> groupId) {
        this.groupId = groupId;
    }
}