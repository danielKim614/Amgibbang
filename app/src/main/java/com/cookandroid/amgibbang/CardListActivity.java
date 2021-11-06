package com.cookandroid.amgibbang;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CardListActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView buttonBack;
    TextView editTextView_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        toolbar = findViewById(R.id.toolbar);
        buttonBack = findViewById(R.id.cardlist_buttonBack);
        editTextView_btn = findViewById(R.id.cardlist_editButton);
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

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CardListActivity.this, "뒤로가기입니다.", Toast.LENGTH_SHORT).show();
            }


        });

        editTextView_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CardListActivity.this, "edit입니다.", Toast.LENGTH_SHORT).show();
            }


        });
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