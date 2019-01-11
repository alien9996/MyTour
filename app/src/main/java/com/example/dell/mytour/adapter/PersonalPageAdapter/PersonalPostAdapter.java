package com.example.dell.mytour.adapter.PersonalPageAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.PersonalPost.ImagePagerAdapter;
import com.example.dell.mytour.model.PersonalPost;
import com.example.dell.mytour.model.model_base.Comment;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Like;
import com.example.dell.mytour.model.model_base.Message;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.model.model_base.Tag;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.activity.PersonalPageActivity;
import com.example.dell.mytour.uis.activity.PersonalPostActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalPostAdapter extends RecyclerView.Adapter<PersonalPostAdapter.ViewHolder> {

    private OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private ArrayList<PersonalPost> personalPosts;
    private Context context;
    private String currentUserid;

    //----------------------------------------------------------------------------------------------
    private String user_post;

    public String getUser_post() {
        return user_post;
    }

    public void setUser_post(String user_post) {
        this.user_post = user_post;
    }

    //----------------------------------------------------------------------------------------------

    public PersonalPostAdapter(ArrayList<PersonalPost> personalPosts, Context context, String currentUserid) {
        this.personalPosts = personalPosts;
        this.context = context;
        this.currentUserid = currentUserid;
    }

    public PersonalPostAdapter(ArrayList<PersonalPost> personalPosts, Context context, String currentUserid, OnItemClickListener listener) {
        this.personalPosts = personalPosts;
        this.context = context;
        this.currentUserid = currentUserid;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_personal_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        String Uid = null;
        final PersonalPost personalPost = personalPosts.get(position);

        final Users user = personalPost.getUsers();


        final Post post = personalPost.getPost();

        if (user != null) {
            Uid = user.getUser_id();
            holder.personal_txt_name_user.setText(user.getUser_full_name());
            if (!TextUtils.isEmpty(user.getUser_avatar()) && user.getUser_avatar().startsWith("http") && context != null) {
                Glide.with(context.getApplicationContext()).load(user.getUser_avatar()).into(holder.personal_img_user);
            }

        }


        holder.personal_txt_time_post.setText(post.getPost_date());
        holder.personal_txt_status_user.setText("cùng với");
        holder.personal_txt_content.setText(post.getPost_content());
        holder.personal_txt_status_user.setText("");
        holder.personal_txt_note_friend.setText("");
        if (!TextUtils.isEmpty(post.getPost_location_map())) {
            holder.personal_txt_location.setText(post.getPost_location_map().substring(0,15)+"...");
        } else {
            holder.personal_txt_location.setVisibility(View.GONE);
        }

//        holder.personal_txt_location.setText();


        //Menu Click
        final String finalUid = Uid;
        if (!currentUserid.equals("")) {
            holder.personal_btn_menu_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.personal_btn_menu_post);

                    popupMenu.inflate(R.menu.post_popup_menu);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.post_popup_delete:
                                    if (currentUserid.equals(finalUid)) {
                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                        final DatabaseReference databaseReference = firebaseDatabase.getReference();
                                        databaseReference.child("Comments").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot commentSnap : dataSnapshot.getChildren()) {
                                                    Comment comment = commentSnap.getValue(Comment.class);
                                                    if (comment.getPost_id().equals(post.getPost_id())) {
                                                        String key = commentSnap.getKey();
                                                        databaseReference.child("Comments").child(key).removeValue();
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        databaseReference.child("Image").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot imageSnap : dataSnapshot.getChildren()) {
                                                    Image image = imageSnap.getValue(Image.class);
                                                    if (image.getPost_id().equals(post.getPost_id())) {
                                                        String key = imageSnap.getKey();
                                                        databaseReference.child("Image").child(key).removeValue();
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        databaseReference.child("Like").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot likeSnap : dataSnapshot.getChildren()) {
                                                    Like like = likeSnap.getValue(Like.class);
                                                    if (like.getPost_id().equals(post.getPost_id())) {
                                                        String key = likeSnap.getKey();
                                                        databaseReference.child("Like").child(key).removeValue();
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        final DatabaseReference postReference = firebaseDatabase.getReference("Post");

                                        postReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot dataPost : dataSnapshot.getChildren()) {
                                                    Post post1 = dataPost.getValue(Post.class);
                                                    if (post1.getPost_id().equals(post.getPost_id())) {
                                                        String key = dataPost.getKey();
                                                        postReference.child(key).removeValue();
                                                        Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();
                                                        personalPosts.remove(position);
                                                        notifyDataSetChanged();
                                                        break;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    } else {
                                        Toast.makeText(context, "Khong the xoa bai viet", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case R.id.post_popup_report:
                                    Toast.makeText(context, "Reported", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
            });
        } else {
            holder.personal_btn_menu_post.setVisibility(View.GONE);
        }

        //======Name friend Click======
        if (!currentUserid.equals("")) {
            holder.personal_txt_name_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent_personal_page = new Intent(context, PersonalPageActivity.class);
                    String Uid = user.getUser_id();
                    intent_personal_page.putExtra("UserId", Uid);
                    context.startActivity(intent_personal_page);
                }
            });
        } else {
            holder.personal_txt_name_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.item_personal_suggest.setVisibility(View.VISIBLE);
                }
            });

        }


        //----------------------Like Click-------------------------
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference userReference = firebaseDatabase.getReference("Like");
        final DatabaseReference userReference2 = firebaseDatabase.getReference("Like");

        if (!currentUserid.equals("")) {
            //=====Check Liked==========
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int like_count = 0;
                    Boolean a = false;
                    for (DataSnapshot dataLike : dataSnapshot.getChildren()) {

                        Like like = dataLike.getValue(Like.class);
                        if (like.getPost_id().equals(post.getPost_id()) && like.getFriend_id().equals(currentUserid)) {
                            a = true;
                            holder.personal_cb_unlike_item_personal_post.setChecked(a);
                        }
                        if (like.getPost_id().equals(post.getPost_id())) {
                            like_count++;
                        }
                    }
                    holder.personal_txt_count_like.setText(like_count + " người thích");
                    holder.personal_cb_unlike_item_personal_post.setChecked(a);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //========End Check Liked==========

            //===========LikeClick=====
            holder.personal_cb_unlike_item_personal_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //heart.toggleLike();
                    if (holder.personal_cb_unlike_item_personal_post.isChecked()) {
                        Date dNow = new Date();
                        SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss - dd/MM/yyyy");
                        String key = userReference.child("Like").push().getKey();
                        Like like = new Like(ft.format(dNow), key, true, post.getPost_id(), currentUserid);
                        userReference.child(key).setValue(like);

                        // phan set du leu cho object thong baoupDataToMessage(1);
                        getPostInfo(post.getPost_id());
                        if(getUser_post() != null){
                            upDataToMessage(1, post.getPost_id());
                        }


                    } else {
                        holder.personal_cb_unlike_item_personal_post.setChecked(false);
                        userReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot dataLike : dataSnapshot.getChildren()) {
                                    Like like = dataLike.getValue(Like.class);
                                    if (like.getPost_id().equals(post.getPost_id()) && like.getFriend_id().equals(currentUserid)) {
                                        String key2 = dataLike.getKey();
                                        userReference.child(key2).removeValue();

                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        deleteMessage(post.getPost_id());
                    }
                }

            });
        } else {

            holder.personal_cb_unlike_item_personal_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.item_personal_suggest.setVisibility(View.VISIBLE);
                }
            });
        }
        //---------------------end LikeClick-------------------------------





        //---------------------------------------Comment Click----------------------

        if (!currentUserid.equals("")) {
            holder.personal_btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PersonalPostActivity.class);
                    intent.putExtra("PostId", post.getPost_id());
                    context.startActivity(intent);
                }
            });
        } else {
            holder.personal_btn_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.item_personal_suggest.setVisibility(View.VISIBLE);
                }
            });
        }


//        //============Image Pager==================
        final ArrayList<String> imgUrls = new ArrayList<>();
        final ImagePagerAdapter imgAdapter = new ImagePagerAdapter(imgUrls, context, ImageView.ScaleType.CENTER_CROP);
        holder.item_personal_post_img_viewpager.setAdapter(imgAdapter);

        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
        DatabaseReference imgReference = firebaseDatabase1.getReference("Image");
        imgReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Image image = dataSnapshot1.getValue(Image.class);
                    if (image.getPost_id().equals(post.getPost_id())){
                        imgUrls.add(image.getImg_link().toString());
                    }
                }

                if (imgUrls.size()!=0){
                    imgAdapter.notifyDataSetChanged();
                }
                else {
                    holder.item_personal_post_img_relative.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.item_personal_post_img_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int img_position, float positionOffset, int positionOffsetPixels) {
                holder.item_personal_post_img_position.setText((img_position + 1) + "/" + imgUrls.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //------------------end LoadImage--------------------------

        //=====================LoadFirstCommentAndCount===================


        final Comment comment = personalPost.getComment();

        if (comment != null) {
            DatabaseReference countReference = firebaseDatabase.getReference("Comments");
            //count comment
            countReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int cmm_count = 0;
                    for (DataSnapshot count : dataSnapshot.getChildren()) {
                        Comment comment1 = count.getValue(Comment.class);
                        if (comment1.getPost_id().equals(post.getPost_id())) {
                            cmm_count++;

                        }
                    }
                    holder.personal_txt_count_comment.setText(cmm_count + " bình luận");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            holder.personal_txt_comment.setText(comment.getCmm_content());
            holder.personal_txt_time_comment.setText(comment.getCmm_date());
            DatabaseReference commentReference = firebaseDatabase.getReference("Users");
            //ava comment
            commentReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot cmmUser : dataSnapshot.getChildren()) {
                        Users cmmU = cmmUser.getValue(Users.class);
                        if (cmmU.getUser_id().equals(comment.getFriend_id())) {
                            if (!TextUtils.isEmpty(cmmU.getUser_avatar()) && cmmU.getUser_avatar().startsWith("http") && context != null) {
                                Glide.with(context.getApplicationContext()).load(cmmU.getUser_avatar()).into(holder.personal_img_friend);
                            }

                            holder.personal_txt_name_friend.setText(cmmU.getUser_full_name());
                            holder.personal_relative_firstcmm.setVisibility(View.VISIBLE);

                        }

                        if (!currentUserid.equals("")) {
                            if (cmmU.getUser_id().equals(currentUserid)) {
                                if (!TextUtils.isEmpty(cmmU.getUser_avatar()) && cmmU.getUser_avatar().startsWith("http") && context != null) {
                                    Glide.with(context.getApplicationContext()).load(cmmU.getUser_avatar()).into(holder.personal_img_comment_ava);
                                }
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {
            holder.personal_relative_firstcmm.setVisibility(View.GONE);
            holder.personal_txt_count_comment.setText("0 bình luận");
        }
        //==============endFirstCommentAndCount===========================

        //==============CommentToPost============================
        if (!currentUserid.equals("")) {
            final DatabaseReference DataReference = firebaseDatabase.getReference();
            DataReference.child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot addCmmUser : dataSnapshot.getChildren()) {
                        Users cmmU = addCmmUser.getValue(Users.class);

                        if (cmmU.getUser_id().equals(currentUserid)) {
                            if (!cmmU.getUser_avatar().equals("") && cmmU.getUser_avatar().startsWith("http") && context != null) {
                                Glide.with(context.getApplicationContext()).load(cmmU.getUser_avatar()).into(holder.personal_img_comment_ava);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            holder.personal_edt_comment_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PersonalPostActivity.class);
                    intent.putExtra("PostId", post.getPost_id());
                    context.startActivity(intent);

                }
            });

        }

        //============getTag================

        final ArrayList<Tag> listTag = new ArrayList<>();
        DatabaseReference tagReference = firebaseDatabase.getReference("Tag");
        tagReference.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot tagSnap : dataSnapshot.getChildren()) {
                    Tag tag = tagSnap.getValue(Tag.class);
                    if (tag.getPost_id().equals(post.getPost_id())) {
                        listTag.add(tag);
                    }
                }
                if (listTag.size() == 0) {
                    holder.personal_txt_status_user.setText("");
                    holder.personal_txt_note_friend.setText("");
                } else {
                    holder.personal_txt_status_user.setText("cùng với ");
                    holder.personal_txt_note_friend.setText(listTag.size() + " người khác");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.personal_txt_note_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, ""+listTag.get(0).getFriend_id(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return personalPosts.size();
    }



    //--------------------------------------------------------------------------------------------------
    private String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        return "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND)
                + " - " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }


    //----------------------------------------------------------------------------------------------
    /*phuong thuc lay id cua nguoi dang bai*/
    private  void getPostInfo(final  String post_id){
        FirebaseDatabase firebase_database = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebase_database.getReference("Post");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if(post_id.equals(post.getPost_id())){
                        setUser_post(post.getUser_id());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*phuong thuc lay id cua nguoi dang bai*/



    //----------------------------------------------------------------------------------------------
    /*phuong thuc up du lieu len object Message*/
    private void upDataToMessage(int type, String post_id) {
        FirebaseDatabase firebase_database = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebase_database.getReference();

        String key = reference.child("Message").push().getKey();

        Message message = new Message(
                key,
                getDateTime(),
                type,
                false,
                post_id,
                getUser_post(),
                currentUserid
        );

        Map<String, Object> message_values = message.toMap();
        Map<String, Object> child_update = new HashMap<>();
        child_update.put("/Message/" + key, message_values);
        reference.updateChildren(child_update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("Status", "Thanh cong");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Status", "That bai");
            }
        });
    }
    /*phuong thuc up du lieu len object Message*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc delete message khi bam vao nut dislike*/
    private void deleteMessage(final String post_id){
        FirebaseDatabase firebase_database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebase_database.getReference("Message");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);
                    if( post_id.equals(message.getPost_id()) && currentUserid.equals(message.getUser_id())){
                        reference.child(snapshot.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*phuong thuc delete message khi bam vao nut dislike*/

    public class ViewHolder extends RecyclerView.ViewHolder {


        CircleImageView personal_img_user, personal_img_friend, personal_img_comment_ava;
        TextView personal_txt_name_user;
        TextView personal_txt_status_user;
        TextView personal_txt_note_friend;
        TextView personal_txt_time_post;
        TextView personal_txt_location;
        TextView personal_txt_name_friend;
        TextView personal_txt_content;
        TextView personal_txt_comment;
        TextView personal_txt_time_comment;
        TextView personal_txt_count_like;
        TextView personal_txt_count_comment;
        TextView item_personal_suggest;
        Button personal_btn_menu_post;
        Button personal_btn_comment;
        ViewPager item_personal_post_img_viewpager;
        RelativeLayout personal_relative_firstcmm;
        CheckBox personal_cb_unlike_item_personal_post;
        EditText personal_edt_comment_text;
        Button personal_btn_comment_clear_text, personal_btn_send_comment;
        RelativeLayout item_personal_post_img_relative;
        TextView item_personal_post_img_position;

        public ViewHolder(final View itemView) {
            super(itemView);

            personal_img_user = itemView.findViewById(R.id.personal_img_user);
            personal_img_friend = itemView.findViewById(R.id.personal_img_friend);
            personal_btn_comment = itemView.findViewById(R.id.personal_btn_comment_item_personal_post);
            personal_btn_menu_post = itemView.findViewById(R.id.personal_btn_menu_item_personal_post);
            personal_txt_content = itemView.findViewById(R.id.personal_txt_content_item_personal_post);
            personal_txt_comment = itemView.findViewById(R.id.personal_txt_comment);
            personal_txt_count_like = itemView.findViewById(R.id.personal_txt_count_like_item_personal_post);
            personal_txt_count_comment = itemView.findViewById(R.id.personal_txt_count_comment_item_personal_post);
            personal_txt_location = itemView.findViewById(R.id.personal_txt_location_personal_post);
            personal_txt_name_friend = itemView.findViewById(R.id.personal_txt_name_friend);
            personal_txt_name_user = itemView.findViewById(R.id.personal_txt_name_user);
            personal_txt_status_user = itemView.findViewById(R.id.personal_txt_status_user);
            personal_txt_note_friend = itemView.findViewById(R.id.personal_txt_note_friend);
            personal_txt_time_post = itemView.findViewById(R.id.personal_txt_time_item_post_personal);
            personal_txt_time_comment = itemView.findViewById(R.id.personal_txt_time_comment);
            item_personal_post_img_viewpager = itemView.findViewById(R.id.item_personal_post_img_viewpager);
            personal_relative_firstcmm = itemView.findViewById(R.id.personal_relative_firstcmm);
            personal_cb_unlike_item_personal_post = itemView.findViewById(R.id.personal_cb_unlike_item_personal_post);
            item_personal_post_img_relative = itemView.findViewById(R.id.item_personal_post_img_relative);
            item_personal_post_img_position = itemView.findViewById(R.id.item_personal_post_img_postision);
            //comment
            personal_img_comment_ava = itemView.findViewById(R.id.personal_img_comment_ava);
            personal_edt_comment_text = itemView.findViewById(R.id.personal_edt_comment_text);
            personal_btn_comment_clear_text = itemView.findViewById(R.id.personal_btn_comment_clear_text);
            personal_btn_send_comment = itemView.findViewById(R.id.personal_btn_send_comment);
            item_personal_suggest = itemView.findViewById(R.id.item_personal_suggest);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
//

        }

    }


}

