package com.example.dell.mytour.adapter.PersonalPageAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.dell.mytour.R;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.uis.activity.ShowImageActivity;
import com.example.dell.mytour.utils.SquareImage;

import java.util.ArrayList;

public class PersonalpageGridImageAdapter extends RecyclerView.Adapter<PersonalpageGridImageAdapter.ViewHolder> {

    private OnItemImageClickListener listener;
    // Define the listener interface
    public interface OnItemImageClickListener {
        void onItemClick(View itemView, int position);
        void onImageClick(SquareImage image, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemImageClickListener listener) {
        this.listener = listener;
    }
    private ArrayList<String> imgUrls;
    private Context context;

    public PersonalpageGridImageAdapter(ArrayList<String> imgUrls, Context context) {
        this.imgUrls = imgUrls;
        this.context = context;
    }

    public PersonalpageGridImageAdapter(ArrayList<String> imgUrls, Context context, OnItemImageClickListener listener) {
        this.imgUrls = imgUrls;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_personalpage_image, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String imgURL = imgUrls.get(position);

        holder.item_personal_progresbar.setVisibility(View.VISIBLE);
        Glide.with(context).load(imgURL).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.item_personal_progresbar.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.item_personal_progresbar.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.item_personal_img);

//        holder.item_personal_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, ShowImageActivity.class);
//                context.startActivity(intent);
//            }
//        });
        holder.item_personal_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!= null){
                    if (position !=RecyclerView.NO_POSITION){
                        listener.onImageClick(holder.item_personal_img, position);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return imgUrls.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        SquareImage item_personal_img;
        ProgressBar item_personal_progresbar;

        public ViewHolder(final View itemView) {
            super(itemView);
            item_personal_img = itemView.findViewById(R.id.item_personal_img);
            item_personal_progresbar = itemView.findViewById(R.id.item_personal_progressbar);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Triggers click upwards to the adapter on click
//                    if (listener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            if (item_personal_img.callOnClick()) {
//                                listener.onItemClick(itemView, position);
//                            }
//                        }
//                    }
//                }
//            });


        }
    }
}