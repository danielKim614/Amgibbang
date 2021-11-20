package com.cookandroid.amgibbang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DictionaryMainActivity extends AppCompatActivity {

    String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_main);

        //toolbar 지정
        Toolbar toolbar = findViewById(R.id.dictionary_toolbar);
        setSupportActionBar(toolbar);  // 액션바를 없앴으니까 그걸 툴바가 대신하게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    // 툴바 왼쪽에 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 타이틀 안 보이게 하기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.button_back);  // 뒤로가기 버튼 아이콘 수정

        Button btn = (Button) findViewById(R.id.searchBtn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TextView searchText = (TextView) findViewById(R.id.searchText);
                //final TextView searchResult = (TextView) findViewById(R.id.searchResult);
                keyword = searchText.getText().toString();

                Intent intent = new Intent(DictionaryMainActivity.this, DictionarySubActivity.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
                //searchResult.setText(str);


            }
        });
    }

    // 뒤로가기 버튼 행동
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

