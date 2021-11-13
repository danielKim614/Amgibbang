package com.cookandroid.amgibbang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DictionaryMainActivity extends AppCompatActivity {

    String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_main);

        Button btn = (Button) findViewById(R.id.searchBtn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TextView searchText = (TextView) findViewById(R.id.searchText);
                //final TextView searchResult = (TextView) findViewById(R.id.searchResult);
                keyword = searchText.getText().toString();

                Intent intent = new Intent(DictionaryMainActivity.this, DictionarySubActivity.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
                //searchResult.setText(str);


            }
        });
    }


}

