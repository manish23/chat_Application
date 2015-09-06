package com.ihs.demo.message_2013011337;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wuchen on 15-9-6.
 */
public class MyArrayAdapter<String> extends ArrayAdapter<String>{
    private TextView t;
    private Context Maincontext;
    public MyArrayAdapter(Context context, int textViewResourceId,ArrayList<String> aStrings) {
        super(context, textViewResourceId,aStrings);
        Maincontext = context;
        // TODO Auto-generated constructor stub
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        for(int i = 0; i < parent.getChildCount(); i++){
            View view = parent.getChildAt(i);
            if (view instanceof TextView) {
                //System.out.println("come here!");
                ((TextView) view).setTextColor(Color.parseColor("#000632"));

            }
        }
        return super.getView(position, convertView, parent);
    }
}
