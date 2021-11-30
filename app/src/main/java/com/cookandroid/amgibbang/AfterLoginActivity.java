package com.cookandroid.amgibbang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AfterLoginActivity extends AppCompatActivity {
    Button logoutButton;
    Button revokeButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        logoutButton = findViewById(R.id.after_login_button_logout);
        revokeButton = findViewById(R.id.after_login_button_revoke);

        mAuth = FirebaseAuth.getInstance();
    }

    // 로그아웃
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    // 탈퇴
    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
    }

    public void onButtonClick(View view) {
        switch(view.getId()) {
            case R.id.after_login_button_logout:  // 로그아웃하고 액티비티 종료됨
                signOut();
                finishAffinity();
                break;
            case R.id.after_login_button_revoke:  // 탈퇴하고 액티비티 종료됨
                revokeAccess();
                finishAffinity();
                break;
        }
    }
}