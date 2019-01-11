package com.example.dell.mytour.adapter.PersonalPageAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.dell.mytour.R;
import com.example.dell.mytour.model.FollowUser;
import com.example.dell.mytour.model.PersonalPost;
import com.example.dell.mytour.model.model_base.Comment;
import com.example.dell.mytour.model.model_base.Follows;
import com.example.dell.mytour.model.model_base.Users;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalListFriendAdapter extends RecyclerView.Adapter<PersonalListFriendAdapter.ViewHolder> {

    private ArrayList<FollowUser> listUsers;
    private Context context;

    public PersonalListFriendAdapter(ArrayList<FollowUser> listUsers, Context context) {
        this.listUsers = listUsers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_list_friend, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        FollowUser followUser = listUsers.get(position);

        Users user = followUser.getUsers();
        Follows follows = followUser.getFollows();
        if (user != null) {


            holder.list_friend_name.setText(user.getUser_full_name());
//        holder.list_friend_time.setText(user.getUser_name());
            holder.list_friend_processbar.setVisibility(View.VISIBLE);
            if (!user.getUser_avatar().equals("") && user.getUser_avatar().startsWith("http")) {


                Glide.with(context.getApplicationContext()).load(user.getUser_avatar()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.list_friend_processbar.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.list_friend_processbar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.list_friend_ava);
            }
            else {
                holder.list_friend_processbar.setVisibility(View.GONE);
                holder.list_friend_ava.setImageResource(R.drawable.ic_friend_black_24dp);
            }
        }
        holder.list_friend_time.setText("Follow tu ngay " + follows.getFollow_date());

    }

    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView list_friend_ava;
        TextView list_friend_name;
        TextView list_friend_time;
        ProgressBar list_friend_processbar;

        public ViewHolder(View itemView) {
            super(itemView);

            list_friend_ava = itemView.findViewById(R.id.list_friend_ava);
            list_friend_name = itemView.findViewById(R.id.list_friend_name);
            list_friend_time = itemView.findViewById(R.id.list_friend_time);
            list_friend_processbar = itemView.findViewById(R.id.list_friend_processbar);

        }
    }

}
