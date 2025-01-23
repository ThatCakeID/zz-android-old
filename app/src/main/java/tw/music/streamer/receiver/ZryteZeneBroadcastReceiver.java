package tw.music.streamer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tw.music.streamer.adaptor.ZryteZeneAdaptor;

public class ZryteZeneBroadcastReceiver extends BroadcastReceiver {

    public static final String PLAY = "ZZ_PLAY";
    public static final String PAUSE = "ZZ_PAUSE";
    public static final String SKIP = "ZZ_SKIP";
    public static final String PREVIOUS = "ZZ_PREVIOUS";

    public ZryteZeneAdaptor zz;

    @Override
    public void onReceive(Context context, Intent intent) {
        zz = new ZryteZeneAdaptor(context);
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case PLAY:
                    zz.requestAction("resume");
                    break;
                case PAUSE:
                    zz.requestAction("pause");
                    break;
                case SKIP:
                    zz.requestAction("skip");
                    break;
                case PREVIOUS:
                    zz.requestAction("previous");
                    break;
            }
        }
    }
}