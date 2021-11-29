package com.cookandroid.amgibbang;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CardListActivity extends AppCompatActivity {

    ImageButton add_btn;
    TextView edit_btn, title;
    EditText editText;
    private CardListAdapter cardListAdapter;
    private RecyclerView recyclerView;
    String titleText;  // 단어장 이름
    static String id;  // document id값

    static boolean editState = false;  // 단어, 뜻 먼저 보어줄 것 결정값

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    static ArrayList<Word> list = new ArrayList<>();
    ArrayList<Word> arraylist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardlist);

        //toolbar 지정
        Toolbar toolbar = findViewById(R.id.cardlist_toolbar);
        setSupportActionBar(toolbar);  // 액션바를 없앴으니까 그걸 툴바가 대신하게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    // 툴바 왼쪽에 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 타이틀 안 보이게 하기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.button_back);  // 뒤로가기 버튼 아이콘 수정

        edit_btn = findViewById(R.id.cardlist_button_edit);
        add_btn = findViewById(R.id.cardlist_addCardButton);
        CheckBox checkAll=findViewById(R.id.cardlist_button_selectAll);
        FloatingActionButton fab = findViewById(R.id.cardlist_fab);
        fab.setOnClickListener(new FABClickListener());

        Intent intent = getIntent();
        titleText = intent.getStringExtra("TITLE");
        id = intent.getStringExtra("ID");

        title = findViewById(R.id.cardlist_title);
        title.setText(titleText);

        editText = findViewById(R.id.edit_text_filter);

        // list 복사본을 만든다.
        arraylist.addAll(list);

        // 텍스트 필터
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editText.getText().toString();
                searchFilter(searchText);
            }
        });

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = findViewById(R.id.cardlist_recyclerView) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        // 리사이클러뷰에 CardListAdapter 객체 지정.
        cardListAdapter = new CardListAdapter(list, this) ;
        recyclerView.setAdapter(cardListAdapter) ;

        // 리스트뷰 클릭 이벤트
        cardListAdapter.setOnItemClickListener(new CardListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int pos) {
                if (editState == false) { // edit 버튼 눌렀을 때는 동작하지 않음
                    Intent intent = new Intent(CardListActivity.this, WordActivity.class);
                    intent.putExtra("TITLE", titleText);
                    intent.putExtra("POSITION", pos);
                    intent.putExtra("ID", id);
                    startActivity(intent);
                }
            }
        });

        // + 버튼 행동
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CardListActivity.this, AddWordActivity.class);
                intent.putExtra("ID", id);
                startActivity(intent);
            }
        };
        add_btn.setOnClickListener(onClickListener);
    }

    // 뒤로가기 버튼 행동
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.none, R.anim.slide_right_exit);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 플로팅액션버튼 리스너
    class FABClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v){
            // 로그아웃, 다크모드 온 오프 창 띄움
            String[] array = {"공부 모드", "스피드 모드", "퀴즈 모드"};
            if(editState==false){
                AlertDialog.Builder builder = new AlertDialog.Builder(CardListActivity.this);
                builder.setItems(array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(array[i]){
                            case "공부 모드":
                                Log.v("다이얼로그", "공부 모드 시작");
                                Intent intentToSt = new Intent(CardListActivity.this, StudyModeActivity.class);
                                intentToSt.putExtra("TITLE", titleText);
                                startActivity(intentToSt);
                                break;
                            case "스피드 모드":
                                Log.v("다이얼로그", "스피드 모드 시작");
                                Intent intentToSp = new Intent(CardListActivity.this, SpeedModeActivity.class);
                                intentToSp.putExtra("TITLE", titleText);
                                startActivity(intentToSp);
                                break;
                            case "퀴즈 모드":
                                Log.v("다이얼로그", "퀴즈 모드 시작");
                                if(list.size()>=4) {
                                    Intent intent1 = new Intent(CardListActivity.this, QuizActivity.class);
                                    intent1.addFlags(FLAG_ACTIVITY_NO_HISTORY);
                                    intent1.putExtra("TITLE", titleText);
                                    startActivity(intent1);
                                }
                                else{
                                    Toast.makeText(CardListActivity.this, "4단어 이하는 퀴즈 모드를 실행할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                });
                builder.show();
            }
        }
    }

    // edit 버튼
    public void onEditButtonClick(View v){
        if(editState == false) {
            editState = true;
            // 버튼 설정
            TextView textView=findViewById(R.id.cardlist_button_edit);
            TextView textView1=findViewById(R.id.cardlist_button_delete);
            TextView textView2=findViewById(R.id.cardlist_button_selectAll);
            textView.setText("확인");
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            add_btn.setVisibility(View.GONE);
            CheckBox checkAll=findViewById(R.id.cardlist_button_selectAll);
            checkAll.setChecked(false);

            recyclerView.setAdapter(cardListAdapter);
        }
        else {
            editState = false;
            TextView textView=findViewById(R.id.cardlist_button_edit);
            TextView textView1=findViewById(R.id.cardlist_button_delete);
            TextView textView2=findViewById(R.id.cardlist_button_selectAll);
            textView.setText("edit");
            textView1.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
            add_btn.setVisibility(View.VISIBLE);

            recyclerView.setAdapter(cardListAdapter);
        }
    }

    public void cardlist_onClickDelete(View v){
        // 삭제 버튼 누를 시 체크된 단어장 삭제됨
        // check가 true 인것만 다시 가져옴

        // check가 true인거 db에서 삭제
        db.collection(id)
                .whereEqualTo("checkBox", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        db.collection(id)
                                .document(document.getId())
                                .delete();
                    }
                    cardListAdapter.notifyDataSetChanged();
                } else {
                    Log.v("main", "오류 발생");
                }
            }
        });

        list.clear();
        db.collection(id)
                .whereEqualTo("checkBox", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Word word = document.toObject(Word.class);
                        list.add(word);
                    }
                    cardListAdapter.notifyDataSetChanged();
                } else {
                    Log.v("main", "오류 발생");
                }
            }
        });
    }

    public void cardlist_onClickSelectAll(View v){
        // 전체 선택 클릭 시 모든 체크박스 체크 됨
        CheckBox checkBox = findViewById(R.id.cardlist_button_selectAll);
        db.collection(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String Cid = document.getId();
                        DocumentReference documentReference =db.collection(id).document(Cid);
                        if(checkBox.isChecked()){
                            documentReference.update("checkBox", true);
                        } else{
                            documentReference.update("checkBox", false);
                        }
                    }
                    cardListAdapter.notifyDataSetChanged();
                } else {
                    Log.v("cardlist", "오류 발생");
                }
            }
        });

        if(checkBox.isChecked()){
            for(Word word:list){
                word.cancelCheck(true);
            }
        } else {
            for(Word word:list){
                word.cancelCheck(false);
            }
        }

    }


    @Override
    public void onStart() {
        super.onStart();

        // Firebase로부터 단어장 데이터 가져오기
        list.clear();

        db.collection(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Word word = document.toObject(Word.class);
                                list.add(word);
                            }
                            cardListAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("test", "Error getting documents.", task.getException());
                        }
                    }
                });
        recyclerView.setAdapter(cardListAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        editState = false; // edit 상태 초기화
    }

    // 검색 필터 구현
    public void searchFilter(String searchText) {
        arraylist.clear();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getWord().toLowerCase().contains(searchText.toLowerCase())) {
                arraylist.add(list.get(i));
            }
        }

        cardListAdapter.filterList(arraylist);
    }
}