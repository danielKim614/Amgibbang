package com.cookandroid.amgibbang;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarProgressbarAdapter extends RecyclerView.Adapter<CalendarProgressbarAdapter.ViewHolder> {
    private ArrayList<CalendarProgressbarInfo> infoList = null;

    public CalendarProgressbarAdapter(ArrayList<CalendarProgressbarInfo> infoList) {
        this.infoList = infoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_progressbar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        return new CalendarProgressbarAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarProgressbarAdapter.ViewHolder holder, int position) {
        CalendarProgressbarInfo info = infoList.get(position);
        holder.progressbar.setProgress(info.progress);
        holder.name.setText(info.name);
        holder.score.setText(info.score + "/" + info.total);
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressbar;
        TextView name;
        TextView score;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            progressbar = itemView.findViewById(R.id.calendar_progressbar_circle);
            name = itemView.findViewById(R.id.calendar_progressbar_name);
            score = itemView.findViewById(R.id.calendar_progressbar_score);
        }
    }

}