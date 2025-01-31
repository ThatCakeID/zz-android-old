package tw.music.streamer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import tw.music.streamer.R;

import tw.music.streamer.loader.ZryteZeneImageLoader;
import tw.music.streamer.adaptor.ZZChat;

public class ZZChatAdapter extends RecyclerView.Adapter<ZZChatAdapter.ZZViewHolder> {

    private ArrayList<ZZChat> data;

    public ZZChatAdapter(ArrayList<ZZChat> a) {
        data = a;
    }

    @NonNull
    @Override
    public ZZViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_1, parent, false);
        return new ZZViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ZZViewHolder h, final int p) {
        h.name.setText(data.get(p).name);
        h.signature.setText(data.get(p).timestamp + " - listening to " + data.get(p).last_song);
        h.msg.setText(data.get(p).message);
        ZryteZeneImageLoader.getInstance(h.title.getContext()).load(data.get(p).photo, h.pp);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ZZViewHolder extends RecyclerView.ViewHolder {
        TextView name, signature, msg;
        ImageView pp;

        public ZZViewHolder(@NonNull View i) {
            super(i);
            name = i.findViewById(R.id.user_name);
            signature = i.findViewById(R.id.user_signature);
            msg = i.findViewById(R.id.user_msg);
            pp = i.findViewById(R.id.user_pp);
        }
    }

}
