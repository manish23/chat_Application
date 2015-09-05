package com.ihs.demo.message_2013011337;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ihs.message_2013011337.R;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private String name;
    private String mid;
    private ListView listView;
    private Button button_send;
    private EditText editText;
    private String myword;
    List<String> chatlist = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        mid = intent.getStringExtra("mid");
        setTitle(name);
        init_view();
    }

    public void init_view(){
        button_send = (Button) findViewById(R.id.btn_send);
        listView = (ListView) findViewById(R.id.List_view);
        editText = (EditText) findViewById(R.id.editText_sendmessage);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chatlist);
        listView.setAdapter(adapter);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myword = null;
                myword = (editText.getText()+"").toString();
                if(myword.length() == 0)
                    return;
                editText.setText("");
                chatlist.add(myword);
                adapter.notifyDataSetChanged();
                listView.setSelection(chatlist.size()-1);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chatactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
