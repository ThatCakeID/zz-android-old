package tw.music.streamer.adaptor;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FileDownloadTask;

import android.content.Context;

import java.io.File;
import java.io.IOException;

public class ZryteZeneSongsManager {

    public static void downloadSong(String fileUrl, String localPath, DownloadCallback callback) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(fileUrl);
        File localFile = new File(localPath);
        try {
            if (!localFile.exists()) {
                localFile.createNewFile();
            }
            storageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                callback.onSuccess(localPath);
            }).addOnFailureListener(exception -> {
                callback.onFailure(exception.getMessage());
            });
        } catch (IOException e) {
            callback.onFailure(e.getMessage());
        }
    }

    public static boolean check(Context context, String key) {
        File songsCacheDir = new File(context.getFilesDir(), "songs-cache");
        if (!songsCacheDir.exists()) songsCacheDir.mkdirs();
        File song = new File(songsCacheDir, key);
        return song.exists() && song.isFile();
    }

    public static interface DownloadCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }
}
