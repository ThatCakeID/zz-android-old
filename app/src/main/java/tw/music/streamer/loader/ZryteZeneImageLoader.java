package tw.music.streamer.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZryteZeneImageLoader {

    public static interface BitmapCallback {
        void onBitmapLoaded(@Nullable Bitmap bitmap);
    }

    private static ZryteZeneImageLoader instance;
    private final LruCache<String, Bitmap> memoryCache;
    private final ExecutorService executorService;
    private final File cacheDir;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private ZryteZeneImageLoader(Context context) {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(@NonNull String key, @NonNull Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
        executorService = Executors.newFixedThreadPool(4);
        cacheDir = new File(context.getFilesDir(), "images-cache");
        if (!cacheDir.exists()) cacheDir.mkdirs();
        preloadDiskCache();
    }

    public static synchronized ZryteZeneImageLoader getInstance(Context context) {
        if (instance == null) instance = new ZryteZeneImageLoader(context.getApplicationContext());
        return instance;
    }

    public void load(String url, ImageView imageView) {
        load(url, imageView, null);
    }

    public void load(String url, @Nullable BitmapCallback callback) {
        Bitmap cachedBitmap = memoryCache.get(String.valueOf(url.hashCode()));
        if (cachedBitmap != null) {
            if (callback != null) callback.onBitmapLoaded(cachedBitmap);
            return;
        }
        executorService.execute(() -> {
            Bitmap bitmap = loadFromDiskCache(String.valueOf(url.hashCode()));
            if (bitmap == null) {
                bitmap = loadFromNetwork(url);
                if (bitmap != null) saveToDiskCache(url, bitmap);
            }
            final Bitmap resultBitmap = bitmap;
            mainHandler.post(() -> {
                if (callback != null) callback.onBitmapLoaded(resultBitmap);
            });
        });
    }

    public void load(String url, ImageView imageView, @Nullable BitmapCallback callback) {
        imageView.setTag(url);
        Bitmap cachedBitmap = memoryCache.get(String.valueOf(url.hashCode()));
        if (cachedBitmap != null) {
            imageView.setImageBitmap(cachedBitmap);
            if (callback != null) callback.onBitmapLoaded(cachedBitmap);
            return;
        }
        executorService.execute(() -> {
            Bitmap bitmap = loadFromDiskCache(String.valueOf(url.hashCode()));
            if (bitmap == null) {
                bitmap = loadFromNetwork(url);
                if (bitmap != null) saveToDiskCache(url, bitmap);
            }
            final Bitmap resultBitmap = bitmap;
            mainHandler.post(() -> {
                if (imageView.getTag().equals(url) && resultBitmap != null) imageView.setImageBitmap(resultBitmap);
                if (callback != null) callback.onBitmapLoaded(resultBitmap);
            });
        });
    }

    private Bitmap loadFromNetwork(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                memoryCache.put(urlString, bitmap);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap loadFromDiskCache(String url) {
        try {
            File file = new File(cacheDir, String.valueOf(url.hashCode()));
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bitmap != null) memoryCache.put(url, bitmap);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveToDiskCache(String url, Bitmap bitmap) {
        try {
            File file = new File(cacheDir, String.valueOf(url.hashCode()));
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearCache() {
        memoryCache.evictAll();
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    private void preloadDiskCache() {
        File[] cachedFiles = cacheDir.listFiles();
        if (cachedFiles != null) {
            for (File file : cachedFiles) {
                executorService.execute(() -> {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        if (bitmap != null) {
                            memoryCache.put(file.getName(), bitmap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}