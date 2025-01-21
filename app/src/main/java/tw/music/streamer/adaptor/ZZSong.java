package tw.music.streamer.adaptor;

import com.google.firebase.database.DataSnapshot;

public class ZZSong {

    public static final String PATH = "https://firebasestorage.googleapis.com/v0/b/teammusic-tw.appspot.com/o/zrytezene%2Fsongs%2F";

    public String key, url_song, url_icon, url_cover, uploader_uid, uploader_name, song_name, song_artist;
    public boolean playing;

    public ZZSong(DataSnapshot a) {
        playing = false;
        key = a.getKey();
        url_song = a.child("song").getValue(String.class);
        url_icon = a.child("icon").getValue(String.class);
        url_cover = a.child("cover").getValue(String.class);
        uploader_uid = a.child("uid").getValue(String.class);
        uploader_name = a.child("uploader").getValue(String.class);
        song_name = a.child("title").getValue(String.class);
        song_artist = a.child("artist").getValue(String.class);
    }
}