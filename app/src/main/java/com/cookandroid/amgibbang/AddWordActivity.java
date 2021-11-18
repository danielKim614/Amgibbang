package com.cookandroid.amgibbang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class AddWordActivity extends AppCompatActivity {
    String word;         // 단어
    String meaning;      // 뜻
    String explanation;  // 설명
    String titleText;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        // 툴바 커스텀
        Toolbar toolbar = findViewById(R.id.add_word_toolbar);
        setSupportActionBar(toolbar);  // 액션바를 없앴으니까 그걸 툴바가 대신하게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    // 툴바 왼쪽에 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 타이틀 안 보이게 하기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.button_back);  // 뒤로가기 버튼 아이콘 수정

        Intent intent = getIntent();
        titleText = intent.getStringExtra("TITLE");
    }

    public void onButtonClick(View view) {
        if (view.getId() == R.id.add_word_button_save) {

            // save 버튼 클릭
            EditText editText = findViewById(R.id.add_word_text_word);
            CharSequence charSequence = (CharSequence) editText.getText();
            word = String.valueOf(charSequence);

            editText = findViewById(R.id.add_word_text_meaning);
            charSequence = (CharSequence) editText.getText();
            meaning = String.valueOf(charSequence);

            editText = findViewById(R.id.add_word_text_explanation);
            charSequence = (CharSequence) editText.getText();
            explanation = String.valueOf(charSequence);

            // word, meaning, explanation 어딘가에 넘기거나 저장해야 함
            db.collection(titleText).document(word).set(new Word(explanation, meaning, word));
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:   // 뒤로 가기 버튼 클릭
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}