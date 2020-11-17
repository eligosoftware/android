package com.zarra.uberclone2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SignUp extends AppCompatActivity {

    private State state;
    private Button btnaction;
    private Button btnonetimeaction;
    private EditText edtUsername,edtPassword,edtNoSignUp;
    private RadioButton rdbtPass,rdbtDriver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        if(ParseUser.getCurrentUser()!=null){
            //ParseUser.logOut();
            transitionToPassengerActivity();
            transitiontoDriverRequestsListActivity();
        }
        state=State.LogIN;

        btnaction=findViewById(R.id.btnaction);
        rdbtDriver=findViewById(R.id.rdbtDriver);
        rdbtPass=findViewById(R.id.rdbtPass);

        edtUsername=findViewById(R.id.edtUsername);
        edtPassword=findViewById(R.id.edtPassword);
        edtNoSignUp=findViewById(R.id.edtNoSignUp);

        btnaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!rdbtDriver.isChecked() && !rdbtPass.isChecked()){
                    Toast.makeText(SignUp.this,"Please select Driver or Passenger option",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(state==State.SignUp){
                    ParseUser appUser=new ParseUser();
                    appUser.setUsername(edtUsername.getText().toString());
                    appUser.setPassword(edtPassword.getText().toString());
                    if(rdbtDriver.isChecked())
                        appUser.put("as","Driver");
                    else if (rdbtPass.isChecked())
                        appUser.put("as","Passenger");
                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                Toast.makeText(SignUp.this,"Signed up successfully",Toast.LENGTH_SHORT).show();
                                transitionToPassengerActivity();
                                transitiontoDriverRequestsListActivity();
                            }
                        }
                    });

                } else if (state==State.LogIN){
                    ParseUser.logInInBackground(edtUsername.getText().toString(), edtPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(e==null){
                                Toast.makeText(SignUp.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                                transitionToPassengerActivity();
                                transitiontoDriverRequestsListActivity();
                            }
                        }
                    });
                }
            }
        });
        btnonetimeaction=findViewById(R.id.btnonetimeaction);
        btnonetimeaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNoSignUp.getText().toString().trim()=="Driver"||edtNoSignUp.getText().toString().trim()=="Passenger"){
                    if(ParseUser.getCurrentUser()==null){
                    ParseAnonymousUtils.logIn(new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user!=null &&  e==null){
                                Toast.makeText(SignUp.this,"We have an anonymous user",Toast.LENGTH_SHORT).show();
                                user.put("as",edtNoSignUp.getText().toString());
                                user.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        transitionToPassengerActivity();
                                        transitiontoDriverRequestsListActivity();
                                    }
                                });

                            }
                        }
                    });
                    }

            } else {
                Toast.makeText(SignUp.this,"Are you a driver or passenger?",Toast.LENGTH_SHORT).show();

            }
        }});


        setButtonTexts();

    }


    private void setButtonTexts(){
        switch (state){
            case LogIN:
                btnaction.setText("LOG IN");
                break;
            case SignUp:
                btnaction.setText("SIGN UP");
                break;
        }
    }
    private void toggleState(){
        if(state==State.LogIN)
            state=State.SignUp;
        else
            state=State.LogIN;
    }
    private void toggleitemText(MenuItem item){
        if(state==State.LogIN)
            item.setTitle("SIGN UP");
        else
            item.setTitle("LOG IN");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.login:
                toggleState();
                setButtonTexts();
                toggleitemText(item);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void transitionToPassengerActivity(){
        if(ParseUser.getCurrentUser()!=null){
            if (ParseUser.getCurrentUser().get("as").equals("Passenger"))
            {
                Intent intent=new Intent(SignUp.this,MapsActivity.class);
                startActivity(intent);
            }
        }
    }

    private void transitiontoDriverRequestsListActivity(){
        if(ParseUser.getCurrentUser()!=null){
            if(ParseUser.getCurrentUser().get("as").equals("Driver")){
                Intent intent=new Intent(SignUp.this,DriverListRequestActivity.class);
                startActivity(intent);
            }
        }
    }

}