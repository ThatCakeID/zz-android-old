package tw.music.streamer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

public class PermissionsActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private TextView pm1, pm2;
    private MaterialButton rqpb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permissions);
        pm1 = findViewById(R.id.pm1text);
        pm2 = findViewById(R.id.pm2text);
        rqpb = findViewById(R.id.pmbutton);
        checkPermissions();
        rqpb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPerm1() && checkPerm2()) {
                    Intent intent = new Intent(getApplicationContext(), StreamingActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    requestPermissions();
                }
            }
        });
    }

    @Override
	public void onStart() {
		super.onStart();
		checkPermissions();
	}

    private void checkPermissions() {
        if (checkPerm1()) pm1.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        if (checkPerm2()) pm2.setTextColor(getResources().getColor(android.R.color.holo_green_light));
    }

    private boolean checkPerm1() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkPerm2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
        }, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int a, @NonNull String[] b, @NonNull int[] c) {
        super.onRequestPermissionsResult(a, b, c);
        if (a == PERMISSION_REQUEST_CODE) checkPermissions();
    }
}
