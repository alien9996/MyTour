package com.example.dell.mytour.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.model.PostItem;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.model.model_base.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHodler> {

    private ArrayList<PostItem> lst_post;
    private Context context;


    public PostAdapter(Context context, ArrayList<PostItem> lst_post) {
        this.context = context;
        this.lst_post = lst_post;
    }


    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_post_layout, parent, false);

        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {


        PostItem post = lst_post.get(lst_post.size()-position-1);

        if(post.getPost_avatar() == null){
            holder.img_item_post.setImageResource(R.drawable.hinh_b);
        } else {
            Glide.with(context).load(post.getPost_avatar()).into(holder.img_item_post);
        }

        if(post.getUser_post_avatar() == null){
            holder.img_avt_user_post.setImageResource(R.drawable.ic_friend_black_24dp);
        } else {
            Glide.with(context).load(post.getUser_post_avatar()).into(holder.img_avt_user_post);
        }

        holder.txt_title_post_item.setText(post.getPost_title());
        holder.txt_quote_content_post_item.setText(post.getPost_content());
        holder.txt_name_user_post.setText(post.getUser_post_name());
        holder.txt_time_post.setText(post.getPost_date());
        if(post.getPost_rating_average() != null){
            holder.txt_count_view.setText(" - " + post.getPost_rating_average().substring(0, 3) + "/5 điểm");
        } else {
            holder.txt_count_view.setText(" -  2.5/5 điểm");
        }

    }

    @Override
    public int getItemCount() {
        return lst_post.size();
    }


    // class ViewHolder
    public static class ViewHodler extends RecyclerView.ViewHolder {
        ImageView img_item_post, img_avt_user_post;
        TextView txt_title_post_item, txt_quote_content_post_item, txt_name_user_post, txt_time_post, txt_count_view;

        public ViewHodler(View itemView) {
            super(itemView);


            img_avt_user_post = itemView.findViewById(R.id.img_avt_user_post);
            img_item_post = itemView.findViewById(R.id.img_item_post);
            txt_title_post_item = itemView.findViewById(R.id.txt_title_post_item);
            txt_quote_content_post_item = itemView.findViewById(R.id.txt_quote_content_post_item);
            txt_name_user_post = itemView.findViewById(R.id.txt_name_user_post);
            txt_time_post = itemView.findViewById(R.id.txt_time_post);
            txt_count_view = itemView.findViewById(R.id.txt_count_view);
        }
    }
}
