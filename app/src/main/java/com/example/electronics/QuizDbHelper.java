package com.example.electronics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.electronics.QuizContract.*;

import java.util.ArrayList;
import java.util.List;


public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Electronics.db";
    private static final int DATABASE_VERSION = 4;

    private static  QuizDbHelper instance;


    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null){
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
                QuestionsTable._ID  +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION4 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT, " +
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

        db.execSQL("DROP TABLE IF EXISTS " +  CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +  QuestionsTable.TABLE_NAME);
    onCreate(db);
    }



    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    private void fillCategoriesTable() {
        Category c1 = new Category("Communications");
        insertCategory(c1);
        Category c2 = new Category("Control Systems");
        insertCategory(c2);
        Category c3 = new Category("Digital Electronics");
        insertCategory(c3);
        Category c4 = new Category("Select All Categories");
        insertCategory(c4);

    }


    public void addCategory(Category category) {
        db = getWritableDatabase();
        insertCategory(category);
    }

    public void addCategories(List<Category> categories) {
        db = getWritableDatabase();

        for (Category category : categories) {
            insertCategory(category);
        }

    }

    private void insertCategory(Category category){
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question(
                "Communications, Easy:\nCompany that offers communication services,\nusually to a few corporate customers,\nover land-wire, sea cable, mobile,\npoint-to-point microwave, and/or satellite systems",
                "IKE",
                "Low Frequency",
                "Carrier",
                "Communication Centre",
                3,
                Question.DIFFICULTY_EASY, Category.Communications);
        insertQuestion(q1);

        Question q2 = new Question(
                "Control Systems, Medium:\nLoopback, or _____ refers to the routing\nof electronics signals, digital data streams,\n or flows of items back to their source\nwithout inentional processing or modification",
                "Concentrations Nodes",
                "Block",
                "Loop-Back",
                "VLSI",
                3,
                Question.DIFFICULTY_MEDIUM, Category.Control_Systems);
        insertQuestion(q2);

        Question q3 = new Question(
                "Control Systems, Medium:\nIn Electronics, an arbiter helps order signals n asynchronous circuits.\n There are also electronic digital circuits callend\n____ that attempt to perform\n arbitration in one clock cycle.",
                "Johnson Noise",
                "ULSI",
                "Synchronizer",
                "Hybrid",
                3,
                Question.DIFFICULTY_MEDIUM, Category.Control_Systems);
        insertQuestion(q3);

        Question q4 = new Question(
                "Digital Electronics, Hard:\n____ is an error-detecting ____ commonly\nused in digital networks and storage devices\nto detect accidental changes to raw data",
                "Binary Digit",
                "Cyclic Redundancy Code",
                "Architecture",
                "IPv4 Multicast Addresses",
                2,
                Question.DIFFICULTY_HARD, Category.Digital_Electronics);
        insertQuestion(q4);

        Question q5 = new Question(
                "Digital Electronics, Hard:\n______, is a non-volatile storage chip used\nin computers and other devices. EEPROM\n can be erased by exposing it to\n an electrical charge.",
                "Memory Coherence",
                "Serializer/Deserializer",
                "Electrically Erasable\nProgrammable\nRead-Only Memory",
                "Asynchronous\nTime Division\nMuliplexing",
                3,
                Question.DIFFICULTY_HARD, Category.Digital_Electronics);
        insertQuestion(q5);


    }


    private void addQuestion(Question question) {
        db = getWritableDatabase();
        insertQuestion(question);
    }

    public void addQuestions(List<Question> questions) {
        db = getWritableDatabase();

        for (Question question : questions) {
            insertQuestion(question);
        }

    }


    private void insertQuestion(Question question){
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }


    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if(c.moveToFirst()) {
            do{
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (c.moveToNext());
        }

        c.close();
        return categoryList;
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()){
            do {
                Question question =  new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questionList;
    }


    public ArrayList<Question> getQuestions(int categoryID, String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionsTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};

        Cursor c = db.query(
                QuestionsTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );


        if (c.moveToFirst()){
            do {
                Question question =  new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            }while (c.moveToNext());
        }
        c.close();
        return questionList;
    }


}
