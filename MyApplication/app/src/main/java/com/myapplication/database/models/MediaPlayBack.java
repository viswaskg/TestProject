package com.myapplication.database.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaPlayBack {

    public static final String TABLE_NAME = "media_track_details";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_WINDOW = "current_window";
    public static final String COLUMN_POSITION = "playback_position";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY,"
                    + COLUMN_WINDOW + " INTEGER,"
                    + COLUMN_POSITION + " INTEGER"
                    + ")";

    public MediaPlayBack(int id) {
        this.id = id;
        this.currentWindow = 0;
        this.playbackPosition = 0;
    }

    public MediaPlayBack(int id, int currentWindow, long playbackPosition) {
        this.id = id;
        this.currentWindow = currentWindow;
        this.playbackPosition = playbackPosition;
    }

    private int id;
    private int currentWindow;
    private long playbackPosition;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentWindow() {
        return currentWindow;
    }

    public void setCurrentWindow(int currentWindow) {
        this.currentWindow = currentWindow;
    }

    public long getPlaybackPosition() {
        return playbackPosition;
    }

    public void setPlaybackPosition(long playbackPosition) {
        this.playbackPosition = playbackPosition;
    }
}