package com.zarra.twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity {

    private EditText edtName, edtPass, edtEmail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ParseInstallation.getCurrentInstallation().saveInBackground();

        setTitle("Sign up");

        edtName=findViewById(R.id.edtName);
        edtPass=findViewById(R.id.edtPass);

        edtPass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    signUp(findViewById(R.id.btnSignUp));
                }
                return false;
            }
        });

        edtEmail=findViewById(R.id.edtUsername);

        if(ParseUser.getCurrentUser()!=null){
            //ParseUser.getCurrentUser().logOut();
            transferttosocialactivity();
        }
    }

    public void rootTapped(View view){
        try{
        InputMethodManager manager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);}
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void signUp(View view){

        if(edtEmail.getText().toString().equals("")||
           edtPass.getText().toString().equals("")||
            edtName.getText().toString().equals("")){

            FancyToast.makeText(SignUp.this,"Empty values are not allowed!", FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();


        }
        else {
            final ParseUser user = new ParseUser();
            user.setUsername(edtName.getText().toString());
            user.setEmail(edtEmail.getText().toString());
            user.setPassword(edtPass.getText().toString());

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Signing up...");
            progressDialog.show();
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(SignUp.this, "User named " + user.get("username") + " is saved", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                        transferttosocialactivity();
                    } else {
                        FancyToast.makeText(SignUp.this, "Error! Try again", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

                    }

                    progressDialog.dismiss();
                }
            });
        }
    }

    private void transferttosocialactivity() {
        Intent intent = new Intent(SignUp.this, TwitterUsers.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void login(View view){

        Intent intent=new Intent(SignUp.this, LogIn.class);
        startActivity(intent);
        finish();




      /* ParseUser.logInInBackground(edtLName.getText().toString(), edtLPass.getText().toString(), new LogInCallback() {
           @Override
           public void done(ParseUser user, ParseException e) {
                if (user!=null && e==null){
                    FancyToast.makeText(SignUpLoginActivity.this,"Succesful login",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                    Intent intent=new Intent(SignUpLoginActivity.this,WelcomeActivity.class);
                    startActivity(intent);
                }
                else {
                    FancyToast.makeText(SignUpLoginActivity.this,"Error! Try again",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                }
           }
       });*/
    }
}
