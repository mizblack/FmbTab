package com.eye3.golfpay.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eye3.golfpay.R;
import com.eye3.golfpay.model.chat.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {

    List<Message> messages = new ArrayList<Message>();
    Context context;

    public ChatMessageAdapter(Context context) {
        this.context = context;
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);

        if (message.isBelongsToCurrentUser()) {
            convertView = messageInflater.inflate(R.layout.my_message, null);
        } else {
            convertView = messageInflater.inflate(R.layout.their_message, null);
        }

        holder.name = (TextView) convertView.findViewById(R.id.name);
        holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
        holder.tvEmergency = convertView.findViewById(R.id.tv_emergency);
        convertView.setTag(holder);

        holder.name.setText(message.getMemberData().getName());
        holder.messageBody.setText(message.getText());

        if (message.isEmergency()) {
            holder.tvEmergency.setVisibility(View.VISIBLE);
        } else {
            holder.tvEmergency.setVisibility(View.GONE);
        }

        return convertView;
    }
}

class MessageViewHolder {
    public View avatar;
    public TextView name;
    public TextView messageBody;
    public TextView tvEmergency;
}