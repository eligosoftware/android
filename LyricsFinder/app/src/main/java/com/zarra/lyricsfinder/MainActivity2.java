package com.zarra.lyricsfinder;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity2 extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_test);

        mRecyclerView=findViewById(R.id.rv_test);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
        mRecyclerView.setAdapter(new MyRecyclerViewAdapter());
    }
}
