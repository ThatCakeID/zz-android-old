package tw.music.streamer.adaptor;

import android.content.Intent;
import android.content.Context;

import java.util.ArrayList;

import tw.music.streamer.service.ZryteZenePlay;

public class ZryteZeneAdaptor {

    private boolean isr, isp, isi;
    private int cd, bu, d;
    private ArrayList<String> e;
    private Context ctx;
    private String sp, n;

    public ZryteZeneAdaptor(Context a) {
        isr = false;
        isp = false;
        isi = false;
        cd = 0;
        d = 0;
        bu = 0;
        e = new ArrayList<>();
        ctx = a;
    }

    public void setInitialized(boolean a) {
        isi = a;
    }

    public boolean isInitialized() {
        return isi;
    }

    public void setRunning(boolean a) {
        isr = a;
    }

    public boolean isRunning() {
        return isr;
    }

    public void setPlaying(boolean a) {
        isp = a;
    }

    public boolean isPlaying() {
        return isp;
    }

    public void setCurrentDuration(int a) {
        cd = a;
    }

    public int getCurrentDuration() {
        return cd;
    }

    public void setDuration(int a) {
        d = a;
    }

    public int getDuration() {
        return d;
    }

    public void setBufferingUpdate(int a) {
        bu = a;
    }

    public int getBufferingUpdate() {
        return bu;
    }

    public void clear() {
        requestAction("stop","-");
    }

    public void addError(String a) {
        e.add(a);
    }

    public void requestAction(String a) {
        Intent jof = new Intent(ZryteZenePlay.ACTION_BROADCAST);
        jof.putExtra("action", a);
        ctx.sendBroadcast(jof);
    }

    public void requestAction(String a, int b) {
        Intent jof = new Intent(ZryteZenePlay.ACTION_BROADCAST);
        jof.putExtra("action", a);
        jof.putExtra("req-data", b);
        ctx.sendBroadcast(jof);
    }

    public void requestAction(String a, String b) {
        Intent jof = new Intent(ZryteZenePlay.ACTION_BROADCAST);
        jof.putExtra("action", a);
        jof.putExtra("req-data", b);
        ctx.sendBroadcast(jof);
        if (a.equals("play")) sp = b;
    }

    public void play(String a, String b, String c, String d) {
        if (isr) {
            Intent jof = new Intent(ZryteZenePlay.ACTION_BROADCAST);
            jof.putExtra("action", "play");
            jof.putExtra("path", a);
            jof.putExtra("title", b);
            jof.putExtra("artist", c);
            jof.putExtra("cover", d);
            ctx.sendBroadcast(jof);
        } else {
            Intent siop = new Intent(ctx, ZryteZenePlay.class);
            siop.putExtra("action", "play");
            siop.putExtra("path", a);
            siop.putExtra("title", b);
            siop.putExtra("artist", c);
            siop.putExtra("cover", d);
            ctx.startForegroundService(siop);
            isr = true;
        }
    }

    public void play(ZZSong a) {
        if (isr) {
            Intent jof = new Intent(ZryteZenePlay.ACTION_BROADCAST);
            jof.putExtra("action", "play");
            jof.putExtra("path", a.url_song);
            jof.putExtra("title", a.song_name);
            jof.putExtra("artist", a.song_artist);
            jof.putExtra("cover", a.url_cover);
            jof.putExtra("key", a.key);
            ctx.sendBroadcast(jof);
        } else {
            Intent siop = new Intent(ctx, ZryteZenePlay.class);
            siop.putExtra("action", "play");
            siop.putExtra("path", a.url_song);
            siop.putExtra("title", a.song_name);
            siop.putExtra("artist", a.song_artist);
            siop.putExtra("cover", a.url_cover);
            siop.putExtra("key", a.key);
            ctx.startForegroundService(siop);
            isr = true;
        }
    }
}