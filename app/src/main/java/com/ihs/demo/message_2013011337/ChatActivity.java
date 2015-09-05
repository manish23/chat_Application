package com.ihs.demo.message_2013011337;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ihs.commons.utils.HSError;
import com.ihs.message_2013011337.R;
import com.ihs.message_2013011337.managers.HSMessageChangeListener;
import com.ihs.message_2013011337.managers.HSMessageManager;
import com.ihs.message_2013011337.types.HSBaseMessage;
import com.ihs.message_2013011337.types.HSMessageType;
import com.ihs.message_2013011337.types.HSOnlineMessage;
import com.ihs.message_2013011337.types.HSTextMessage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends HSActionBarActivity implements HSMessageChangeListener{
    private String name;
    private String mid;
    private ListView listView;
    private Button button_send;
    private EditText editText;
    private String myword;
    private String get_word;
    List<HSBaseMessage> chatlist = new ArrayList<HSBaseMessage>();
    List<String> chatlist_String = new ArrayList<String>();
    private ArrayAdapter<HSBaseMessage> adapter;
    private ArrayAdapter<String> String_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        mid = intent.getStringExtra("mid");
        setTitle(name);
        HSMessageManager.getInstance().addListener(this, new Handler());
        init_view();

    }

    public void init_view(){
        button_send = (Button) findViewById(R.id.btn_send);
        listView = (ListView) findViewById(R.id.List_view);
        editText = (EditText) findViewById(R.id.editText_sendmessage);
        String_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, chatlist_String);
        listView.setAdapter(String_adapter);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myword = null;
                myword = (editText.getText() + "").toString();
                if (myword.length() == 0)
                    return;
                editText.setText("");
                HSTextMessage textMessage = new HSTextMessage(mid, myword);
                chatlist.add(textMessage);
                chatlist_String.add(myword);
                String_adapter.notifyDataSetChanged();
                listView.setSelection(chatlist_String.size() - 1);
                send();
            }
        });
    }
    public void send(){
        HSTextMessage textMessage = new HSTextMessage(mid, myword);
        HSMessageManager.getInstance().send(textMessage, new HSMessageManager.SendMessageCallback() {
            @Override
            public void onMessageSentFinished(HSBaseMessage message, boolean success, HSError error) {

            }
        },new Handler());
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

    @Override
    public void onMessageChanged(HSMessageChangeType changeType, List<HSBaseMessage> messages) {
        if(changeType == HSMessageChangeType.ADDED){
            for(int i = 0; i < messages.size(); i++){
                chatlist.add(messages.get(i));
                if(messages.get(i).getType() == HSMessageType.TEXT){
                    HSTextMessage textMessage = (HSTextMessage)messages.get(i);
                    if(textMessage.getFrom().equals(mid)) {
                        chatlist_String.add(textMessage.getText());
                        String_adapter.notifyDataSetChanged();
                        listView.setSelection(chatlist_String.size() - 1);
                    }
                }
            }
        }
    }

    @Override
    public void onTypingMessageReceived(String fromMid) {

    }

    @Override
    public void onOnlineMessageReceived(HSOnlineMessage message) {

    }

    @Override
    public void onUnreadMessageCountChanged(String mid, int newCount) {

    }

    @Override
    public void onReceivingRemoteNotification(JSONObject pushInfo) {

    }
}
