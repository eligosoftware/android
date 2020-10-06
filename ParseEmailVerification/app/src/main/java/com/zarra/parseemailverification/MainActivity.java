package com.zarra.parseemailverification;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    private EditText mUsername;
    private EditText mUserEmail;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseInstallation.getCurrentInstallation().saveInBackground();

        mUsername=findViewById(R.id.edtUsername);
        mUserEmail=findViewById(R.id.edtEmail);
        mPassword=findViewById(R.id.pwdPassword);

    }

    public void signupIsPressed(View btnView){

        Toast.makeText(this,"Sign up is pressed",Toast.LENGTH_SHORT).show();

        try {
            ParseUser user=new ParseUser();
            user.setUsername(mUsername.getText().toString());
            user.setEmail(mUserEmail.getText().toString());
            user.setPassword(mPassword.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                        ParseUser.logOut();
                        alertDisplayer("Account Created Successfully!","Please verify your email before Login", false);
                    }
                    else{
                        ParseUser.logOut();
                        alertDisplayer("Account Creation failed","Reason: "+e.getMessage(),true);
                    }
                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void alertDisplayer(String title, String message, final boolean error) {
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if(!error) {
                            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        builder.create().show();

    }
}