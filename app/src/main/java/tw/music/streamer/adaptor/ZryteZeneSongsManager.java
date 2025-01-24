package tw.music.streamer.adaptor;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FileDownloadTask;

import android.os.AsyncTask;
import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ZryteZeneSongsManager {

    public static void download(Context a, String b, String c) {
        new DownloadSong(a, b, c).execute();
    }

    public static String check(Context context, String key) {
        File songsCacheDir = new File(context.getFilesDir(), "songs-cache");
        if (!songsCacheDir.exists()) songsCacheDir.mkdirs();
        File song = new File(songsCacheDir, key);
        if (song.exists() && song.isFile()) {
            return song.getAbsolutePath();
        } else {
            return "-";
        }
    }

    public static class DownloadSong extends AsyncTask<Void, Void, Boolean> {
        private Context context;
        private String firebaseUrl;
        private String fileName;

        public DownloadSong(Context a, String b, String c) {
            context = a;
            firebaseUrl = b;
            fileName = c;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                File songsCacheDir = new File(context.getFilesDir(), "songs-cache");
                if (!songsCacheDir.exists() && !songsCacheDir.mkdirs()) {
                    return false;
                }
                File songFile = new File(songsCacheDir, fileName);
                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(firebaseUrl);
                File tempFile = File.createTempFile("zz-song", null);
                storageRef.getFile(tempFile).addOnSuccessListener(taskSnapshot -> {
                    try {
                        InputStream input = context.getContentResolver().openInputStream(android.net.Uri.fromFile(tempFile));
                        FileOutputStream output = new FileOutputStream(songFile);
                        byte[] buffer = new byte[4096];
                        int length;
                        while ((length = input.read(buffer)) > 0) {
                            output.write(buffer, 0, length);
                        }
                        input.close();
                        output.close();
                        tempFile.delete();
                    } catch (Exception e) {
                    }
                });
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
        }
    }

}
