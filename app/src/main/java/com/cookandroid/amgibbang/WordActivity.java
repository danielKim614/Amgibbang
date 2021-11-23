package com.cookandroid.amgibbang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.Transaction;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class WordActivity extends AppCompatActivity {
    boolean editState = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String titleText;
    int pos;
    String id;

    ArrayList<Word> list = new ArrayList<>();

    EditText word;
    EditText meaning;
    EditText explanation;
    TextView edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        // 툴바 커스텀
        Toolbar toolbar = findViewById(R.id.word_toolbar);
        setSupportActionBar(toolbar);  // 액션바를 없앴으니까 그걸 툴바가 대신하게 하기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);    // 툴바 왼쪽에 뒤로가기 버튼 추가
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // 타이틀 안 보이게 하기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.button_back);  // 뒤로가기 버튼 아이콘 수정

        TextView toolbarText = findViewById(R.id.word_toolbar_text);

        Intent intent = getIntent();
        titleText = intent.getStringExtra("TITLE");
        pos = intent.getIntExtra("POSITION", 0);
        id = intent.getStringExtra("ID");
        toolbarText.setText(titleText);

        word = findViewById(R.id.word_text_word);
        meaning = findViewById(R.id.word_text_meaning);
        explanation = findViewById(R.id.word_text_explanation);
        edit = findViewById(R.id.word_button_edit);

        db.collection(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task){
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Word word = document.toObject(Word.class);
                                list.add(word);
                                Log.d("dabin", "add "  + word.word + " , list size=" + String.valueOf(list.size()));
                                // 파이어스토어 쿼리 비동기 호출때문에 넣어놓은 로그
                            }
                        } else {
                            Log.d("dabin", "Error getting documents: ", task.getException());
                        }

                        Word curWord = list.get(pos);
                        word.setText(curWord.word);
                        meaning.setText(curWord.meaning);
                        explanation.setText(curWord.explanation);
                    }
                });



    }

    public void onButtonClick(View view) {
        switch(view.getId()) {
            case R.id.word_button_edit:  // 단어 상세 내용 편집하기 (edit 버튼 저장 버튼으로 바꾸고 텍스트뷰 editable하게 변경)
                if (editState == false) {
                    editState = true;
                    edit.setText("save");
                    word.setEnabled(true);
                    meaning.setEnabled(true);
                    explanation.setEnabled(true);
                } else {
                    editState = false;
                    edit.setText("edit");
                    word.setEnabled(false);
                    meaning.setEnabled(false);
                    explanation.setEnabled(false);

                    Word updWord = new Word(String.valueOf(explanation.getText()), String.valueOf(meaning.getText()), String.valueOf(word.getText()));
                    Log.d("dabin", updWord.word + updWord.meaning);
                    list.remove(pos);
                    list.add(pos, updWord);

                    db.collection(id)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    int i = 0;
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Word updWord = list.get(i); i++;
                                            String docId = document.getId();
                                            db.collection(id).document(docId).set(updWord);
                                        }
                                    } else {
                                        Log.d("dabin", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                }
                break;
            case R.id.word_button_prev_word: // 이전 단어로 넘어가기
                if (editState == true) break;
                if (pos == 0) {
                    Log.d("dabin", "pos == 0");
                    Toast.makeText(WordActivity.this, "첫 단어입니다", Toast.LENGTH_SHORT).show();
                    break;
                }
                pos = pos - 1;
                showWord(pos);
                break;
            case R.id.word_button_next_word: // 다음 단어로 넘어가기
                if (editState == true) break;
                if (pos == list.size() - 1) {
                    Log.d("dabin", "pos == " + (list.size() - 1));
                    Toast.makeText(WordActivity.this, "마지막 단어입니다", Toast.LENGTH_SHORT).show();
                    break;
                }
                pos = pos + 1;
                showWord(pos);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showWord(int pos) {
        Word curWord = list.get(pos);
        word.setText(curWord.word);
        meaning.setText(curWord.meaning);
        explanation.setText(curWord.explanation);
    }
}