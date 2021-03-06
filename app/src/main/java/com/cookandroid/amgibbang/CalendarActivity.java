package com.cookandroid.amgibbang;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {
    private TextView monthYearText;
    private LocalDate selectedDate;

    ArrayList<String> daysInMonth;
    private RecyclerView calendarProgressbarRecyclerView;
    private RecyclerView calendarRecyclerView;
    CalendarAdapter calendarAdapter;
    RecyclerView.LayoutManager calendarLayoutManager;

    String selectedDay;
    int flag = 0;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    static String yearId;
    static String monthId;
    String dayId;
    static String userEmail;

    int colorList[];
    ArrayList<CalendarProgressbarInfo> infoList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = findViewById(R.id.calendar_toolbar);
        setSupportActionBar(toolbar);  // 액션바를 없앴으니까 그걸 툴바가 대신하게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    // 툴바 왼쪽에 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 타이틀 안 보이게 하기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.button_back);  // 뒤로가기 버튼 아이콘 수정

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("EMAIL");
        initWidgets();
        calendarProgressbarRecyclerView = findViewById(R.id.calendar_progressbar_recyclerview);
        selectedDate = LocalDate.now();
        int day = selectedDate.getDayOfMonth();
        selectedDay = String.valueOf(day);

        setMonthView();
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendar_recyclerview);
        monthYearText = findViewById(R.id.calendar_month_year_textview);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        daysInMonth = daysInMonthArray(selectedDate);

        colorList = new int[32]; // 0은 안 씀
        Log.d("dabin2", "in setList");

        db.collection(CalendarActivity.yearId + CalendarActivity.monthId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("dabin2", "in for문");
                                CalendarCellInfo info = document.toObject(CalendarCellInfo.class);
                                int index = Integer.valueOf(document.getId());
                                colorList[index] = info.level;
                                Log.d("dabin2", index + ", " + info.level);
                            }
                        } else {
                            Log.d("dabin", "Error getting documents: ", task.getException());
                        }

                        calendarAdapter= new CalendarAdapter(daysInMonth,selectedDay, flag, colorList);
                        calendarLayoutManager = new GridLayoutManager(getApplicationContext(), 7);
                        calendarRecyclerView.setLayoutManager(calendarLayoutManager);
                        calendarRecyclerView.setAdapter(calendarAdapter);


                        // 캘린더 셀 클릭 이벤트
                        calendarAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int pos) {
                                // dayId에 선택된 날짜 들어감
                                dayId = daysInMonth.get(pos);
                                // 클릭된 셀 배경 바꿔주고 and 프로그래스바 보여주기
                                click(pos);
                                showProgressbar();
                            }
                        });

                        if (flag == 0) {
                            dayId = selectedDay;
                            showProgressbar();
                        } else {
                            dayId = "-";
                            showProgressbar();
                        }
                    }
                });
    }

    // 캘린더 셀 선택하면 선택되게 바꿔주고 프로그래스바 보여주기
    private void click(int pos) {
        flag = 0;
        // 아이템 하나만 선택되게
        for (int i = 0; i < calendarAdapter.getItemCount(); i++) {
            CalendarViewHolder holder = (CalendarViewHolder) calendarRecyclerView.findViewHolderForAdapterPosition(i);

            if (i == pos) {
                holder.dot.setVisibility(View.VISIBLE);
                holder.dot.bringToFront();
                selectedDay = String.valueOf(holder.dayOfMonth.getText());
                continue;
            }
            holder.dot.setVisibility(View.INVISIBLE);
        }
    }

    private void showProgressbar() {
        // 프로그래스바에 담을 데이터 리스트 초기화
        infoList.clear();

        // 프로그래스바 정보 db에서 가져와서 리사이클러뷰 보여주기
        db.collection(yearId).document(monthId).collection(dayId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CalendarProgressbarInfo info = (CalendarProgressbarInfo) document.toObject(CalendarProgressbarInfo.class);
                                infoList.add(info);
                            }
                        } else {
                            Log.d("dabin", "Error getting documents: ", task.getException());
                        }
                        setProgressbarView(infoList);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM");
        String monthYear = date.format(formatter);

        formatter = DateTimeFormatter.ofPattern("yyyy");
        yearId = userEmail + date.format(formatter);
        formatter = DateTimeFormatter.ofPattern("MMMM");
        monthId = date.format(formatter);

        return monthYear;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek)
                daysInMonthArray.add("");
            else
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
        }
        return daysInMonthArray;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();   // 뒤로가기 버튼 누르면 액티비티 종료
                overridePendingTransition(R.anim.none, R.anim.slide_right_exit);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view) {
        flag--;
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view) {
        flag++;
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    private void setProgressbarView(ArrayList<CalendarProgressbarInfo> infoList) {
        CalendarProgressbarAdapter adapter= new CalendarProgressbarAdapter(infoList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 4);
        calendarProgressbarRecyclerView.setLayoutManager(layoutManager);
        calendarProgressbarRecyclerView.setAdapter(adapter);
    }
}