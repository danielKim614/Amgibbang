package com.cookandroid.amgibbang;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;


public class SingleItemView extends LinearLayout {

    TextView textView, textView2;

    public SingleItemView(Context context) {
        super(context);
        init(context);
    }

    public SingleItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.single_item_list, this, true);

        textView = findViewById(R.id.itemlist_word);
        textView2 = findViewById(R.id.itemlist_meaning);
    }

    public void setWord(String word) {
        textView.setText(word);
    }

    public void setMeaning(String meaning) {
        textView2.setText(meaning);
    }
}