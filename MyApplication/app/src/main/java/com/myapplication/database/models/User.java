package com.myapplication.database.models;

public class User {
    public static final String TABLE_NAME = "user_details";

    public static final String COLUMN_ID = "UserId";
    public static final String COLUMN_USERNAME = "Username";
    public static final String COLUMN_LOGINSTATUS = "LoginStatus";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " varchar(20) PRIMARY KEY,"
                    + COLUMN_USERNAME + " varchar(40),"
                    + COLUMN_LOGINSTATUS + " INTEGER"
                    + ")";

    private String Username, UserId;
    private int LoginStatus = 0;

    public User() {
    }

    public User(String Username, String UserId,
                int LoginStatus) {
        this.Username = Username;
        this.UserId = UserId;
        this.LoginStatus = LoginStatus;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getLoginStatus() {
        return LoginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        LoginStatus = loginStatus;
    }
}