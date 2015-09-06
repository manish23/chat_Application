package com.ihs.demo.message_2013011337;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ihs.commons.notificationcenter.HSGlobalNotificationCenter;
import com.ihs.commons.notificationcenter.INotificationObserver;
import com.ihs.commons.utils.HSBundle;
import com.ihs.message_2013011337.R;
import com.ihs.message_2013011337.managers.HSMessageChangeListener;
import com.ihs.message_2013011337.managers.HSMessageManager;
import com.ihs.message_2013011337.types.HSBaseMessage;
import com.ihs.message_2013011337.types.HSOnlineMessage;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessagesFragment extends Fragment implements INotificationObserver, HSMessageChangeListener{

    private ListView listView;
    private MessageAdapter messageAdapter = null;
    private HSBaseMessage hsBaseMessage;
    private List<HSBaseMessage> list_hsBaseMessage = new ArrayList<>();
    private HSMessageChangeType changeType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        listView = (ListView) view.findViewById(R.id.message_list);
        final List<Contact> contacts = new ArrayList<>();

        messageAdapter = new MessageAdapter(this.getActivity(), R.layout.cell_item_message, contacts, list_hsBaseMessage);
        listView.setAdapter(messageAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String mid = contacts.get(position).getMid();
                String name = contacts.get(position).getName();
                //Toast.makeText(getActivity(), "你点击了名字为：" + name + " mid为：" + mid + "的联系人，需要在这里跳转到同此人的聊天界面（一个Activity）", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(getActivity(), ChatActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("mid", mid);
                startActivity(intent);

            }

        });
        HSGlobalNotificationCenter.addObserver("Message_changed", this);
        refresh();
        return view;
    }
    public void refresh(){

        messageAdapter.getContacts().clear();
        for (Contact contact: FriendManager.getInstance().getAllFriends()){
            List<HSBaseMessage> res = HSMessageManager.getInstance().queryMessages(contact.getMid(), 1, -1).getMessages();
            if(!res.isEmpty()){
                messageAdapter.getHsBaseMessage().add(res.get(0));
                messageAdapter.getContacts().add(contact);
                messageAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onReceive(String s, HSBundle hsBundle) {
        refresh();
        changeType =(HSMessageChangeType)hsBundle.getObject("changeType");
        List<?> list = hsBundle.getObjectList("message");
        for(int i = 0; i < list.size(); i++){
            HSBaseMessage hs_Message = (HSBaseMessage)list.get(i);
            this.list_hsBaseMessage.add(hs_Message);
        }
    }

    @Override
    public void onMessageChanged(HSMessageChangeType changeType, List<HSBaseMessage> messages) {

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
