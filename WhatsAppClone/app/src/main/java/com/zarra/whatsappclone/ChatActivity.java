package com.zarra.whatsappclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private String sender;
    private String recipient;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    private EditText edtBody;
    private Button btnSent;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Message> data=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sender=getIntent().getStringExtra("username");
        recipient= ParseUser.getCurrentUser().getUsername();

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setTitle(sender);
        readMessages();

        edtBody=findViewById(R.id.edtMessage);
        edtBody.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnSent.performClick();
                    return true;
                }
                return false;
            }
        });
        btnSent=findViewById(R.id.btnSend);
        btnSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    private void sendMessage(){
        if(! edtBody.getText().toString().trim().equals("")){

            ParseObject object=ParseObject.create("Chat");
            Date date = Calendar.getInstance().getTime();
            object.put("waDateTime",dateFormat.format(date));
            object.put("waMessage", edtBody.getText().toString().trim());
            object.put("waSender", recipient);
            object.put("waRecipient", sender);

            object.saveInBackground();

            readMessages();
        }

        edtBody.setText("");
    }

    private void readMessages() {
        try{
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Chat");
        query.whereContainedIn("waRecipient", new ArrayList<>(Arrays.asList(recipient,sender)));
        query.whereContainedIn("waSender", new ArrayList<>(Arrays.asList(recipient,sender)));
        //query.whereEqualTo("waGroup",null);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                data.clear();
                if((e==null) && objects.size()>0){
                    for(ParseObject object:objects){

                        try {
                            Message message=new Message(dateFormat.parse((String) object.get("waDateTime")),
                                    (String)object.get("waMessage"),
                                    (String)object.get("waSender"),
                                    (String)object.get("waRecipient"),
                                    recipient.equals(object.get("waSender"))
                                    );
                            data.add(message);
                        } catch (java.text.ParseException parseException) {
                            parseException.printStackTrace();
                        }
                    }

                    adapter = new MessageAdaptor(data);
                    recyclerView.setAdapter(adapter);
                    recyclerView.scrollToPosition(data.size() - 1);
                }
            }
        });


        } catch (Exception e){
            e.printStackTrace();
        }
    }

}