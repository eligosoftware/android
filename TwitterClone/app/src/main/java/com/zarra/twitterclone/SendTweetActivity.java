package com.zarra.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTweetActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText tweet_message;

    private Button viewOtherTweets;
    private ListView otherTweetsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        tweet_message=findViewById(R.id.tweet_message);
        tweet_message.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
                    sendTweet();
                }
                return false;
            }
        });

        viewOtherTweets=findViewById(R.id.viewOtherUsersTweets);
        otherTweetsList=findViewById(R.id.viewTweetsListView);
        viewOtherTweets.setOnClickListener(this);



    }

    public void onsendClick(View view){

       sendTweet();
    }

    private void sendTweet() {
        String tw_message=tweet_message.getText().toString();
        tw_message=tw_message.trim();
        if(!tw_message.equals("")){
            ParseObject object=ParseObject.create("Tweet");
            object.put("text",tw_message);
            object.put("user", ParseUser.getCurrentUser().getUsername());

            ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Loading..");
            progressDialog.show();
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        FancyToast.makeText(SendTweetActivity.this,"Tweet sent!", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                        tweet_message.setText("");
                    }
                    else{
                        FancyToast.makeText(SendTweetActivity.this,"Error: "+e.getMessage(), Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                    }
                    progressDialog.dismiss();
                }
            });

        }
        else{
            FancyToast.makeText(SendTweetActivity.this,"Tweet message cannot be empty!", Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
        }
    }

    @Override
    public void onClick(View v) {

        final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(SendTweetActivity.this, tweetList, android.R.layout.simple_list_item_2, new String[]{"tweetUserName", "tweetValue"}, new int[]{android.R.id.text1, android.R.id.text2});

        try{
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tweet");
        query.whereContainedIn("user",ParseUser.getCurrentUser().getList("fanOf"));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                    if(objects.size()>0 && e==null){
                        for(ParseObject tweet:objects) {

                            HashMap<String,String> userTweet=new HashMap<>();
                            userTweet.put("tweetUserName",tweet.get("user").toString());
                            userTweet.put("tweetValue",tweet.get("text").toString());

                            tweetList.add(userTweet);

                        }
                        otherTweetsList.setAdapter(adapter);
                    }
            }
        });
    } catch (Exception e){
            e.printStackTrace();
        }
    }
}