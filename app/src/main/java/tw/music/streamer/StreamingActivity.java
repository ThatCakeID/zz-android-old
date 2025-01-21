package tw.music.streamer;

import android.content.Context;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import tw.music.streamer.adaptor.ZZSong;
import tw.music.streamer.adaptor.ZryteZeneAdaptor;
import tw.music.streamer.adapter.ZZSongAdapter;
import tw.music.streamer.service.ZryteZenePlay;

public class StreamingActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference db_song;
    private StorageReference fs_quota;
    private StorageReference fs_music;
    private StorageReference fs_icon;
    private StorageReference fs_cover;

    private ZryteZeneAdaptor zz;

    private RecyclerView rv_random_songs;
    private RecyclerView rv_songs;
    private LinearLayoutManager lm1;
    private GridLayoutManager lm2;
    private TextView user_welcome;
    private ImageView user_icon;

    private ArrayList<ZZSong> zz_songs;

    private ZZSongAdapter ar_songs;

    @Override
    protected void onCreate(Bundle a) {
        super.onCreate(a);
        setContentView(R.layout.stream_base);
        initVariables(getApplicationContext());
        initFirebase();
        initLayout();
        initLogic(getApplicationContext());
        initFirebaseListener(getApplicationContext());
    }

    private void initVariables(Context a) {
        zz_songs = new ArrayList<>();
        ar_songs = new ZZSongAdapter(zz_songs);
        lm1 = new LinearLayoutManager(a, LinearLayoutManager.HORIZONTAL, false);
        lm2 = new GridLayoutManager(a, 2);
    }

    private void initFirebase() {
        auth = FirebaseAuth.getInstance();
        db_song = FirebaseDatabase.getInstance().getReference("zrytezene/songs");
        //db_admin = FirebaseDatabase.getInstance().getReference("profile/admins");
        //db_comment = FirebaseDatabase.getInstance().getReference("zrytezene/comments");
        //db_profile = FirebaseDatabase.getInstance().getReference("profile/text");
        //db_like = FirebaseDatabase.getInstance().getReference("zrytezene/likes");
        //db_profileimg = FirebaseDatabase.getInstance().getReference("profile/image");
        fs_quota = FirebaseStorage.getInstance().getReference("/");
        fs_music = FirebaseStorage.getInstance().getReference("zrytezene/songs");
        fs_icon = FirebaseStorage.getInstance().getReference("zrytezene/icons");
        fs_cover = FirebaseStorage.getInstance().getReference("zrytezene/covers");
    }

    private void initFirebaseListener(final Context z) {
        db_song.limitToFirst(20).get().addOnCompleteListener(a -> {
            if (a.isSuccessful()) {
                DataSnapshot b = a.getResult();
                if (b.exists()) {
                    //String musicData = b.getValue(String.class);
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

        FirebaseDatabase.getInstance().getReference("profile/image/" + auth.getCurrentUser().getUid()).get().addOnCompleteListener(a -> {
            if (a.isSuccessful()) {
                DataSnapshot b = a.getResult();
                if (b.exists() && b.hasChild("url")) {
                    Glide.with(z).load(b.child("url").getValue(String.class)).into(user_icon);
                } else {
                    // error: photo profile not found
                }
            } else {
                // error: task.getException().getMessage()
            }
        });

        FirebaseDatabase.getInstance().getReference("profile/text/" + auth.getCurrentUser().getUid()).get().addOnCompleteListener(a -> {
            if (a.isSuccessful()) {
                DataSnapshot b = a.getResult();
                if (b.exists() && b.hasChild("username")) {
                    user_welcome.setText("Hello, " + b.child("username").getValue(String.class) + "!");
                } else {
                    // error: name not found
                }
            } else {
                // error: task.getException().getMessage()
            }
        });
    }

    private void initLayout() {
        user_welcome = findViewById(R.id.profile_name);
        user_icon = findViewById(R.id.profile_icon);
        rv_random_songs = findViewById(R.id.random_music_container);
        rv_songs = findViewById(R.id.uploaded_music_container);
    }

    private void initLogic(Context a) {
		rv_random_songs.setLayoutManager(lm1);
        rv_songs.setLayoutManager(lm2);
        rv_songs.setAdapter(ar_songs);
    }

}