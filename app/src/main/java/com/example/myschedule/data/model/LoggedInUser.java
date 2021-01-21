package com.example.myschedule.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String groupName;
    private String FIO;
    private String mail;
    private String password;
    private int idGroup;
    private int idUser;

    public LoggedInUser() {
    }

    public LoggedInUser(LoggedInUser user) {
        this.groupName = user.groupName;
        this.FIO = user.FIO;
        this.mail = user.getMail();
        this.password = user.getPassword();
        this.idGroup = user.getIdGroup();
        this.idUser = user.getIdUser();
    }
public void clear(LoggedInUser[] mas){
        for(int i=0;i<mas.length;i++)
        {
            mas[i].setGroupName(null);
            mas[i].setFIO(null);
            mas[i].setMail(null);
            mas[i].setPassword(null);
            mas[i].setIdGroup(0);
            mas[i].setIdUser(0);
        }
}
    public LoggedInUser(int idUser, String FIO,String mail,String password,int idGroup,String groupName) {
        this.groupName = groupName;
        this.FIO = FIO;
        this.mail = mail;
        this.password = password;
        this.idGroup = idGroup;
        this.idUser = idUser;
    }
    public LoggedInUser(int idUser,String FIO, String mail, String password, int idGroup ) {
        this.FIO = FIO;
        this.mail = mail;
        this.password = password;
        this.idGroup = idGroup;
        this.idUser = idUser;
    }
    public LoggedInUser(String FIO, String mail, int idGroup, int idUser) {
        this.FIO = FIO;
        this.mail = mail;
        this.idGroup = idGroup;
        this.idUser = idUser;
    }

    public LoggedInUser(String mail, String password, String groupName) {
        this.groupName = groupName;
        this.password = password;
        this.mail = mail;
    }
    public LoggedInUser(String fio, String mail, String password, String groupName) {
        this.FIO=fio;
        this.groupName = groupName;
        this.password = password;
        this.mail = mail;
    }

    public LoggedInUser(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }



    public String getGroupName() {
        return groupName;
    }

    public String getFIO() {
        return FIO;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public boolean isAdmin(LoggedInUser user){
        if(user.mail=="admen@gmail.com"&&user.password=="11111111")
        return true;
        else return false;
    }




}