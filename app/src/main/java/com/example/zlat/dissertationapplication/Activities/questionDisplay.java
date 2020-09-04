package com.example.zlat.dissertationapplication.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zlat.dissertationapplication.QuizContentProviderContract;
import com.example.zlat.dissertationapplication.R;

public class questionDisplay extends AppCompatActivity {
int id;
EditText ptQuestion, ptAnswer1,ptAnswer2,ptAnswer3,ptAnswer4,ptCorrectQuestion;
Button btnDelete, btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_display);

        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);

        ptQuestion = findViewById(R.id.ptQuestion);

        ptAnswer1 = findViewById(R.id.ptAnswer1);
        ptAnswer2 = findViewById(R.id.ptAnswer2);
        ptAnswer3 = findViewById(R.id.ptAnswer3);
        ptAnswer4 = findViewById(R.id.ptAnswer4);

        ptCorrectQuestion = findViewById(R.id.ptCorrectQuestion);

        Bundle bun = getIntent().getExtras();
        if (bun != null) {
            id = bun.getInt("id");
            String[] projection = new String[] {
                    QuizContentProviderContract.QuestionsTable._ID,
                    QuizContentProviderContract.QuestionsTable.COLUMN_QUESTION,
                    QuizContentProviderContract.QuestionsTable.COLUMN_OPTION1,
                    QuizContentProviderContract.QuestionsTable.COLUMN_OPTION2,
                    QuizContentProviderContract.QuestionsTable.COLUMN_OPTION3,
                    QuizContentProviderContract.QuestionsTable.COLUMN_OPTION4,
                    QuizContentProviderContract.QuestionsTable.COLUMN_ANSWER_NR,
                    QuizContentProviderContract.QuestionsTable.COLUMN_CATEGORY_ID
            };

            Cursor c = getContentResolver().query(QuizContentProviderContract.quizQuestionsURI, projection, QuizContentProviderContract.QuestionsTable._ID + " = " + id, null, null);
            if (c.moveToFirst()) {

                ptQuestion.setText(c.getString(1));
                }
                ptAnswer1.setText(c.getString(2));
                ptAnswer2.setText(c.getString(3));
                ptAnswer3.setText(c.getString(4));
                ptAnswer4.setText(c.getString(5));
                ptCorrectQuestion.setText(c.getString(6));
            }
        }

    public void updateQuestion(View V) {
        ContentValues newValues = new ContentValues();newValues.put(QuizContentProviderContract.QuestionsTable.COLUMN_QUESTION, ptQuestion.getText().toString());
        newValues.put(QuizContentProviderContract.QuestionsTable.COLUMN_OPTION1, ptAnswer1.getText().toString());
        newValues.put(QuizContentProviderContract.QuestionsTable.COLUMN_OPTION2, ptAnswer2.getText().toString());
        newValues.put(QuizContentProviderContract.QuestionsTable.COLUMN_OPTION3, ptAnswer3.getText().toString());
        newValues.put(QuizContentProviderContract.QuestionsTable.COLUMN_OPTION4, ptAnswer4.getText().toString());
        newValues.put(QuizContentProviderContract.QuestionsTable.COLUMN_ANSWER_NR, ptCorrectQuestion.getText().toString());

            getContentResolver().update(QuizContentProviderContract.quizQuestionsURI, newValues, "_id = " + id, null);
            Intent intent = new Intent(questionDisplay.this, MainActivity.class);
            startActivity(intent);
    }

    public void deleteButton(View V){
        Bundle bun = getIntent().getExtras();
        int id = bun.getInt("id");
        getContentResolver().delete(QuizContentProviderContract.quizQuestionsURI, "_id =" + id, null);
        finish();
    }

}
