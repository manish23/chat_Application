package com.ihs.demo.message_2013011337;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ihs.message_2013011337.managers.HSMessageManager.*;

public class ChatActivity extends HSActionBarActivity implements HSMessageChangeListener{
    private String name;
    private String mid;
    private ListView listView;
    private Button button_send;
    private EditText editText;
    private String myword;
    private String get_word;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<HSBaseMessage> chatlist = new ArrayList<>();
    List<ChatEntity> Data_Entity = new ArrayList<>();
    private MsgAdapter msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        mid = intent.getStringExtra("mid");
        setTitle(name);
        init_view();

    }
    public void search(){
        List<HSBaseMessage> res = HSMessageManager.getInstance().queryMessages(mid, 0, -1).getMessages();
        for(int i = res.size()-1; i >= 0; i--){
            HSBaseMessage hsBaseMessage = res.get(i);
            if(hsBaseMessage.getType() == HSMessageType.TEXT){
                HSTextMessage hsTextMessage = (HSTextMessage)hsBaseMessage;
                String word = hsTextMessage.getText();
                Date _date = hsBaseMessage.getTimestamp();
                String date = formatter.format(_date);
                ChatEntity chatEntity;
                if(hsBaseMessage.getFrom().equals(mid)){
                    chatEntity = new ChatEntity(word, date, false);
                }else{
                    chatEntity = new ChatEntity(word, date, true);
                }
                Data_Entity.add(chatEntity);
            }
        }
    }
    public void init_view(){
        button_send = (Button) findViewById(R.id.btn_send);
        listView = (ListView) findViewById(R.id.List_view);
        editText = (EditText) findViewById(R.id.editText_sendmessage);
        search();
        msgAdapter = new MsgAdapter(this, Data_Entity);
        listView.setAdapter(msgAdapter);
        msgAdapter.notifyDataSetChanged();
        listView.setSelection(Data_Entity.size() - 1);
        HSMessageManager.getInstance().addListener(this, new Handler());
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myword = null;
                myword = (editText.getText() + "").toString();
                if (myword.length() == 0)
                    return;
                editText.setText("");
                HSTextMessage textMessage = new HSTextMessage(mid, myword);
                Date date = new Date(System.currentTimeMillis());
                String str = formatter.format(date);

                ChatEntity chatEntity = new ChatEntity(myword, str, true);
                chatlist.add(textMessage);
                Data_Entity.add(chatEntity);

                msgAdapter.notifyDataSetChanged();
                listView.setSelection(Data_Entity.size() - 1);
                send();
            }
        });
    }
    public void send(){
        HSTextMessage textMessage = new HSTextMessage(mid, myword);
        getInstance().send(textMessage, new SendMessageCallback() {
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
                        Date date = new Date(System.currentTimeMillis());
                        String str = formatter.format(date);
                        ChatEntity chatEntity = new ChatEntity(textMessage.getText(), str, false);
                        Data_Entity.add(chatEntity);
                        msgAdapter.notifyDataSetChanged();
                        listView.setSelection(Data_Entity.size() - 1);
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
