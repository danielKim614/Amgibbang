package com.cookandroid.amgibbang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CardListActivity extends AppCompatActivity {

    ImageButton add_btn;
    TextView edit_btn;
    boolean editstate = false;

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

        ListView listView = findViewById(R.id.cardlist_listview);
        SingleAdapter adapter = new SingleAdapter();

        adapter.addItem(new SingleItem("ant", "개미"));
        adapter.addItem(new SingleItem("apple", "사과"));
        adapter.addItem(new SingleItem("ball", "공"));
        adapter.addItem(new SingleItem("balloon", "풍선"));
        adapter.addItem(new SingleItem("banana", "바나나"));
        adapter.addItem(new SingleItem("bee", "벌"));
        adapter.addItem(new SingleItem("ant", "개미"));
        adapter.addItem(new SingleItem("apple", "사과"));
        adapter.addItem(new SingleItem("ball", "공"));
        adapter.addItem(new SingleItem("balloon", "풍선"));
        adapter.addItem(new SingleItem("banana", "바나나"));
        adapter.addItem(new SingleItem("bee", "벌"));
        adapter.addItem(new SingleItem("ant", "개미"));
        adapter.addItem(new SingleItem("apple", "사과"));
        adapter.addItem(new SingleItem("ball", "공"));
        adapter.addItem(new SingleItem("balloon", "풍선"));
        adapter.addItem(new SingleItem("banana", "바나나"));
        adapter.addItem(new SingleItem("bee", "벌"));
        listView.setAdapter(adapter);

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

    // 리스트뷰 어뎁터
    class SingleAdapter extends BaseAdapter {
        ArrayList<SingleItem> items = new ArrayList<SingleItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(SingleItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position)  {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SingleItemView singleItemView = null;
            if (convertView == null) {
                singleItemView = new SingleItemView(getApplicationContext());
            }
            else {
                singleItemView = (SingleItemView) convertView;
            }
            SingleItem item = items.get(position);
            singleItemView.setWord(item.getWord());
            singleItemView.setMeaning(item.getMeaning());
            return singleItemView;
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
        }
    }

    public void cardlist_onClickDelete(View v){
        // 삭제 버튼 누를 시 체크된 단어장 삭제됨
    }

    public void cardlist_onClickSelectAll(View v){
        // 원 체크 후 확인 버튼 누를 시 삭제
    }

}