package com.example.myquiz;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    private static final long START_TIME_IN_MILLIS = 30000;

    private TextView questionTextView, scoreTextView, timerTextView;
    private Button option1Button, option2Button, option3Button, option4Button, restartButton, startButton, exitButton;

    private DataBaseHelper dataBaseHelper;
    private Cursor questionCursor;

    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBaseHelper = new DataBaseHelper(this);

        timerTextView = findViewById(R.id.timerTextView);
        exitButton = findViewById(R.id.exitButton);


        questionTextView = findViewById(R.id.questionTextView);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);
        option4Button = findViewById(R.id.option4Button);
        scoreTextView = findViewById(R.id.scoreTextView);
        restartButton = findViewById(R.id.restartButton);
        startButton = findViewById(R.id.startButton);

        loadQuestionsFromDatabase();

        startButton.setOnClickListener(view -> {
            startButton.setVisibility(View.GONE);
            exitButton.setVisibility(View.GONE);
            option1Button.setVisibility(View.VISIBLE);
            option2Button.setVisibility(View.VISIBLE);
            option3Button.setVisibility(View.VISIBLE);
            option4Button.setVisibility(View.VISIBLE);
            scoreTextView.setVisibility(View.VISIBLE);
            timerTextView.setVisibility(View.VISIBLE);

            startTimer();
            displayQuestion();
        });

        exitButton.setOnClickListener(view -> {
            finish();
            System.exit(0);
        });

        View.OnClickListener answerClickListener = view -> {
            Button clickedButton = (Button) view;
            int selectedAnswerIndex = (int) clickedButton.getTag();

            if (questionCursor.moveToPosition(currentQuestionIndex)) {
                int correctAnswer = questionCursor.getInt(questionCursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_CORRECT_ANSWER));

                if (selectedAnswerIndex == correctAnswer) {
                    score++;
                    Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
                }
                scoreTextView.setText("Score: " + score);
            }

            currentQuestionIndex++;
            if (currentQuestionIndex < questionCursor.getCount()) {
                displayQuestion();
            } else {
                endQuiz();
            }
        };

        option1Button.setOnClickListener(answerClickListener);
        option2Button.setOnClickListener(answerClickListener);
        option3Button.setOnClickListener(answerClickListener);
        option4Button.setOnClickListener(answerClickListener);

        restartButton.setOnClickListener(view -> restartQuiz());
    }

    private void loadQuestionsFromDatabase() {
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        questionCursor = db.query(DataBaseHelper.TABLE_QUESTIONS, null, null, null, null, null, null);
    }

    private void displayQuestion() {
        if (questionCursor.moveToPosition(currentQuestionIndex)) {
            String question = questionCursor.getString(questionCursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_QUESTION));
            String option1 = questionCursor.getString(questionCursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_OPTION1));
            String option2 = questionCursor.getString(questionCursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_OPTION2));
            String option3 = questionCursor.getString(questionCursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_OPTION3));
            String option4 = questionCursor.getString(questionCursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_OPTION4));

            questionTextView.setText(question);
            option1Button.setText(option1);
            option1Button.setTag(1);
            option2Button.setText(option2);
            option2Button.setTag(2);
            option3Button.setText(option3);
            option3Button.setTag(3);
            option4Button.setText(option4);
            option4Button.setTag(4);

        }
    }

    private void endQuiz() {
        questionTextView.setText("Quiz Over! Your final score is: " + score);
        option1Button.setVisibility(View.GONE);
        option2Button.setVisibility(View.GONE);
        option3Button.setVisibility(View.GONE);
        option4Button.setVisibility(View.GONE);
        restartButton.setVisibility(View.VISIBLE);
        timerTextView.setVisibility(View.GONE);
        exitButton.setVisibility(View.VISIBLE);
        countDownTimer.cancel();



        // Fetching from LoginActivity UserName
        String username = getIntent().getStringExtra("USERNAME");
        int lastScore = -1;
        String lastDate = null;


        if (username != null) {
            // Fetch the last score and Date from the database
            Pair<Integer, String> lastScoreAndDate = dataBaseHelper.getLastScoreAndDate(username);
            lastScore = lastScoreAndDate.first;
            lastDate = lastScoreAndDate.second;


            // Update the score in the dataBase
            dataBaseHelper.updateScore(username, score);
            // Update the Date in the dataBase
            dataBaseHelper.updateDate(username);
        }
        String lastScoreText = lastScore != -1
                ? "Your last score was: " + lastScore + " on " + (lastDate != null ? lastDate : "unknown date")
                : "No previous score found.";
        questionTextView.setText("Quiz Over! " + lastScoreText + "\nYour final score is: " + score);

    }

    private void restartQuiz() {
        score = 0;
        currentQuestionIndex = 0;
        scoreTextView.setText("Score: " + score);
        option1Button.setVisibility(View.VISIBLE);
        option2Button.setVisibility(View.VISIBLE);
        option3Button.setVisibility(View.VISIBLE);
        option4Button.setVisibility(View.VISIBLE);
        restartButton.setVisibility(View.GONE);
        timerTextView.setVisibility(View.VISIBLE);
        exitButton.setVisibility(View.GONE);
        startTimer();
        displayQuestion();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(START_TIME_IN_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Time: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Time: 0");
                Toast.makeText(MainActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                endQuiz();
            }
        }.start();
    }
}