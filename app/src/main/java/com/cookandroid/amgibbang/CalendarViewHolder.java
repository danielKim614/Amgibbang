package com.cookandroid.amgibbang;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarViewHolder extends RecyclerView.ViewHolder {
    public final TextView dayOfMonth;

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemClickListener listener) {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.calendar_cell);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) listener.onItemClick(view, pos);
            }
        });
    }
}
