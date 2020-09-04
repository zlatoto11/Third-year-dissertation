package com.example.zlat.dissertationapplication.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zlat.dissertationapplication.QuizContentProviderContract;
import com.example.zlat.dissertationapplication.R;

public class addCategoryActivity extends AppCompatActivity {

    Button addBtn;
    TextView txtToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        addBtn = findViewById(R.id.btnAddCategory);
        txtToAdd = findViewById(R.id.ptCategoriestoAdd);
    }

    public void addCategory(View V) {

        String categoryText = txtToAdd.getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(QuizContentProviderContract.CategoriesTable.COLUMN_NAME, categoryText);
        getContentResolver().insert(QuizContentProviderContract.quizCategories_URI,cv);

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
