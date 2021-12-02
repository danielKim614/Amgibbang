package com.cookandroid.amgibbang;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class TimeTableActivity extends AppCompatActivity {
//    FirebaseFirestore db = FirebaseFirestore.getInstance();

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//    private RecyclerView recyclerView;
//    private TimeTableAdapter timetableAdapter;
    LocalDate now;
    String localYear;
    String localMonth;
    String localDate;
    int year;
    int month;
    int day;
    TextView dateText;
    TextView timeText;

    static ArrayList<String> titleList = new ArrayList<>();
    static ArrayList<Time> timeList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        //toolbar 지정
        Toolbar toolbar = findViewById(R.id.time_table_toolbar);
        setSupportActionBar(toolbar);  // 액션바를 없앴으니까 그걸 툴바가 대신하게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    // 툴바 왼쪽에 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 타이틀 안 보이게 하기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.button_back);  // 뒤로가기 버튼 아이콘 수정

//        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
//        recyclerView = findViewById(R.id.time_table_recyclerView) ;
//        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;
//
//        // 리사이클러뷰에 CardListAdapter 객체 지정.
//        timetableAdapter = new TimeTableAdapter(this) ;
//        recyclerView.setAdapter(timetableAdapter) ;

        //날짜 가져오기
        now = LocalDate.now();
        localYear = now.format(DateTimeFormatter.ofPattern("yyyy"));
        localMonth = now.format(DateTimeFormatter.ofPattern("MMMM"));
        localDate = now.format(DateTimeFormatter.ofPattern("dd"));

        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);

        setDateText(dateText);

//        // 프로그래스바 정보 db에서 가져와서 리사이클러뷰 보여주기
//        db.collection(user + localYear + "Time").document(localMonth).collection(localDate)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Time time = (Time) document.toObject(Time.class);
//                                timeList.add(time);
//                            }
//                        } else {
//                            Log.d("TimeTable", "Error getting documents: ", task.getException());
//                        }
//                        setTimeTableView(timeList);
//                    }
//                });
    }

    // 날짜 텍스트뷰 설정
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDateText(TextView textview) {
        year = now.getYear();
        month = now.getMonthValue();
        day = now.getDayOfMonth();
        textview.setText(year+"."+month+"."+day);
    }

    // 날짜 변경
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onButtonClick(View view) {
        switch(view.getId()) {
            case R.id.previousDay:
                now = now.minusDays(1);
                setDateText(dateText);
                break;
            case R.id.nextDay:
                now = now.plusDays(1);
                setDateText(dateText);
                break;
        }
    }

    private void setTimeTableView(ArrayList<Time> timeList) {

    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();   // 뒤로가기 버튼 누르면 액티비티 종료
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        overridePendingTransition(R.anim.slide_right_enter, R.anim.none);
//
//        // Firebase로부터 시간 데이터 가져오기
//        db.collection(user + localYear + "Time").document(localMonth).collection(localDate)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Time time = document.toObject(Time.class);
//                                timeList.add(time);
//                            }
//                        } else {
//                            Log.w("test", "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//        recyclerView.setAdapter(timetableAdapter);
//    }
}