package com.example.zlat.dissertationapplication.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.zlat.dissertationapplication.Category;
import com.example.zlat.dissertationapplication.QuizContentProviderContract;
import com.example.zlat.dissertationapplication.QuizDbHelper;
import com.example.zlat.dissertationapplication.R;

import java.util.List;

public class CreateQuestionActivity extends AppCompatActivity {

    TextView question, answer1, answer2, answer3, answer4, correctAnswer;
    Button saveQuestion;
    Spinner category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        question = findViewById(R.id.ptQuestion);
        answer1 = findViewById(R.id.ptAnswer1);
        answer2 = findViewById(R.id.ptAnswer2);
        answer3 = findViewById(R.id.ptAnswer3);
        answer4 = findViewById(R.id.ptAnswer4);
        correctAnswer = findViewById(R.id.ptCorrectAnswer);

        saveQuestion = findViewById(R.id.btnSaveQuestion);

        category = findViewById(R.id.spinnerCategorySave);

        loadCategories();
    }

    public void saveQuestion(View V){
        String questionQ = question.getText().toString();

        String firstAnswer = answer1.getText().toString();
        String secondAnswer = answer2.getText().toString();
        String thirdAnswer = answer3.getText().toString();
        String fourthAnswer = answer4.getText().toString();
        String rightAnswer = correctAnswer.getText().toString();

        Category selectedCategory = (Category) category.getSelectedItem();
        int categoryID = selectedCategory.getId();

        ContentValues valuesToAdd = new ContentValues();
        valuesToAdd.put(QuizContentProviderContract.QuestionsTable.COLUMN_QUESTION, questionQ);
        valuesToAdd.put(QuizContentProviderContract.QuestionsTable.COLUMN_OPTION1,firstAnswer);
        valuesToAdd.put(QuizContentProviderContract.QuestionsTable.COLUMN_OPTION2,secondAnswer);
        valuesToAdd.put(QuizContentProviderContract.QuestionsTable.COLUMN_OPTION3,thirdAnswer);
        valuesToAdd.put(QuizContentProviderContract.QuestionsTable.COLUMN_OPTION4,fourthAnswer);
        valuesToAdd.put(QuizContentProviderContract.QuestionsTable.COLUMN_ANSWER_NR,rightAnswer);
        valuesToAdd.put(QuizContentProviderContract.QuestionsTable.COLUMN_CATEGORY_ID, categoryID);
        getContentResolver().insert(QuizContentProviderContract.quizQuestionsURI,valuesToAdd);

        Intent intent = new Intent(CreateQuestionActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void loadCategories(){
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        List<Category> categories = dbHelper.getAllCategeories();
        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,categories);   //Adapter creates a view from the data. Adapter responsible with managing data you can see. //
        // Adapter view controls what we should see
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapterCategories);
    }
}
