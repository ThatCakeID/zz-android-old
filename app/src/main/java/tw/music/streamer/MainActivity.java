package tw.music.streamer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textview.MaterialTextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private MaterialTextView welcome_text;
    private LinearLayout screen;
    private boolean nr;

    @Override
    protected void onCreate(Bundle a) {
        super.onCreate(a);
        setContentView(R.layout.main);
        initVariables(getApplicationContext());
        initFirebase(getApplicationContext());
        initOnClick(getApplicationContext());
        initLogic(getApplicationContext());
    }

    private void initVariables(final Context a) {
        welcome_text = findViewById(R.id.wlctext);
        screen = findViewById(R.id.base);
        nr = false;
    }

    private void initFirebase(final Context a) {
        auth = FirebaseAuth.getInstance();
    }

    private void initOnClick(final Context a) {
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                retry();
            }
        });
    }

    private void initLogic(final Context a) {
        getVersion(a);
    }

    private void getVersion(final Context a) {
        FirebaseDatabase.getInstance().getReference("zrytezene/version").get()
        .addOnFailureListener(e -> {
            nr = true;
            welcome_text.setText("Failed to connect to ZryteZene, tap on the screen to retry");
            e.printStackTrace();
        }).addOnCompleteListener(b -> {
            if (b.isSuccessful()) {
                onVersionRetrieved(b.getResult());
            } else {
                nr = true;
                welcome_text.setText("Failed to connect to ZryteZene, tap on the screen to retry");
            }
        });
    }

    private void onVersionRetrieved(DataSnapshot a) {
        if (a.exists()) {
            welcome_text.setText(a.child("chadwords").getValue(String.class));
            if (checkVersion(a)) {
                if (checkAccount()) {
                    if (checkPermissions()) {
                        openActivity(StreamingActivity.class);
                    } else {
                        openActivity(PermissionsActivity.class);
                    }
                } else {
                    openActivity(LandingActivity.class, a.child("discord-server").getValue(String.class));
                }
            } else {
                openActivity(UpdateAppActivity.class);
            }
        } else {
            welcome_text.setText("Seems like we're closing access to ZryteZene for maintenance, please try again later");
        }
    }

    private void retry() {
        if (!nr) return;
        nr = false;
        getVersion(getApplicationContext());
    }

    private boolean checkVersion(DataSnapshot a) {
        return a.child("mobile").getValue(String.class).equals("1");
    }

    private boolean checkPermissions() {
        boolean a = true;
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) a = false;
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK) != PackageManager.PERMISSION_GRANTED)) a = false;
        return a;
    }

    private boolean checkAccount() {
        return auth.getCurrentUser() != null;
    }

    private void openActivity(Class<?> a) {
        openActivity(a, null);
    }

    private void openActivity(Class<?> a, String b) {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), a);
            if (b!=null) intent.putExtra("discord-server", b);
            startActivity(intent);
            finish();
        }, 2000);
    }

}