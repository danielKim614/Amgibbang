package com.cookandroid.amgibbang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ModeResultAdapter extends RecyclerView.Adapter<ModeResultAdapter.ViewHolder> {
    private ArrayList<Word> list = null;
    boolean isExpanded = false;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView word;
        TextView meaning;

        ImageView markO;
        ImageView markX;

        LinearLayout expandBar;

        FrameLayout expandButton;
        ImageButton expandMoreButton;
        ImageButton expandLessButton;

        ViewHolder(View itemView) {
            super(itemView) ;

            word = itemView.findViewById(R.id.mode_result_tv_word);
            meaning = itemView.findViewById(R.id.mode_result_tv_meaning);

            markO = itemView.findViewById(R.id.mode_result_iv_o);
            markX = itemView.findViewById(R.id.mode_result_iv_x);

            expandBar = itemView.findViewById(R.id.mode_result_expand);

            expandButton = itemView.findViewById(R.id.mode_result_button_expand);
            expandMoreButton = itemView.findViewById(R.id.mode_result_button_expand_more);
            expandLessButton = itemView.findViewById(R.id.mode_result_button_expand_less);
        }
    }

    ModeResultAdapter(ArrayList<Word> list) {
        list = list ;
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
        holder.word.setText(list.get(position).word);
        holder.meaning.setText(list.get(position).meaning);
        holder.expandBar.setVisibility(View.GONE);

        if (list.get(position).isRight == true) {
            holder.markO.setVisibility(View.VISIBLE);
            holder.markX.setVisibility(View.INVISIBLE);
            holder.expandButton.setEnabled(false);
            holder.expandMoreButton.setVisibility(View.INVISIBLE);
            holder.expandLessButton.setVisibility(View.INVISIBLE);
        } else {
            holder.markO.setVisibility(View.INVISIBLE);
            holder.markX.setVisibility(View.VISIBLE);
            holder.expandButton.setEnabled(true);
            holder.expandMoreButton.setVisibility(View.VISIBLE);
            holder.expandLessButton.setVisibility(View.INVISIBLE);
        }

        holder.expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpanded == false) {
                    isExpanded = true;
                    holder.expandMoreButton.setVisibility(View.INVISIBLE);
                    holder.expandLessButton.setVisibility(View.VISIBLE);
                    holder.expandBar.setVisibility(View.VISIBLE);
                } else {
                    isExpanded = false;
                    holder.expandMoreButton.setVisibility(View.VISIBLE);
                    holder.expandLessButton.setVisibility(View.INVISIBLE);
                    holder.expandBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
