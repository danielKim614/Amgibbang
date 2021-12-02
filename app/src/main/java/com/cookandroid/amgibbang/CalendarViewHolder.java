package com.cookandroid.amgibbang;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarViewHolder extends RecyclerView.ViewHolder {
    public final TextView dayOfMonth;
    public final ImageView dot;

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemClickListener listener) {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.calendar_cell);
        dot = itemView.findViewById(R.id.calendar_cell_dot);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = String.valueOf(dayOfMonth.getText());
                if (str.equals("")) return;
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) listener.onItemClick(view, pos);
            }
        });
    }
}