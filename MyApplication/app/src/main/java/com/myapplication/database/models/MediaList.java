package com.myapplication.database.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaList {

    public static final String TABLE_NAME = "media_details";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DESCRIPTION = "Username";
    public static final String COLUMN_THUMB = "thumb";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_URL = "url";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY,"
                    + COLUMN_DESCRIPTION + " varchar(40),"
                    + COLUMN_THUMB + " varchar(40),"
                    + COLUMN_TITLE + " varchar(40),"
                    + COLUMN_URL + " varchar(40)"
                    + ")";

    public MediaList(Integer id, String description, String thumb, String title, String url) {
        this.description = description;
        this.id = id;
        this.thumb = thumb;
        this.title = title;
        this.url = url;
    }

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("url")
    @Expose
    private String url;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}