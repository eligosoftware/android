package com.zarra.lyricsfinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    String[] mylist={"apples","pears","oranges","grapefruits","mandarins","limes","nectarines","apricots",
    "peaches","plums","bananas","mangoes","strawberries","raspberries","blueberries","kiwifruit","passionfruit"};
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.rv_item,parent,false);



        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.getTxtName().setText(mylist[position]);
        holder.getTxtnum().setText(String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        return mylist.length;
    }
}
