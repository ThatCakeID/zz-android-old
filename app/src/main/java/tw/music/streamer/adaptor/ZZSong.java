package tw.music.streamer.adaptor;

import com.google.firebase.database.DataSnapshot;

public class ZZSong {

    public static final String PATH = "https://firebasestorage.googleapis.com/v0/b/teammusic-tw.appspot.com/o/zrytezene%2Fsongs%2F";

    public String key, url_song, url_icon, url_cover, uploader_uid, uploader_name, song_name, song_artist, color1;
    public boolean playing, cl;

    public ZZSong(DataSnapshot a) {
        playing = false;
        cl = false;
        key = a.getKey();
        url_song = PATH + a.child("song").getValue(String.class);
        url_icon = PATH + a.child("icon").getValue(String.class);
        url_cover = PATH + a.child("cover").getValue(String.class);
        uploader_uid = a.child("uid").getValue(String.class);
        uploader_name = a.child("uploader").getValue(String.class);
        song_name = a.child("title").getValue(String.class);
        song_artist = a.child("artist").getValue(String.class);
        color1 = a.child("color-bline").getValue(String.class);
    }
}