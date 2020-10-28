package com.zarra.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView mListView;
    //private TextView txtLoading;

    private ArrayList<String> arrayList;
    private ArrayAdapter mArrayAdapter;

    //private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        //toolbar=findViewById(R.id.my_toolbar);
        //setSupportActionBar(toolbar);
        mListView=findViewById(R.id.listview);

        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        arrayList=new ArrayList();
     //   mArrayAdapter=new ArrayAdapter<String>(TwitterUsers.this,R.layout.rowlayout,R.id.label,arrayList);// android.R.layout.simple_list_item_1,arrayList);
        mArrayAdapter=new ArrayAdapter<String>(TwitterUsers.this, android.R.layout.simple_list_item_checked,arrayList);// android.R.layout.simple_list_item_1,arrayList);

        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                    if (users.size() > 0) {
                        for (ParseUser user : users) {
                            arrayList.add(user.getUsername());
                        }
                        mListView.setAdapter(mArrayAdapter);

                        for(String twitterUser:arrayList){
                            if(ParseUser.getCurrentUser().getList("fanOf")!=null){
                                if(ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)){
                                    mListView.setItemChecked(arrayList.indexOf(twitterUser),true);
                                }
                            }
                        }

                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutUser:
                ParseUser.logOut();
                finish();
                startActivity(new Intent(TwitterUsers.this,SignUp.class));
                break;
            case R.id.sendtweet:
                Intent sendTweetIntent=new Intent(TwitterUsers.this,SendTweetActivity.class);
                startActivity(sendTweetIntent);
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckedTextView checkedTextView= (CheckedTextView) view;

        if(checkedTextView.isChecked()){
            ParseUser.getCurrentUser().add("fanOf",arrayList.get(position));
        }else{
            ParseUser.getCurrentUser().getList("fanOf").remove(arrayList.get(position));
            List currentUserFanOf=ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",currentUserFanOf);
        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    FancyToast.makeText(TwitterUsers.this,"Saved", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                }
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}