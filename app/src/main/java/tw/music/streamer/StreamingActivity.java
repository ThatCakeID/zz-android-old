package tw.music.streamer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import tw.music.streamer.adaptor.ZZSong;
import tw.music.streamer.adaptor.ZryteZeneAdaptor;
import tw.music.streamer.adapter.ZZSongAdapter;
import tw.music.streamer.adapter.ZZOnClickListener;
import tw.music.streamer.adapter.ZZRandomSongAdapter;
import tw.music.streamer.service.ZryteZenePlay;

public class StreamingActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference db_song, db_drsong;
    private StorageReference fs_music;
    private BroadcastReceiver zzreceiver;

    private RecyclerView rv_random_songs, rv_songs;
    private LinearLayoutManager lm1;
    private GridLayoutManager lm2;
    private TextView user_welcome, taptext1, taptext2, taptext3, taptext4, mp_title, mp_artist;
    private ImageView user_icon, tapicon1, tapicon2, tapicon3, mp_play, mp_icon, mp_close, bg_drop;
    private LinearLayout menu_bar, tapbar1, tapbar2, tapbar3, tapbar4, mp_base;
    private ProgressBar mp_bar;

    private ArrayList<ZZSong> zz_songs, zz_songs2;

    private ZryteZeneAdaptor zz;
    private ZZRandomSongAdapter ra_songs;
    private ZZSongAdapter ar_songs;
    private ZZOnClickListener zz_click1;

    private int openMenu = 0;

    @Override
    protected void onCreate(Bundle a) {
        super.onCreate(a);
        setContentView(R.layout.stream_base);
        initVariables(getApplicationContext());
        initFirebase();
        initLayout();
        initOnClick(getApplicationContext());
        initLogic(getApplicationContext());
        initFirebaseListener(getApplicationContext());
        initBackgroundServices(getApplicationContext());
    }

    private void initVariables(Context a) {
        zz_songs = new ArrayList<>();
        zz_songs2 = new ArrayList<>();
        zz_click1 = new ZZOnClickListener() {
            public void onItemClicked(int a) {
                playFromZZSongs(a);
            }
        };
        ra_songs = new ZZRandomSongAdapter(zz_songs2);
        ar_songs = new ZZSongAdapter(zz_songs, zz_click1);
        lm1 = new LinearLayoutManager(a, LinearLayoutManager.HORIZONTAL, false);
        lm2 = new GridLayoutManager(a, 2);
    }

    private void initFirebase() {
        auth = FirebaseAuth.getInstance();
        db_song = FirebaseDatabase.getInstance().getReference("zrytezene/songs");
        db_drsong = FirebaseDatabase.getInstance().getReference("zrytezene/daily-random-songs");
    }

    private void initFirebaseListener(final Context z) {
        db_song.limitToFirst(20).get().addOnCompleteListener(a -> {
            if (a.isSuccessful()) {
                DataSnapshot b = a.getResult();
                if (b.exists()) {
                    for (DataSnapshot c : b.getChildren()) {
                        zz_songs.add(new ZZSong(c));
                    }
                    ar_songs.notifyDataSetChanged();
                } else {
                    // songs empty
                }
            } else {
                // error: task.getException().getMessage()
            }
        });

        db_drsong.get().addOnCompleteListener(a -> {
            if (a.isSuccessful()) {
                DataSnapshot b = a.getResult();
                if (b.exists()) {
                    for (DataSnapshot c : b.getChildren()) {
                        zz_songs2.add(new ZZSong(c));
                    }
                    ra_songs.notifyDataSetChanged();
                }
            }
        });

        FirebaseDatabase.getInstance().getReference("profile/image/" + auth.getCurrentUser().getUid()).get().addOnCompleteListener(a -> {
            if (a.isSuccessful()) {
                DataSnapshot b = a.getResult();
                if (b.exists() && b.hasChild("url")) {
                    Glide.with(z).load(b.child("url").getValue(String.class)).diskCacheStrategy(DiskCacheStrategy.ALL).apply(RequestOptions.circleCropTransform()).into(user_icon);
                }
            }
        });

        FirebaseDatabase.getInstance().getReference("profile/text/" + auth.getCurrentUser().getUid()).get().addOnCompleteListener(a -> {
            if (a.isSuccessful()) {
                DataSnapshot b = a.getResult();
                if (b.exists() && b.hasChild("username")) {
                    user_welcome.setText("Hello, " + b.child("username").getValue(String.class) + "!");
                }
            }
        });
    }

    private void initLayout() {
        user_welcome = findViewById(R.id.profile_name);
        user_icon = findViewById(R.id.profile_icon);
        rv_random_songs = findViewById(R.id.random_music_container);
        rv_songs = findViewById(R.id.uploaded_music_container);
        bg_drop = findViewById(R.id.sbbg);
        menu_bar = findViewById(R.id.sb_bottom_menu_bar);
        mp_bar = findViewById(R.id.zzmp1_progress);
        mp_base = findViewById(R.id.zzmp1_base);
        mp_play = findViewById(R.id.zzmp1_play);
        mp_icon = findViewById(R.id.zzmp1_icon);
        mp_close = findViewById(R.id.zzmp1_stop);
        mp_title = findViewById(R.id.zzmp1_title);
        mp_artist = findViewById(R.id.zzmp1_artist);
        tapbar1 = findViewById(R.id.sbmb1);
        tapbar2 = findViewById(R.id.sbmb2);
        tapbar3 = findViewById(R.id.sbmb3);
        tapbar4 = findViewById(R.id.sbmb4);
        tapicon1 = findViewById(R.id.sbmi1);
        tapicon2 = findViewById(R.id.sbmi2);
        tapicon3 = findViewById(R.id.sbmi3);
        taptext1 = findViewById(R.id.sbmt1);
        taptext2 = findViewById(R.id.sbmt2);
        taptext3 = findViewById(R.id.sbmt3);
        taptext4 = findViewById(R.id.sbmt4);
        mp_base.setVisibility(View.GONE);
    }

    private void initOnClick(final Context a) {
        tapbar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                openMenuBar(1,true);
            }
        });
        tapbar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                openMenuBar(2,true);
            }
        });
        tapbar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                openMenuBar(3,true);
            }
        });
        tapbar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                openMenuBar(4,true);
            }
        });
        mp_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                if (zz.isPlaying()) {
                    zz.requestAction("pause");
                } else {
                    zz.requestAction("resume");
                }
            }
        });
        mp_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                mp_base.setVisibility(View.GONE);
                bg_drop.setVisibility(View.GONE);
                Intent c = new Intent(getApplicationContext(), ZryteZenePlay.class);
                stopService(c);
                zz = new ZryteZeneAdaptor(getApplicationContext());
            }
        });
    }

    private void initLogic(final Context a) {
        scaleLower(tapicon1);
        scaleLower(tapicon2);
        scaleLower(tapicon3);
        scaleLower(user_icon);
        openMenuBar(1,false);
        getWindow().setStatusBarColor(0xFF000000);
        menu_bar.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.argb(90,0,0,0),Color.argb(150,0,0,0)}));
		rv_random_songs.setLayoutManager(lm1);
        rv_random_songs.setAdapter(ra_songs);
        rv_songs.setLayoutManager(lm2);
        rv_songs.setAdapter(ar_songs);
    }

    private void initBackgroundServices(final Context a) {
        zz = new ZryteZeneAdaptor(a);
        zzreceiver = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
			    if (intent != null && intent.getAction() != null) {
				    if (intent.getAction().equals(ZryteZenePlay.ACTION_UPDATE)) {
					    String m = intent.getStringExtra("update");
					    if (m.equals("on-prepared")) {
                            zz.setPlaying(true);
                            zz.setCurrentDuration(0);
                            zz.setDuration(intent.getIntExtra("data",0)/1000);
                            mp_bar.setProgress(0);
                            mp_bar.setMax(zz.getDuration());
                        } else if (m.equals("on-reqmedia")) {
                            int b = intent.getIntExtra("status",0);
                            if (b > 0) {
                                if (b==1) mp_play.setImageResource(R.drawable.ic_pause_white);
                                zz.setPlaying(b == 1);
                                zz.setCurrentDuration(intent.getIntExtra("currentDuration",0)/1000);
                                zz.setDuration(intent.getIntExtra("duration",0)/1000);
                                mp_bar.setMax(zz.getDuration());
                                mp_bar.setProgress(zz.getCurrentDuration());
                                loadSongFromKey(intent.getStringExtra("key"));
                            }
                        } else if (m.equals("on-tick")) {
                            zz.setCurrentDuration(intent.getIntExtra("data",0));
                            mp_bar.setProgress(zz.getCurrentDuration()/1000);
                        } else if (m.equals("on-completion")) {
                            zz.setPlaying(false);
                            mp_play.setImageResource(R.drawable.ic_play_arrow_white);
                        } else if (m.equals("on-error")) {
                            zz.addError(intent.getStringExtra("data"));
                            mp_play.setImageResource(R.drawable.ic_play_arrow_white);
                        } else if (m.equals("on-seekerror")) {
                        } else if (m.equals("on-initialized")) {
                        } else if (m.equals("on-bufferupdate")) {
                            zz.setBufferingUpdate(intent.getIntExtra("data",0));
                        } else if (m.equals("request-play")) {
                        } else if (m.equals("request-pause")) {
                            zz.setPlaying(false);
                            mp_play.setImageResource(R.drawable.ic_play_arrow_white);
                        } else if (m.equals("request-resume")) {
                            zz.setPlaying(true);
                            mp_play.setImageResource(R.drawable.ic_pause_white);
                        } else if (m.equals("request-stop")) {
                            zz.setPlaying(false);
                            mp_play.setImageResource(R.drawable.ic_play_arrow_white);
                        } else if (m.equals("request-seek")) {
                            zz.setCurrentDuration(intent.getIntExtra("data",0)/1000);
                        } else if (m.equals("request-restart")) {
                            zz.setCurrentDuration(0);
                        } else if (m.equals("request-reset")) {
                        }
				    }
			    }
		    }
	    };
        IntentFilter filr = new IntentFilter(ZryteZenePlay.ACTION_UPDATE);
		registerReceiver(zzreceiver, filr, Context.RECEIVER_NOT_EXPORTED);
        zz.requestAction("request-media");
    }

    private void playFromZZSongs(int a) {
        mp_artist.setText(zz_songs.get(a).song_artist);
        mp_title.setText(zz_songs.get(a).song_name);
        mp_play.setImageResource(R.drawable.ic_pause_white);
        Glide.with(getApplicationContext()).load(zz_songs.get(a).url_icon).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new RoundedCorners(dip(5))).into(mp_icon);
        mp_base.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext()).load(zz_songs.get(a).url_cover).diskCacheStrategy(DiskCacheStrategy.ALL).into(bg_drop);
        mp_bar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(zz_songs.get(a).color1)));
        zz.play(zz_songs.get(a));
    }

    private void loadSongFromKey(String a) {
        FirebaseDatabase.getInstance().getReference("zrytezene/songs/" + a).get().addOnCompleteListener(b -> {
            if (b.isSuccessful()) {
                DataSnapshot c = b.getResult();
                if (c.exists()) {
                    bg_drop.setVisibility(View.VISIBLE);
                    mp_artist.setText(c.child("artist").getValue(String.class));
                    mp_title.setText(c.child("title").getValue(String.class));
                    Glide.with(getApplicationContext()).load(c.child("icon").getValue(String.class)).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new RoundedCorners(dip(5))).into(mp_icon);
                    mp_base.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext()).load(c.child("cover").getValue(String.class)).diskCacheStrategy(DiskCacheStrategy.ALL).into(bg_drop);
                    mp_bar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(c.child("color-bline").getValue(String.class))));
                }
            }
        });
    }

    private void openMenuBar(int a, boolean b) {
        if (a==openMenu) return;
        tapicon1.setColorFilter(0xFF757575, PorterDuff.Mode.MULTIPLY);
        tapicon2.setColorFilter(0xFF757575, PorterDuff.Mode.MULTIPLY);
        tapicon3.setColorFilter(0xFF757575, PorterDuff.Mode.MULTIPLY);
        taptext1.setTextColor(0xFF757575);
        taptext2.setTextColor(0xFF757575);
        taptext3.setTextColor(0xFF757575);
        taptext4.setTextColor(0xFF757575);
        if (b) {
            if (openMenu==1) animateScaleDown(tapicon1);
            if (openMenu==2) animateScaleDown(tapicon2);
            if (openMenu==3) animateScaleDown(tapicon3);
            if (openMenu==4) animateScaleDown(user_icon);
        };
        if (a == 1) {
            tapicon1.clearColorFilter();
            taptext1.setTextColor(0xFFFFFFFF);
            if (b) {
                animateScaleUp(tapicon1);
            } else {
                scaleBigger(tapicon1);
            }
        } else if (a == 2) {
            tapicon2.clearColorFilter();
            taptext2.setTextColor(0xFFFFFFFF);
            if (b) {
                animateScaleUp(tapicon2);
            } else {
                scaleBigger(tapicon2);
            }
        } else if (a == 3) {
            tapicon3.clearColorFilter();
            taptext3.setTextColor(0xFFFFFFFF);
            if (b) {
                animateScaleUp(tapicon3);
            } else {
                scaleBigger(tapicon3);
            }
        } else if (a == 4) {
            taptext4.setTextColor(0xFFFFFFFF);
            if (b) {
                animateScaleUp(user_icon);
            } else {
                scaleBigger(user_icon);
            }
        };
        openMenu = a;
    }

    private void scaleLower(View a) {
        a.setScaleX(0.8f);
        a.setScaleY(0.8f);
    }

    private void scaleBigger(View a) {
        a.setScaleX(1f);
        a.setScaleY(1f);
    }

    private void animateScaleUp(View a) {
        a.animate().setDuration(300).scaleX(1f).scaleY(1f);
    }

    private void animateScaleDown(View a) {
        a.animate().setDuration(300).scaleX(0.8f).scaleY(0.8f);
    }

    private int dip(int a) {
        return (int) (a * getApplicationContext().getResources().getDisplayMetrics().density);
    }

}