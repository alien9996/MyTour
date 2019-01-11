package com.example.dell.mytour.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.dell.mytour.R;
import com.example.dell.mytour.uis.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingFCM extends FirebaseMessagingService {

    private String TAG = "Message:";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        // get notification content
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, remoteMessage.getNotification().getBody());
            //Log.e(TAG, remoteMessage.getNotification().getTitle());
            sendNotification(remoteMessage.getNotification().getBody());
        }

        // get data content

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, remoteMessage.getData() + "");
            Log.e(TAG, remoteMessage.getData().get("k1") + "");
        }
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/*Request code*/, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); /* Lay ra am thanh thong bao mac dinh cua may*/
        NotificationCompat.Builder notificationBuider = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_local_post_office_black_24dp)
                .setContentTitle("MyTour thông báo")
                .setContentText(messageBody)
                .setAutoCancel(true)/*Thuoc tinh cho phet nguoi dung vuot sang de xoa thong bao*/
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0/*ID of notification*/, notificationBuider.build());


    }

}
