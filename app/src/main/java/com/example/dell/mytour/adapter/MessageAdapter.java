package com.example.dell.mytour.adapter;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.RelativeDateTimeFormatter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.model.MessageItem;
import com.example.dell.mytour.model.model_base.Comment;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<MessageItem> lst_message;
    private Context context;

    public MessageAdapter(Context context, ArrayList<MessageItem> lst_message){
        this.lst_message = lst_message;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_messages,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(lst_message.size() > 0){
            MessageItem message = lst_message.get(lst_message.size() - position -1);

            if(message.isMessage_status()){
                holder.relative_layout_message.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

            if(message.getUser_img_link() != null && !("").equals(message.getUser_img_link())){
                Glide.with(context).load(message.getUser_img_link()).into(holder.img_avt_friend_message);
            } else {
                holder.img_avt_friend_message.setImageResource(R.drawable.ic_friend_black_24dp);
            }

            holder.txt_name_friend_message.setText(message.getUser_name());
            holder.txt_content_message.setText(message.getMessage_content());
            holder.txt_time_message.setText(message.getMessage_date());
        }
    }

    @Override
    public int getItemCount() {
        return lst_message.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView img_avt_friend_message;
        public TextView txt_time_message, txt_content_message, txt_name_friend_message;
        public RelativeLayout relative_layout_message;

        public ViewHolder(View itemView) {
            super(itemView);
            img_avt_friend_message = itemView.findViewById(R.id.img_avt_frient_message);
            txt_content_message = itemView.findViewById(R.id.txt_content_massage);
            txt_time_message = itemView.findViewById(R.id.txt_time_message);
            txt_name_friend_message = itemView.findViewById(R.id.txt_name_friend_message);
            relative_layout_message = itemView.findViewById(R.id.relative_layout_message);

        }
    }
}
