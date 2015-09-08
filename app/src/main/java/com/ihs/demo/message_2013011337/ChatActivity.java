package com.ihs.demo.message_2013011337;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
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
    private String status;
    private ChatEntity _chatEntity;
    private String myword;
    public MediaPlayer player;
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
    @Override
    protected void onDestroy(){
        HSMessageManager.getInstance().markRead(mid);
        super.onDestroy();
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
                status = hsBaseMessage.getStatus().valueOf(hsBaseMessage.getStatus().getValue()).toString();
                if(hsBaseMessage.getFrom().equals(mid)){
                    chatEntity = new ChatEntity(word, date, "read", false);
                }else{
                    chatEntity = new ChatEntity(word, date, status.toLowerCase(), true);
                }
                Data_Entity.add(chatEntity);
            }else{
                String date = formatter.format(hsBaseMessage.getTimestamp());
                ChatEntity chatEntity = new ChatEntity(hsBaseMessage.toString(), date, "read", true);
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
                send();
            }
        });
    }

    private void play_ringtone(){
        try {
            player = MediaPlayer.create(this, R.raw.message_ringtone_sent);
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    player.release();
                    player = null;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void send(){
        final HSTextMessage textMessage = new HSTextMessage(mid, myword);
        play_ringtone();
        getInstance().send(textMessage, new SendMessageCallback() {
            @Override
            public void onMessageSentFinished(HSBaseMessage message, boolean success, HSError error) {
//                play_ringtone();
            }
        }, new Handler());
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
        if(changeType == HSMessageChangeType.ADDED && !messages.isEmpty()){
            for(int i = 0; i < messages.size(); i++){
                chatlist.add(messages.get(i));
                if(messages.get(i).getType() == HSMessageType.TEXT){
                    HSTextMessage textMessage = (HSTextMessage)messages.get(i);
                    if(textMessage.getFrom().equals(mid)) {
                        Date date = new Date(System.currentTimeMillis());
                        String str = formatter.format(date);
                        _chatEntity = new ChatEntity(textMessage.getText(), str, "read", false);
                        Data_Entity.add(_chatEntity);
                        msgAdapter.notifyDataSetChanged();
                        listView.setSelection(Data_Entity.size() - 1);
                    }
                    else if(textMessage.getTo().equals(mid)){
                        Date date = new Date(System.currentTimeMillis());
                        String str = formatter.format(date);
                        status = textMessage.getStatus().valueOf(textMessage.getStatus().getValue()).toString();
                        ChatEntity chatEntity = new ChatEntity(textMessage.getText(), str, status.toLowerCase(), true);
                        Data_Entity.add(chatEntity);
                        msgAdapter.notifyDataSetChanged();
                        listView.setSelection(Data_Entity.size() - 1);
                    }
                }else{
                    Date date = new Date(System.currentTimeMillis());
                    String str = formatter.format(date);
                    _chatEntity = new ChatEntity(messages.get(i).toString(), str, "read", false);
                    Data_Entity.add(_chatEntity);
                    msgAdapter.notifyDataSetChanged();
                    listView.setSelection(Data_Entity.size() - 1);

                }
            }
        }
        if(changeType == HSMessageChangeType.UPDATED && !messages.isEmpty()){
            for(int i = 0; i < messages.size(); i++){
                chatlist.add(messages.get(i));
                if(messages.get(i).getType() == HSMessageType.TEXT){
                    HSTextMessage textMessage = (HSTextMessage)messages.get(i);
                    if(textMessage.getTo().equals(mid)){
                        Date date = new Date(System.currentTimeMillis());
                        String str = formatter.format(date);
                        status = textMessage.getStatus().valueOf(textMessage.getStatus().getValue()).toString();
                        for(int j = Data_Entity.size()-1; j >= 0; j--){
                            if(Data_Entity.get(j).getText().equals(textMessage.getText()))
                                Data_Entity.get(j).setText(str, status.toLowerCase());
                        }
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
