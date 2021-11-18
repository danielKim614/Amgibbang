package com.cookandroid.amgibbang;

import static com.cookandroid.amgibbang.MainActivity.editState;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>{

    private Context context;
    private ArrayList<MainCard> cards;

    public MainAdapter(ArrayList<MainCard> cards, Context context) {
        this.cards = cards;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
        holder.name.setText(cards.get(position).getName());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String curName = holder.name.getText().toString();  // 클릭 한 것 값 가져옴
                Toast.makeText(view.getContext(), curName, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, CardListActivity.class);
                intent.putExtra("TITLE", curName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(cards!=null){
            return cards.size();
        }
        return 0;
    }

    public void remove(int position){
        // 값 삭제
        try{
            cards.remove(position);      //리스트 지움
            notifyItemRemoved(position);    //새로 고침
        }catch (IndexOutOfBoundsException ex){
            ex. printStackTrace();
        }
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        protected TextView name;
        protected EditText editName;
        protected ImageView bookmark_no;
        protected ImageView bookmark_yes;
        protected CheckBox check;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.main_listName);
            this.bookmark_no = itemView.findViewById(R.id.bookmark_no);
            this.bookmark_yes = itemView.findViewById(R.id.bookmark_yes);
            this.check = itemView.findViewById(R.id.main_check);
            //빈 북마크 클릭 시
            bookmark_no.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    bookmark_no.setVisibility(View.INVISIBLE);
                    bookmark_yes.setVisibility(View.VISIBLE);
                }
            });
            //채워진 북마크 클릭 시
            bookmark_yes.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    bookmark_no.setVisibility(View.VISIBLE);
                    bookmark_yes.setVisibility(View.INVISIBLE);
                }
            });
            //체크박스 클릭 시
            check.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    // 어레이에 추가
                }
            });

            if(editState==true){
                check.setVisibility(View.VISIBLE);
            }

        }
    }
}