package com.example.electronics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Spinner;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static SeekBar seek_bar;
    private static TextView seek_Text;

    private Button button;
    private static final int Request_Code_Quiz = 1;


    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";
    public static final String EXTRA_NO_OF_QUESTION = "extraNoOfQuestion";




    public static final String SHARED_PREFS = "shared_prefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";
    private TextView textViewHighscore;

    private Spinner spinnerCategory;
    private Spinner spinnerDifficulty;
    private Spinner spinnerNoOfQuestion;

    private int highscore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewHighscore = findViewById(R.id.text_view_highscore);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);
        spinnerNoOfQuestion = findViewById(R.id.spinner_no_of_questions);


        loadCategories();
        loadDifficultyLevels();
        loadHighscore();
        loadNoOfQuestion();


        button = findViewById(R.id.buttonquiz);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainQuiz();
            }
        });


        seek_bar =  findViewById(R.id.seekBar);
        seek_Text = findViewById(R.id.seekText);

        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;

                seek_Text.setText(progress + "/" + seekBar.getMax());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do something here, if you want to do anything at the start of
                // touching the seekbar


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Display the value in textview
                seek_Text.setText(progress + "/" + seekBar.getMax());

            }
        });



    }

    public void openMainQuiz(){
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String categoryName = selectedCategory.getName();

        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        //String no_of_questions = spinnerNoOfQuestion.getSelectedItem().toString();

        String no_of_questions = Integer.toString(seek_bar.getProgress());

        Intent intent = new Intent(this, MainQuiz.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryID);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        intent.putExtra(EXTRA_NO_OF_QUESTION, no_of_questions);



        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        startActivityForResult(intent, Request_Code_Quiz);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == Request_Code_Quiz){
                if (resultCode == RESULT_OK){
                    int score =  data.getIntExtra(MainQuiz.EXTRA_SCORE, 0 );
                    if (score > highscore) {
                        updateHighscore(score);

                    }

                }
            }
    }

    private void loadCategories(){
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        List<Category> categories = dbHelper.getAllCategories();

        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);
    }

    private void loadDifficultyLevels() {

        String[] difficultyLevels = Question.getAllDifficultyLevels();

        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);
    }



    private void loadHighscore() {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE,0);
        textViewHighscore.setText("Highscore: " + highscore);

    }

    private void updateHighscore(int highscoreNew) {
        highscore = highscoreNew;
        textViewHighscore.setText("Highscore: " + highscore);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }


    private void loadNoOfQuestion() {
        String[] noOfQuestion =  new String[] { "1", "2", "3" };

        ArrayAdapter<String> adapterNoOfQuestion  = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, noOfQuestion);

        adapterNoOfQuestion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerNoOfQuestion.setAdapter(adapterNoOfQuestion);

    }






}



