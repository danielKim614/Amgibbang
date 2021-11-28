package com.cookandroid.amgibbang;

import static com.cookandroid.amgibbang.CardListActivity.editState;
import static com.cookandroid.amgibbang.CardListActivity.id;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Word> mData;
    CardListAdapter.OnItemClickListener listener;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public CardListAdapter(ArrayList<Word> list, Context context) {
        this.mData = list ;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) { return position; }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //카드뷰 설정해줌
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlist_recyclerview_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(CardListAdapter.ViewHolder holder, int position) {
        holder.textView1.setText(mData.get(position).getWord()) ;
        holder.textView2.setText(mData.get(position).getMeaning());

        //체크박스
        boolean isChecked = mData.get(position).getCheckBox();
        if(isChecked==true){
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.itemView.setTag(position);

        if(editState==false) {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
        else { //editState가 True 일 때
            // 선택 삭제
            holder.checkBox.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    String curWord = holder.textView1.getText().toString();  // 클릭 한 것 값 가져옴
                    db.collection(id)
                            .whereEqualTo("word", curWord)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot document : task.getResult()){
                                            String Cid = document.getId();
                                            DocumentReference documentReference =db.collection(id)
                                                    .document(Cid);
                                            if(holder.checkBox.isChecked()){
                                                documentReference.update("checkBox", true);
                                            } else{
                                                documentReference.update("checkBox", false);
                                            }
                                        }
                                    } else {
                                        Log.v("main", "오류 발생");
                                    }
                                }
                            });
                }
            });
            holder.checkBox.setVisibility(View.VISIBLE);
        }
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        if(mData!=null){
            return mData.size();
        }
        return 0;
    }

    public void  filterList(ArrayList<Word> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    public void remove(int position){
        // 값 삭제
        try{
            mData.remove(position);      //리스트 지움
            notifyItemRemoved(position);    //새로 고침
        }catch (IndexOutOfBoundsException ex){
            ex. printStackTrace();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    public void setOnItemClickListener(CardListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2 ;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            this.textView1 = itemView.findViewById(R.id.itemlist_word) ;
            this.textView2 = itemView.findViewById(R.id.itemlist_meaning) ;
            this.checkBox = itemView.findViewById(R.id.single_item_list_checkbox);

            //체크박스 클릭 시
            checkBox.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    // 어레이에 추가
                }
            });

            if(editState==true){
                checkBox.setVisibility(View.VISIBLE);
            } else {
                checkBox.setVisibility(View.INVISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAbsoluteAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) listener.onItemClick(view, pos);
                }
            });
        }
    }
}