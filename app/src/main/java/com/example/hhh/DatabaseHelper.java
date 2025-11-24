package com.example.hhh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "requests.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_REQUESTS = "requests";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_CLIENT = "client";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_CAR_MODEL = "car_model";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_STATUS = "status";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_REQUESTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_CLIENT + " TEXT, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_CAR_MODEL + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_COLOR + " TEXT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_TIME + " TEXT, " +
                    COLUMN_STATUS + " TEXT DEFAULT 'in_progress');";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);
        onCreate(db);
    }

    // Добавление заявки на ремонт
    public long addRepairRequest(String client, String phone, String carModel,
                                 String description, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, "repair");
        values.put(COLUMN_CLIENT, client);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_CAR_MODEL, carModel);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_STATUS, "in_progress");

        long result = db.insert(TABLE_REQUESTS, null, values);
        db.close();
        return result;
    }

    // Добавление заявки на покраску
    public long addPaintRequest(String client, String phone, String carModel,
                                String color, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, "paint");
        values.put(COLUMN_CLIENT, client);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_CAR_MODEL, carModel);
        values.put(COLUMN_COLOR, color);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_STATUS, "in_progress");

        long result = db.insert(TABLE_REQUESTS, null, values);
        db.close();
        return result;
    }

    // Получение статистики
    public int getTotalRepairCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REQUESTS +
                " WHERE " + COLUMN_TYPE + " = 'repair'", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    public int getRepairInProgressCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REQUESTS +
                " WHERE " + COLUMN_TYPE + " = 'repair' AND " +
                COLUMN_STATUS + " = 'in_progress'", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    public int getRepairCompletedCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REQUESTS +
                " WHERE " + COLUMN_TYPE + " = 'repair' AND " +
                COLUMN_STATUS + " = 'completed'", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    public int getTotalPaintCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REQUESTS +
                " WHERE " + COLUMN_TYPE + " = 'paint'", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    public int getPaintInProgressCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REQUESTS +
                " WHERE " + COLUMN_TYPE + " = 'paint' AND " +
                COLUMN_STATUS + " = 'in_progress'", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    public int getPaintCompletedCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REQUESTS +
                " WHERE " + COLUMN_TYPE + " = 'paint' AND " +
                COLUMN_STATUS + " = 'completed'", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    // Удалить все заявки
    public int deleteAllRequests() {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedCount = db.delete(TABLE_REQUESTS, null, null);
        db.close();
        return deletedCount;
    }

    // Удалить выполненные заявки
    public int deleteCompletedRequests() {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedCount = db.delete(TABLE_REQUESTS,
                COLUMN_STATUS + " = ?",
                new String[]{"completed"});
        db.close();
        return deletedCount;
    }

    // Метод для изменения статуса заявки
    public boolean updateRequestStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATUS, status);

        int rowsAffected = db.update(TABLE_REQUESTS, values,
                COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    // Удалить конкретную заявку по ID
    public boolean deleteRequest(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_REQUESTS,
                COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    // Получить заявку по ID
    public Cursor getRequestById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_REQUESTS +
                " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}