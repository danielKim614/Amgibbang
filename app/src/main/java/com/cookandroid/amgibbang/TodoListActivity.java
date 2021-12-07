package com.cookandroid.amgibbang;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class TodoListActivity extends AppCompatActivity {
    private static final String TAG = "TodoListActivity";

    public static NoteDatabase noteDatabase = null;

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

        openDatabase();

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

        // 플로팅액션버튼(RESET) 버튼
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) findViewById(R.id.todo_list_fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sqlReset = "delete from " + NoteDatabase.TABLE_NOTE;

                    NoteDatabase database = NoteDatabase.getInstance(context);
                    database.execSQL(sqlReset);
                }
            });
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
        Log.v("String",todo);

        String sqlSave = "insert into " + NoteDatabase.TABLE_NOTE + " (TODO) values (" +
                "'" + todo + "')";

        if(todo.getBytes().length > 0) {
            NoteDatabase database = NoteDatabase.getInstance(context);
            database.execSQL(sqlSave);
        }
        else
            Toast.makeText(this, "값을 입력하세요.", Toast.LENGTH_SHORT).show();

        inputToDo.setText("");
    }

}