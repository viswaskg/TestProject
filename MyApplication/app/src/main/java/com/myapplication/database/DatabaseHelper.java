package com.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.myapplication.database.models.MediaList;
import com.myapplication.database.models.MediaPlayBack;
import com.myapplication.database.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String media_details = "MEDIA_DETAILS";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "MediaPlayer.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private void CreateTable(SQLiteDatabase db) {
        // TODO Create tables
        /*user table*/
        db.execSQL(User.CREATE_TABLE);

        /*media table*/
        db.execSQL(MediaList.CREATE_TABLE);

        /*track details table*/
        db.execSQL(MediaPlayBack.CREATE_TABLE);
    }

    public void doLogin(String email, String username) {
        if (checkUserExists(email))
            updateLogin(email);
        else
            addUser(email, username);
    }

    private boolean addUser(String userId, String username) {

        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(User.COLUMN_ID, userId);
        values.put(User.COLUMN_USERNAME, username);
        values.put(User.COLUMN_LOGINSTATUS, 1);
        // insert row
        long id = db.insert(User.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id != 1;
    }

    private boolean checkUserExists(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + User.TABLE_NAME + " where " + User.COLUMN_ID + "= " + userId, null);

        User user = new User();

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();


            user.setUserId(cursor.getString(cursor.getColumnIndex(User.COLUMN_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(User.COLUMN_USERNAME)));
            user.setLoginStatus(cursor.getInt(cursor.getColumnIndex(User.COLUMN_LOGINSTATUS)));
        }

        // close db connection
        db.close();

        return user.getLoginStatus() == 1;
    }

    public boolean getLoginStatus() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + User.TABLE_NAME, null);
        User user = new User();

        // looping through all rows and adding to list
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            user.setUserId(cursor.getString(cursor.getColumnIndex(User.COLUMN_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(User.COLUMN_USERNAME)));
            user.setLoginStatus(cursor.getInt(cursor.getColumnIndex(User.COLUMN_LOGINSTATUS)));
        }

        // close db connection
        db.close();

        return user.getLoginStatus() == 1;
    }

    private void updateLogin(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + User.COLUMN_LOGINSTATUS + " set Login_status= 1 where " + User.COLUMN_ID + " =" + userId;
        db.close();
        db.execSQL(sql, null);
    }

    public void logout() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE login_details set Login_status= ?";
        db.execSQL(sql, new String[]{"Logged out"});
        db.close();
    }

    private boolean addMedia(MediaList mediaList) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(MediaList.COLUMN_ID, mediaList.getId());
        values.put(MediaList.COLUMN_TITLE, mediaList.getTitle());
        values.put(MediaList.COLUMN_DESCRIPTION, mediaList.getDescription());
        values.put(MediaList.COLUMN_THUMB, mediaList.getThumb());
        values.put(MediaList.COLUMN_URL, mediaList.getUrl());
        // insert row
        long id = db.insert(MediaList.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id != 1;
    }

    public void addAllMedia(List<MediaList> mediaLists) {
        for (MediaList mediaList : mediaLists)
            addMedia(mediaList);
    }

    public int getMediaCount() {
        String selectQuery = "SELECT * FROM " + MediaList.TABLE_NAME;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null)
            return cursor.getCount();
        return 0;
    }

    public MediaList geMediabyId(Integer id) {
        MediaList mediaList = null;
        String selectQuery = "SELECT * FROM " + MediaList.TABLE_NAME + " WHERE " + MediaList.COLUMN_ID + " == " + id;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            mediaList = new MediaList(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4));
        }
        return mediaList;
    }

    public List<MediaList> getAllMedia() {
        List<MediaList> mediaLists = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + MediaList.TABLE_NAME;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                mediaLists.add(new MediaList(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)));
            } while (cursor.moveToNext());
        }
        return mediaLists;
    }

    public List<MediaList> getAllOtherMedia(MediaList mediaList) {
        List<MediaList> mediaLists = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + MediaList.TABLE_NAME + " WHERE " + MediaList.COLUMN_ID + " <> " + mediaList.getId();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                mediaLists.add(new MediaList(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)));
            } while (cursor.moveToNext());
        }
        return mediaLists;
    }

    public boolean addPlayBackDetails(int id, int window, int position) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(MediaPlayBack.COLUMN_ID, id);
        values.put(MediaPlayBack.COLUMN_WINDOW, window);
        values.put(MediaPlayBack.COLUMN_POSITION, position);
        // insert row
        long responseID = db.insert(MediaPlayBack.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return responseID != 1;
    }

    public MediaPlayBack getMediaDetails(int id) {
        String selectQuery = "SELECT * FROM " + MediaPlayBack.TABLE_NAME + " WHERE " + MediaPlayBack.COLUMN_ID + " = " + id;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

               return new MediaPlayBack(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2));
        } else
            addPlayBackDetails(id, 0, 0);
        return new MediaPlayBack(id);
    }

    public boolean doUpdatePlayBack(int id, int window, long position) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(MediaPlayBack.COLUMN_WINDOW, window);
        values.put(MediaPlayBack.COLUMN_POSITION, position);
        // insert row
        long responseID = db.update(MediaPlayBack.TABLE_NAME, values,MediaPlayBack.COLUMN_ID+"="+id, null );

        // close db connection
        db.close();

        // return newly inserted row id
        return responseID != 1;
    }

    public boolean removePlayBack(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(MediaPlayBack.TABLE_NAME, MediaPlayBack.COLUMN_ID + "=" + id, null) > 0;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        CreateTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }
}
