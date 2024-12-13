package com.example.myquiz;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    private static final long START_TIME_IN_MILLIS = 30000;
    private TextView questionTextView, scoreTextView, timerTextView;
    private Button option1Button, option2Button, option3Button, option4Button, restartButton, startButton ,exitButton;
    private ImageView questionImagesView;


    String[] questions = {
            "What house does Harry belong to?",
            "Who is Harry's godfather?",
            "What is the name of Harry's pet owl?",
            "Who is the Half-Blood Prince?",
            "What spell is used to disarm an opponent?",
            "What platform at King's Cross Station does the Hogwarts Express depart from?",
            "What shape is Harry's scar?",
            "What is the name of the Weasley's house?",
            "What potion grants the drinker good luck?",
            "Who is the headmaster of Hogwarts when Harry first attends?"
    };

    String[][] options = {
            { "Hufflepuff", "Ravenclaw", "Gryffindor", "Slytherin"},
            {"Sirius Black", "Remus Lupin", "Albus Dumbledore", "Severus Snape"},
            { "Scabbers", "Crookshanks", "Hedwig", "Errol"},
            { "Tom Riddle", "Draco Malfoy", "Harry Potter", "Severus Snape"},
            { "Stupefy", "Expelliarmus", "Avada Kedavra", "Wingardium Leviosa"},
            {"Platform 9 3/4", "Platform 10", "Platform 9", "Platform 8 3/4"},
            { "Star","Lightning bolt", "Circle", "Triangle"},
            { "Grimmauld Place", "Hogwarts", "The Burrow", "Shell Cottage"},
            {"Felix Felicis", "Polyjuice Potion", "Veritaserum", "Amortentia"},
            { "Minerva McGonagall", "Severus Snape", "Dolores Umbridge", "Albus Dumbledore"}
    };

    int[] questionImages = {
            R.drawable.question1,
            R.drawable.question2,
            R.drawable.question3,
            R.drawable.question4,
            R.drawable.question5,
            R.drawable.question6,
            R.drawable.question7,
            R.drawable.question8,
            R.drawable.question9,
            R.drawable.question10
    };

    private final int[] correctAnswers = {2, 0, 2, 3, 1, 0, 1, 2, 0, 3 }; // Indexes of the correct answers
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        questionImagesView = findViewById(R.id.questionImageView);
        exitButton = findViewById(R.id.exitButton);
        // Initialize views
        questionTextView = findViewById(R.id.questionTextView);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);
        option4Button = findViewById(R.id.option4Button);
        scoreTextView = findViewById(R.id.scoreTextView);
        restartButton = findViewById(R.id.restartButton);
        startButton = findViewById(R.id.startButton);

        // Display the first question
        startButton.setOnClickListener(view -> {
            startButton.setVisibility(View.GONE); // Hide Start button
            exitButton.setVisibility(View.GONE);
            option1Button.setVisibility(View.VISIBLE);
            option2Button.setVisibility(View.VISIBLE);
            option3Button.setVisibility(View.VISIBLE);
            option4Button.setVisibility(View.VISIBLE);
            scoreTextView.setVisibility(View.VISIBLE);
            timerTextView.setVisibility(View.VISIBLE);

            startTimer(); // Start the timer
            displayQuestion(); // Show the first question
        });

        exitButton.setOnClickListener(view -> {
            // Exit the app
            finish(); // Close the current activity
            System.exit(0); // Optional: Terminate the app process
        });

        // Set button click listeners
        View.OnClickListener answerClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button clickedButton = (Button) view;
                int selectedAnswerIndex = (int) clickedButton.getTag();

                // Check if the answer is correct
                if (selectedAnswerIndex == correctAnswers[currentQuestionIndex]) {
                    score++;
                    Toast.makeText(MainActivity.this, "Right!", Toast.LENGTH_SHORT).show();
                    scoreTextView.setText("Score: " + score);
                } else{
                    Toast.makeText(MainActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
                }

                // Move to the next question or finish the quiz
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.length) {
                    displayQuestion();
                } else {
                    endQuiz();
                }
            }
        };

        option1Button.setOnClickListener(answerClickListener);
        option2Button.setOnClickListener(answerClickListener);
        option3Button.setOnClickListener(answerClickListener);
        option4Button.setOnClickListener(answerClickListener);

        // Restart button listener
        restartButton.setOnClickListener(view -> restartQuiz());
    }

    private void displayQuestion() {
        questionTextView.setText(questions[currentQuestionIndex]);
        option1Button.setText(options[currentQuestionIndex][0]);
        option1Button.setTag(0);
        option2Button.setText(options[currentQuestionIndex][1]);
        option2Button.setTag(1);
        option3Button.setText(options[currentQuestionIndex][2]);
        option3Button.setTag(2);
        option4Button.setText(options[currentQuestionIndex][3]);
        option4Button.setTag(3);
        questionImagesView.setImageResource(questionImages[currentQuestionIndex]);

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
        questionImagesView.setImageResource(R.drawable.end);
        countDownTimer.cancel();


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
        restartTimer();
        displayQuestion();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(START_TIME_IN_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the timer every second
                timerTextView.setText("Time: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                // Handle when the timer finishes
                timerTextView.setText("Time: 0");
                // Optionally, disable buttons or end the quiz
                Toast.makeText(MainActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
                endQuiz();
            }
        }.start();
    }

    private void restartTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Cancel the current timer
        }
        startTimer(); // Start a new timer
    }
}