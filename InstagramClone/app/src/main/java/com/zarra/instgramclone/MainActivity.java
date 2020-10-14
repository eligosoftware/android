package com.zarra.instgramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText name,punch_speed,punch_power,kick_speed,kick_power;
    private TextView txtgetdata;
    private Button btngetall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        name=findViewById(R.id.name);
        punch_power=findViewById(R.id.punch_power);
        punch_speed=findViewById(R.id.punch_speed);
        kick_power=findViewById(R.id.kick_power);
        kick_speed=findViewById(R.id.kick_speed);

        txtgetdata=findViewById(R.id.txtgetdata);
        txtgetdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery("Kickboxer");
                parseQuery.getInBackground("vkqS6M29aD", new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {

                        if(e!=null) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                        else{
                            name.setText(object.get("name").toString());
                            punch_power.setText(object.get("punch_power").toString());
                            punch_speed.setText(object.get("punch_speed").toString());
                            kick_power.setText(object.get("kick_power").toString());
                            kick_speed.setText(object.get("kick_speed").toString());
                            }
                        }

                });
            }
        });
        btngetall=findViewById(R.id.btngetall);
        btngetall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query=ParseQuery.getQuery("Kickboxer");

                query.whereGreaterThan("punch_power",1000);

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        String message="";
                        for(ParseObject object:objects){
                            message+=object.get("name").toString()+"\n";
                        }
                        if(message.equals(""))
                            FancyToast.makeText(MainActivity.this,"Empty result",FancyToast.LENGTH_LONG,FancyToast.CONFUSING,true).show();
                        else
                            FancyToast.makeText(MainActivity.this,message,FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();

                    }
                });
            }
        });
    }

    public void helloWorldTapped(View view){
       /* ParseObject boxer=new ParseObject("Boxer");
        boxer.put("punch_speed",200);
        boxer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(MainActivity.this,"Saved boxer",Toast.LENGTH_LONG).show();
                }
            }
        });*/
        final ParseObject kickboxer=new ParseObject("Kickboxer");
        kickboxer.put("punch_speed",Integer.parseInt(punch_speed.getText().toString()));
        kickboxer.put("name",name.getText().toString());
        kickboxer.put("punch_power",Integer.parseInt(punch_power.getText().toString()));
        kickboxer.put("kick_speed",Integer.parseInt(kick_speed.getText().toString()));
        kickboxer.put("kick_power",Integer.parseInt(kick_power.getText().toString()));

        kickboxer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    //Toast.makeText(MainActivity.this,"Kickboxer named "+kickboxer.get("name")+" is saved",Toast.LENGTH_LONG).show();
                    FancyToast.makeText(MainActivity.this,"Kickboxer named "+kickboxer.get("name")+" is saved",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
                }
            }
        });
    }

    public void transition(View view){
        Intent intent=new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);
    }
}