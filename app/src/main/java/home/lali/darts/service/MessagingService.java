package home.lali.darts.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import home.lali.darts.LiveMatchActivity;
import home.lali.darts.R;

public class MessagingService extends FirebaseMessagingService {

    private Uri notiSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();

            createNotification(data);
        }
    }

    private void createNotification(Map<String, String> data) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Intent resIntent = new Intent(this, LiveMatchActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(resIntent);
        PendingIntent resPenIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setSmallIcon(R.drawable.launcher)
                .setContentTitle(data.get("title"))
                .setContentText(data.get("body"))
                .setSound(notiSound)
                .setVibrate(new long[] {100, 200, 300, 200, 100})
                .setAutoCancel(true)
                .setContentIntent(resPenIntent);

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}