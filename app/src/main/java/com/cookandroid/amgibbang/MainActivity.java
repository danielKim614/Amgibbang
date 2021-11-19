package com.cookandroid.amgibbang;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    static boolean editState = false;
    static boolean bookmarkState = false;
    static boolean editTextState = false;
    String dialogInput;
    private ArrayList<MainCard> cards;
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //toolbar 지정
        Toolbar main_toolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(main_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Toolbar main_bottom_toolbar=findViewById(R.id.main_bottom_toolbar);
        setSupportActionBar(main_bottom_toolbar);
        //리사이클러뷰
        recyclerView = findViewById(R.id.main_rv);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        cards = new ArrayList<>();   // MainCard 리스트
        mainAdapter = new MainAdapter(cards, MainActivity.this);
        recyclerView.setAdapter(mainAdapter);

        //데이터 베이스에서 값 가져오기
        db.collection("CardList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        MainCard card = document.toObject(MainCard.class);
                        cards.add(card);
                    }
                    mainAdapter.notifyDataSetChanged();
                } else {
                    Log.v("main", "오류 발생");
                }
            }
        });
    }

    public void main_onClickMove(View v){
        // 화면 전환
        if(editState==false){
            switch (v.getId()){
                case R.id.main_button_cal:
                    Intent intent=new Intent(MainActivity.this, CalendarActivity.class);
                    startActivity(intent);
                    break;
                case R.id.main_button_timer:
                    break;
                case R.id.main_button_dictionary:
                    Intent intent1=new Intent(MainActivity.this, DictionaryMainActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    }
    public void main_onClickAdd(View v){
        // 단어장 추가
        // 다이얼로그 올라옴
        if(editState==false){
            MainCustomDialog dialog = new MainCustomDialog(this);
            dialog.setMainDialogListener(new MainCustomDialog.MainCustomDialogListener(){
                @Override
                public void mainDialogPositive(String inputName) {
                    dialogInput=inputName;
                    MainCard mainCard = new MainCard(dialogInput, false, false);
                    cards.add(mainCard);
                    db.collection("CardList").document().set(mainCard);
                    mainAdapter.notifyDataSetChanged(); // 새로 고침
                }
                @Override
                public void mainDialogNegative(){
                    Log.v("다이얼로그", "취소되었습니다.");
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    }
    //카드리스트로 이동
    public void main_onClickList(View v){
        if(editState==false){
            Intent intent = new Intent(MainActivity.this, CardListActivity.class);
            startActivity(intent);
        }
    }
    public void main_onClickBookmarkList(View v){
        //북마크 된 것들만 화면에 나타냄
        if(bookmarkState==false){
            bookmarkState=true;
            cards.clear();
            ImageView imageView = findViewById(R.id.main_button_bookmark);
            imageView.setImageResource(R.drawable.button_bookmark_filled);
            db.collection("CardList").whereEqualTo("bookmark", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            MainCard card = document.toObject(MainCard.class);
                            cards.add(card);
                        }
                        mainAdapter.notifyDataSetChanged();
                    } else {
                        Log.v("main", "오류 발생");
                    }
                }
            });
        } else{
            bookmarkState=false;
            cards.clear();
            ImageView imageView = findViewById(R.id.main_button_bookmark);
            imageView.setImageResource(R.drawable.button_bookmark_notfilled);
            db.collection("CardList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            MainCard card = document.toObject(MainCard.class);
                            cards.add(card);
                        }
                        mainAdapter.notifyDataSetChanged();
                    } else {
                        Log.v("main", "오류 발생");
                    }
                }
            });
        }


    }

    public void main_setting(View v){
        // 로그아웃, 다크모드 온 오프 창 띄움
        String[] array = {"다크 모드 ON", "다크 모드 OFF"};
        if(editState==false){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(array, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    switch(array[i]){
                        case "다크 모드 ON":
                            Log.v("다이얼로그", "다크모드를 킵니다.");
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            break;
                        case "다크 모드 OFF":
                            Log.v("다이얼로그", "다크모드를 끕니다.");
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            break;
                    }
                }
            });
            builder.show();
        }
    }
    public void main_onClickEdit(View v){
        // edit 버튼
        if(editState==false){
            editState=true;
            // 버튼 설정
            TextView textView=findViewById(R.id.main_button_edit);
            TextView textView1=findViewById(R.id.main_button_delete);
            TextView textView2=findViewById(R.id.main_button_selectAll);
            textView.setText("확인");
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            CheckBox checkAll=findViewById(R.id.main_button_selectAll);
            checkAll.setChecked(false);

            recyclerView.setAdapter(mainAdapter);
        }
        else{
            editState=false;
            TextView textView=findViewById(R.id.main_button_edit);
            TextView textView1=findViewById(R.id.main_button_delete);
            TextView textView2=findViewById(R.id.main_button_selectAll);
            textView.setText("edit");
            textView1.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);

            recyclerView.setAdapter(mainAdapter);
        }
    }
    public void main_onClickDelete(View v){
        // 삭제 버튼 누를 시 체크된 단어장 삭제됨
        // check가 true인거 db에서 삭제
        db.collection("CardList").whereEqualTo("check", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        db.collection("CardList").document(document.getId()).delete();
                    }
                    mainAdapter.notifyDataSetChanged();
                } else {
                    Log.v("main", "오류 발생");
                }
            }
        });

        // check가 true 인것만 다시 가져옴
        cards.clear();
        db.collection("CardList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        MainCard card = document.toObject(MainCard.class);
                        cards.add(card);
                    }
                    mainAdapter.notifyDataSetChanged();
                } else {
                    Log.v("main", "오류 발생");
                }
            }
        });
        // 다시 보여줌
        recyclerView.setAdapter(mainAdapter);
    }

    public void main_onClickSelectAll(View v){
        // 전체 선택 클릭 시 모든 체크박스 체크 됨
        CheckBox checkBox = findViewById(R.id.main_button_selectAll);
        cards.clear();
        db.collection("CardList").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String id = document.getId();
                        DocumentReference documentReference =db.collection("CardList").document(id);
                        if(checkBox.isChecked()){
                            documentReference.update("check", true);
                        } else{
                            documentReference.update("check", false);
                        }
                        MainCard card = document.toObject(MainCard.class);
                        cards.add(card);
                    }
                    mainAdapter.notifyDataSetChanged();
                } else {
                    Log.v("main", "오류 발생");
                }
            }
        });

        recyclerView.setAdapter(mainAdapter);

    }
}