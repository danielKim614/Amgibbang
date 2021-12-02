package com.cookandroid.amgibbang;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    public final ArrayList<String> daysOfMonth;
    OnItemClickListener listener;
    String selectedDay;  // 선택(클릭)된 날짜
    int flag;  // 선택(클릭)된 날짜가 있는 달일 때 flag가 0이 됨.
    int colorList[];

    @RequiresApi(api = Build.VERSION_CODES.O)
    public CalendarAdapter(ArrayList<String> daysOfMonth, String selectedDay, int flag, int colorList[]) {
        this.daysOfMonth = daysOfMonth;
        this.selectedDay = selectedDay;
        this.flag = flag;
        this.colorList = colorList;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = 125;
        return new CalendarViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String day = daysOfMonth.get(position);

        holder.dayOfMonth.setText(day);
        if (flag == 0 && day.equals(selectedDay)) {
            holder.dot.setVisibility(View.VISIBLE);
            holder.dot.bringToFront();
        }
        int intDay;
        if (day.equals("")) return;
        else intDay = Integer.valueOf(day);

        holder.dayOfMonth.setBackgroundResource(getBackgroundResourceId(colorList[intDay]));
    }

    private int getBackgroundResourceId(int n) {
        switch(n) {
            case 1:
                return R.drawable.calendar_cell_background1;
            case 2:
                return R.drawable.calendar_cell_background2;
            case 3:
                return R.drawable.calendar_cell_background3;
            case 4:
                return R.drawable.calendar_cell_background4;
            default:
                return android.R.color.transparent;
        }
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
