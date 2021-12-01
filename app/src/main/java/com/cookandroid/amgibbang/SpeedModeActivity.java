package com.cookandroid.amgibbang;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;

public class SpeedModeActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView wordTextView;
    ImageView oMark;
    ImageView xMark;
    TextView secButton;
    int curPos; // 현재 단어 위치
    int sec;    // 타이머 초
    int score = 0;
    boolean DialogShowState; // 도움말 다이얼로그 띄울지말지
    boolean showState = false;

    SharedPreferences sharedPref;
    Thread thread;
    Handler mHandler = new Handler();



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

        sharedPref = getPreferences(Context.MODE_PRIVATE);

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
                                CardListActivity.list.get(curPos).isRight = true;
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
                                CardListActivity.list.get(curPos).isRight = false;
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
        thread = new Thread() {
            public void run() {
                for (curPos = 0; curPos < CardListActivity.list.size(); curPos++) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showState = false;
                            xMark.setVisibility(View.INVISIBLE);
                            oMark.setVisibility(View.INVISIBLE);
                            wordTextView.setText(CardListActivity.list.get(curPos).word);
                        }
                    });

                    for (int i = 0; i < sec; i++) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            return;
                        }
                        if (showState == true) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                return;
                            }
                            break;
                        }
                    }

                    if (showState == false) {
                        showState = true;
                        CardListActivity.list.get(curPos).isRight = false;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                xMark.setVisibility(View.VISIBLE);
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        CalendarProgressbarInfo info = new CalendarProgressbarInfo(score, CardListActivity.list.size());
                        Intent intent = new Intent(SpeedModeActivity.this, ModeResultActivity.class);
                        intent.putExtra("TITLE", titleText);
                        intent.putExtra("INFO", info);
                        startActivity(intent);
                    }
                });
            }
        };

        DialogShowState = sharedPref.getBoolean("DIALOG_SHOW", true);

        if (DialogShowState == true)
            showHelpDialog();
        else
            thread.start();
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

    public void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View helpDialogView = inflater.inflate(R.layout.speed_mode_hlep_dialog, null);
        builder.setView(helpDialogView);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                thread.start();
            }
        });

        CheckBox check = helpDialogView.findViewById(R.id.speed_mode_dialog_check);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("DIALOG_SHOW", false);
                    editor.commit();
                }
            }
        });

        AlertDialog helpDialog = builder.create();
        helpDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (thread != null) {
            thread.interrupt();
            thread=null;
        }
    }
}