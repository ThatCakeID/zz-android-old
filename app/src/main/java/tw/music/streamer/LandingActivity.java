package tw.music.streamer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {

    private TextView btn_login, btn_register, btn_discord, zzt1, zzt2, zzt3;
    private LinearLayout land_base;
    private Typeface ttf1, ttf2;
    private Uri zz_discord;

    @Override
    protected void onCreate(Bundle a) {
        super.onCreate(a);
        setContentView(R.layout.landing);
        initVariables(getApplicationContext());
        initOnClick(getApplicationContext());
        initLogic(getApplicationContext());
    }

    private void initVariables(final Context a) {
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        btn_discord = findViewById(R.id.btn_discord);
        land_base = findViewById(R.id.land_base);
        zzt1 = findViewById(R.id.zzt1);
        zzt2 = findViewById(R.id.zzt2);
        zzt3 = findViewById(R.id.zzt3);
        ttf1 = Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf");
        ttf2 = Typeface.createFromAsset(getAssets(), "fonts/googlesansbold.ttf");
        zz_discord = Uri.parse(getIntent().getStringExtra("discord-server"));
    }

    private void initOnClick(final Context a) {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
        btn_discord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(zz_discord);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
                }
            }
        });
    }

    private void initLogic(final Context a) {
        zzt1.setTypeface(ttf1, 0);
        zzt2.setTypeface(ttf2, 0);
        roundCorner(btn_login, dip(20), Color.parseColor("#fcc2ff"));
        roundCornerWithOutline(btn_register, dip(20), Color.WHITE, Color.parseColor("#c9d0ff"));
        roundCorner(btn_discord, dip(15), Color.parseColor("#3b396e"));
        roundTopCorner(land_base, dip(12));
        btn_login.setElevation(3f);
        btn_register.setElevation(3f);
        btn_discord.setElevation(3f);
    }

    private void roundCorner(View a, float b, int c) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(c);
        d.setCornerRadius(b);
        a.setBackground(d);
    }

    private void roundCornerWithOutline(View a, float b, int c, int d) {
        GradientDrawable e = new GradientDrawable();
        e.setColor(c);
        e.setCornerRadius(b);
        e.setStroke(dip(2), d);
        a.setBackground(e);
    }

    private void roundTopCorner(View a, float b) {
        GradientDrawable c = new GradientDrawable();
        c.setColor(Color.WHITE);
        c.setCornerRadii(new float[]{b, b, b, b, 0f, 0f, 0f, 0f});
        a.setBackground(c);
    }

    private int dip(int a) {
        return (int) (a * getApplicationContext().getResources().getDisplayMetrics().density);
    }

}