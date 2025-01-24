package tw.music.streamer.adapter;

import android.graphics.Color;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ZZRandomSongAdapter extends RecyclerView.Adapter<ZZRandomSongAdapter.ZZViewHolder> {

    private ArrayList<ZZSong> data;

    public ZZRandomSongAdapter(ArrayList<ZZSong> a) {
        data = a;
    }

    @NonNull
    @Override
    public ZZViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item_2, parent, false);
        return new ZZViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ZZViewHolder h, int p) {
        h.title.setText(data.get(p).song_name);
        h.line.setBackgroundColor(Color.parseColor(data.get(p).color1));
        Glide.with(h.title.getContext()).load(data.get(p).url_cover).diskCacheStrategy(DiskCacheStrategy.ALL).into(h.cover);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ZZViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView cover;
        LinearLayout line;

        public ZZViewHolder(@NonNull View i) {
            super(i);
            title = i.findViewById(R.id.sli2_title);
            cover = i.findViewById(R.id.sli2_cover);
            line = i.findViewById(R.id.sli2_bottom_line);
        }
    }
}
