package com.cookandroid.amgibbang;

import static android.os.SystemClock.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SpeedModeActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static ArrayList<Word> list = CardListActivity.list;
    TextView wordTextView;
    ImageView oMark;
    ImageView xMark;
    TextView secButton;
    int curPos; // 현재 단어 위치
    int sec;    // 타이머 초
    int score = 0;

    Handler mHandler = new Handler();

    boolean showState = false;

    // 스와이프에 필요한 변수
    private GestureDetector mGestures;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_mode);

        // 툴바 커스텀
        Toolbar toolbar = findViewById(R.id.mode_toolbar);
        setSupportActionBar(toolbar);  // 액션바를 없앴으니까 그걸 툴바가 대신하게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    // 툴바 왼쪽에 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 타이틀 안 보이게 하기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.button_back);  // 뒤로가기 버튼 아이콘 수정

        TextView toolbarText = findViewById(R.id.mode_toolbar_text);
        wordTextView = findViewById(R.id.speed_mode_word);
        oMark = findViewById(R.id.speed_mode_o);
        xMark = findViewById(R.id.speed_mode_x);
        secButton = findViewById(R.id.speed_mode_button_sec);

        // 타이머 몇 초로 시작할건지
        sec = 3;
        secButton.setText(sec + "s");

        Intent rIntent = getIntent();
        String titleText = rIntent.getStringExtra("TITLE");
        toolbarText.setText(titleText);

        // 스와이프 제스쳐
        mGestures = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        //if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                        //  return false;

                        // right to left swipe
                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.d("dabin", "right to left");
                            if (showState == false) {
                                oMark.setVisibility(View.VISIBLE);
                                list.get(curPos).isRight = true;
                                score++;
                                showState = true;
                            }
                        }
                        // left to right swipe
                        else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.d("dabin", "left to right");
                        }
                        // down to up swipe
                        else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.d("dabin", "dowm to up");
                            if (showState == false) {
                                xMark.setVisibility(View.VISIBLE);
                                list.get(curPos).isRight = false;
                                showState = true;
                            }
                        }
                        // up to down swipe
                        else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.d("dabin", "up to down");
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        // 작업 스레드에서 초 세는 작업 해줌
        Thread thread = new Thread() {
            public void run() {
                for (curPos = 0; curPos < list.size(); curPos++) {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showState = false;
                            xMark.setVisibility(View.INVISIBLE);
                            oMark.setVisibility(View.INVISIBLE);
                            wordTextView.setText(list.get(curPos).word);
                        }
                    });

                    for (int i = 0; i < sec; i++) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (showState == true) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }

                    if (showState == false) {
                        showState = true;
                        list.get(curPos).isRight = false;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                xMark.setVisibility(View.VISIBLE);
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        thread.start();




/*        CalendarProgressbarInfo info = new CalendarProgressbarInfo(score, list.size());
        Intent intent = new Intent(this, ModeResultActivity.class);
        intent.putExtra("TITLE", titleText);
        intent.putExtra("INFO", info);
        Log.d("dabin", "putExtra 끝냄");
        startActivity(intent);*/
        // 작업 스레드 끝나기를 기다린 다음 스피드 모드 결과 화면 띄우기
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestures != null) {
            return mGestures.onTouchEvent(event);
        } else {
            return super.onTouchEvent(event);
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

    public void onButtonClick(View view) {
        if (sec < 10) {
            sec++;
            secButton.setText(sec + "s");
        }
        else {
            sec = 1;
            secButton.setText(sec + "s");
        }
    }
}