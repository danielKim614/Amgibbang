package com.cookandroid.amgibbang;

import static com.cookandroid.amgibbang.MainActivity.user;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class TodoListActivity extends AppCompatActivity {
    private static final String TAG = "TodoListActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static NoteDatabase noteDatabase = null;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    LocalDate now;
    String localYear;
    String localMonth;
    String localDate;
    int year, month, day;
    int hour, minute, second;
    TextView dateText;
    TextView timeText;
    EditText inputToDo;
    Fragment todoFragment;
    Context context;

    static ArrayList<Time> timeList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        //toolbar 지정
        Toolbar toolbar = findViewById(R.id.todo_list_toolbar);
        setSupportActionBar(toolbar);  // 액션바를 없앴으니까 그걸 툴바가 대신하게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    // 툴바 왼쪽에 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 타이틀 안 보이게 하기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.button_back);  // 뒤로가기 버튼 아이콘 수정

        //날짜 가져오기
        now = LocalDate.now();
        localYear = now.format(DateTimeFormatter.ofPattern("yyyy"));
        localMonth = now.format(DateTimeFormatter.ofPattern("MMMM"));
        localDate = now.format(DateTimeFormatter.ofPattern("dd"));

        dateText = findViewById(R.id.dateText);
        timeText = findViewById(R.id.timeText);

        getTimeFromDB();
        openDatabase();

        setDateText(dateText);
        setTimeText(timeText);

        // fragment
        todoFragment = new TodoFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container,todoFragment).commit();

        // to-do 저장 버튼
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                saveToDo();
                Toast.makeText(getApplicationContext(),"추가 되었습니다.",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void resetDate() {
        localYear = now.format(DateTimeFormatter.ofPattern("yyyy"));
        localMonth = now.format(DateTimeFormatter.ofPattern("MMMM"));
        localDate = now.format(DateTimeFormatter.ofPattern("dd"));
    }

    public void getTimeFromDB() {
        hour = 0; minute = 0; second = 0;

        timeList.clear();

        // db에서 공부 시간 가져오기
        db.collection(user + localYear + "Time").document(localMonth).collection(localDate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Time time = (Time) document.toObject(Time.class);
                                timeList.add(time);
                            }
                        } else {
                            Log.d("TimeTable", "Error getting documents: ", task.getException());
                        }

                        for (int i = 0; i < timeList.size(); i++) {
                            // 총 공부 시간 연산
                            hour = hour + timeList.get(i).getHour();
                            minute = minute + timeList.get(i).getMinute();
                            second = second + timeList.get(i).getSecond();
                        }

                        // 총 공부 시간 연산 정리
                        if (second >= 60) {
                            minute = minute + (second / 60);
                            second = second % 60;
                        }
                        if (minute >= 60) {
                            hour = hour + (minute / 60);
                            minute = minute % 60;
                        }

                        Log.v("value", ""+hour+":"+minute+":"+second);
                    }
                });
    }

    // 날짜 텍스트뷰 설정
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDateText(TextView textview) {
        year = now.getYear();
        month = now.getMonthValue();
        day = now.getDayOfMonth();
        textview.setText(year+"."+month+"."+day);
    }

    // 시간 텍스트뷰 설정
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setTimeText(TextView textview) {
        textview.setText(hour+"."+minute+"."+second);
    }

    // 날짜 변경
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onButtonClick(View view) {
        switch(view.getId()) {
            case R.id.previousDay:
                now = now.minusDays(1);
                resetDate();
                setDateText(dateText);

                getTimeFromDB();
                setTimeText(timeText);
                break;
            case R.id.nextDay:
                now = now.plusDays(1);
                resetDate();
                setDateText(dateText);

                getTimeFromDB();
                setTimeText(timeText);
                break;
        }
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

    public void openDatabase() {

        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }

        noteDatabase = NoteDatabase.getInstance(this);
        boolean isOpen = noteDatabase.open();
        if (isOpen) {
            Log.d("DB", "Note database is open.");
        } else {
            Log.d("DB", "Note database is not open.");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }
    }

    private void saveToDo(){
        inputToDo = findViewById(R.id.inputToDo);

        String todo = inputToDo.getText().toString();

        String sqlSave = "insert into " + NoteDatabase.TABLE_NOTE + " (TODO) values (" +
                "'" + todo + "')";

        NoteDatabase database = NoteDatabase.getInstance(context);
        database.execSQL(sqlSave);


        inputToDo.setText("");
    }

}