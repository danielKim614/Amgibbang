package com.cookandroid.amgibbang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>{

    private ArrayList<String> nameList;
    private String editTextName;

    public MainAdapter(ArrayList<String> nameList) {
        this.nameList = nameList;
    }

    @NonNull
    @Override   // 추가할 아이템 설정
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //카드뷰 설정해줌
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recyclerview_item, parent, false);
        MainViewHolder holder = new MainViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        //name을 리스트 안에 있는 값으로 변환
        holder.name.setText(nameList.get(position));

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String curName = holder.name.getText().toString();  // 클릭 한 것 값 가져옴
                Toast.makeText(view.getContext(), curName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(nameList!=null){
            return nameList.size();
        }
        return 0;
    }

    public void remove(int position){
        // 값 삭제
        try{
            nameList.remove(position);      //리스트 지움
            notifyItemRemoved(position);    //새로 고침
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        protected TextView name;
        protected EditText editName;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.main_listName);
        }
    }
}