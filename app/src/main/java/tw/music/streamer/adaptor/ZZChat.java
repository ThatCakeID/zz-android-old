package tw.music.streamer.adaptor;

import com.google.firebase.database.DataSnapshot;

public class ZZChat {

    public String key, name, message, last_song, timestamp, photo;

    public ZZChat(DataSnapshot a) {
        key = a.getKey();
        name = a.child("f").getValue(String.class);
        message = a.child("m").getValue(String.class);
        last_song = a.child("ls").getValue(String.class);
        timestamp = a.child("ts").getValue(String.class);
        photo = a.child("p").getValue(String.class);
    }
}