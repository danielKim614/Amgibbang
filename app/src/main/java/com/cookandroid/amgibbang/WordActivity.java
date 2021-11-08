package com.cookandroid.amgibbang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class WordActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        // 툴바 커스텀
        Toolbar toolbar = findViewById(R.id.word_toolbar);
        setSupportActionBar(toolbar);  // 액션바를 없앴으니까 그걸 툴바가 대신하게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    // 툴바 왼쪽에 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 타이틀 안 보이게 하기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.button_back);  // 뒤로가기 버튼 아이콘 수정

        TextView toolbarText = findViewById(R.id.word_toolbar_text);
        toolbarText.setText("단어장이름");

    }

    public void onButtonClick(View view) {
        switch(view.getId()) {
            case R.id.word_button_edit: ;  // 단어 상세 내용 편집하기 (edit 버튼 저장 버튼으로 바꾸고 텍스트뷰 editable하게 변경)
            case R.id.word_button_prev_word: ; // 이전 단어로 넘어가기
            case R.id.word_button_next_word: ; // 다음 단어로 넘어가기
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home: // toolbar의 back 버튼이 눌렸을 때
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}