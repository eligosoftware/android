package com.zarra.lyricsfinder;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    private TextView txtnum;
    private TextView txtName;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        txtnum=itemView.findViewById(R.id.txtnum);
        txtName=itemView.findViewById(R.id.txtname);
    }

    public TextView getTxtnum() {
        return txtnum;
    }

    public TextView getTxtName() {
        return txtName;
    }
}
