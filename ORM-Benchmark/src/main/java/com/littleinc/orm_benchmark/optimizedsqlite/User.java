package com.littleinc.orm_benchmark.optimizedsqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

public class User {

    public static final String TABLE_NAME = "user";

    public static final String LAST_NAME_COLUMN = "last_name";

    public static final String FIRST_NAME_COLUMN = "first_name";

    private long mId;

    private String mLastName;

    private String mFirstName;

    public static void createTable(DataBaseHelper helper) {
        helper.getWritableDatabase().execSQL(
                new StringBuilder("CREATE TABLE '").append(TABLE_NAME)
                        .append("' ('").append(BaseColumns._ID)
                        .append("' INTEGER PRIMARY KEY AUTOINCREMENT, '")
                        .append(LAST_NAME_COLUMN).append("' TEXT, '")
                        .append(FIRST_NAME_COLUMN).append("' TEXT);")
                        .toString());
    }

    public static void dropTable(DataBaseHelper helper) {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL(new StringBuilder("DROP TABLE '").append(TABLE_NAME)
                .append("';").toString());
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public void prepareForInsert(final SQLiteStatement insertUser) {
        insertUser.bindString(1, mLastName);
        insertUser.bindString(2, mFirstName);
    }
}
