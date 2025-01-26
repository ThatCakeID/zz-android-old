package tw.music.streamer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {

    private TextView btn_login, btn_register, btn_discord, zzt1, zzt2;
    private Tyfeface ttf1, ttf2;
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
        roundCorner(btn_discord, dip(10), Color.parseColor("#3b396e"));
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

}