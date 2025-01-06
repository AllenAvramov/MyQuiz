package com.example.myquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyQuizData";
    public static final int DATABASE_VERSION = 5;

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_SCORES = "scores";
    public static final String TABLE_QUESTIONS = "questions";

    // For All columns
    public static final String COLUMN_ID = "id";

    // User Table Columns
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Score Table Columns
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_DATE = "date";

    // Question Table Columns
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_OPTION1 = "option1";
    public static final String COLUMN_OPTION2 = "option2";
    public static final String COLUMN_OPTION3 = "option3";
    public static final String COLUMN_OPTION4 = "option4";
    public static final String COLUMN_CORRECT_ANSWER = "correct_answer";

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

        // Inserting User Admin
//        String INSERT_ADMIN_USER = "INSERT INTO " + TABLE_USERS + " (" + COLUMN_USERNAME + ", " + COLUMN_PASSWORD + ") VALUES ('admin', '1234')";
//        db.execSQL(INSERT_ADMIN_USER);

        // Inserting All the questions
        // Insert quiz questions
        insertQuestion(db, "What house does Harry belong to?", "Hufflepuff", "Ravenclaw", "Gryffindor", "Slytherin", 3);
        insertQuestion(db, "Who is Harry's godfather?", "Sirius Black", "Remus Lupin", "Albus Dumbledore", "Severus Snape", 1);
        insertQuestion(db, "What is the name of Harry's pet owl?", "Scabbers", "Crookshanks", "Hedwig", "Errol", 3);
        insertQuestion(db, "Who is the Half-Blood Prince?", "Tom Riddle", "Draco Malfoy", "Harry Potter", "Severus Snape", 4);
        insertQuestion(db, "What spell is used to disarm an opponent?", "Stupefy", "Expelliarmus", "Avada Kedavra", "Wingardium Leviosa", 2);
        insertQuestion(db, "What platform at King's Cross Station does the Hogwarts Express depart from?", "Platform 9 3/4", "Platform 10", "Platform 9", "Platform 8 3/4", 1);
        insertQuestion(db, "What shape is Harry's scar?", "Star", "Lightning bolt", "Circle", "Triangle", 2);
        insertQuestion(db, "What is the name of the Weasley's house?", "Grimmauld Place", "Hogwarts", "The Burrow", "Shell Cottage", 3);
        insertQuestion(db, "What potion grants the drinker good luck?", "Felix Felicis", "Polyjuice Potion", "Veritaserum", "Amortentia", 1);
        insertQuestion(db, "Who is the headmaster of Hogwarts when Harry first attends?", "Minerva McGonagall", "Severus Snape", "Dolores Umbridge", "Albus Dumbledore", 4);

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
            return db.insert(TABLE_USERS, null, values) ;
        } catch (Exception e) {
            return -1; // Return -1 to indicate failure
        }
    }

    private void insertQuestion(SQLiteDatabase db, String question, String option1, String option2, String option3, String option4, int correctAnswer) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, question);
        values.put(COLUMN_OPTION1, option1);
        values.put(COLUMN_OPTION2, option2);
        values.put(COLUMN_OPTION3, option3);
        values.put(COLUMN_OPTION4, option4);
        values.put(COLUMN_CORRECT_ANSWER, correctAnswer);
        db.insert(TABLE_QUESTIONS, null, values);
    }

    public void addScore(String username, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_SCORE, score);

        db.insert(TABLE_SCORES, null, values);
    }

    public void updateScore(String username, int newScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE, newScore);

        // Update only the score where username matches
        db.update(TABLE_SCORES, values, COLUMN_USERNAME + " = ?", new String[]{username});
    }

    public void updateDate(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        values.put(COLUMN_DATE, currentDate);

        // Update only the date where username matches
        db.update(TABLE_SCORES, values, COLUMN_USERNAME + " = ?", new String[]{username});
    }

    public Pair<Integer, String> getLastScoreAndDate(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int lastScore = -1; // Default to -1 to indicate no score found
        String lastDate = null;

        String query = "SELECT " + COLUMN_SCORE + ", " + COLUMN_DATE +
                " FROM " + TABLE_SCORES +
                " WHERE " + COLUMN_USERNAME + " = ?" +
                " ORDER BY " + COLUMN_DATE + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            lastScore = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE));
            lastDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
        }
        cursor.close();
        return new Pair<>(lastScore, lastDate);
    }


}
