package com.cookandroid.amgibbang;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.apphosting.datastore.testing.DatastoreTestTrace;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.ListCollectionIdsRequest;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

public class CalendarActivity extends AppCompatActivity {
    private TextView monthYearText;
    private LocalDate selectedDate;

    ArrayList<String> daysInMonth;
    private RecyclerView calendarProgressbarRecyclerView;
    private RecyclerView calendarRecyclerView;
    CalendarAdapter calendarAdapter;
    RecyclerView.LayoutManager calendarLayoutManager;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String yearId;
    String monthId;
    String dayId;
    String userEmail;

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
        calendarAdapter= new CalendarAdapter(daysInMonth);
        calendarLayoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(calendarLayoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        int day = selectedDate.getDayOfMonth();
        dayId = String.valueOf(day);

        int startWeek = selectedDate.withDayOfMonth(1).getDayOfWeek().getValue();
        Log.d("캘린더", "pos: " + (day + startWeek - 1));


        // 캘린더 셀 클릭 이벤트
        calendarAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                // dayId에 선택된 날짜 들어감
                dayId = daysInMonth.get(pos);
                // 클릭된 셀 배경 바꿔주고 and 프로그래스바 보여주기
                clickAndShow(pos);
            }
        });
        Log.d("캘린더", "클릭리스너 달았음");
        CalendarViewHolder holder = (CalendarViewHolder) calendarRecyclerView.findViewHolderForAdapterPosition(day + startWeek - 1);
        //holder.dayOfMonth.setBackgroundResource(R.drawable.calendar_background_cell);
    }

    // 캘린더 셀 선택하면 선택되게 바꿔주고 프로그래스바 보여주기
    private void clickAndShow(int pos) {
        // 아이템 하나만 선택되게
        for (int i = 0; i < calendarAdapter.getItemCount(); i++) {
            CalendarViewHolder holder = (CalendarViewHolder) calendarRecyclerView.findViewHolderForAdapterPosition(i);
            Log.d("캘린더", String.valueOf(holder.dayOfMonth.getText()));
            if (i == pos) {
                holder.dayOfMonth.setBackgroundResource(R.drawable.calendar_background_cell);
                continue;
            }
            holder.dayOfMonth.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }

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
                                // 단어장 이름을 못가져옴..왜지
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