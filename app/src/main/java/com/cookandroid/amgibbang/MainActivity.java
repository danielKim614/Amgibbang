package com.cookandroid.amgibbang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    boolean editState = false;
    int a=1;
    String dialogInput;
    private ArrayList<MainCard> cards;
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //toolbar 지정
        Toolbar main_toolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Toolbar main_bottom_toolbar=findViewById(R.id.main_bottom_toolbar);
        setSupportActionBar(main_bottom_toolbar);

        //리사이클러뷰
        recyclerView = findViewById(R.id.main_rv);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        cards = new ArrayList<>();   // string 리스트
        mainAdapter = new MainAdapter(cards);
        recyclerView.setAdapter(mainAdapter);
    }

    public void main_onClickMove(View v){
        // 화면 전환
        switch (v.getId()){
            case R.id.main_button_cal:
                Intent intent=new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
                break;
            case R.id.main_button_timer:
                break;
            case R.id.main_button_dictionary:
                Intent intent1=new Intent(MainActivity.this, DictionaryMainActivity.class);
                startActivity(intent1);
                break;
        }
    }

    public void main_onClickAdd(View v){
        // 단어장 추가
        // 다이얼로그 올라옴
        MainCustomDialog dialog = new MainCustomDialog(this);
        dialog.setMainDialogListener(new MainCustomDialog.MainCustomDialogListener(){
            @Override
            public void mainDialogPositive(String inputName) {
                dialogInput=inputName;
                Log.v("다이얼로그", "입력되었습니다.");
                Log.v("다이얼로그", "입력값 : "+dialogInput);
                MainCard mainCard = new MainCard(dialogInput);
                cards.add(mainCard);
                mainAdapter.notifyDataSetChanged(); // 새로 고침
            }
            @Override
            public void mainDialogNegative(){
                Log.v("다이얼로그", "취소되었습니다.");
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    //카드리스트로 이동
    public void main_onClickList(View v){
        if(editState==false){
            Intent intent = new Intent(MainActivity.this, CardListActivity.class);
            startActivity(intent);
        }
    }

    public void main_onClickBookmarkList(View v){
        //북마크 된 것들만 화면에 나타냄
    }

    public void main_onClickBookmark(View v){
        //북마크 실행
    }

    public void main_setting(View v){
        // 로그아웃, 다크모드 온 오프 창 띄움
        String[] array = {"다크 모드 ON", "다크 모드 OFF"};
        if(editState==false){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(array, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch(array[i]){
                        case "다크 모드 ON":
                            Log.v("다이얼로그", "다크모드를 킵니다.");
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            break;
                        case "다크 모드 OFF":
                            Log.v("다이얼로그", "다크모드를 끕니다.");
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            break;
                    }
                }
            });
            builder.show();
        }
    }

    public void main_onClickEdit(View v){
        // edit 버튼
        if(editState==false){
            editState=true;
            // 버튼 설정
            TextView textView=findViewById(R.id.main_button_edit);
            TextView textView1=findViewById(R.id.main_button_delete);
            TextView textView2=findViewById(R.id.main_button_selectAll);
            textView.setText("확인");
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            CheckBox checkAll=findViewById(R.id.main_button_selectAll);
            checkAll.setChecked(false);

            //체크하는 부분 생성
            CheckBox check1=findViewById(R.id.main_check);
            check1.setVisibility(View.VISIBLE);
        }
        else{
            editState=false;
            TextView textView=findViewById(R.id.main_button_edit);
            TextView textView1=findViewById(R.id.main_button_delete);
            TextView textView2=findViewById(R.id.main_button_selectAll);
            textView.setText("edit");
            textView1.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);

            //체크하는 부분 삭제
            CheckBox check1=findViewById(R.id.main_check);
            //체크 모두 초기화
            check1.setChecked(false);
            check1.setVisibility(View.INVISIBLE);
        }
    }

    public void main_onClickDelete(View v){
        // 삭제 버튼 누를 시 체크된 단어장 삭제됨
    }

    public void main_onClickCheck(View v){
        // 원 체크 후 확인 버튼 누를 시 삭제
    }

    public void main_onClickSelectAll(View v){
        // 전체 선택 클릭 시 모든 체크박스 체크 됨
        // 모든 체크박스 불러옴
    }

}