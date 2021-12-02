package com.cookandroid.amgibbang;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ModeResultAdapter extends RecyclerView.Adapter<ModeResultAdapter.ViewHolder> {
    private ArrayList<Word> list;
    boolean isExpanded = false;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView word;
        TextView meaning;

        ImageView markO;
        ImageView markX;

        LinearLayout expandBar;

        ImageButton expandButton;

        ViewHolder(View itemView) {
            super(itemView) ;
            Log.d("dabin", "in ViewHolder 생성자");

            word = itemView.findViewById(R.id.mode_result_tv_word);
            meaning = itemView.findViewById(R.id.mode_result_tv_meaning);

            markO = itemView.findViewById(R.id.mode_result_iv_o);
            markX = itemView.findViewById(R.id.mode_result_iv_x);

            expandBar = itemView.findViewById(R.id.mode_result_expand);

            expandButton = itemView.findViewById(R.id.mode_result_button_expand);
        }
    }

    ModeResultAdapter(ArrayList<Word> list) {
        this.list = list ;
    }

    @NonNull
    @Override
    public ModeResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.mode_result_recyclerview_item, parent, false);
        ModeResultAdapter.ViewHolder vh = new ModeResultAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("dabin", "in onBindViewHolder");
        holder.word.setText(list.get(position).word);
        holder.meaning.setText(list.get(position).meaning);
        holder.expandBar.setVisibility(View.GONE);

        if (list.get(position).isRight == true) {
            holder.markO.setVisibility(View.VISIBLE);
            holder.markX.setVisibility(View.INVISIBLE);
            holder.expandButton.setVisibility(View.INVISIBLE);
        } else {
            holder.markO.setVisibility(View.INVISIBLE);
            holder.markX.setVisibility(View.VISIBLE);
            holder.expandButton.setVisibility(View.VISIBLE);;
        }

        holder.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("dabin", "clicked");
                if (isExpanded == false) {
                    isExpanded = true;
                    holder.expandBar.setVisibility(View.VISIBLE);
                    holder.expandButton.setBackgroundResource(R.drawable.button_expand_less_gray);
                } else {
                    isExpanded = false;;
                    holder.expandBar.setVisibility(View.GONE);
                    holder.expandButton.setBackgroundResource(R.drawable.button_expand_more_gray);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
