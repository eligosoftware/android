package com.zarra.instgramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LogIn extends AppCompatActivity {

    private EditText edtEmail, edtPass;


    public void rootTapped(View view){
        try{
        InputMethodManager manager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log in");
        edtEmail =findViewById(R.id.edtEmail);
        edtPass =findViewById(R.id.edtPass);

        edtPass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    login(findViewById(R.id.btnlogin));
                }
                return false;
            }
        });

        if(ParseUser.getCurrentUser()!=null){
            //ParseUser.getCurrentUser().logOut();
            transferttosocialactivity();
        }
    }

    public void signUp(View view){

        Intent intent=new Intent(LogIn.this,SignUp.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

       /* final ParseUser user=new ParseUser();
        user.setUsername(edtSName.getText().toString());
        user.setPassword(edtSPass.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    FancyToast.makeText(SignUpLoginActivity.this,"User named "+user.get("username")+" is saved",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                    Intent intent=new Intent(SignUpLoginActivity.this,WelcomeActivity.class);
                    startActivity(intent);
                }
                else{
                    FancyToast.makeText(SignUpLoginActivity.this,"Error! Try again",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                }
            }
        });*/
    }
    public void login(View view){
        if(edtEmail.getText().toString().equals("")||
                edtPass.getText().toString().equals("")
                ){

            FancyToast.makeText(LogIn.this,"Empty values are not allowed!",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();


        }
        else {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
       ParseUser.logInInBackground(edtEmail.getText().toString(), edtPass.getText().toString(), new LogInCallback() {
           @Override
           public void done(ParseUser user, ParseException e) {
                if (user!=null && e==null){
                    FancyToast.makeText(LogIn.this,"Succesful login",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                    transferttosocialactivity();
                }
                else {
                    FancyToast.makeText(LogIn.this,"Error! Try again",FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                }
               progressDialog.dismiss();
           }
       });
    }}

    private void transferttosocialactivity() {
        Intent intent = new Intent(LogIn.this, SocialMediaActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
