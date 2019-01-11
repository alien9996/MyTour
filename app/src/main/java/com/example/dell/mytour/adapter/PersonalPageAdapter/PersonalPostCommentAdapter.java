package com.example.dell.mytour.adapter.PersonalPageAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.model.PostComment;
import com.example.dell.mytour.model.model_base.Answer;
import com.example.dell.mytour.model.model_base.Comment;
import com.example.dell.mytour.model.model_base.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.model.PostComment;
import com.example.dell.mytour.model.model_base.Comment;
import com.example.dell.mytour.model.model_base.Users;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PersonalPostCommentAdapter extends RecyclerView.Adapter<PersonalPostCommentAdapter.ViewHolder> {

    OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onAnswerClick(EditText editText, View view, int position);

        void onSendClick(Button button, View view, ArrayList<Answer> answerList, int position);

        void onMenuClick(Button button, View view, int position);
    }

    private ArrayList<PostComment> postComments;
    private Context context;
    private String currentUserId;

    public PersonalPostCommentAdapter(ArrayList<PostComment> postComments, Context context, String currentUserId) {
        this.postComments = postComments;
        this.context = context;
        this.currentUserId = currentUserId;
    }

    public PersonalPostCommentAdapter(ArrayList<PostComment> postComments, Context context, String currentUserId, OnItemClickListener listener) {
        this.postComments = postComments;
        this.context = context;
        this.currentUserId = currentUserId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_personal_post_comment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final PostComment postComment = postComments.get(position);

        Users user = postComment.getUsers();
        final Comment comments = postComment.getComment();


        if (user != null) {
            if (!user.getUser_avatar().equals("") && user.getUser_avatar().startsWith("http") && context != null) {
                Glide.with(context.getApplicationContext()).load(user.getUser_avatar()).into(holder.personal_comment_img_friend);
            } else
                holder.personal_comment_img_friend.setImageResource(R.drawable.ic_friend_black_24dp);

            holder.personal_comment_txt_name_friend.setText(user.getUser_full_name());
            holder.personal_comment_btn_menu_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onMenuClick(holder.personal_comment_btn_menu_item, holder.itemView, position);
                        }
                    }
                }
            });
        }


        if (comments != null) {
            holder.personal_comment_txt_comment.setText(comments.getCmm_content());
            holder.personal_comment_txt_time_comment.setText(comments.getCmm_date());
        }


        holder.personal_comment_txt_comment.post(new Runnable() {
            @Override
            public void run() {
                if (holder.personal_comment_txt_comment.getLineCount() > 3) {
                    holder.personal_comment_txt_xemthem.setVisibility(View.VISIBLE);
                } else {
                    holder.personal_comment_txt_xemthem.setVisibility(View.GONE);
                }
            }
        });


        holder.personal_comment_txt_xemthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.personal_comment_txt_comment.getLineCount() > 3) {
                    if (holder.personal_comment_txt_comment.getMaxLines() < 4) {
                        holder.personal_comment_txt_comment.setMaxLines(1000);
                        holder.personal_comment_txt_xemthem.setText("rút gọn.");
                    } else {
                        holder.personal_comment_txt_comment.setMaxLines(3);
                        holder.personal_comment_txt_xemthem.setText("xem thêm...");

                    }

                } else {
                    holder.personal_comment_txt_xemthem.setVisibility(View.GONE);

                }

            }
        });
        //=============Load comment ava===========
        if (!currentUserId.equals("")) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Users users = dataSnapshot1.getValue(Users.class);
                        if (currentUserId.equals(users.getUser_id())) {
                            if (!users.getUser_avatar().equals("") && users.getUser_avatar().startsWith("http")) {
                                Glide.with(context.getApplicationContext()).load(users.getUser_avatar()).into(holder.personal_post_img_answer_ava);
                            } else
                                holder.personal_post_img_answer_ava.setImageResource(R.drawable.ic_friend_black_24dp);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            holder.personal_post_img_answer_ava.setImageResource(R.drawable.ic_friend_black_24dp);
        }


        //====see answer ===========
        holder.personal_comment_txt_answer_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.item_post_comment_answer.getVisibility() == View.GONE) {
                    holder.item_post_comment_answer.setVisibility(View.VISIBLE);
                } else {
                    holder.item_post_comment_answer.setVisibility(View.GONE);
                }

            }
        });

        //==============Recycle View ListAnswer======================

        final ArrayList<Answer> answers = new ArrayList<>();


        RecyclerView.LayoutManager manager = new LinearLayoutManager(context);
        holder.post_comment_list_answer.setLayoutManager(manager);
        final PersonalPostAnswerCommentAdapter answerCommentAdapter = new PersonalPostAnswerCommentAdapter(answers, context, currentUserId);
        holder.post_comment_list_answer.setAdapter(answerCommentAdapter);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dataReference = firebaseDatabase.getReference();
        dataReference.child("Answers").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                answers.clear();
                for (DataSnapshot answerSnap : dataSnapshot.getChildren()) {
                    Answer answer = answerSnap.getValue(Answer.class);
                    if (answer.getCmm_id().equals(comments.getCmm_id())) {
                        answers.add(answer);
                    }
                }
                holder.personal_comment_txt_answer_comment.setText(answers.size() + " trả lời");
                answerCommentAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (!currentUserId.equals("")){
            //=========Answer Click======================
            holder.personal_post_edt_answer_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAnswerClick(holder.personal_post_edt_answer_text, holder.itemView, position);
                        }
                    }
                }
            });

            //==========SendClick=========================
            holder.personal_post_btn_send_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onSendClick(holder.personal_post_btn_send_answer, holder.itemView, answers, position);
                            holder.item_post_comment_answer.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }
        else {
            holder.personal_post_edt_answer_text.setVisibility(View.GONE);
            holder.personal_post_btn_send_answer.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return postComments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView personal_comment_img_friend;
        TextView personal_comment_txt_name_friend;
        TextView personal_comment_txt_time_comment;
        TextView personal_comment_txt_comment;
        TextView personal_comment_txt_xemthem;
        Button personal_comment_btn_menu_item;
        TextView personal_comment_txt_answer_comment;
        RelativeLayout item_post_comment_answer;
        CircleImageView personal_post_img_answer_ava;
        EditText personal_post_edt_answer_text;
        Button personal_post_btn_send_answer;
        RecyclerView post_comment_list_answer;

        public ViewHolder(View itemView) {
            super(itemView);

            personal_comment_img_friend = itemView.findViewById(R.id.personal_comment_img_friend);
            personal_comment_txt_name_friend = itemView.findViewById(R.id.personal_comment_txt_name_friend);
            personal_comment_txt_time_comment = itemView.findViewById(R.id.personal_comment_txt_time_comment);
            personal_comment_txt_comment = itemView.findViewById(R.id.personal_comment_txt_comment);
            personal_comment_txt_xemthem = itemView.findViewById(R.id.personal_comment_txt_xemthem);
            personal_comment_btn_menu_item = itemView.findViewById(R.id.personal_comment_btn_menu_item);
            personal_comment_txt_answer_comment = itemView.findViewById(R.id.personal_comment_txt_answer_comment);
            item_post_comment_answer = itemView.findViewById(R.id.item_post_comment_answer);
            personal_post_img_answer_ava = itemView.findViewById(R.id.personal_post_img_answer_ava);
            personal_post_edt_answer_text = itemView.findViewById(R.id.personal_post_edt_answer_text);
            personal_post_btn_send_answer = itemView.findViewById(R.id.personal_post_btn_send_answer);
            post_comment_list_answer = itemView.findViewById(R.id.post_comment_list_answer);
        }
    }
}
