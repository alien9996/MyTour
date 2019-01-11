package com.example.dell.mytour.adapter.PersonalPageAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.model.model_base.Answer;
import com.example.dell.mytour.model.model_base.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalPostAnswerCommentAdapter extends  RecyclerView.Adapter<PersonalPostAnswerCommentAdapter.ViewHolder>{

    private ArrayList<Answer> list;
    private  Context context;
    private String currentUserId;

    public PersonalPostAnswerCommentAdapter(ArrayList<Answer> list, Context context, String currentUserId) {
        this.list = list;
        this.context = context;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_answer_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Answer answer = list.get(position);

        if (answer!= null){
            holder.personal_answer_txt_comment.setText(answer.getAns_content());
            holder.personal_comment_txt_time_comment.setText(answer.getAns_date());

            final String uId = answer.getFriend_id();
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Users users = dataSnapshot1.getValue(Users.class);
                        if (uId.equals(users.getUser_id())) {
                            holder.personal_answer_txt_name_friend.setText(users.getUser_full_name());
                            if (!users.getUser_avatar().equals("") && users.getUser_avatar().startsWith("http")){
                                Glide.with(context.getApplicationContext()).load(users.getUser_avatar()).into(holder.personal_answer_img_friend);

                            }
                            else {
                                holder.personal_answer_img_friend.setImageResource(R.drawable.ic_friend_black_24dp);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }




        holder.personal_answer_txt_xemthem.post(new Runnable() {
            @Override
            public void run() {
                if (holder.personal_answer_txt_comment.getLineCount() > 2){
                    holder.personal_answer_txt_xemthem.setVisibility(View.VISIBLE);
                }
                else {
                    holder.personal_answer_txt_xemthem.setVisibility(View.GONE);
                }
            }
        });
        holder.personal_answer_txt_xemthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.personal_answer_txt_comment.getLineCount()>2){
                    if (holder.personal_answer_txt_comment.getMaxLines() < 3){
                        holder.personal_answer_txt_comment.setMaxLines(1000);
                        holder.personal_answer_txt_xemthem.setText("rút gọn");
                    }
                    else {
                        holder.personal_answer_txt_comment.setMaxLines(2);
                        holder.personal_answer_txt_xemthem.setText("xem thêm");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView personal_answer_img_friend;
        TextView personal_answer_txt_name_friend;
        TextView personal_comment_txt_time_comment;
        TextView personal_answer_txt_comment;
        TextView personal_answer_txt_xemthem;
        public ViewHolder(View itemView) {
            super(itemView);

            personal_answer_img_friend = itemView.findViewById(R.id.personal_answer_img_friend);
            personal_answer_txt_name_friend =itemView.findViewById(R.id.personal_answer_txt_name_friend);
            personal_comment_txt_time_comment = itemView.findViewById(R.id.personal_comment_txt_time_comment);
            personal_answer_txt_comment = itemView.findViewById(R.id.personal_answer_txt_comment);
            personal_answer_txt_xemthem= itemView.findViewById(R.id.personal_answer_txt_xemthem);
        }
    }
}
