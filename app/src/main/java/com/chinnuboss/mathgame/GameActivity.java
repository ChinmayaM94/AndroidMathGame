package com.chinnuboss.mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    TextView score;
    TextView life;
    TextView time;
    TextView question;
    EditText answer;
    Button ok;
    Button next;
    Random random = new Random();
    int number1;
    int number2;
    int userAnswer;
    int realAnswer;
    int userScore = 0;
    int userLife = 3;
    CountDownTimer timer;
    private static final long STARTTIME_IN_MILLISECONDS = 30000;
    boolean timer_running;
    long timeLeft = STARTTIME_IN_MILLISECONDS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        score = findViewById(R.id.textViewScore);
        life = findViewById(R.id.textViewLife);
        time = findViewById(R.id.textViewTime);
        question = findViewById(R.id.textViewQuestion);
        answer = findViewById(R.id.editTextAnswer);
        ok = findViewById(R.id.buttonOk);
        next = findViewById(R.id.buttonNext);

        gameContinue();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userAnswer = Integer.valueOf(answer.getText().toString());
                pauseTimer();

                if(userAnswer == realAnswer) {
                    ok.setVisibility(View.INVISIBLE);
                    userScore = userScore +10;
                    score.setText(""+userScore);
                    question.setText("Congratulations! Your answer is correct");
                } else {
                    ok.setVisibility(View.INVISIBLE);
                    userLife = userLife-1;
                    life.setText(""+userLife);
                    question.setText("Sorry! Your answer is wrong");
                }
                if(userLife == 0) {
                    Toast.makeText(getApplicationContext(), "Game over", Toast.LENGTH_LONG).show();
                    next.setText("Check result");
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer.setText("");
                ok.setVisibility(View.VISIBLE);
                resetTimer();
                gameContinue();

                if(userLife == 0) {
                    Intent intent = new Intent(GameActivity.this, Result.class);
                    intent.putExtra("score", userScore);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    public void gameContinue() {
        number1 = random.nextInt(100);
        number2 = random.nextInt(100);

        Intent intent = getIntent();
        String operation = intent.getStringExtra("operation");
        if(operation.equalsIgnoreCase("addition")) {
            realAnswer = number1 + number2;
            question.setText(number1 + " + " + number2);
        } else if(operation.equalsIgnoreCase("subtraction")) {
            realAnswer = number1 - number2;
            question.setText(number1 + " - " + number2);
        } else if(operation.equalsIgnoreCase("multiplication")) {
            realAnswer = number1 * number2;
            question.setText(number1 + " * " + number2);
        }
        startTimer();
    }

    public void startTimer() {
        timer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                updateTime();
            }

            @Override
            public void onFinish() {
                timer_running = false;
                pauseTimer();
                resetTimer();
                updateTime();
                question.setText("Sorry! Time is up");
                ok.setVisibility(View.INVISIBLE);
                userLife = userLife -1;
                life.setText(""+userLife);

            }
        }.start();
        timer_running = true;
    }

    public void updateTime() {
        int seconds = (int)(timeLeft / 1000) % 60;
        String time_left = String.format(Locale.getDefault(), "%02d", seconds);
        time.setText(time_left);

    }

    public void pauseTimer() {
        timer.cancel();
        timer_running = false;

    }

    public void resetTimer() {
        timeLeft = STARTTIME_IN_MILLISECONDS;
        updateTime();
    }
}