package com.cookandroid.amgibbang;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    static boolean editState = false;
    static boolean bookmarkState = false;
    static boolean editTextState = false;
    static boolean settingState = false;
    static boolean darkState;
    static String user;
    String dialogInput;
    String documentId;
    private ToggleButton darkButton;
    private ArrayList<MainCard> cards;
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

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
        darkButton = findViewById(R.id.setting_toggle);
        mAuth = FirebaseAuth.getInstance();

        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        //인텐트 가져오기
        Intent intent = getIntent();
        user = intent.getStringExtra("CardList");
        Log.v("로그인", user);
        //데이터 베이스에서 값 가져오기
        db.collection(user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                    Intent intent1=new Intent(MainActivity.this, CalendarActivity.class);
                    intent1.putExtra("EMAIL", user);
                    startActivity(intent1);
                    break;
                case R.id.main_button_timer:
                    Intent intent2=new Intent(MainActivity.this, TodoListActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.main_button_dictionary:
                    Intent intent3=new Intent(MainActivity.this, DictionaryMainActivity.class);
                    startActivity(intent3);
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
                    db.collection(user).document().set(mainCard);
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
            db.collection(user).whereEqualTo("bookmark", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
            db.collection(user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
        if(settingState==false){
            settingState=true;
            LinearLayout linearLayout = findViewById(R.id.main_setting);
            linearLayout.setVisibility(View.VISIBLE);
        }
        else{
            settingState=false;
            LinearLayout linearLayout = findViewById(R.id.main_setting);
            linearLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void darkMode(View v){
        if(darkButton.isChecked()){
            settingState=false;
            darkState=true;
            setDark(true);
        }
        else{
            settingState=false;
            darkState=false;
            setDark(false);
        }
    }

    public void setDark(boolean b){
        if(b==true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
        // check가 true 인것만 다시 가져옴

        // check가 true인거 db에서 삭제
        db.collection(user).whereEqualTo("checkBox", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String dId = document.getId();
                        documentId = dId;
                        db.collection(user).document(document.getId()).delete();
                        Log.v("삭제", documentId);
                        db.collection(documentId)
                                .whereEqualTo("checkBox", false)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(QueryDocumentSnapshot document : task.getResult()){
                                                db.collection(documentId)
                                                        .document(document.getId())
                                                        .delete();
                                            }
                                        } else {
                                            Log.v("main", "오류 발생");
                                        }
                                    }
                                });
                        db.collection(documentId).document().delete();
                    }
                    mainAdapter.notifyDataSetChanged();
                } else {
                    Log.v("main", "오류 발생");
                }
            }
        });
        // 카드리스트 모두 삭제
        //collection 삭제

        cards.clear();
        db.collection(user).whereEqualTo("checkBox", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    public void main_onClickSelectAll(View v){
        // 전체 선택 클릭 시 모든 체크박스 체크 됨
        CheckBox checkBox = findViewById(R.id.main_button_selectAll);
        db.collection(user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String id = document.getId();
                        DocumentReference documentReference =db.collection("CardList").document(id);
                        if(checkBox.isChecked()){
                            documentReference.update("checkBox", true);
                        } else{
                            documentReference.update("checkBox", false);
                        }
                    }
                    mainAdapter.notifyDataSetChanged();
                } else {
                    Log.v("main", "오류 발생");
                }
            }
        });

        if(checkBox.isChecked()){
            for(MainCard card:cards){
                card.cancelCheck(true);
            }
        } else {
            for(MainCard card:cards){
                card.cancelCheck(false);
            }
        }

    }

    public void onPause() {
        super.onPause();

        overridePendingTransition(0, 0);
    }

    // 로그아웃
    private void signOut() {
        mAuth.getInstance().signOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("284297907533-jcfk29m3idrj57jtuajg2l8r91jv3mhv.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
        Toast.makeText(MainActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
    }

    // 탈퇴
    private void revokeAccess() {
        //데이터 삭제
        db.collection(user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String dId = document.getId();
                        documentId = dId;
                        db.collection(user).document(document.getId()).delete();
                        Log.v("삭제", documentId);
                        db.collection(documentId)
                                .whereEqualTo("checkBox", false)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(QueryDocumentSnapshot document : task.getResult()){
                                                db.collection(documentId)
                                                        .document(document.getId())
                                                        .delete();
                                            }
                                        } else {
                                            Log.v("main", "오류 발생");
                                        }
                                    }
                                });
                        db.collection(documentId).document().delete();
                    }
                    mainAdapter.notifyDataSetChanged();
                } else {
                    Log.v("main", "오류 발생");
                }
            }
        });

        //날짜 삭제 2(collection 없는 것)
        for(int i=1; i<=12; i++){
            String month = Integer.toString(i);
            String c = user+2021+month+"월";
            db.collection(c).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            db.collection(c).document(document.getId()).delete();
                        }
                        mainAdapter.notifyDataSetChanged();
                    } else {
                        Log.v("main", "오류 발생");
                    }
                }
            });
        }

        //날짜 삭제1
        String c = user+2021;
        for(int i=1; i<=12; i++){
            String month = Integer.toString(i)+"월";
            for(int j=1; j<=31; j++){
                String date = Integer.toString(j);
                db.collection(c).document(month).collection(date).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                documentId = document.getId();
                                db.collection(c).document(month).collection(date).document(documentId).delete();
                                Log.v("삭제", documentId);
                            }
                            mainAdapter.notifyDataSetChanged();
                        } else {
                            Log.v("main", "오류 발생");
                        }
                    }
                });
            }
        }

        db.collection(c).document("12월").collection("1").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.v("삭제", document.getId());
                            }
                        } else {
                            Log.v("삭제", "실패");
                        }
                    }
                });


        mAuth.getCurrentUser().delete();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("284297907533-jcfk29m3idrj57jtuajg2l8r91jv3mhv.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
    }

    public void onButtonClick(View view) {
        switch(view.getId()) {
            case R.id.logOut:  // 로그아웃하고 액티비티 종료됨
                signOut();
                Intent intent1 = new Intent(this, SplashActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.Delete:  // 탈퇴하고 액티비티 종료됨
                //다이얼로그 띄움
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("탈퇴하시겠습니까?").setMessage("탈퇴 시 모든 데이터가 삭제됩니다.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        revokeAccess();
                        Intent intent2 = new Intent(MainActivity.this, SplashActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        //
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
    }
}