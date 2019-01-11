package com.example.dell.mytour.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.LoginFilter;
import android.util.Log;
import android.widget.Toast;

import com.example.dell.mytour.R;
import com.example.dell.mytour.model.model_base.Comment;
import com.example.dell.mytour.model.model_base.Friend;
import com.example.dell.mytour.model.model_base.Like;
import com.example.dell.mytour.model.model_base.Message;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.activity.MainActivity;
import com.example.dell.mytour.uis.activity.PersonalPostActivity;
import com.example.dell.mytour.uis.activity.result.FriendListActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessagingService extends Service {

    // khai bao notification
    private NotificationManager notification_manager;
    private NotificationCompat.Builder builder;

    // khai bao cac bien lam viec voi fire base
    private FirebaseDatabase firebase_database;
    private DatabaseReference database_reference;
    private FirebaseUser firebase_user;
    private String user_id;

    // cac bien int, String thuong dung trong bai
    private String user_name;


    // cac phuong thuc khoi tao
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public MessagingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        firebase_database = FirebaseDatabase.getInstance();


        firebase_user = FirebaseAuth.getInstance().getCurrentUser();
        if (firebase_user != null) {
            postValueListener();
            messageValueListener();
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //----------------------------------------------------------------------------------------------
    /*phuong thuc lang nghe su thay doi cua object Post tren filebase*/
    private void postValueListener() {

        database_reference = firebase_database.getReference("Post");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long total_post = dataSnapshot.getChildrenCount();
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    i++;
                    if (total_post == i && !firebase_user.getUid().equals(post.getUser_id())) {
                        getDataFromFirebaseUser(post.getUser_id(), 3, "");
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*phuong thuc lang nghe su thay doi cua object Post tren filebase*/



    //----------------------------------------------------------------------------------------------
    /*phuong thuc lang nghe su kien tu firebase Message*/
    private void messageValueListener() {

        database_reference = firebase_database.getReference("Message");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long total_post = dataSnapshot.getChildrenCount();
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    i++;
                    if (total_post == i && firebase_user.getUid().equals(message.getFriend_id()) && !message.isMessage_status()) {
                        getDataFromFirebaseUser(message.getUser_id(), message.getMessage_type(), message.getPost_id());
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*phuong thuc lang nghe su kien tu firebase Message*/



    //----------------------------------------------------------------------------------------------
    /**/

    /**/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc lay du lieu tu object Users*/
    private void getDataFromFirebaseUser(final String user_id, final int type, final String post_id) {

        database_reference = firebase_database.getReference("Users");
        database_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);
                    if (user_id.equals(user.getUser_id())) {
                        if (type == 3) {
                            showNotification("Bài đăng mới từ " + user.getUser_full_name(), 1, "");
                        }
                        if (type == 2) {
                            showNotification("Lời mời kết bạn từ " + user.getUser_full_name(), 2, "");
                        }
                        if(type == 0){
                            showNotification(user.getUser_full_name() + " đã bình luận vào bài viết của bạn", 3, post_id);
                        }
                        if(type == 1){
                            showNotification(user.getUser_full_name() + " đã thích bài viết của bạn", 4, post_id);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*phuong thuc lay du lieu tu object Users*/


    // ----------------------------------------------------------------------------------------------
    /*phuong thuc show notification*/
    private void showNotification(String message_content, int type, String post_id) {
        notification_manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this, "default");

        // thiet lap thong tin
        builder.setSmallIcon(R.drawable.ic_local_post_office_black_24dp);
        builder.setContentTitle("MyTour");
        builder.setContentText(message_content);

        //neu muon thong bao co hinh nhu tin nhan
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setPriority(1); // set do uu tien

        // thet lap  action khi kich vao message


        Intent intent = null;
        if (type == 1 ) {
            intent = new Intent(MessagingService.this, MainActivity.class);
        }
        if(type == 2) {
            intent = new Intent(MessagingService.this, FriendListActivity.class);
        }

        if(type == 3 || type == 4){
            intent = new Intent(MessagingService.this, PersonalPostActivity.class);
            intent.putExtra("PostId", post_id);
        }

        // pending intent de lay thong tin tu ngoai vao
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        builder.setContentIntent(pendingIntent);
        notification_manager.notify(9999, builder.build());
    }
    /*phuong thuc show notification*/

}
