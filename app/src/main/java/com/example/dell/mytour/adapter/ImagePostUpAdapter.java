package com.example.dell.mytour.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.mytour.R;
import com.example.dell.mytour.model.model_base.Image;

import java.util.ArrayList;

public class ImagePostUpAdapter extends RecyclerView.Adapter<ImagePostUpAdapter.ViewHolder>{

    ArrayList<Bitmap> bitmaps;
    Context context;

    public ImagePostUpAdapter(ArrayList<Bitmap> bitmaps, Context context){
        this.bitmaps = bitmaps;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_image_post_up, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap = bitmaps.get(position);
        holder.img_post_up.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_post_up;
        public ViewHolder(View itemView) {
            super(itemView);
            img_post_up = itemView.findViewById(R.id.img_post_up);
        }
    }
}
