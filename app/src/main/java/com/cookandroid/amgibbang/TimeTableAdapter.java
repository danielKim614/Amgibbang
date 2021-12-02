package com.cookandroid.amgibbang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.ViewHolder> {

    private Context context;
    String [] AP = {"am", "pm"};
    int [] H = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public TimeTableAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) { return position; }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //카드뷰 설정해줌
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_table_recyclerview_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(TimeTableAdapter.ViewHolder holder, int position) {
//        holder.hours.setText(AP+H);
//        holder.min10.setText(mData.get(position).getWord());
//        holder.min20.setText(mData.get(position).getWord());
//        holder.min30.setText(mData.get(position).getWord());
//        holder.min40.setText(mData.get(position).getWord());
//        holder.min50.setText(mData.get(position).getWord());
//        holder.min60.setText(mData.get(position).getWord());

        holder.itemView.setTag(position);
    }


    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return 0;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView hours;
        View min10, min20, min30, min40, min50, min60 ;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            this.hours = itemView.findViewById(R.id.hours) ;
            this.min10 = itemView.findViewById(R.id.min10) ;
            this.min20 = itemView.findViewById(R.id.min20) ;
            this.min30 = itemView.findViewById(R.id.min30) ;
            this.min40 = itemView.findViewById(R.id.min40) ;
            this.min50 = itemView.findViewById(R.id.min50) ;
            this.min60 = itemView.findViewById(R.id.min60) ;
        }
    }
}