package com.example.zlat.dissertationapplication;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.zlat.dissertationapplication.QuizContentProviderContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quizDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static QuizDbHelper instance;

    private SQLiteDatabase db;

    private QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context){
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + "( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION4 + " TEXT," +
                QuestionsTable.COLUMN_ANSWER_NR + " TEXT, " +
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable(){
        Category c1 = new Category("Programming");
        insertCategory(c1);
        Category c2 = new Category("Geography");
        insertCategory(c2);
        Category c3 = new Category("Maths");
        insertCategory(c3);
    }

    public void addCategory(Category category) {
    db = getWritableDatabase();
    insertCategory(category);
    }

    public void addCategories(List<Category> categories){
        db = getWritableDatabase();
        for (Category category : categories){
            insertCategory(category);
        }
    }
    private void insertCategory(Category category){
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }



    private void fillQuestionsTable() {
        QuestionClass q1 = new QuestionClass("PROGRAMMING Process of inserting an element in stack is called ____________", "Create", "Push", "Evaluation", "Pop", "Push", Category.PROGRAMMING);
        insertQuestion(q1);
        QuestionClass q2 = new QuestionClass("PROGRAMMING Process of removing an element from stack is called __________ ?", "Create", "Push", "Evaluation", "Pop", "Pop",Category.PROGRAMMING);
        insertQuestion(q2);
        QuestionClass q3 = new QuestionClass("MATH Who invented Calculus?", "Isaac Newton", "Leonhard Euler", "Albert Einstein", "Robert Hooke", "Isaac Newton",Category.MATH);
        insertQuestion(q3);
        QuestionClass q4 = new QuestionClass("MATH What is the equation for the area of a triangle?", "a^2 + b^2 = c^2", "bh/2", "h(a+b)/2", "ab^2", "bh/2",Category.MATH);
        insertQuestion(q4);
        QuestionClass q5 = new QuestionClass("PROGRAMMING Depth First Search is equivalent to which of the traversal in the Binary Trees?", "Pre-order Traversal", " Post-order Traversal", "Level-order Traversal", "In-order Traversal", "Pre-order Traversal", Category.PROGRAMMING);
        insertQuestion(q5);
        QuestionClass q6 = new QuestionClass("PROGRAMMING The Data structure used in standard implementation of Breadth First Search is?", "Stack", "Queue", " Linked List", "None of the mentioned", "Queue", Category.PROGRAMMING);
        insertQuestion(q6);
        QuestionClass q7 = new QuestionClass("MATH 3+4 = ____", "7", "4", "3", "11", "7", Category.MATH);
        insertQuestion(q7);
        QuestionClass q8 = new QuestionClass("MATH 48*32", "1578", "1536", "1248", "4832", "1536", Category.MATH);
        insertQuestion(q8);
        QuestionClass q9 = new QuestionClass("MATH 12b - 8 = 4?", "b = 4", "b = 2", "b = 8", "b = 1", "b = 1", Category.MATH);
        insertQuestion(q9);
        QuestionClass q10 = new QuestionClass("GEOGRAPHY Which of these is not a national park?", "Dartmoor", "Peak District", "The Fens", "The New Forest", "The Fens", Category.GEOGRAPHY);
        insertQuestion(q10);
        QuestionClass q11 = new QuestionClass("GEOGRAPHY What is the tallest mountain in Japan?", "Mount Kita", "Mount Everest", "Mount Fuji", "Mount Olympus", "Mount Fuji", Category.GEOGRAPHY);
        insertQuestion(q11);
        QuestionClass q12 = new QuestionClass("GEOGRAPHY What is the capital of Malta?", "Lima", "Valletta", "Nairobi", "Birkirkara", "Valletta", Category.GEOGRAPHY);
        insertQuestion(q12);

    }

    public void addQuestion(QuestionClass question) {
        db = getWritableDatabase();
        insertQuestion(question);
    }

    public void addQuestions(List<QuestionClass> questions) {
        db = getWritableDatabase();

        for (QuestionClass question : questions) {
            insertQuestion(question);
        }
    }

    private void insertQuestion(QuestionClass question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswer());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public List<Category> getAllCategeories(){
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if (c.moveToFirst()){
            do{
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex((CategoriesTable.COLUMN_NAME))));
                categoryList.add(category);
            } while (c.moveToNext());
        }
        c.close();
        return categoryList;
    }

    public ArrayList<QuestionClass> getAllQuestions(int categoryID) {
        ArrayList<QuestionClass> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(categoryID)};

        //Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);
        Cursor c = db.query(QuestionsTable.TABLE_NAME,null,selection,selectionArgs,null,null,null);

        if (c.moveToFirst()) {
            do {
                QuestionClass question = new QuestionClass();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnswer(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
}