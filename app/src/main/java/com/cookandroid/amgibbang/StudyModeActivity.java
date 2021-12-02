package com.cookandroid.amgibbang;

import static com.cookandroid.amgibbang.MainActivity.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StudyModeActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Word> list = CardListActivity.list;
    String titleText;  // 단어장 이름
    TextView wordTextView;
    ImageButton settingButton;
    public int settingValue = 0;
    int tempValue = 0;
    int curPos = 0; // 현재 단어 위치
    String localYear;
    String localMonth;
    String localDate;
    LocalTime startTime, endTime; // 액티비티 시작 및 종료 시간
    int hour, minute, second;

    // 스와이프에 필요한 변수
    private GestureDetector mGestures;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_mode);

        // 툴바 커스텀
        Toolbar toolbar = findViewById(R.id.mode_toolbar);
        setSupportActionBar(toolbar);  // 액션바를 없앴으니까 그걸 툴바가 대신하게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    // 툴바 왼쪽에 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 타이틀 안 보이게 하기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.button_back);  // 뒤로가기 버튼 아이콘 수정

        settingButton = findViewById(R.id.study_mode_setting);
        settingButton.setOnClickListener(new settingClickListener());

        TextView toolbarText = findViewById(R.id.mode_toolbar_text);
        wordTextView = findViewById(R.id.study_mode_word);
        wordTextView.setText(list.get(curPos).word);

        Intent intent = getIntent();
        titleText = intent.getStringExtra("TITLE");
        toolbarText.setText(intent.getStringExtra("TITLE"));

        //날짜 및 시간 가져오기
        localYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
        localMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM"));
        localDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd"));

        // 스와이프 제스쳐
        mGestures = new GestureDetector(this,
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (tempValue == 0) {
                        wordTextView.setText(list.get(curPos).meaning);
                        tempValue = 1;
                    }
                    else {
                        wordTextView.setText(list.get(curPos).word);
                        tempValue = 0;
                    }
                    return super.onSingleTapUp(e);
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    //if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    //  return false;
                        // right to left swipe
                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.d("StudyMode", "right to left");
                            ++curPos;
                            if (curPos < list.size()) {
                                if (settingValue == 0)
                                    wordTextView.setText(list.get(curPos).word);
                                else
                                    wordTextView.setText(list.get(curPos).meaning);
                            }
                            else {
                                Toast.makeText(StudyModeActivity.this.getApplicationContext(), "마지막 단어입니다.", Toast.LENGTH_SHORT).show();
                                curPos--;
                            }
                        }
                        // left to right swipe
                        else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            Log.d("StudyMode", "left to right");
                            --curPos;
                            if (curPos >= 0) {
                                    if (settingValue == 0)
                                        wordTextView.setText(list.get(curPos).word);
                                    else
                                        wordTextView.setText(list.get(curPos).meaning);
                            }
                            else {
                                Toast.makeText(StudyModeActivity.this.getApplicationContext(), "첫 단어입니다.", Toast.LENGTH_SHORT).show();
                                curPos = 0;
                            }
                        }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });
    }

    // setting버튼 리스너
    class settingClickListener implements View.OnClickListener { // * 커스텀 다이얼로그로 바꿔야 *
        @Override
        public void onClick(View v){
            // 단어 or 뜻 먼저 표시
            String[] array = {"단어 먼저 표시", "뜻 먼저 표시"};
            AlertDialog.Builder builder = new AlertDialog.Builder(StudyModeActivity.this);
            builder.setItems(array, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch(array[i]){
                        case "단어 먼저 표시":
                            settingValue = 0;
                            wordTextView.setText(list.get(curPos).word);
                            break;
                        case "뜻 먼저 표시":
                            settingValue = 1;
                            wordTextView.setText(list.get(curPos).meaning);
                            break;
                    }
                }
            });
            builder.show();
        }
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

    // 공부 시간 기록
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setTime() {
        Duration duration = Duration.between(startTime, endTime);
        hour = (int)duration.getSeconds() / 3600;
        minute = (int)(duration.getSeconds() % 3600) / 60;
        second = (int)(duration.getSeconds() % 3600) % 60;

        Time time = new Time(titleText, hour, minute, second);
        db.collection(user + localYear + "Time").document(localMonth).collection(localDate).document().set(time);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        startTime = LocalTime.now();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStop() {
        super.onStop();
        endTime = LocalTime.now();
        setTime();
    }
}
