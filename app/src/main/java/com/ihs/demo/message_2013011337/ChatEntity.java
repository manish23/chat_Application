package com.ihs.demo.message_2013011337;


/**
 * Created by wuchen on 15-9-5.
 */
public class ChatEntity {
    private String text;
    private String date;
    private boolean issend = true;
    public String getText(){
        return text;
    }
    public String getDate(){

        return date;
    }
    public void setText(String date){
        this.date = date;
    }
    public boolean get_Issend(){
        return issend;
    }
    public ChatEntity(String text, String date, boolean issend){
        this.date = date;   this.text = text;   this.issend = issend;
    }
}
