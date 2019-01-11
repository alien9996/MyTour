package com.example.dell.mytour.adapter.PersonalPost;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.model.ItemFriend;
import com.example.dell.mytour.model.model_base.Friend;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListFriendAdapter extends RecyclerView.Adapter<ListFriendAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ItemFriend> lst_firend;
    private boolean show_button;

    public ListFriendAdapter(Context context, ArrayList<ItemFriend> lst_firend, boolean show_button) {
        this.context = context;
        this.lst_firend = lst_firend;
        this.show_button = show_button;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_tag_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (lst_firend.size() > 0) {
            final ItemFriend friend = lst_firend.get(lst_firend.size() - position - 1);
            if (("").equals(friend.getAvatar()) || friend.getAvatar() == null) {
                holder.img_avt_frient_search.setImageResource(R.drawable.ic_friend_black_24dp);
            } else {
                Glide.with(context).load(friend.getAvatar()).into(holder.img_avt_frient_search);
            }
            holder.txt_name_friend_search.setText(friend.getFull_name());
            holder.txt_address_friend.setText(friend.getAddress());
            if (show_button) {
                holder.btn_confirm_friend.setVisibility(View.VISIBLE);
                holder.btn_confirm_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addFriend(friend.getFriend_id());
                        updateStatus(friend.getFriend_key(), friend.getFriend_id());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return lst_firend.size();
    }

    // phuong thuc get Date
    //--------------------------------------------------------------------------------------------------
    private String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        return "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND)
                + " - " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }

    //----------------------------------------------------------------------------------------------
    // cac phuong thuc set ket ban
    /*khi click vao nut ket ban*/
    private void addFriend(String friend_id) {
        FirebaseDatabase firebase_database = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebase_database.getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String key = reference.child("Friend").push().getKey();

        Friend friend = new Friend(key, friend_id, true, getDateTime(), user.getUid());

        Map<String, Object> friend_values = friend.toMap();
        Map<String, Object> child_add = new HashMap<>();

        child_add.put("/Friend/" + key, friend_values);

        reference.updateChildren(child_add).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Add Friend", "That bai");
            }
        });

    }

    /*khi click vao nut ket ban*/

    //----------------------------------------------------------------------------------------------
    /*update friend stattus true*/
    private void updateStatus(String key, String friend_id){
        FirebaseDatabase firebase_database = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebase_database.getReference();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Friend friend = new Friend(key, user.getUid(), true, getDateTime(), friend_id);

        Map<String, Object> friend_values = friend.toMap();
        Map<String, Object> child_add = new HashMap<>();

        child_add.put("/Friend/" + key, friend_values);

        reference.updateChildren(child_add).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Add Friend", "That bai");
            }
        });
    }
    /*update friend stattus true*/

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img_avt_frient_search;
        TextView txt_name_friend_search, txt_address_friend;
        Button btn_confirm_friend;

        public ViewHolder(View itemView) {
            super(itemView);
            img_avt_frient_search = itemView.findViewById(R.id.img_avt_frient_search);
            txt_name_friend_search = itemView.findViewById(R.id.txt_name_friend_search);
            txt_address_friend = itemView.findViewById(R.id.txt_address_friend);
            btn_confirm_friend = itemView.findViewById(R.id.btn_confirm_friend);
        }
    }
}
