package com.cookandroid.amgibbang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    private ArrayList<Word> mData = null ;
    private int ck = 0;
    CardListAdapter.OnItemClickListener listener;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2 ;
        CheckBox checkBox;

        ViewHolder(View itemView, CardListAdapter.OnItemClickListener listener) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.itemlist_word) ;
            textView2 = itemView.findViewById(R.id.itemlist_meaning) ;
            checkBox = itemView.findViewById(R.id.single_item_list_checkbox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) listener.onItemClick(view, pos);
                }
            });
        }

    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    CardListAdapter(ArrayList<Word> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public CardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.cardlist_recyclerview_item, parent, false) ;
        CardListAdapter.ViewHolder vh = new CardListAdapter.ViewHolder(view, listener) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(CardListAdapter.ViewHolder holder, int position) {
        holder.textView1.setText(mData.get(position).word) ;
        holder.textView2.setText(mData.get(position).meaning);

        if(ck == 1) {
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        else
            holder.checkBox.setVisibility(View.INVISIBLE);
    }

    public void updateCheckbox(int n) {
        ck = n;
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

        public interface OnItemClickListener {
            void onItemClick(View view, int pos);
        }

        public void setOnItemClickListener(CardListAdapter.OnItemClickListener listener) {
            this.listener = listener;
        }
}