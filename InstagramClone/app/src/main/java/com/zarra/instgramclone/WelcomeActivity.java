package com.zarra.instgramclone;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class WelcomeActivity extends AppCompatActivity {


    private TextView txtWelcome;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        txtWelcome=findViewById(R.id.txtWelcome);
        txtWelcome.setText("Welcome, "+ParseUser.getCurrentUser().get("username").toString());
    }
    public void logout(View view){
        ParseUser.logOut();
        finish();
    }
}
