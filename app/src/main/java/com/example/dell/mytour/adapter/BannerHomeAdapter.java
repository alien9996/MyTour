package com.example.dell.mytour.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.model.PostItem;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.uis.activity.post_activity.PostDetailsActivity;

import java.util.ArrayList;

public class BannerHomeAdapter extends RecyclerView.Adapter<BannerHomeAdapter.ViewHolder> implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ArrayList<PostItem> lst_banner;
    private Context context;
    public BannerHomeAdapter( Context context, ArrayList<PostItem> lst_banner){
        this.lst_banner = lst_banner;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_banner_home_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(lst_banner.size() > 0 ){
            PostItem content = lst_banner.get(lst_banner.size() - position -1);

            if(content.getPost_avatar() == null){
                holder.img_banner_item.setImageResource(R.drawable.nhatrangdem);
            }else {
                Glide.with(context).load(content.getPost_avatar()).into(holder.img_banner_item);
            }

            if(content.getPost_rating_average() != null){
                holder.txt_count_like_banner_item.setText(" "+content.getPost_rating_average().substring(0, 3)+"/5 điểm");
            } else {
                holder.txt_count_like_banner_item.setText(" 2.5/5 điểm");
            }

            holder.txt_distance_banner_item.setText(" "+ content.getPost_location_stop());
            holder.txt_title_banner_item.setText(content.getPost_advertisement_title());
            holder.txt_price_new_banner_item.setText(content.getPost_price_new());
            holder.txt_price_old_banner_item.setText(content.getPost_price_old());
            holder.txt_price_old_banner_item.setPaintFlags(holder.txt_price_old_banner_item.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

            holder.btn_detail_banner_item.setOnClickListener(this);
        }

    }

    @Override
    public int getItemCount() {
        if(lst_banner.size() > 5){
            return  5;
        } else {
            return lst_banner.size();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_detail_banner_item:
                Intent intent = new Intent(context, PostDetailsActivity.class);
                context.startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, PostDetailsActivity.class);
        context.startActivity(intent);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        Button btn_detail_banner_item;
        ImageView img_banner_item;
        TextView txt_title_banner_item,txt_count_like_banner_item,
                txt_distance_banner_item,txt_price_new_banner_item,
                txt_price_old_banner_item;

        public ViewHolder(View itemView) {
            super(itemView);
            img_banner_item = itemView.findViewById(R.id.img_banner_item);
            txt_title_banner_item = itemView.findViewById(R.id.txt_title_banner_item);
            txt_count_like_banner_item = itemView.findViewById(R.id.txt_count_like_banner_item);
            txt_distance_banner_item = itemView.findViewById(R.id.txt_distance_banner_item);
            txt_price_new_banner_item = itemView.findViewById(R.id.txt_price_new_banner_item);
            txt_price_old_banner_item = itemView.findViewById(R.id.txt_price_old_banner_item);

            btn_detail_banner_item = itemView.findViewById(R.id.btn_detail_banner_item);
        }
    }
}

