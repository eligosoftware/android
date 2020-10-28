package com.zarra.whatsappclone;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessageAdaptor extends RecyclerView.Adapter<MessageAdaptor.MyViewHolder> {

    private ArrayList<Message> mMessages;

    public MessageAdaptor(ArrayList<Message> messages) {
        mMessages = messages;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_p,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message message=mMessages.get(position);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        holder.txtDate.setText((message.isOwn()?"Me: ":"")+dateFormat.format(message.getMessageDate()));
        holder.txtMessage.setText(String.valueOf(message.getMessageBody()));
        if(message.isOwn()) {
            holder.itemView.setBackgroundColor(Color.parseColor("#00e6e6"));

            LinearLayout layout=holder.itemView.findViewById(R.id.linlayout);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.RIGHT;
            layout.setLayoutParams(params);
        }
        else{
            holder.itemView.setBackgroundColor(Color.parseColor("#ffff00"));
        LinearLayout layout=holder.itemView.findViewById(R.id.linlayout);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.LEFT;
        layout.setLayoutParams(params);}
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDate;
        private TextView txtMessage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMessage=itemView.findViewById(R.id.txtMessage);
            txtDate=itemView.findViewById(R.id.txtMessageTime);


        }



    }


}
