package com.cookandroid.amgibbang;

import static com.cookandroid.amgibbang.MainActivity.bookmarkState;
import static com.cookandroid.amgibbang.MainActivity.editState;
import static com.cookandroid.amgibbang.MainActivity.editTextState;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>{

    private Context context;
    private ArrayList<MainCard> cards;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String edited;
    private String dID;

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
        //북마크
        boolean isBookmark = cards.get(position).getBookmark();
        if(isBookmark==true){
            holder.bookmark_no.setVisibility(View.INVISIBLE);
            holder.bookmark_yes.setVisibility(View.VISIBLE);
        } else {
            holder.bookmark_no.setVisibility(View.VISIBLE);
            holder.bookmark_yes.setVisibility(View.INVISIBLE);
        }
        //체크박스
        boolean isChecked = cards.get(position).getCheckBox();
        if(isChecked==true){
            holder.check.setChecked(true);
        } else {
            holder.check.setChecked(false);
        }

        holder.itemView.setTag(position);

        if(editState==false){
            holder.check.setVisibility(View.INVISIBLE);
            holder.bookmark_no.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    String curName = holder.name.getText().toString();  // 클릭 한 것 값 가져옴
                    for(MainCard card:cards){
                        if(card.getName()==curName){
                            card.cancelBookmark(true);
                        }
                    }
                    notifyDataSetChanged();
                    db.collection("CardList").whereEqualTo("name", curName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    String id = document.getId();
                                    DocumentReference documentReference =db.collection("CardList").document(id);
                                    documentReference.update("bookmark", true);
                                }
                            } else {
                                Log.v("main", "오류 발생");
                            }
                        }
                    });
                }
            });

            holder.bookmark_yes.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    String curName = holder.name.getText().toString();  // 클릭 한 것 값 가져옴
                    notifyDataSetChanged();
                    for(MainCard card:cards){
                        if(card.getName()==curName){
                            card.cancelBookmark(false);
                        }
                    }
                    db.collection("CardList").whereEqualTo("name", curName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    String id = document.getId();
                                    DocumentReference documentReference =db.collection("CardList").document(id);
                                    documentReference.update("bookmark", false);
                                }
                            } else {
                                Log.v("main", "오류 발생");
                            }
                        }
                    });
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    String curName = holder.name.getText().toString();  // 클릭 한 것 값 가져옴
                    db.collection("CardList").whereEqualTo("name", curName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    String id = document.getId();
                                    Log.v("다이얼로그", id);
                                    dID = id;
                                    Intent intent = new Intent(context, CardListActivity.class);
                                    intent.putExtra("TITLE", curName);
                                    intent.putExtra("ID", dID);
                                    Log.v("다이얼로그", curName + " / "+dID);
                                    context.startActivity(intent);
                                }
                                //notifyDataSetChanged();
                            } else {
                                Log.v("main", "오류 발생");
                            }
                        }
                    });
                }
            });
        } else { //editState가 false 일 때

            //이름 변경
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    String curName = holder.name.getText().toString();
                    MainCustomDialog dialog = new MainCustomDialog(context);
                    dialog.setMainDialogListener(new MainCustomDialog.MainCustomDialogListener(){
                        @Override
                        public void mainDialogPositive(String inputName) {
                            String dialogInput=inputName;
                            db.collection("CardList").whereEqualTo("name", curName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(QueryDocumentSnapshot document : task.getResult()){
                                            String id = document.getId();
                                            DocumentReference documentReference =db.collection("CardList").document(id);
                                            documentReference.update("name", dialogInput);
                                        }
                                    } else {
                                        Log.v("main", "오류 발생");
                                    }
                                }
                            });
                            for(MainCard card:cards){
                                if(card.getName()==curName){
                                    card.cancelName(dialogInput);
                                }
                            }
                            notifyDataSetChanged(); // 새로 고침
                        }
                        @Override
                        public void mainDialogNegative(){
                            Log.v("다이얼로그", "취소되었습니다.");
                        }
                    });
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });

            // 선택 삭제
            holder.check.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    String curName = holder.name.getText().toString();  // 클릭 한 것 값 가져옴
                    db.collection("CardList").whereEqualTo("name", curName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    String id = document.getId();
                                    DocumentReference documentReference =db.collection("CardList").document(id);
                                    if(holder.check.isChecked()){
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
            holder.check.setVisibility(View.VISIBLE);
        }
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
        protected ToggleButton darkMode;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.main_listName);
            this.bookmark_no = itemView.findViewById(R.id.bookmark_no);
            this.bookmark_yes = itemView.findViewById(R.id.bookmark_yes);
            this.check = itemView.findViewById(R.id.main_check);
            this.darkMode = itemView.findViewById(R.id.setting_toggle);

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