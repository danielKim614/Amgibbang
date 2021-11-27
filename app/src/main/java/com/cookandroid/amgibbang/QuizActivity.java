package com.cookandroid.amgibbang;

import static android.os.SystemClock.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    String title;
    ArrayList<Word> list = CardListActivity.list;
    TextView total;
    TextView solved;
    ArrayList<Integer> randomList = new ArrayList<Integer>();
    ArrayList<Integer> random4 = new ArrayList<Integer>();
    ArrayList<String> question = new ArrayList<String>();
    ArrayList<String> answer = new ArrayList<String>();
    ArrayList<String> selection = new ArrayList<String>();
    TextView quiz;
    TextView select1;
    TextView select2;
    TextView select3;
    TextView select4;
    ImageView o;
    ImageView x;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Toolbar toolbar = findViewById(R.id.quiz_toolbar);
        setSupportActionBar(toolbar);  // 액션바를 없앴으니까 그걸 툴바가 대신하게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    // 툴바 왼쪽에 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 타이틀 안 보이게 하기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.button_back);  // 뒤로가기 버튼 아이콘 수정

        //인텐트 가져오기
        Intent intent = getIntent();
        title = intent.getStringExtra("TITLE");
        TextView textView = findViewById(R.id.quiz_title);
        quiz = findViewById(R.id.quiz_question);
        select1 = findViewById(R.id.quiz_answer1);
        select2 = findViewById(R.id.quiz_answer2);
        select3 = findViewById(R.id.quiz_answer3);
        select4 = findViewById(R.id.quiz_answer4);
        o = findViewById(R.id.quiz_o);
        x = findViewById(R.id.quiz_x);
        textView.setText(title);

        total = findViewById(R.id.quiz_total);
        solved = findViewById(R.id.quiz_solved);
        int k = list.size();
        total.setText(String.valueOf(k));

        //랜덤으로 리스트 지정
        random(list.size());
        int n=0;
        for(int i=0; i<list.size(); i++){
            n=randomList.get(i);
            //랜덤으로 문제 지정
            question.add(list.get(n).getMeaning());
            //Log.v("퀴즈모드", "문제"+i+" : "+list.get(n).getMeaning());
            //문제에 맞는 정답 저장
            answer.add(list.get(n).getWord());
        }

        screen();
    }

    //뒤로가기버튼
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //채점 버튼
    public void score(View view){

    }

    //난수 발생 함수
    public void random(int n){
        //Log.v("퀴즈 모드", "랜덤리스트 클리어");
        randomList.clear();
        int a;
        Random r = new Random();
        for(int i=0; i<n; i++){
            a = r.nextInt(n);
            randomList.add(i,a);
            for(int j=0; j<i;j++){
                if(randomList.get(i)==randomList.get(j)){
                    i--;
                }
            }
        }
        for(int i=0; i<n; i++){
            //Log.v("퀴즈 모드", "랜덤 : "+ randomList.get(i));
        }
    }

    //화면 지정
    public void screen(){
        int k = position+1;
        solved.setText(String.valueOf(k));
        quiz.setText(question.get(position));
        String correct = answer.get(position);
        //Log.v("퀴즈 모드", "정답 : "+correct);
        //선택지 1~4 중에 랜덤으로 한 곳에 정답이 들어감
        random(list.size());
        boolean isIN=false;
        for(int i=0; i<4; i++){
            if(randomList.get(i)!=position) {
                selection.add(answer.get(randomList.get(i)));
                //Log.v("퀴즈 모드", "선택지 : " + selection.get(i));
            }
            else{
                selection.add(answer.get(randomList.get(i)));
                //Log.v("퀴즈 모드", "선택지(정답) : " + selection.get(i));
                isIN=true;
            }
        }
        if(isIN==false){
            Random r = new Random();
            int a = r.nextInt(4);
            //Log.v("퀴즈 모드", "정답이 들어가는 선택지"+a);
            selection.add(a, correct);
        }
        for(int i=0; i<4; i++){
            //Log.v("퀴즈 모드", "선택지 : "+selection.get(i));
        }
        select1.setText(selection.get(0));
        select2.setText(selection.get(1));
        select3.setText(selection.get(2));
        select4.setText(selection.get(3));
    }

    // 다음 화면으로 이동
    public void next(){
        if(position+1 == list.size()){
            //인텐트로 이동
        }
        else{
            position++;
            screen();
        }
    }

    //정답 맞는지 확인
    public void isCorrect(int n){
        if (selection.get(n).equals(answer.get(position))) {
            o.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    o.setVisibility(View.INVISIBLE);
                }
            }, 500);
            Log.v("퀴즈 모드", "정답입니다.");
        } else {
            x.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    x.setVisibility(View.INVISIBLE);
                }
            }, 500);
            Log.v("퀴즈 모드", "정답이 아닙니다.");
        }
        selection.clear();
    }
    //버튼 1 클릭
    public void main_onClickList1(View view) {
        isCorrect(0);
        next();
    }

    //버튼 2 클릭
    public void main_onClickList2(View view){
        isCorrect(1);
        next();
    }

    //버튼 3 클릭
    public void main_onClickList3(View view){
        isCorrect(2);
        next();
    }

    //버튼 4 클릭
    public void main_onClickList4(View view){
        isCorrect(3);
        next();
    }
}