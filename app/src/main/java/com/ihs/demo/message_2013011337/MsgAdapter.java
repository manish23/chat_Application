package com.ihs.demo.message_2013011337;

import android.R.integer;
import android.content.Context;
import android.database.DataSetObserver;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihs.message_2013011337.R;

import java.util.ArrayList;
import java.util.List;

public class MsgAdapter extends BaseAdapter {

    //ListView视图的内容由IMsgViewType决定
    public static interface IMsgViewType {
        //对方发来的信息
        int IMVT_COM_MSG = 0;
        //自己发出的信息
        int IMVT_TO_MSG = 1;
    }

    private static final String TAG = MsgAdapter.class.getSimpleName();
    private List<ChatEntity> data;
    private Context context;
    private LayoutInflater mInflater;

    public MsgAdapter(Context context, List<ChatEntity> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }

    //获取ListView的项个数
    public int getCount() {
        return data.size();
    }

    //获取项
    public Object getItem(int position) {
        return data.get(position);
    }

    //获取项的ID
    public long getItemId(int position) {
        return position;
    }

    //获取项的类型
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        ChatEntity entity = data.get(position);

        if (entity.get_Issend()) {
            return IMsgViewType.IMVT_COM_MSG;
        } else {
            return IMsgViewType.IMVT_TO_MSG;
        }

    }

    //获取项的类型数
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 2;
    }

    //获取View
    public View getView(int position, View convertView, ViewGroup parent) {

        ChatEntity entity = data.get(position);
        boolean isComMsg = entity.get_Issend();

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (isComMsg) {
                //如果是对方发来的消息，则显示的是左气泡
                convertView = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
                viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.send_time);
                viewHolder.tvContent = (TextView) convertView.findViewById(R.id.send_text);
            } else {
                //如果是自己发出的消息，则显示的是右气泡
                convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);
                viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.receive_time);
                viewHolder.tvContent = (TextView) convertView.findViewById(R.id.receive_text);
            }

            viewHolder.isComMsg = isComMsg;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvSendTime.setText(entity.getDate());
        viewHolder.tvContent.setText(entity.getText());

        return convertView;
    }

    //通过ViewHolder显示项的内容
    static class ViewHolder {
        public TextView tvSendTime;
        public TextView tvContent;
        public boolean isComMsg = true;
    }
}