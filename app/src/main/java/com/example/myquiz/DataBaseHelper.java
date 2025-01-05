package com.example.myquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyQuizData";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_SCORES = "scores";
    private static final String TABLE_QUESTIONS = "questions";

    // For All columns
    private static final String COLUMN_ID = "id";

    // User Table Columns
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Score Table Columns
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_DATE = "date";

    // Question Table Columns
    private static final String COLUMN_QUESTION = "question";
    private static final String COLUMN_OPTION1 = "option1";
    private static final String COLUMN_OPTION2 = "option2";
    private static final String COLUMN_OPTION3 = "option3";
    private static final String COLUMN_OPTION4 = "option4";
    private static final String COLUMN_CORRECT_ANSWER = "correct_answer";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USERNAME + " TEXT UNIQUE," + COLUMN_PASSWORD + " TEXT)";
        String CREATE_SCORES_TABLE = "CREATE TABLE " + TABLE_SCORES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USERNAME + " TEXT," + COLUMN_SCORE + " INTEGER," + COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_QUESTION + " TEXT," + COLUMN_OPTION1 + " TEXT," + COLUMN_OPTION2 + " TEXT," + COLUMN_OPTION3 + " TEXT," + COLUMN_OPTION4 + " TEXT," + COLUMN_CORRECT_ANSWER + " INTEGER)";
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_SCORES_TABLE);
        db.execSQL(CREATE_QUESTIONS_TABLE);

        String INSERT_ADMIN_USER = "INSERT INTO " + TABLE_USERS + " (" + COLUMN_USERNAME + ", " + COLUMN_PASSWORD + ") VALUES ('admin', '1234')";
        db.execSQL(INSERT_ADMIN_USER);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        onCreate(db);
    }

    public long addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        try {
            // Attempt to insert the new user
            return db.insertOrThrow(TABLE_USERS, null, values);
        } catch (Exception e) {
            return -1; // Return -1 to indicate failure
        }
    }
}
