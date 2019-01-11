package com.example.dell.mytour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.model.PostItem;
import com.example.dell.mytour.model.model_base.Post;

import java.util.ArrayList;

public class PostHomeAdapter extends RecyclerView.Adapter<PostHomeAdapter.ViewHolder> {

    private ArrayList<PostItem> lst_post_home;
    private Context context;

    public PostHomeAdapter(Context context, ArrayList<PostItem> lst_post_home) {
        this.lst_post_home = lst_post_home;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_post_home_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(lst_post_home.size() > 0){
            PostItem post = lst_post_home.get(lst_post_home.size()-position-1);

            if (post.getPost_avatar() == null) {
                holder.img_post_item_home.setImageResource(R.drawable.hinh_b);
            } else {
                Glide.with(context).load(post.getPost_avatar()).into(holder.img_post_item_home);
            }

            holder.txt_title_post_item_home.setText(post.getPost_title());
            holder.txt_quote_post_item_home.setText(post.getPost_content());
            holder.txt_name_user_post_item_home.setText(post.getUser_post_name());
            if(post.getPost_rating_average() != null){
                holder.txt_count_view_item_home.setText(" "+post.getPost_rating_average().substring(0, 3)+"/5 điểm");
            } else {
                holder.txt_count_view_item_home.setText(" 2.5/5 điểm");
            }
        }

    }

    @Override
    public int getItemCount() {

        if(lst_post_home.size() > 5){
            return  5;
        } else {
            return lst_post_home.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_post_item_home;
        TextView txt_title_post_item_home, txt_quote_post_item_home, txt_name_user_post_item_home, txt_count_view_item_home;

        public ViewHolder(View itemView) {
            super(itemView);
            img_post_item_home = itemView.findViewById(R.id.img_post_item_home);
            txt_title_post_item_home = itemView.findViewById(R.id.txt_title_post_item_home);
            txt_quote_post_item_home = itemView.findViewById(R.id.txt_quote_post_item_home);
            txt_name_user_post_item_home = itemView.findViewById(R.id.txt_name_user_post_item_home);
            txt_count_view_item_home = itemView.findViewById(R.id.txt_count_view_item_home);
        }
    }
}
