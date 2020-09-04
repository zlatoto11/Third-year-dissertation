package com.example.zlat.dissertationapplication.Activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zlat.dissertationapplication.QuestionClass;
import com.example.zlat.dissertationapplication.QuizDbHelper;
import com.example.zlat.dissertationapplication.R;

import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 15000;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";


    private TextView textViewQuestion,textViewScore,textViewCountDown,textViewCategory,textViewQuestionCount;
    private Button bt1, bt2, bt3, bt4;

    private ColorStateList textColorDefaultButton;
    private ColorStateList TextColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private long secondsLeft = 0;

    private ArrayList<QuestionClass> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private QuestionClass currentQuestion;

    private int score;
    private boolean answered;

    private long backPressedTime;
    private Handler mHandler = new Handler();
    private int delay = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        textViewCategory = findViewById(R.id.text_view_category);
        bt1 = findViewById(R.id.button1);
        bt2 = findViewById(R.id.button2);
        bt3 = findViewById(R.id.button3);
        bt4 = findViewById(R.id.button4);

        textColorDefaultButton = bt1.getTextColors();

        TextColorDefaultCd = textViewCountDown.getTextColors();

        Intent intent = getIntent();
        int categoryID = intent.getIntExtra(MainActivity.EXTRA_CATEGORY_ID, 0);
        String categoryName = intent.getStringExtra(MainActivity.EXTRA_CATEGORY_NAME);

        textViewCategory.setText("Category: " + categoryName);

        if (savedInstanceState == null) {
            QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
            questionList = dbHelper.getAllQuestions(categoryID);
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();
        } else{
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
                if (questionList == null){
                    finish(); //TODO FIX THIS BECAUSE IT SKIPS THE QUESTION WHEN IT ROTATES SCREEN
                }
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1); // question counter is 1 ahead of actual index, question 1 - index 0;
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            //answered = savedInstanceState.getBoolean(KEY_ANSWERED);
            //startCountdown();
            updateCountDownText();
            showSolution();
        }

       /* buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        *//*  checkAnswer();*//*
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });*/
    }

    private void showNextQuestion() {
        bt1.setBackgroundResource(android.R.drawable.btn_default);
        bt2.setBackgroundResource(android.R.drawable.btn_default);
        bt3.setBackgroundResource(android.R.drawable.btn_default);
        bt4.setBackgroundResource(android.R.drawable.btn_default);

        bt1.setTextColor(textColorDefaultButton);
        bt2.setTextColor(textColorDefaultButton);
        bt3.setTextColor(textColorDefaultButton);
        bt4.setTextColor(textColorDefaultButton);

        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());

            bt1.setText(currentQuestion.getOption1());
            bt2.setText(currentQuestion.getOption2());
            bt3.setText(currentQuestion.getOption3());
            bt4.setText(currentQuestion.getOption4());

            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountdown();
            enableButton();
        } else {
            finishQuiz();
        }
    }

    private void startCountdown(){
        countDownTimer = new CountDownTimer(timeLeftInMillis,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                Log.d("g53ids","" + timeLeftInMillis);
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                showSolution();
            }
        }.start();
    }

    private void updateCountDownText() {

       /* int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60; // divide by 1000 to get seconds, then modulus 60 to only show seconds left.
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes  , seconds);*/

        if (Math.round((float) timeLeftInMillis / 1000.0f) != secondsLeft) {
            secondsLeft = Math.round((float) timeLeftInMillis / 1000.0f);

            textViewCountDown.setText("" + secondsLeft);

            /*textViewCountDown.setText(timeFormatted);*/

            if (timeLeftInMillis < 10000) {
                textViewCountDown.setTextColor(Color.RED);
            } else {
                textViewCountDown.setTextColor(textColorDefaultButton);
            }
        }
    }

    public void buttonA(View view) {
        answered = true;
        countDownTimer.cancel();
        if (currentQuestion.getOption1().equals(currentQuestion.getAnswer())) {
            bt1.setBackgroundColor(Color.GREEN);
            score++;
            textViewScore.setText("Score: " + score);
            showSolution();
        } else{
            bt1.setBackgroundColor(Color.RED);
            showSolution();
        }
        disableButton();
    }

    public void buttonB(View view) {
        answered = true;
        countDownTimer.cancel();
        if (currentQuestion.getOption2().equals(currentQuestion.getAnswer())) {
            bt2.setBackgroundColor(Color.GREEN);
            score++;
            textViewScore.setText("Score: " + score);
            showSolution();
        } else{
            bt2.setBackgroundColor(Color.RED);
            showSolution();
        }
        disableButton();
    }
    public void buttonC(View view) {
        answered = true;
        countDownTimer.cancel();
        if (currentQuestion.getOption3().equals(currentQuestion.getAnswer())) {
            bt3.setBackgroundColor(Color.GREEN);
            score++;
            textViewScore.setText("Score: " + score);
            showSolution();
        } else{
            bt3.setBackgroundColor(Color.RED);
            showSolution();
        }
        disableButton();
    }
    public void buttonD(View view) {
        answered = true;
        countDownTimer.cancel();
        if (currentQuestion.getOption4().equals(currentQuestion.getAnswer())) {
            bt4.setBackgroundColor(Color.GREEN);
            score++;
            textViewScore.setText("Score: " + score);
            showSolution();
        } else{
            bt4.setBackgroundColor(Color.RED);
            showSolution();
        }
        disableButton();
    }

    private void showSolution() {
        if (currentQuestion.getOption1().equals(currentQuestion.getAnswer())) {
            bt1.setBackgroundColor(Color.GREEN);
            bt1.setTextColor(Color.BLACK);
            textViewQuestion.setText("The correct answer is: " + currentQuestion.getAnswer());
        }
        if (currentQuestion.getOption2().equals(currentQuestion.getAnswer())) {
            bt2.setBackgroundColor(Color.GREEN);
            bt2.setTextColor(Color.BLACK);
            textViewQuestion.setText("The correct answer is: " + currentQuestion.getAnswer());
        }
        if (currentQuestion.getOption3().equals(currentQuestion.getAnswer())) {
            bt3.setBackgroundColor(Color.GREEN);
            bt3.setTextColor(Color.BLACK);
            textViewQuestion.setText("The correct answer is: " + currentQuestion.getAnswer());
        }
        if (currentQuestion.getOption4().equals(currentQuestion.getAnswer())) {
            bt4.setBackgroundColor(Color.GREEN);
            bt4.setTextColor(Color.BLACK);
            textViewQuestion.setText("The correct answer is: " + currentQuestion.getAnswer());
        }

        delayQuestion();
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            finishQuiz();
        }else{
            Toast.makeText(this,"Press back again to finish",Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    public void delayQuestion(){
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    showNextQuestion();
                }
            }, delay); // 2 seconds
    }
    public void disableButton(){
        bt1.setEnabled(false);
        bt2.setEnabled(false);
        bt3.setEnabled(false);
        bt4.setEnabled(false);

    }

    public void enableButton(){
        bt1.setEnabled(true);
        bt2.setEnabled(true);
        bt3.setEnabled(true);
        bt4.setEnabled(true);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }
}
