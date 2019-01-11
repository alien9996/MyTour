package com.example.dell.mytour.adapter.PersonalPageAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.mytour.R;
import com.example.dell.mytour.model.MessageItem;
import com.example.dell.mytour.model.model_base.Like;


import java.util.ArrayList;

public class PersonalListDiaryAdapter extends RecyclerView.Adapter<PersonalListDiaryAdapter.ViewHolder> {

    private ArrayList<MessageItem> lst_diary;
    private Context context;

    public PersonalListDiaryAdapter(ArrayList<MessageItem> lst_diary, Context context) {
        this.lst_diary = lst_diary;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_card_diary, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        if (lst_diary.size() > 0) {
            MessageItem message = lst_diary.get(lst_diary.size() - position - 1);

            holder.item_diary_state.setText(message.getMessage_content());
            holder.item_diary_name_friend.setText(message.getUser_name());
            holder.item_diary_time_like.setText(message.getMessage_date());
        }


    }

    @Override
    public int getItemCount() {
        return lst_diary.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_diary_state;
        TextView item_diary_name_friend;
        TextView item_diary_time_like;


        public ViewHolder(View itemView) {
            super(itemView);

            item_diary_state = itemView.findViewById(R.id.item_diary_state);
            item_diary_name_friend = itemView.findViewById(R.id.item_diary_name_friend);
            item_diary_time_like = itemView.findViewById(R.id.item_diary_time_like);
        }
    }
}
