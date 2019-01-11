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
import com.example.dell.mytour.model.PostDetailsItem;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PostItemDetailsAdapter extends RecyclerView.Adapter<PostItemDetailsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PostDetailsItem> lst_post_item;

    public PostItemDetailsAdapter(Context context, ArrayList<PostDetailsItem> lst_post_item){
        this.context = context;
        this.lst_post_item = lst_post_item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_post_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostDetailsItem item = lst_post_item.get(position);

        if(item.getPost_content() == null){
            holder.txt_content_post_details.setVisibility(View.GONE);
        } else {
            holder.txt_content_post_details.setText(item.getPost_content());
        }
        if(item.getPost_image_description() == null){
            holder.txt_description_post_details.setVisibility(View.GONE);
        } else {
            holder.txt_description_post_details.setText(item.getPost_image_description());
        }

        if(item.getPost_image_link() == null){
            holder.img_post_details.setVisibility(View.GONE);
        } else {
            Glide.with(context).load(item.getPost_image_link()).into(holder.img_post_details);
        }
    }

    @Override
    public int getItemCount() {
        return lst_post_item.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_content_post_details, txt_description_post_details;
        ImageView img_post_details;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_content_post_details = itemView.findViewById(R.id.txt_content_post_details);
            txt_description_post_details = itemView.findViewById(R.id.txt_description_post_details);
            img_post_details = itemView.findViewById(R.id.img_post_details);
        }
    }
}
