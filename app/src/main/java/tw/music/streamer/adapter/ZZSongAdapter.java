package tw.music.streamer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import tw.music.streamer.R;
import tw.music.streamer.adaptor.ZZSong;

import tw.music.streamer.loader.ZryteZeneImageLoader;

public class ZZSongAdapter extends RecyclerView.Adapter<ZZSongAdapter.ZZViewHolder> {

    private ArrayList<ZZSong> data;
    private ZZOnClickListener listener;

    public ZZSongAdapter(ArrayList<ZZSong> a, ZZOnClickListener b) {
        data = a;
        listener = b;
    }

    @NonNull
    @Override
    public ZZViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item_1, parent, false);
        return new ZZViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ZZViewHolder h, final int p) {
        h.title.setText(data.get(p).song_name);
        h.artist.setText(data.get(p).song_artist);
        if (!data.get(p).cl) ZryteZeneImageLoader.getInstance(h.title.getContext()).load(data.get(p).url_cover, h.cover);
        data.get(p).cl = true;
        h.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View a) {
                callListener(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ZZViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView artist;
        ImageView cover;

        public ZZViewHolder(@NonNull View i) {
            super(i);
            title = i.findViewById(R.id.sli1_title);
            artist = i.findViewById(R.id.sli1_artist);
            cover = i.findViewById(R.id.sli1_cover);
        }
    }

    public void callListener(int a) {
        if (listener!=null) {
            listener.onItemClicked(a);
        }
    }
}
