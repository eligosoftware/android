package com.zarra.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    //private TextView txtLoading;

    private ArrayList<String> arrayList;
    private ArrayAdapter mArrayAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView=findViewById(R.id.listview);
        mSwipeRefreshLayout=findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fillListView();
                if(mSwipeRefreshLayout.isRefreshing())
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        });
//        mListView.setOnItemClickListener(this);
//        mListView.setOnItemLongClickListener(this);
        arrayList=new ArrayList();
        mArrayAdapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,arrayList);// android.R.layout.simple_list_item_1,arrayList);
        //mArrayAdapter=new ArrayAdapter<String>(TwitterUsers.this, android.R.layout.simple_list_item_checked,arrayList);// android.R.layout.simple_list_item_1,arrayList);
        fillListView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,ChatActivity.class);
                intent.putExtra("username",arrayList.get(position));
                startActivity(intent);
            }
        });
    }

    private void fillListView(){
        try {
            arrayList.clear();
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
                startActivity(new Intent(MainActivity.this,SignUp.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}