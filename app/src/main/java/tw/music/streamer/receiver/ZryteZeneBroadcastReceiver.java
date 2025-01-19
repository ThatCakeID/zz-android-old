package tw.music.streamer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import tw.music.streamer.service.ZryteZenePlay;

public class ZryteZeneBroadcastReceiver extends BroadcastReceiver {

    public static final String PLAY = "tw.music.streamer.ACTION_PLAY";
    public static final String PAUSE = "tw.music.streamer.ACTION_PAUSE";
    public static final String NEXT = "tw.music.streamer.ACTION_NEXT";
    public static final String PREVIOUS = "tw.music.streamer.ACTION_PREVIOUS";

    @Override
    public void onReceive(Context a, Intent b) {
        if (b == null || b.getAction() == null) return;

        String c = b.getAction();
        Intent d = new Intent(a, ZryteZenePlay.class);

        switch (c) {
            case PLAY:
                d.putExtra("action", "play");
                break;

            case PAUSE:
                d.putExtra("action", "pause");
                break;

            case NEXT:
                d.putExtra("action", "next");
                break;

            case PREVIOUS:
                d.putExtra("action", "previous");
                break;

            default:
                Log.w("MusicBroadcastReceiver", "Unknown action: " + action);
                return;
        }

        a.startService(d);
    }
}
