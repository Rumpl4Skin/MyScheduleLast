package com.example.myschedule.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private int password;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    public int getPassword() {
        return password;
    }

    String getDisplayName() {
        return displayName;
    }
}