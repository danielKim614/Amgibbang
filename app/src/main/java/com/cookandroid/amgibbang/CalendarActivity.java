package com.cookandroid.amgibbang;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    private RecyclerView calendarProgressbarRecyclerView;

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

        initWidgets();
        calendarProgressbarRecyclerView = findViewById(R.id.calendar_progressbar_recyclerview);
        selectedDate = LocalDate.now();
        setMonthView();

        ArrayList<CalendarProgressbarInfo> infoList = new ArrayList<>();
        infoList.add(new CalendarProgressbarInfo(3, 10));
        infoList.add(new CalendarProgressbarInfo(4, 10));

        setProgressbarView(infoList);
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendar_recyclerview);
        monthYearText = findViewById(R.id.calendar_month_year_textview);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter= new CalendarAdapter(daysInMonth);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        // 캘린더 셀 클릭 이벤트
        calendarAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                // 클릭하면 하나만 선택되게
                Log.d("dabin", "clicked");
                for (int i = 0; i < calendarAdapter.getItemCount(); i++) {
                    CalendarViewHolder holder =  (CalendarViewHolder) calendarRecyclerView.findViewHolderForAdapterPosition(i);
                    if (i == pos) {
                        holder.dayOfMonth.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.calendar_background_cell, null));
                        continue;
                    }
                    holder.dayOfMonth.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
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
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    private void setProgressbarView(ArrayList<CalendarProgressbarInfo> infoList) {
        CalendarProgressbarAdapter adapter= new CalendarProgressbarAdapter(infoList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        calendarProgressbarRecyclerView.setLayoutManager(layoutManager);
        calendarProgressbarRecyclerView.setAdapter(adapter);
    }
}