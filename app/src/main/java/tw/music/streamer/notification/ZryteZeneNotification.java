package tw.music.streamer.notification;

import tw.music.streamer.R;

import android.os.Build;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media.app.NotificationCompat.MediaStyle;
//import androidx.media.session.MediaSessionCompat;

import android.support.v4.media.session.MediaSessionCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import tw.music.streamer.receiver.ZryteZeneBroadcastReceiver;
import tw.music.streamer.service.ZryteZenePlay;
import tw.music.streamer.StreamingActivity;

public class ZryteZeneNotification {
	
	public static Notification setup(Context a) {
		NotificationChannel ch = new NotificationChannel(
		ZryteZenePlay.CHANNEL_ID,
		"ZryteZene Player",
		NotificationManager.IMPORTANCE_LOW
		);
		ch.setSound(null, null);
		ch.enableLights(false);
		ch.enableVibration(false);
		NotificationManager mr = a.getSystemService(NotificationManager.class);
		if (mr != null) {
			mr.createNotificationChannel(ch);
		}

		Intent openAppIntent = new Intent(a, StreamingActivity.class);
    	PendingIntent openAppPendingIntent = PendingIntent.getActivity(a, 0, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		
		Notification notification = new Notification.Builder(a, ZryteZenePlay.CHANNEL_ID)
		.setContentTitle("ZryteZene")
		.setContentText("Idle...")
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentIntent(openAppPendingIntent)
		.setOnlyAlertOnce(true)
		.build();
		return notification;
	}

	public static void update(Context a, boolean b, MediaSessionCompat c, String d, String e, String f) {
		if (f.equals("-")) {
			updateWithMedia(a,b,c,d,e,null);
		} else {
			Glide.with(a)
        		.asBitmap()
        		.load(f)
        		.into(new CustomTarget<Bitmap>() {
            		@Override
            		public void onResourceReady(@NonNull Bitmap g, @NonNull Transition<? super Bitmap> h) {
                		updateWithMedia(a,b,c,d,e,g);
            		}
            		@Override
            		public void onLoadCleared(@Nullable Drawable i) {
            		}
        	});
		}
	}

	public static void updateWithMedia(Context a, boolean b, MediaSessionCompat c, String d, String e, Bitmap f) {
        NotificationChannel channel = new NotificationChannel(ZryteZenePlay.CHANNEL_ID, "ZryteZene Player", NotificationManager.IMPORTANCE_LOW);
        NotificationManager manager = a.getSystemService(NotificationManager.class);
        if (manager != null) {
           	manager.createNotificationChannel(channel);
        }

    	Intent playPauseIntent = new Intent(a, ZryteZeneBroadcastReceiver.class).setAction(b ? ZryteZeneBroadcastReceiver.PAUSE : ZryteZeneBroadcastReceiver.PLAY);
    	Intent previousIntent = new Intent(a, ZryteZeneBroadcastReceiver.class).setAction(ZryteZeneBroadcastReceiver.PREVIOUS);
    	Intent nextIntent = new Intent(a, ZryteZeneBroadcastReceiver.class).setAction(ZryteZeneBroadcastReceiver.SKIP);
		PendingIntent playPausePendingIntent = PendingIntent.getService(a, 0, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		PendingIntent previousPendingIntent = PendingIntent.getService(a, 0, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    	PendingIntent nextPendingIntent = PendingIntent.getService(a, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

		Notification nf;
		if (f == null) {
    		nf = new NotificationCompat.Builder(a, ZryteZenePlay.CHANNEL_ID)
        	.setContentTitle(d)
        	.setContentText(e)
        	.setSmallIcon(R.drawable.ic_launcher)
        	.setStyle(new MediaStyle()
            	.setMediaSession(c.getSessionToken())
            	.setShowActionsInCompactView(0, 1, 2))
        	.addAction(android.R.drawable.ic_media_previous, "Previous", previousPendingIntent)
        	.addAction(b ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play, "Play/Pause", playPausePendingIntent)
        	.addAction(android.R.drawable.ic_media_next, "Next", nextPendingIntent)
			.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        	.setPriority(NotificationCompat.PRIORITY_LOW)
        	.setOngoing(true)
        	.build();
		} else {
			nf = new NotificationCompat.Builder(a, ZryteZenePlay.CHANNEL_ID)
        	.setContentTitle(d)
        	.setContentText(e)
        	.setSmallIcon(R.drawable.ic_launcher)
			.setLargeIcon(f)
        	.setStyle(new MediaStyle()
            	.setMediaSession(c.getSessionToken())
            	.setShowActionsInCompactView(0, 1, 2))
        	.addAction(android.R.drawable.ic_media_previous, "Previous", previousPendingIntent)
        	.addAction(b ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play, "Play/Pause", playPausePendingIntent)
        	.addAction(android.R.drawable.ic_media_next, "Next", nextPendingIntent)
			.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        	.setPriority(NotificationCompat.PRIORITY_LOW)
        	.setOngoing(true)
        	.build();
		}
		NotificationManager m = (NotificationManager) a.getSystemService(Context.NOTIFICATION_SERVICE);
        if (m != null) {
            m.notify(ZryteZenePlay.NOTIFICATION_ID, nf);
        }
	}
	
}
