package com.zarra.whatsappclone;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private Date messageDate;
    private String messageBody;
    private String sender;
    private String receiver;
    private boolean own;


    public Message(Date messageDate, String messageBody, String sender, String receiver,boolean own) {
        this.messageDate = messageDate;
        this.messageBody = messageBody;
        this.sender = sender;
        this.receiver = receiver;
        this.own=own;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }
    public boolean isOwn(){
        return own;
    }
}
