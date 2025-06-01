package com.example.admin.Notify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.admin.Model.OrderModel;
import com.example.admin.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;
import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        Random random=new Random();
        int number=random.nextInt();
        String address=remoteMessage.getData().get("Address");
        String prodname=remoteMessage.getData().get("Prodname");
        String username=remoteMessage.getData().get("Username");
        String date=remoteMessage.getData().get("Date");
        String time=remoteMessage.getData().get("Time");
        String orderkey=remoteMessage.getData().get("OrderKey");
        String prodkey=remoteMessage.getData().get("ProdKey");
        String image=remoteMessage.getData().get("Image");
        String userkey=remoteMessage.getData().get("UserKey");
        String qty=remoteMessage.getData().get("Qty");
        String amount=remoteMessage.getData().get("Amount");
//        title = remoteMessage.getData().get("Title");
//        message = remoteMessage.getData().get("Message");
//        imageurl=remoteMessage.getData().get("Extra");
        Bitmap bitmap=getBitmapFromURL(image);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_image)
                        .setContentTitle("A new Order has been placed for "+prodname)
                        .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Delivery to : "+address+"\nAmount Recieved : "+amount+" Rs\nOrder Quantity : "+qty+"\nOrdered By : "+username))
                        .setLargeIcon(bitmap)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("0");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "0",
                    "My App",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        notificationManager.notify(number, builder.build());
    }
    public static Bitmap getBitmapFromURL(String src) {
        /*try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }*/
        try {
            URL url = new URL(src);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;

        } catch(IOException e) {
            System.out.println(e);
            return null;
        }
    }
}
