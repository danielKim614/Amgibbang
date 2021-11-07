package com.cookandroid.amgibbang;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CardListActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageButton buttonBack, add_btn;
    Button edit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        toolbar = findViewById(R.id.toolbar);
        buttonBack = findViewById(R.id.cardlist_buttonBack);
        edit_btn = findViewById(R.id.cardlist_editButton);
        add_btn = findViewById(R.id.cardlist_addCardButton);

        ListView listView = findViewById(R.id.cardlist_listview);
        SingleAdapter adapter = new SingleAdapter();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

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
                    //뒤로가기 버튼 행동
                    case R.id.cardlist_buttonBack:
                        Log.v("superoid","=================클릭 back================");
                        //Intent intent = new Intent(MainActivity.this,SubActivity.class);
                       //startActivity(intent);
                        break;
                    //edit 버튼 행동
                    case R.id.cardlist_editButton:
                        Log.v("superoid","=================클릭 edit================");
                        break;
                    //+ 버튼 행동
                    case R.id.cardlist_addCardButton:
                        Log.v("superoid","=================클릭 +================");
                        break;
                }
            }
        };

        buttonBack.setOnClickListener(onClickListener);
        edit_btn.setOnClickListener(onClickListener);
        add_btn.setOnClickListener(onClickListener);
    }

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
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SingleItemView singleItemView = null;
            if (convertView == null) {
                singleItemView = new SingleItemView(getApplicationContext());
            } else {
                singleItemView = (SingleItemView) convertView;
            }
            SingleItem item = items.get(position);
            singleItemView.setWord(item.getWord());
            singleItemView.setMeaning(item.getMeaning());
            return singleItemView;
        }
    }
}