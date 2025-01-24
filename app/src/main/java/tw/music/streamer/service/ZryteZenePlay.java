package tw.music.streamer.service;

import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.app.Service;
import android.app.Activity;
import android.app.NotificationManager;
import android.media.MediaPlayer;
import android.media.AudioManager;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
//import androidx.media.session.MediaSessionCompat;

import android.support.v4.media.session.MediaSessionCompat;

import tw.music.streamer.notification.ZryteZeneNotification;
import tw.music.streamer.adaptor.ZryteZeneSongsManager;

public class ZryteZenePlay extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
	
	public static final String ACTION_BROADCAST = "tw.music.streamer.ACTION";
	public static final String ACTION_UPDATE = "tw.music.streamer.ACTION_UPDATE";
	
	public static final String CHANNEL_ID = "zrytezene_channel";
	public static final int NOTIFICATION_ID = 2;
	
	private BroadcastReceiver br;
	private MediaPlayer mp;
	private IntentFilter ief;
	private SharedPreferences sp;
	private String lm, act, csp, sn, sa, sc, sk, sap;
	private Intent ita;
	private Handler ha = new Handler();
	private boolean pd = false;
	private MediaSessionCompat msc;
	
	@Override
	public void onCreate() {
		super.onCreate();
		if (ief == null) initializePlayer();
	}
	
	@Override
	public int onStartCommand(Intent a, int b, int c) {
		if (ief == null) initializePlayer();
		if (a != null) onReceived(a);
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(br);
		if (mp!=null) {
			mp.release();
			mp = null;
		}
		if (msc!=null) {
			msc.release();
			msc = null;
		}
		super.onDestroy();
	}
	
	@Nullable
	@Override
	public IBinder onBind(Intent a) {
		return null;
	}
	
	private Runnable pr = new Runnable() {
		@Override
		public void run() {
			if (mp != null) {
				if (mp.isPlaying()) tellActivity("on-tick", mp.getCurrentPosition());
				ha.postDelayed(this, 500);
			}
		}
	};
	
	
	private void initializePlayer() {
		//startForeground(NOTIFICATION_ID, ZryteZeneNotification.setup(getApplicationContext()));
		sp = getSharedPreferences("teamdata", Activity.MODE_PRIVATE);
		lm = sp.getString("fvsAsc", "");
		msc = new MediaSessionCompat(getApplicationContext(), "ZryteZenePlay");
		mp = new MediaPlayer();
		br = new BroadcastReceiver() {
			@Override
			public void onReceive(Context a, Intent b) {
				if (b != null) onReceived(a,b);
			}
		};
		ief = new IntentFilter(ACTION_BROADCAST);
		registerReceiver(br, ief, Context.RECEIVER_NOT_EXPORTED);
		applyMediaListener();
		tellActivity("on-initialized");
	}
	
	@Override
	public void onPrepared(MediaPlayer a) {
		a.start();
		pd = true;
		ZryteZeneNotification.update(getApplicationContext(), true, msc, sn, sa, sc);
		tellActivity("on-prepared",a.getDuration());
		ha.post(pr);
	}
	
	@Override
	public void onBufferingUpdate(MediaPlayer a, int b) {
		tellActivity("on-bufferupdate",b);
	}
	
	@Override
	public void onCompletion(MediaPlayer a) {
		ZryteZeneNotification.update(getApplicationContext(), false, msc, sn, sa, sc);
		tellActivity("on-completion","1");
	}
	
	@Override
	public boolean onError(MediaPlayer a, int b, int c) {
		ZryteZeneNotification.update(getApplicationContext(), false, msc, sn, sa, sc);
		tellActivity("on-error", String.format("Error(%s%s)", b, c));
		return true;
	}
	
	private void onReceived(Intent a) {
		onReceived(getApplicationContext(), a);
	}
	
	private void onReceived(Context a, Intent b) {
		if (!b.hasExtra("action")) return;
		act = b.getStringExtra("action");
		if (act.equals("seek")) {
			seekSong(b);
		} else if (act.equals("play")) {
			playSong(b);
		} else if (act.equals("pause")) {
			pauseSong();
		} else if (act.equals("resume")) {
			resumeSong();
		} else if (act.equals("stop")) {
			stopSong();
		} else if (act.equals("update-sp")) {
			updateSP(b);
		} else if (act.equals("restart-song")) {
			restartSong();
		} else if (act.equals("reset")) {
			resetMedia();
		} else if (act.equals("request-media")) {
			requestMedia();
		}
	}
	
	private void applyMediaListener() {
		mp.setOnPreparedListener(this);
		mp.setOnBufferingUpdateListener(this);
		mp.setOnCompletionListener(this);
		mp.setOnErrorListener(this);
	}
	
	private void playSong(Intent a) {
		csp = a.getStringExtra("path");
		sn = a.getStringExtra("title");
		sa = a.getStringExtra("artist");
		sc = a.getStringExtra("cover");
		sk = a.getStringExtra("key");
		if (mp != null) {
			mp.reset();
		} else {
			mp = new MediaPlayer();
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			applyMediaListener();
		}
		try {
			sap = ZryteZeneSongsManager.check(getApplicationContext(), sk);
			mp.setDataSource(sap.equals("-") ? csp : sap);
			mp.prepareAsync();
			tellActivity("request-play");
		} catch (Exception e) {
			tellActivity("on-error", e.toString());
		}
		if (sap.equals("-")) ZryteZeneSongsManager.download(getApplicationContext(), csp, sk);
	}
	
	private void pauseSong() {
		if (mp==null) return;
		if (mp.isPlaying()) {
			mp.pause();
			ZryteZeneNotification.update(getApplicationContext(), false, msc, sn, sa, sc);
			tellActivity("request-pause");
		}
	}
	
	private void resumeSong() {
		if (mp==null) return;
		if (isPrepared() && !mp.isPlaying()) {
			mp.start();
			ZryteZeneNotification.update(getApplicationContext(), true, msc, sn, sa, sc);
			tellActivity("request-resume");
		}
	}
	
	private void stopSong() {
		if (mp==null) return;
		if (mp.isPlaying()) {
			mp.stop();
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			if (notificationManager != null) {
				notificationManager.cancel(NOTIFICATION_ID);
			}
			ha.removeCallbacks(pr);
			stopForeground(true);
			tellActivity("request-stop");
		}
	}
	
	private void seekSong(Intent a) {
		if (mp==null) return;
		if (isPrepared()) {
			int b = a.getIntExtra("req-data",0);
			if (b > mp.getDuration()) return;
			mp.seekTo(b);
			tellActivity("request-seek", b);
		} else {
			tellActivity("on-seekerror");
		}
	}
	
	private void restartSong() {
		if (mp==null) return;
		if (!isPrepared()) return;
		if (mp.isPlaying()) mp.stop();
		mp.seekTo(0);
		mp.start();
		tellActivity("request-restart");
	}
	
	private void resetMedia() {
		mp = new MediaPlayer();
		tellActivity("request-reset");
	}
	
	private void tellActivity(String a) {
		ita = new Intent(ACTION_UPDATE);
		ita.putExtra("update", a);
		sendBroadcast(ita);
	}
	
	private void tellActivity(String a, String b) {
		ita = new Intent(ACTION_UPDATE);
		ita.putExtra("update", a);
		ita.putExtra("data", b);
		sendBroadcast(ita);
	}
	
	private void tellActivity(String a, int b) {
		ita = new Intent(ACTION_UPDATE);
		ita.putExtra("update", a);
		ita.putExtra("data", b);
		sendBroadcast(ita);
	}
	
	private void updateSP(Intent a) {
		String b = a.getStringExtra("req-data");
		if (b.equals("loop")) {
			lm = sp.getString("fvsAsc", "");
		}
	}
	
	private void requestMedia() {
		ita = new Intent(ACTION_UPDATE);
		ita.putExtra("update", "on-reqmedia");
		ita.putExtra("status", mp == null ? 0 : mp.isPlaying() ? 1 : isPrepared() ? 2 : 0);
		if (mp != null && isPrepared()) {
			ita.putExtra("duration", mp.getDuration());
			ita.putExtra("currentDuration", mp.getCurrentPosition());
			ita.putExtra("key", sk);
		}
		sendBroadcast(ita);
	}
	
	private boolean isPrepared() {
		return pd;
	}
}