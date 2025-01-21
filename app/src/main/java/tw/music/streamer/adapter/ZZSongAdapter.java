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

import com.bumptech.glide.Glide;

public class ZZSongAdapter extends RecyclerView.Adapter<ZZSongAdapter.ZZViewHolder> {

    private ArrayList<ZZSong> data;

    public ZZSongAdapter(ArrayList<ZZSong> a) {
        data = a;
    }

    @NonNull
    @Override
    public ZZViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item_1, parent, false);
        return new ZZViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ZZViewHolder h, int p) {
        h.title.setText(data.get(p).song_name);
        h.artist.setText(data.get(p).song_artist);
        Glide.with(h.title.getContext()).load(data.get(p).url_cover).into(h.cover);
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
}
