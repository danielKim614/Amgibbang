package com.cookandroid.amgibbang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    boolean editstate = false;
    Dialog dialog;
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
                break;
        }
    }

    public void main_onClickAdd(View v){
        // 단어장 추가
        // 다이얼로그 올라옴
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.main_add_dialog);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경

    }

    //다이얼로그 취소
    public void main_dialogCancel(View v){
        dialog.dismiss();
    }

    //다이얼로그 확인
    public void main_dialogAdd(View v){
        //입력 값 가져옴
        dialog.dismiss();
    }

    public void main_onClickBookmarkList(View v){
        //북마크 된 것들만 화면에 나타냄
    }

    public void main_onClickBookmark(View v){
        //북마크 실행
    }

    public void main_setting(View v){
        // 툴바의 설정
    }

    public void main_onClickEdit(View v){
        // edit 버튼
        if(editstate==false){
            editstate=true;
            // 버튼 설정
            TextView textView=findViewById(R.id.main_button_edit);
            TextView textView1=findViewById(R.id.main_button_delete);
            TextView textView2=findViewById(R.id.main_button_selectAll);
            textView.setText("확인");
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);

            //체크하는 부분 생성
            CheckBox check1=findViewById(R.id.main_cardlist_check1);
            CheckBox check2=findViewById(R.id.main_cardlist_check2);
            check1.setVisibility(View.VISIBLE);
            check2.setVisibility(View.VISIBLE);
        }
        else{
            editstate=false;
            TextView textView=findViewById(R.id.main_button_edit);
            TextView textView1=findViewById(R.id.main_button_delete);
            TextView textView2=findViewById(R.id.main_button_selectAll);
            textView.setText("edit");
            textView1.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);

            //체크하는 부분 삭제
            CheckBox check1=findViewById(R.id.main_cardlist_check1);
            CheckBox check2=findViewById(R.id.main_cardlist_check2);
            //체크 모두 초기화
            check1.setChecked(false);
            check2.setChecked(false);
            check1.setVisibility(View.INVISIBLE);
            check2.setVisibility(View.INVISIBLE);
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
        CheckBox check1=findViewById(R.id.main_cardlist_check1);
        CheckBox check2=findViewById(R.id.main_cardlist_check2);
        //모두 체크 or 해제
        CheckBox checkBox=findViewById(R.id.main_button_selectAll);
        if(checkBox.isChecked()){
            check1.setChecked(true);
            check2.setChecked(true);
        }
        else{
            check1.setChecked(false);
            check2.setChecked(false);
        }
    }
}