
/**
 * Created by wuchen on 15-9-6.
 */
package com.ihs.demo.message_2013011337;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ihs.message_2013011337.R;
import com.ihs.message_2013011337.managers.HSMessageManager;
import com.ihs.message_2013011337.types.HSBaseMessage;
import com.ihs.message_2013011337.types.HSMessageType;
import com.ihs.message_2013011337.types.HSTextMessage;

public class MessageAdapter extends ArrayAdapter<Contact> {

    private List<Contact> contacts;
    private Context context;
    private List<HSBaseMessage> hsBaseMessage = new ArrayList<>();


    private class ViewHolder {
        TextView nameDate;
        TextView detailTextView;
    }

    public List<Contact> getContacts() {

        return contacts;
    }



    public MessageAdapter(Context context, int resource, List<Contact> objects) {
        super(context, resource, objects);
        this.contacts = objects;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.cell_item_message, parent, false);
            TextView titleView = (TextView) convertView.findViewById(R.id.name_Date_view);
            TextView detailView = (TextView) convertView.findViewById(R.id.detail_text);
            holder.nameDate = titleView;
            holder.detailTextView = detailView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Contact contact = contacts.get(position);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        hsBaseMessage = HSMessageManager.getInstance().queryMessages(contact.getMid(), 0, -1).getMessages();
        HSBaseMessage baseMessage = hsBaseMessage.get(0);
        String date = formatter.format(baseMessage.getTimestamp());
        holder.nameDate.setText("" + contact.getName() + ": " + date);
        if(baseMessage.getType() == HSMessageType.TEXT){
            HSTextMessage hsTextMessage = (HSTextMessage)baseMessage;
            String text = hsTextMessage.getText().toString();
            if(text.length() > 23)
                text = text.substring(0, 22) +"...";
            holder.detailTextView.setText(text);
            holder.detailTextView.setTextSize(15);
        }
        holder.nameDate.setTextColor(Color.parseColor("#000622"));
        holder.detailTextView.setTextColor(Color.parseColor("#000622"));

        return convertView;
    }
}
