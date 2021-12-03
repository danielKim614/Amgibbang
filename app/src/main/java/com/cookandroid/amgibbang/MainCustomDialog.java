package com.cookandroid.amgibbang;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cookandroid.amgibbang.R;

public class MainCustomDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private String curName;
    private EditText editText;
    private MainCustomDialogListener mainCustomDialogListener;
    TextView positive;
    TextView negative;

    public MainCustomDialog(Context context){
        super(context);
        this.context=context;
    }

    public MainCustomDialog(Context context, String curName){
        super(context);
        this.context=context;
        this.curName = curName;
    }

    //인터페이스 설정
    interface  MainCustomDialogListener{
        void mainDialogPositive(String inputName);
        void mainDialogNegative();
    }
    //호출할 리스너
    public void setMainDialogListener(MainCustomDialogListener mainCustomDialogListener){
        this.mainCustomDialogListener = mainCustomDialogListener;
    }
    //확인 버튼 눌렀을 때 메소드 호출
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.main_dialog_activity);

        editText = findViewById(R.id.main_dialog_edittext);
        positive = findViewById(R.id.main_dialog_btnPositive);
        negative = findViewById(R.id.main_dialog_btnNegative);

        if(curName!=null){
            editText.setText(curName);
        }
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.main_dialog_btnPositive:
                String name = editText.getText().toString();
                //인터페이스 함수 호출 -> 변수에 저장된 값 전달
                mainCustomDialogListener.mainDialogPositive(name);
                dismiss();
                break;
            case R.id.main_dialog_btnNegative:
                cancel();
                break;
        }
    }


}
