package com.example.myquiz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton, registerButton;
    private EditText userNameText, passwordText;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameText = findViewById(R.id.userNameText);
        passwordText = findViewById(R.id.passwordText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        dataBaseHelper = new DataBaseHelper(this);



        loginButton.setOnClickListener(view -> {
            String userName = userNameText.getText().toString();
            String password = passwordText.getText().toString();

            if (validateLogin(userName, password)) {
                // Navigate to the quiz (MainActivity)
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USERNAME", userName);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(view -> {
            String username = userNameText.getText().toString();
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        });
    }

    private boolean validateLogin(String username, String password) {
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DataBaseHelper.TABLE_USERS + " WHERE " +
                        DataBaseHelper.COLUMN_USERNAME + " = ? AND " +
                        DataBaseHelper.COLUMN_PASSWORD + " = ?",
                new String[]{username, password}
        );
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        return isValid;
    }
}
