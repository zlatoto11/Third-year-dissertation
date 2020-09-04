package com.example.zlat.dissertationapplication.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.zlat.dissertationapplication.QuizContentProviderContract;
import com.example.zlat.dissertationapplication.R;

public class viewQuestionsActivity extends AppCompatActivity {
    ListView qListView;
    SimpleCursorAdapter dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_questions);

        displayQuestions();

        qListView = findViewById(R.id.questionListView);
        qListView.setClickable(true);
        qListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {     //Receives ID and sends it off to viewRunActivity.
                TextView tv = view.findViewById(R.id.textView_ID);        //Used to target specific data to display.

                Intent intent = new Intent(viewQuestionsActivity.this,questionDisplay.class);
                Bundle dataToSend = new Bundle();

                dataToSend.putInt("id", Integer.valueOf(tv.getText().toString()));

                intent.putExtras(dataToSend);
                startActivity(intent);

            }
        });

    }

    public void displayQuestions() {       //refreshes screen and updates listView
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

        String[] columnsToDisplay = new String[]{
                QuizContentProviderContract.QuestionsTable._ID,
                QuizContentProviderContract.QuestionsTable.COLUMN_QUESTION,
                QuizContentProviderContract.QuestionsTable.COLUMN_ANSWER_NR
        };
        int[] displayTo = new int[]{
                R.id.textView_ID,
                R.id.textView_QuestionDisplay,
                R.id.textView_Answer
        };

        Cursor cursor = getContentResolver().query(QuizContentProviderContract.quizQuestionsURI, projection, null, null, null);

        dataAdapter = new SimpleCursorAdapter(this, R.layout.row_layout,
                cursor,
                columnsToDisplay,
                displayTo,
                0);

        ListView listView = findViewById(R.id.questionListView);
        listView.setAdapter(dataAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayQuestions();
    }
}
