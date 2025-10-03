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

import tw.music.streamer.loader.ZryteZeneImageLoader;

import tw.music.streamer.receiver.ZryteZeneBroadcastReceiver;
import tw.music.streamer.service.ZryteZenePlay;
import tw.music.streamer.StreamingActivity;

public class ZryteZeneNotification {
	
	public static Notification setup(Context a) {
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

	public static void update(Context a, boolean b, MediaSessionCompat c, String d, String e, String f, NotificationManager g) {
		if (f.equals("-")) {
			updateWithMedia(a,b,c,d,e,null,g);
		} else {
			ZryteZeneImageLoader.getInstance(a).load(f, bitmap -> {
				if (bitmap!=null) updateWithMedia(a,b,c,d,e,bitmap,g);
			});
		}
	}

	public static void updateWithMedia(Context a, boolean b, MediaSessionCompat c, String d, String e, Bitmap f, NotificationManager g) {
    	Intent playPauseIntent = new Intent(a, ZryteZeneBroadcastReceiver.class).setAction(b ? ZryteZeneBroadcastReceiver.PAUSE : ZryteZeneBroadcastReceiver.PLAY);
    	Intent previousIntent = new Intent(a, ZryteZeneBroadcastReceiver.class).setAction(ZryteZeneBroadcastReceiver.PREVIOUS);
    	Intent nextIntent = new Intent(a, ZryteZeneBroadcastReceiver.class).setAction(ZryteZeneBroadcastReceiver.SKIP);
		PendingIntent playPausePendingIntent = PendingIntent.getBroadcast(a, 0, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
		PendingIntent previousPendingIntent = PendingIntent.getBroadcast(a, 0, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    	PendingIntent nextPendingIntent = PendingIntent.getBroadcast(a, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

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
        if (g != null) g.notify(ZryteZenePlay.NOTIFICATION_ID, nf);
	}
	
}
