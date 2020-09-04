package com.example.zlat.dissertationapplication.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zlat.dissertationapplication.Category;
import com.example.zlat.dissertationapplication.QuizDbHelper;
import com.example.zlat.dissertationapplication.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";

    public static final String SHARED_PREFS= "sharedPrefs";
    public static final String KEY_HIGHSCHORE = "keyHighscore";

    private TextView textViewHighScore;
    private Spinner spinnerCategory;
    private Button addQuestion;

    private int highScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewHighScore = findViewById(R.id.text_view_highscore);
        spinnerCategory = findViewById(R.id.spinner_category);

        Button buttonStartQuiz = findViewById(R.id.startQuizButton);
        addQuestion = findViewById(R.id.btnAddQuestion);

        loadCategories();
        loadHighscore();

    }

    public void goToCategoryActivity(View V){
        Intent intent = new Intent(this,addCategoryActivity.class);
        startActivity(intent);
    }

    public void goToViewQuestions(View V){
        Intent intent = new Intent(this,viewQuestionsActivity.class);
        startActivity(intent);
    }

    public void startQuiz(View V){
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String categoryName = selectedCategory.getName();

        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID,categoryID);
        intent.putExtra(EXTRA_CATEGORY_NAME,categoryName);
        startActivityForResult(intent,REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_QUIZ){
            if(resultCode == RESULT_OK){
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                if (score > highScore){
                    updateHighscore(score);
                }
            }
        }
    }

    private void loadCategories(){
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        List<Category> categories = dbHelper.getAllCategeories();
        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,categories);   //Adapter creates a view from the data. Adapter responsible with managing data you can see. //
                                                                    // Adapter view controls what we should see
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);
    }

    private void loadHighscore(){
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highScore = prefs.getInt(KEY_HIGHSCHORE, 0);
        textViewHighScore.setText("Highscore: " + highScore);
    }

    private void updateHighscore(int highscoreNew){
        highScore = highscoreNew;
        textViewHighScore.setText("Highscore: " + highScore);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE); //Shared Preferences is data which persists
        SharedPreferences.Editor editor = prefs.edit(); //even after the application is turned off.
        editor.putInt(KEY_HIGHSCHORE,highScore);
        editor.apply();
    }

    public void createQuestionButton(View V){
        Intent moveToAddQuestion = new Intent(this,CreateQuestionActivity.class);
        //startActivityForResult(movetoMaps, ACTIVITY_MAPS_ACTIVITY);
        startActivity(moveToAddQuestion);
        Toast.makeText(getApplicationContext(), "Create Questions Page!", Toast.LENGTH_SHORT).show();
       // btnstartRun.setEnabled(false);
    }
}
