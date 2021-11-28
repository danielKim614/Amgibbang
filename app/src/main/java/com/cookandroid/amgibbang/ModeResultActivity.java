package com.cookandroid.amgibbang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ModeResultActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Word> list = CardListActivity.list;
    //ArrayList<Word> quizList = QuizActivity.list;
    TextView toolbarText;
    ProgressBar progressbar;
    TextView progressbarScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_result);

        // 툴바 커스텀
        Toolbar toolbar = findViewById(R.id.mode_toolbar);
        setSupportActionBar(toolbar);  // 액션바를 없앴으니까 그걸 툴바가 대신하게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    // 툴바 왼쪽에 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 타이틀 안 보이게 하기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.button_back);  // 뒤로가기 버튼 아이콘 수정

        toolbarText = findViewById(R.id.mode_toolbar_text);
        progressbar = findViewById(R.id.mode_result_progressbar);
        progressbarScore = findViewById(R.id.mode_result_progressbar_score);

        Intent intent = getIntent();
        String titleText = intent.getStringExtra("TITLE");
        toolbarText.setText(titleText);
        CalendarProgressbarInfo info = (CalendarProgressbarInfo) intent.getSerializableExtra("INFO");
        progressbar.setProgress(info.progress);
        progressbarScore.setText(info.score + "/" + info.total);

        recyclerView = findViewById(R.id.mode_result_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ModeResultAdapter adapter = new ModeResultAdapter(list);
        recyclerView.setAdapter(adapter);
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