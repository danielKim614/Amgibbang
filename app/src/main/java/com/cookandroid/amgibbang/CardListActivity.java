package com.cookandroid.amgibbang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CardListActivity extends AppCompatActivity {

    ImageButton add_btn;
    TextView edit_btn;
    boolean editstate = false;
    private CardListAdapter cardListAdapter;
    private RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        //toolbar 지정
        Toolbar toolbar = findViewById(R.id.cardlist_toolbar);
        setSupportActionBar(toolbar);  // 액션바를 없앴으니까 그걸 툴바가 대신하게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    // 툴바 왼쪽에 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 타이틀 안 보이게 하기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.button_back);  // 뒤로가기 버튼 아이콘 수정

        edit_btn = findViewById(R.id.cardlist_button_edit);
        add_btn = findViewById(R.id.cardlist_addCardButton);
        FloatingActionButton fab = findViewById(R.id.cardlist_fab);
        fab.setOnClickListener(new FABClickListener());


        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<Word> list = new ArrayList<>();
        list.add(new Word("사과", "apple"));
        list.add(new Word("사과", "apple"));
        list.add(new Word("사과", "apple"));
        list.add(new Word("사과", "apple"));
        list.add(new Word("사과", "apple"));
        list.add(new Word("사과", "apple"));
        list.add(new Word("사과", "apple"));
        list.add(new Word("사과", "apple"));
        list.add(new Word("사과", "apple"));
        list.add(new Word("사과", "apple"));
        list.add(new Word("사과", "apple"));
        list.add(new Word("사과", "apple"));
        list.add(new Word("사과", "apple"));
        list.add(new Word("사과", "apple"));
        list.add(new Word("사과", "apple"));



//        for (int i=0; i<100; i++) {
//            list.add(String.format("TEXT %d", i)) ;
//        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = findViewById(R.id.cardlist_RecyclerView) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        cardListAdapter = new CardListAdapter(list) ;
        recyclerView.setAdapter(cardListAdapter) ;

        // 캘린더 셀 클릭 이벤트
        cardListAdapter.setOnItemClickListener(new CardListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int pos){
                // 클릭하면 하나만 선택되게
                Log.d("dabin", "clicked");
            }
        });

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    // edit 버튼 행동

                    // + 버튼 행동
                    case R.id.cardlist_addCardButton:
                        Intent intent=new Intent(CardListActivity.this, AddWordActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };
        add_btn.setOnClickListener(onClickListener);
    }

    // 뒤로가기 버튼 행동
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 플로팅액션버튼 리스너
    class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.v("superoid","=================클릭 Fab================");
        }
    }


    public void onEditButtonClick(View v){
        // edit 버튼
        if(editstate == false) {
            editstate = true;
            // 버튼 설정
            TextView textView=findViewById(R.id.cardlist_button_edit);
            TextView textView1=findViewById(R.id.cardlist_button_delete);
            TextView textView2=findViewById(R.id.cardlist_button_selectAll);
            CheckBox checkBox=findViewById(R.id.single_item_list_checkbox);
            textView.setText("확인");
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            add_btn.setVisibility(View.GONE);
            checkBox.setVisibility(View.VISIBLE);

            //체크하는 부분 생성
            cardListAdapter.updateCheckbox(1);
        }

        else {
            editstate = false;
            TextView textView=findViewById(R.id.cardlist_button_edit);
            TextView textView1=findViewById(R.id.cardlist_button_delete);
            TextView textView2=findViewById(R.id.cardlist_button_selectAll);
            CheckBox checkBox=findViewById(R.id.single_item_list_checkbox);
            textView.setText("edit");
            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            add_btn.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.INVISIBLE);

            //체크하는 부분 생성
            cardListAdapter.updateCheckbox(0);
        }
    }

    public void cardlist_onClickDelete(View v){
        // 삭제 버튼 누를 시 체크된 단어장 삭제됨
    }

    public void cardlist_onClickSelectAll(View v){
        // 원 체크 후 확인 버튼 누를 시 삭제
    }

}