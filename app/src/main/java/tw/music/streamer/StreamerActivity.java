package tw.music.streamer;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.DynamicLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import tw.music.streamer.adaptor.ZryteZeneAdaptor;
import tw.music.streamer.service.ZryteZenePlay;

public class StreamerActivity extends AppCompatActivity {

    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private FirebaseStorage _firebase_storage = FirebaseStorage.getInstance();

    private Toolbar _toolbar;
    private DrawerLayout _drawer;
    private HashMap<String, Object> likes_map = new HashMap<>();
    private HashMap<String, Object> commentsMap = new HashMap<>();
    private boolean isAdmin = false;
    private String currentlyPlaying = "";
    private HashMap<String, Object> upload_map = new HashMap<>();
    private double tabsPos = 0;
    private double openNum = 0;
    private boolean isPlaylist = false;
    private double numPlaylist = 0;
    private boolean isSearching = false;
    private boolean hasPic = false;
    private HashMap<String, Object> img_maps = new HashMap<>();

    private ArrayList<String> usrname_list = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> upload_list = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> theme_map = new ArrayList<>();
    private ArrayList<String> adminsList = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> like_tmp = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> profile_map = new ArrayList<>();
    private ArrayList<String> likeChild = new ArrayList<>();
    private ArrayList<String> childkey = new ArrayList<>();
    private ArrayList<String> oldChildKey = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> oldListmap = new ArrayList<>();
    private ArrayList<String> commentsChildKey = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> commentsTmp = new ArrayList<>();
    private ArrayList<String> dialog_list = new ArrayList<>();
    private ArrayList<String> currentlyChild = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> currentlyMap = new ArrayList<>();
    private ArrayList<String> playlistString = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> playlistMap = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> playKeys = new ArrayList<>();
    private ArrayList<String> animateList = new ArrayList<>();

    private LinearLayout linear1;
    private LinearLayout linear2;
    private LinearLayout linear_title;
    private LinearLayout linear_data;
    private ListView listview1;
    private LinearLayout linear_title2;
    private LinearLayout linear_title_shdw;
    private ImageView image_drawer;
    private TextView text_zryte;
    private TextView text_zene;
    private TextView text_playlist;
    private EditText edittext_search;
    private ImageView image_search;
    private ImageView image_user;
    private TextView text_data;
    private ProgressBar progressbar1;
    private LinearLayout linear3;
    private LinearLayout linear5;
    private LinearLayout linear6;
    private HorizontalScrollView hscroll1;
    private ImageView image_menu;
    private LinearLayout linear16;
    private ImageView image_favs;
    private ImageView image_comment;
    private ImageView image_repeat;
    private ImageView image_nightcore;
    private ImageView image_download;
    private ImageView image_lyrics;
    private ImageView image_share;
    private LinearLayout linear7;
    private LinearLayout linear8;
    private SeekBar seekbar1;
    private LinearLayout linear9;
    private LinearLayout linear12;
    private LinearLayout linear15;
    private ImageView image_album;
    private LinearLayout linear14;
    private LinearLayout linear13;
    private TextView text_title;
    private TextView text_artist;
    private TextView text_current;
    private LinearLayout linear10;
    private LinearLayout linear11;
    private TextView text_duration;
    private ImageView image_prev;
    private ImageView image_play;
    private ImageView image_next;
    private LinearLayout _drawer_linear1;
    private ScrollView _drawer_vscroll1;
    private LinearLayout _drawer_linear10;
    private LinearLayout _drawer_linear_usr;
    private LinearLayout _drawer_linear5;
    private LinearLayout _drawer_linear2;
    private LinearLayout _drawer_linear6;
    private LinearLayout _drawer_linear_upload;
    private LinearLayout _drawer_linear7;
    private LinearLayout _drawer_linear_theme;
    private LinearLayout _drawer_linear_themesstr;
    private LinearLayout _drawer_linear8;
    private LinearLayout _drawer_linear_info;
    private LinearLayout _drawer_linear_discord;
    private LinearLayout _drawer_linear_settings;
    private LinearLayout _drawer_linear9;
    private LinearLayout _drawer_linear_daedit;
    private LinearLayout _drawer_linear_logcat;
    private ImageView _drawer_imageview_tm;
    private TextView _drawer_text_zryte;
    private TextView _drawer_text_zene;
    private ImageView _drawer_imageview_upload;
    private TextView _drawer_text_upload;
    private ImageView _drawer_imageview_theme;
    private TextView _drawer_text_theme;
    private ImageView _drawer_imageview_themesstr;
    private TextView _drawer_text_themesstr;
    private ImageView _drawer_imageview_info;
    private TextView _drawer_text_info;
    private ImageView _drawer_imageview_discord;
    private TextView _drawer_text_discord;
    private ImageView _drawer_imageview_settings;
    private TextView _drawer_text_settings;
    private ImageView _drawer_imageview_daedit;
    private TextView _drawer_text_daedit;
    private ImageView _drawer_imageview_logcat;
    private TextView _drawer_text_logcat;
    private ImageView _drawer_image_user;
    private LinearLayout _drawer_linear_usr2;
    private TextView _drawer_text_user;
    private TextView _drawer_text_email;

    private ObjectAnimator obj = new ObjectAnimator();
    private ObjectAnimator obj2 = new ObjectAnimator();
    private ObjectAnimator obj3 = new ObjectAnimator();
    private SharedPreferences data;
    private DatabaseReference upload_text = _firebase.getReference("upload/text");
    private ChildEventListener _upload_text_child_listener;
    private DatabaseReference profile_admins = _firebase.getReference("profile/admins");
    private ChildEventListener _profile_admins_child_listener;
    private DatabaseReference comments_db = _firebase.getReference("upload/msg");
    private ChildEventListener _comments_db_child_listener;
    private FirebaseAuth Auth;
    private OnCompleteListener<AuthResult> _Auth_create_user_listener;
    private OnCompleteListener<AuthResult> _Auth_sign_in_listener;
    private OnCompleteListener<Void> _Auth_reset_password_listener;
    private DatabaseReference profile = _firebase.getReference("profile/text");
    private ChildEventListener _profile_child_listener;
    private DatabaseReference fb_likes = _firebase.getReference("upload/likes");
    private ChildEventListener _fb_likes_child_listener;
    private DatabaseReference prof_img = _firebase.getReference("profile/image");
    private ChildEventListener _prof_img_child_listener;
    private Intent intent = new Intent();
    private AlertDialog.Builder d;
    private Intent i2 = new Intent();
    private StorageReference check_quota = _firebase_storage.getReference("/");
    private OnCompleteListener<Uri> _check_quota_upload_success_listener;
    private OnSuccessListener<FileDownloadTask.TaskSnapshot> _check_quota_download_success_listener;
    private OnSuccessListener _check_quota_delete_success_listener;
    private OnProgressListener _check_quota_upload_progress_listener;
    private OnProgressListener _check_quota_download_progress_listener;
    private OnFailureListener _check_quota_failure_listener;
    private StorageReference upload_storage = _firebase_storage.getReference("upload/music");
    private OnCompleteListener<Uri> _upload_storage_upload_success_listener;
    private OnSuccessListener<FileDownloadTask.TaskSnapshot> _upload_storage_download_success_listener;
    private OnSuccessListener _upload_storage_delete_success_listener;
    private OnProgressListener _upload_storage_upload_progress_listener;
    private OnProgressListener _upload_storage_download_progress_listener;
    private OnFailureListener _upload_storage_failure_listener;
    private StorageReference music_image = _firebase_storage.getReference("upload/image");
    private OnCompleteListener<Uri> _music_image_upload_success_listener;
    private OnSuccessListener<FileDownloadTask.TaskSnapshot> _music_image_download_success_listener;
    private OnSuccessListener _music_image_delete_success_listener;
    private OnProgressListener _music_image_upload_progress_listener;
    private OnProgressListener _music_image_download_progress_listener;
    private OnFailureListener _music_image_failure_listener;
    private ObjectAnimator obj4 = new ObjectAnimator();
    private ObjectAnimator obj5 = new ObjectAnimator();
    private ObjectAnimator obj6 = new ObjectAnimator();
    private double _f;
    private double _t;
    private ArrayAdapter<String> Listview2Adapter;
    private GridView listview2;
    private Handler dataHandlerReceiverZero;
    private Handler dataHandlerReceiverOne;
    private Handler dataHandlerReceiverTwo;
    private Handler dataHandlerReceiverThree;
    private Handler dataHandlerReceiverFour;
    private Handler dataHandlerReceiverFive;
    private com.google.android.material.tabs.TabLayout x_tab;
    private androidx.viewpager.widget.ViewPager viewPager;
    private android.graphics.drawable.AnimationDrawable rocketAnimation;
    private ProgressDialog coreprog;
    private ZryteZeneAdaptor zz;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.streamer);
        com.google.firebase.FirebaseApp.initializeApp(this);
        initialize(_savedInstanceState);
        //initializeLogic();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            initializeLogic();
        }
    }

    private void initialize(Bundle _savedInstanceState) {

        _toolbar = findViewById(R.id._toolbar);
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        _toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _v) {
                onBackPressed();
            }
        });
        _drawer = findViewById(R.id._drawer);
        ActionBarDrawerToggle _toggle = new ActionBarDrawerToggle(StreamerActivity.this, _drawer, _toolbar, R.string.app_name, R.string.app_name);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();

        LinearLayout _nav_view = findViewById(R.id._nav_view);

        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        linear_title = findViewById(R.id.linear_title);
        linear_data = findViewById(R.id.linear_data);
        listview1 = findViewById(R.id.listview1);
        linear_title2 = findViewById(R.id.linear_title2);
        linear_title_shdw = findViewById(R.id.linear_title_shdw);
        image_drawer = findViewById(R.id.image_drawer);
        text_zryte = findViewById(R.id.text_zryte);
        text_zene = findViewById(R.id.text_zene);
        text_playlist = findViewById(R.id.text_playlist);
        edittext_search = findViewById(R.id.edittext_search);
        image_search = findViewById(R.id.image_search);
        image_user = findViewById(R.id.image_user);
        text_data = findViewById(R.id.text_data);
        progressbar1 = findViewById(R.id.progressbar1);
        linear3 = findViewById(R.id.linear3);
        linear5 = findViewById(R.id.linear5);
        linear6 = findViewById(R.id.linear6);
        hscroll1 = findViewById(R.id.hscroll1);
        image_menu = findViewById(R.id.image_menu);
        linear16 = findViewById(R.id.linear16);
        image_favs = findViewById(R.id.image_favs);
        image_comment = findViewById(R.id.image_comment);
        image_repeat = findViewById(R.id.image_repeat);
        image_nightcore = findViewById(R.id.image_nightcore);
        image_download = findViewById(R.id.image_download);
        image_lyrics = findViewById(R.id.image_lyrics);
        image_share = findViewById(R.id.image_share);
        linear7 = findViewById(R.id.linear7);
        linear8 = findViewById(R.id.linear8);
        seekbar1 = findViewById(R.id.seekbar1);
        linear9 = findViewById(R.id.linear9);
        linear12 = findViewById(R.id.linear12);
        linear15 = findViewById(R.id.linear15);
        image_album = findViewById(R.id.image_album);
        linear14 = findViewById(R.id.linear14);
        linear13 = findViewById(R.id.linear13);
        text_title = findViewById(R.id.text_title);
        text_artist = findViewById(R.id.text_artist);
        text_current = findViewById(R.id.text_current);
        linear10 = findViewById(R.id.linear10);
        linear11 = findViewById(R.id.linear11);
        text_duration = findViewById(R.id.text_duration);
        image_prev = findViewById(R.id.image_prev);
        image_play = findViewById(R.id.image_play);
        image_next = findViewById(R.id.image_next);
        _drawer_linear1 = _nav_view.findViewById(R.id.linear1);
        _drawer_vscroll1 = _nav_view.findViewById(R.id.vscroll1);
        _drawer_linear10 = _nav_view.findViewById(R.id.linear10);
        _drawer_linear_usr = _nav_view.findViewById(R.id.linear_usr);
        _drawer_linear5 = _nav_view.findViewById(R.id.linear5);
        _drawer_linear2 = _nav_view.findViewById(R.id.linear2);
        _drawer_linear6 = _nav_view.findViewById(R.id.linear6);
        _drawer_linear_upload = _nav_view.findViewById(R.id.linear_upload);
        _drawer_linear7 = _nav_view.findViewById(R.id.linear7);
        _drawer_linear_theme = _nav_view.findViewById(R.id.linear_theme);
        _drawer_linear_themesstr = _nav_view.findViewById(R.id.linear_themesstr);
        _drawer_linear8 = _nav_view.findViewById(R.id.linear8);
        _drawer_linear_info = _nav_view.findViewById(R.id.linear_info);
        _drawer_linear_discord = _nav_view.findViewById(R.id.linear_discord);
        _drawer_linear_settings = _nav_view.findViewById(R.id.linear_settings);
        _drawer_linear9 = _nav_view.findViewById(R.id.linear9);
        _drawer_linear_daedit = _nav_view.findViewById(R.id.linear_daedit);
        _drawer_linear_logcat = _nav_view.findViewById(R.id.linear_logcat);
        _drawer_imageview_tm = _nav_view.findViewById(R.id.imageview_tm);
        _drawer_text_zryte = _nav_view.findViewById(R.id.text_zryte);
        _drawer_text_zene = _nav_view.findViewById(R.id.text_zene);
        _drawer_imageview_upload = _nav_view.findViewById(R.id.imageview_upload);
        _drawer_text_upload = _nav_view.findViewById(R.id.text_upload);
        _drawer_imageview_theme = _nav_view.findViewById(R.id.imageview_theme);
        _drawer_text_theme = _nav_view.findViewById(R.id.text_theme);
        _drawer_imageview_themesstr = _nav_view.findViewById(R.id.imageview_themesstr);
        _drawer_text_themesstr = _nav_view.findViewById(R.id.text_themesstr);
        _drawer_imageview_info = _nav_view.findViewById(R.id.imageview_info);
        _drawer_text_info = _nav_view.findViewById(R.id.text_info);
        _drawer_imageview_discord = _nav_view.findViewById(R.id.imageview_discord);
        _drawer_text_discord = _nav_view.findViewById(R.id.text_discord);
        _drawer_imageview_settings = _nav_view.findViewById(R.id.imageview_settings);
        _drawer_text_settings = _nav_view.findViewById(R.id.text_settings);
        _drawer_imageview_daedit = _nav_view.findViewById(R.id.imageview_daedit);
        _drawer_text_daedit = _nav_view.findViewById(R.id.text_daedit);
        _drawer_imageview_logcat = _nav_view.findViewById(R.id.imageview_logcat);
        _drawer_text_logcat = _nav_view.findViewById(R.id.text_logcat);
        _drawer_image_user = _nav_view.findViewById(R.id.image_user);
        _drawer_linear_usr2 = _nav_view.findViewById(R.id.linear_usr2);
        _drawer_text_user = _nav_view.findViewById(R.id.text_user);
        _drawer_text_email = _nav_view.findViewById(R.id.text_email);
        data = getSharedPreferences("teamdata", Activity.MODE_PRIVATE);
        Auth = FirebaseAuth.getInstance();
        d = new AlertDialog.Builder(this);
        zz = new ZryteZeneAdaptor(this);
        Intent siop = new Intent(getApplicationContext(), ZryteZenePlay.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(siop);
        } else {
            startService(siop);
        }

        image_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (isPlaylist) {
                    isPlaylist = false;
                    childkey.clear();
                    upload_list.clear();
                    listview1.setAdapter(new Listview1Adapter(upload_list));
                    listview1.setVisibility(View.GONE);
                    listview2.setVisibility(View.VISIBLE);
                    text_playlist.setVisibility(View.GONE);
                    text_zryte.setVisibility(View.VISIBLE);
                    text_zene.setVisibility(View.VISIBLE);
                    image_search.setVisibility(View.VISIBLE);
                    image_user.setVisibility(View.VISIBLE);
                    image_drawer.setImageResource(R.drawable.ic_menu);
                    x_tab.setVisibility(View.VISIBLE);
                } else {
                    if (isSearching) {
                        isSearching = false;
                        x_tab.setVisibility(View.VISIBLE);
                        edittext_search.setVisibility(View.GONE);
                        text_zryte.setVisibility(View.VISIBLE);
                        text_zene.setVisibility(View.VISIBLE);
                        image_search.setVisibility(View.VISIBLE);
                        image_user.setVisibility(View.VISIBLE);
                        image_drawer.setImageResource(R.drawable.ic_menu);
                        _setMenu(tabsPos);
                        _abandonFocus();
                    } else {
                        if (_drawer.isDrawerOpen(GravityCompat.START)) {
                            _drawer.closeDrawer(GravityCompat.START);
                        } else {
                            _drawer.openDrawer(GravityCompat.START);
                        }
                    }
                }
            }
        });

        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                final String _charSeq = _param1.toString();
                childkey.clear();
                upload_list.clear();
                double _tmpNum = 0;
                for (int _repeat13 = 0; _repeat13 < oldListmap.size(); _repeat13++) {
                    if (usrname_list.contains(oldListmap.get((int) _tmpNum).get("uid").toString())) {
                        if (oldListmap.get((int) _tmpNum).get("name").toString().toLowerCase().contains(_charSeq.toLowerCase().trim()) || (profile_map.get(usrname_list.indexOf(oldListmap.get((int) _tmpNum).get("uid").toString())).get("username").toString().toLowerCase().contains(_charSeq.toLowerCase().trim()) || _charSeq.trim().equals(""))) {
                            childkey.add(0, oldChildKey.get((int) (_tmpNum)));
                            {
                                HashMap<String, Object> _item = new HashMap<>();
                                _item = oldListmap.get((int) _tmpNum);
                                upload_list.add(0, _item);
                            }
                            int _index = listview1.getFirstVisiblePosition();
                            View _view = listview1.getChildAt(0);
                            int _top = (_view == null) ? 0 : _view.getTop();
                            listview1.setAdapter(new Listview1Adapter(upload_list));
                            listview1.setSelectionFromTop(_index, _top);
                        } else {
                            if ((_tmpNum == (oldListmap.size() - 1)) && (upload_list.size() == 0)) {
                                int _index = listview1.getFirstVisiblePosition();
                                View _view = listview1.getChildAt(0);
                                int _top = (_view == null) ? 0 : _view.getTop();
                                listview1.setAdapter(new Listview1Adapter(upload_list));
                                listview1.setSelectionFromTop(_index, _top);
                            }
                        }
                    } else {
                        if (oldListmap.get((int) _tmpNum).get("name").toString().toLowerCase().contains(_charSeq.toLowerCase().trim()) || (oldListmap.get((int) _tmpNum).get("uid").toString().toLowerCase().contains(_charSeq.toLowerCase().trim()) || _charSeq.trim().equals(""))) {
                            childkey.add(0, oldChildKey.get((int) (_tmpNum)));
                            {
                                HashMap<String, Object> _item = new HashMap<>();
                                _item = oldListmap.get((int) _tmpNum);
                                upload_list.add(0, _item);
                            }
                            int _index = listview1.getFirstVisiblePosition();
                            View _view = listview1.getChildAt(0);
                            int _top = (_view == null) ? 0 : _view.getTop();
                            listview1.setAdapter(new Listview1Adapter(upload_list));
                            listview1.setSelectionFromTop(_index, _top);
                        } else {
                            if ((_tmpNum == (oldListmap.size() - 1)) && (upload_list.size() == 0)) {
                                int _index = listview1.getFirstVisiblePosition();
                                View _view = listview1.getChildAt(0);
                                int _top = (_view == null) ? 0 : _view.getTop();
                                listview1.setAdapter(new Listview1Adapter(upload_list));
                                listview1.setSelectionFromTop(_index, _top);
                            }
                        }
                    }
                    _tmpNum++;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {

            }

            @Override
            public void afterTextChanged(Editable _param1) {

            }
        });

        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                isSearching = true;
                listview2.setVisibility(View.GONE);
                listview1.setVisibility(View.VISIBLE);
                x_tab.setVisibility(View.GONE);
                text_zryte.setVisibility(View.GONE);
                text_zene.setVisibility(View.GONE);
                image_search.setVisibility(View.GONE);
                image_user.setVisibility(View.GONE);
                edittext_search.setVisibility(View.VISIBLE);
                image_drawer.setImageResource(R.drawable.ic_arrow_back_white);
                edittext_search.setText("");
                edittext_search.requestFocus();
                android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edittext_search, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT);
                childkey.clear();
                upload_list.clear();
                double _tmpNum = 0;
                for (int _repeat27 = 0; _repeat27 < oldListmap.size(); _repeat27++) {
                    childkey.add(0, oldChildKey.get((int) (_tmpNum)));
                    {
                        HashMap<String, Object> _item = new HashMap<>();
                        _item = oldListmap.get((int) _tmpNum);
                        upload_list.add(0, _item);
                    }
                    int _index = listview1.getFirstVisiblePosition();
                    View _viewl = listview1.getChildAt(0);
                    int _top = (_viewl == null) ? 0 : _viewl.getTop();
                    listview1.setAdapter(new Listview1Adapter(upload_list));
                    listview1.setSelectionFromTop(_index, _top);
                    _tmpNum++;
                }
            }
        });

        image_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                intent.setClass(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);
            }
        });

        image_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (openNum == 2) {
                    openNum = 1;
                    obj.setTarget(linear2);
                    obj.setPropertyName("translationY");
                    obj.setFloatValues(SketchwareUtil.getDip(getApplicationContext(), -200), SketchwareUtil.getDip(getApplicationContext(), -50));
                    obj.setInterpolator(new DecelerateInterpolator());
                    obj.setDuration(500);
                    obj.start();
                    obj2.setTarget(image_menu);
                    obj2.setPropertyName("rotation");
                    obj2.setFloatValues((float) (0), (float) (180));
                    obj2.setInterpolator(new DecelerateInterpolator());
                    obj2.setDuration(500);
                    obj2.start();
                    obj3.setTarget(linear7);
                    obj3.setPropertyName("alpha");
                    obj3.setFloatValues((float) (1), (float) (0));
                    obj3.setInterpolator(new DecelerateInterpolator());
                    obj3.setDuration(500);
                    obj3.start();
                } else {
                    openNum = 2;
                    obj.setTarget(linear2);
                    obj.setPropertyName("translationY");
                    obj.setFloatValues(SketchwareUtil.getDip(getApplicationContext(), -50), SketchwareUtil.getDip(getApplicationContext(), -200));
                    obj.setInterpolator(new DecelerateInterpolator());
                    obj.setDuration(500);
                    obj.start();
                    obj2.setTarget(image_menu);
                    obj2.setPropertyName("rotation");
                    obj2.setFloatValues((float) (180), (float) (0));
                    obj2.setInterpolator(new DecelerateInterpolator());
                    obj2.setDuration(500);
                    obj2.start();
                    obj3.setTarget(linear7);
                    obj3.setPropertyName("alpha");
                    obj3.setFloatValues((float) (0), (float) (1));
                    obj3.setInterpolator(new DecelerateInterpolator());
                    obj3.setDuration(500);
                    obj3.start();
                }
            }
        });

        image_favs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (likeChild.contains(currentlyPlaying.concat(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
                    fb_likes.child(currentlyPlaying.concat(FirebaseAuth.getInstance().getCurrentUser().getUid())).removeValue();
                } else {
                    upload_map = new HashMap<>();
                    upload_map.put("key", currentlyPlaying);
                    upload_map.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    fb_likes.child(currentlyPlaying.concat(FirebaseAuth.getInstance().getCurrentUser().getUid())).updateChildren(upload_map);
                }
            }
        });

        image_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                intent.setClass(getApplicationContext(), MessageActivity.class);
                intent.putExtra("key", currentlyPlaying);
                startActivity(intent);
            }
        });

        image_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (data.getString("fvsAsc", "").equals("0")) {
                    data.edit().putString("fvsAsc", "1").commit();
                    image_repeat.setAlpha((float) (1.0d));
                } else {
                    if (data.getString("fvsAsc", "").equals("1")) {
                        data.edit().putString("fvsAsc", "2").commit();
                        image_repeat.setImageResource(R.drawable.ic_repeat_one_white);
                    } else {
                        if (data.getString("fvsAsc", "").equals("2")) {
                            data.edit().putString("fvsAsc", "3").commit();
                            image_repeat.setImageResource(R.drawable.ic_shuffle_white);
                        } else {
                            data.edit().putString("fvsAsc", "0").commit();
                            image_repeat.setAlpha((float) (0.5d));
                            image_repeat.setImageResource(R.drawable.ic_repeat_white);
                        }
                    }
                }
                zz.requestAction("update-sp","loop");
            }
        });

        image_nightcore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (data.getString("nightcore", "").equals("1")) {
                        data.edit().putString("nightcore", "0").commit();
                        image_nightcore.setAlpha((float) (0.5d));
                        //tmservice._setNightcore(1.00f);
                    } else {
                        data.edit().putString("nightcore", "1").commit();
                        image_nightcore.setAlpha((float) (1.0d));
                        float _tmpFloat = 1.10f + ((float) Double.parseDouble(data.getString("nightcoreSpeed", "")) * 0.05f);
                        //tmservice._setNightcore(_tmpFloat);
                    }
                } else {
                    data.edit().putString("nightcore", "0").commit();
                    image_nightcore.setAlpha((float) (0.5d));
                    _customSnack("Android Lollipop or lower doesn't support nightcore feature!", 2);
                }
            }
        });

        image_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                d.setCancelable(false);
                d.setTitle("Download");
                d.setMessage("Do you want to save this music to your storage?");
                d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        _downloadFile(currentlyMap.get(currentlyChild.indexOf(currentlyPlaying)).get("url").toString(), FileUtil.getPublicDir(Environment.DIRECTORY_MUSIC));
                        _customSnack("Downloading...", 0);
                        _resetDialog();
                    }
                });
                d.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        _resetDialog();
                    }
                });
                d.create().show();
            }
        });

        image_lyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (text_title.getText().toString().contains("-")) {
                    intent.setClass(getApplicationContext(), LyricsActivity.class);
                    intent.putExtra("musicName", text_title.getText().toString());
                    startActivity(intent);
                } else {
                    intent.setClass(getApplicationContext(), LyricsActivity.class);
                    intent.putExtra("musicName", text_artist.getText().toString().concat(" - ".concat(text_title.getText().toString())));
                    startActivity(intent);
                }
            }
        });

        image_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (currentlyPlaying.length() > 0) {
                    i2.setData(Uri.parse("https://zrytezene.xyz/music.html?play=".concat(currentlyPlaying)));
                    i2.setAction(Intent.ACTION_VIEW);
                    startActivity(i2);
                }
            }
        });

        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar _param1, int _param2, boolean _param3) {
                final int _progressValue = _param2;
                text_current.setText(new DecimalFormat("00").format(_progressValue / 60).concat(":".concat(new DecimalFormat("00").format(_progressValue % 60))));
            }

            @Override
            public void onStartTrackingTouch(SeekBar _param1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar _param2) {
                zz.requestAction("seek", seekbar1.getProgress() * 1000);
            }
        });

        image_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (data.getString("fvsAsc", "").equals("3")) {
                    _play(currentlyChild.get(SketchwareUtil.getRandom(0, currentlyMap.size() - 1)));
                } else {
                    if (currentlyChild.indexOf(currentlyPlaying) > 0) {
                        _play(currentlyChild.get(currentlyChild.indexOf(currentlyPlaying) - 1));
                    } else {
                        _play(currentlyChild.get(currentlyMap.size() - 1));
                    }
                }
            }
        });

        image_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (zz.isPlaying()) {
                    zz.requestAction("pause");
                } else if (zz.isInitialized()) {
                    zz.requestAction("resume");
                }
            }
        });

        image_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (data.getString("fvsAsc", "").equals("3")) {
                    _play(currentlyChild.get(SketchwareUtil.getRandom(0, currentlyMap.size() - 1)));
                } else {
                    if (currentlyMap.size() > (currentlyChild.indexOf(currentlyPlaying) + 1)) {
                        _play(currentlyChild.get(currentlyChild.indexOf(currentlyPlaying) + 1));
                    } else {
                        _play(currentlyChild.get(0));
                    }
                }
            }
        });

        _upload_text_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                linear_data.setVisibility(View.GONE);
                listview1.setVisibility(View.VISIBLE);
                x_tab.setEnabled(true);
                {
                    LinearLayout _tabStrip = ((LinearLayout) x_tab.getChildAt(0));
                    _tabStrip.setEnabled(true);
                    for (int i = 0; i < _tabStrip.getChildCount(); i++) {
                        _tabStrip.getChildAt(i).setClickable(true);
                    }
                }
                oldChildKey.add(_childKey);
                oldListmap.add(_childValue);
                if (isSearching) {
                    if (_childValue.get("name").toString().toLowerCase().contains(edittext_search.getText().toString().toLowerCase().trim()) || (_childValue.get("uid").toString().toLowerCase().contains(edittext_search.getText().toString().toLowerCase().trim()) || edittext_search.getText().toString().toLowerCase().trim().equals(""))) {
                        childkey.add(0, _childKey);
                        upload_list.add(0, _childValue);
                        int _index = listview1.getFirstVisiblePosition();
                        View _view = listview1.getChildAt(0);
                        int _top = (_view == null) ? 0 : _view.getTop();
                        listview1.setAdapter(new Listview1Adapter(upload_list));
                        listview1.setSelectionFromTop(_index, _top);
                    }
                } else {
                    if (tabsPos == 0) {
                        childkey.add(0, _childKey);
                        upload_list.add(0, _childValue);
                        int _index = listview1.getFirstVisiblePosition();
                        View _view = listview1.getChildAt(0);
                        int _top = (_view == null) ? 0 : _view.getTop();
                        listview1.setAdapter(new Listview1Adapter(upload_list));
                        listview1.setSelectionFromTop(_index, _top);
                    }
                    if (tabsPos == 2) {
                        double _tmpNum = 0;
                        boolean _tmp_bool = false;
                        for (int _repeat57 = 0; _repeat57 < upload_list.size(); _repeat57++) {
                            if ((Double.parseDouble(_childValue.get("view").toString()) > Double.parseDouble(upload_list.get((int) _tmpNum).get("view").toString())) || (Double.parseDouble(_childValue.get("view").toString()) == Double.parseDouble(upload_list.get((int) _tmpNum).get("view").toString()))) {
                                if (!_tmp_bool) {
                                    childkey.add((int) (_tmpNum), _childKey);
                                    upload_list.add((int) _tmpNum, _childValue);
                                    int _index = listview1.getFirstVisiblePosition();
                                    View _view = listview1.getChildAt(0);
                                    int _top = (_view == null) ? 0 : _view.getTop();
                                    listview1.setAdapter(new Listview1Adapter(upload_list));
                                    listview1.setSelectionFromTop(_index, _top);
                                    _tmp_bool = true;
                                }
                            }
                            _tmpNum++;
                        }
                        if (!_tmp_bool) {
                            childkey.add(_childKey);
                            upload_list.add(_childValue);
                            int _index = listview1.getFirstVisiblePosition();
                            View _view = listview1.getChildAt(0);
                            int _top = (_view == null) ? 0 : _view.getTop();
                            listview1.setAdapter(new Listview1Adapter(upload_list));
                            listview1.setSelectionFromTop(_index, _top);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                upload_text.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        oldListmap = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                oldListmap.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        if (childkey.contains(_childKey)) {
                            upload_list.get(childkey.indexOf(_childKey)).put("url", _childValue.get("url").toString());
                            upload_list.get(childkey.indexOf(_childKey)).put("name", _childValue.get("name").toString());
                            upload_list.get(childkey.indexOf(_childKey)).put("view", _childValue.get("view").toString());
                            upload_list.get(childkey.indexOf(_childKey)).put("uid", _childValue.get("uid").toString());
                            if (_childValue.containsKey("img")) {
                                upload_list.get(childkey.indexOf(_childKey)).put("img", _childValue.get("img").toString());
                            }
                            int _index = listview1.getFirstVisiblePosition();
                            View _view = listview1.getChildAt(0);
                            int _top = (_view == null) ? 0 : _view.getTop();
                            listview1.setAdapter(new Listview1Adapter(upload_list));
                            listview1.setSelectionFromTop(_index, _top);
                        }
                        if (currentlyChild.contains(_childKey)) {
                            currentlyMap.get(currentlyChild.indexOf(_childKey)).put("url", _childValue.get("url").toString());
                            currentlyMap.get(currentlyChild.indexOf(_childKey)).put("name", _childValue.get("name").toString());
                            currentlyMap.get(currentlyChild.indexOf(_childKey)).put("view", _childValue.get("view").toString());
                            currentlyMap.get(currentlyChild.indexOf(_childKey)).put("uid", _childValue.get("uid").toString());
                            if (_childValue.containsKey("img")) {
                                currentlyMap.get(currentlyChild.indexOf(_childKey)).put("img", _childValue.get("img").toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (currentlyPlaying.equals(_childKey)) {
                    currentlyPlaying = "";
                    openNum = 0;
                    obj.setTarget(linear2);
                    obj.setPropertyName("translationX");
                    obj.setFloatValues((float) (0), (float) (SketchwareUtil.getDisplayWidthPixels(getApplicationContext()) + SketchwareUtil.getDisplayHeightPixels(getApplicationContext())));
                    obj.setInterpolator(new DecelerateInterpolator());
                    obj.setDuration(500);
                    obj.start();
                    _customNav(theme_map.get(0).get("colorBackground").toString());
                    //tmservice._removeFocus();
                    //tmservice._resetMp();
                    currentlyMap.clear();
                    currentlyChild.clear();
                }
                oldListmap.remove(oldChildKey.indexOf(_childKey));
                oldChildKey.remove(oldChildKey.indexOf(_childKey));
                if (childkey.contains(_childKey)) {
                    upload_list.remove(childkey.indexOf(_childKey));
                    childkey.remove(childkey.indexOf(_childKey));
                }
                //if (!tmservice._isMpNull() && currentlyChild != null) {
                    //if (currentlyChild.contains(_childKey)) {
                        //currentlyMap.remove(currentlyChild.indexOf(_childKey));
                        //currentlyChild.remove(currentlyChild.indexOf(_childKey));
                    //}
                //}
                int _index = listview1.getFirstVisiblePosition();
                View _view = listview1.getChildAt(0);
                int _top = (_view == null) ? 0 : _view.getTop();
                listview1.setAdapter(new Listview1Adapter(upload_list));
                listview1.setSelectionFromTop(_index, _top);
            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        upload_text.addChildEventListener(_upload_text_child_listener);

        _profile_admins_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                adminsList.add(_childKey);
                if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    isAdmin = true;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        profile_admins.addChildEventListener(_profile_admins_child_listener);

        _comments_db_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                commentsChildKey.add(_childKey);
                commentsTmp.add(_childValue);
                if (commentsMap.containsKey(_childValue.get("key").toString())) {
                    commentsMap.put(_childValue.get("key").toString(), String.valueOf((long) (Double.parseDouble(commentsMap.get(_childValue.get("key").toString()).toString()) + 1)));
                } else {
                    commentsMap.put(_childValue.get("key").toString(), "1");
                }
                int _index = listview1.getFirstVisiblePosition();
                View _view = listview1.getChildAt(0);
                int _top = (_view == null) ? 0 : _view.getTop();
                listview1.setAdapter(new Listview1Adapter(upload_list));
                listview1.setSelectionFromTop(_index, _top);
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                commentsTmp.remove(commentsChildKey.indexOf(_childKey));
                commentsChildKey.remove(commentsChildKey.indexOf(_childKey));
                if (Double.parseDouble(commentsMap.get(_childValue.get("key").toString()).toString()) > 1) {
                    commentsMap.put(_childValue.get("key").toString(), String.valueOf((long) (Double.parseDouble(commentsMap.get(_childValue.get("key").toString()).toString()) - 1)));
                } else {
                    commentsMap.remove(_childValue.get("key").toString());
                }
                int _index = listview1.getFirstVisiblePosition();
                View _view = listview1.getChildAt(0);
                int _top = (_view == null) ? 0 : _view.getTop();
                listview1.setAdapter(new Listview1Adapter(upload_list));
                listview1.setSelectionFromTop(_index, _top);
            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        comments_db.addChildEventListener(_comments_db_child_listener);

        _profile_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                usrname_list.add(_childKey);
                profile_map.add(_childValue);
                if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    _drawer_text_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    _drawer_text_user.setText(_childValue.get("username").toString());
                }
                int _index = listview1.getFirstVisiblePosition();
                View _view = listview1.getChildAt(0);
                int _top = (_view == null) ? 0 : _view.getTop();
                listview1.setAdapter(new Listview1Adapter(upload_list));
                listview1.setSelectionFromTop(_index, _top);
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                profile.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        profile_map = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                profile_map.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            _drawer_text_user.setText(_childValue.get("username").toString());
                        }
                        int _index = listview1.getFirstVisiblePosition();
                        View _view = listview1.getChildAt(0);
                        int _top = (_view == null) ? 0 : _view.getTop();
                        listview1.setAdapter(new Listview1Adapter(upload_list));
                        listview1.setSelectionFromTop(_index, _top);
                    }

                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        profile.addChildEventListener(_profile_child_listener);

        _fb_likes_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                likeChild.add(_childKey);
                like_tmp.add(_childValue);
                if (likes_map.containsKey(_childValue.get("key").toString())) {
                    likes_map.put(_childValue.get("key").toString(), String.valueOf((long) (Double.parseDouble(likes_map.get(_childValue.get("key").toString()).toString()) + 1)));
                } else {
                    likes_map.put(_childValue.get("key").toString(), "1");
                }
                _refreshLikes();
                if ((tabsPos == 1) && (_childValue.get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && (oldChildKey.contains(_childValue.get("key").toString()) && !isSearching))) {
                    childkey.add(0, _childValue.get("key").toString());
                    {
                        HashMap<String, Object> _item = new HashMap<>();
                        _item = oldListmap.get(oldChildKey.indexOf(_childValue.get("key")));
                        upload_list.add(0, _item);
                    }
                }
                int _index = listview1.getFirstVisiblePosition();
                View _view = listview1.getChildAt(0);
                int _top = (_view == null) ? 0 : _view.getTop();
                listview1.setAdapter(new Listview1Adapter(upload_list));
                listview1.setSelectionFromTop(_index, _top);
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                like_tmp.remove(likeChild.indexOf(_childKey));
                likeChild.remove(likeChild.indexOf(_childKey));
                if (Double.parseDouble(likes_map.get(_childValue.get("key").toString()).toString()) > 1) {
                    likes_map.put(_childValue.get("key").toString(), String.valueOf((long) (Double.parseDouble(likes_map.get(_childValue.get("key").toString()).toString()) - 1)));
                } else {
                    likes_map.remove(_childValue.get("key").toString());
                }
                _refreshLikes();
                if ((tabsPos == 1) && (childkey.contains(_childValue.get("key").toString()) && !isSearching)) {
                    upload_list.remove(childkey.indexOf(_childValue.get("key").toString()));
                    childkey.remove(childkey.indexOf(_childValue.get("key").toString()));
                }
                int _index = listview1.getFirstVisiblePosition();
                View _view = listview1.getChildAt(0);
                int _top = (_view == null) ? 0 : _view.getTop();
                listview1.setAdapter(new Listview1Adapter(upload_list));
                listview1.setSelectionFromTop(_index, _top);
            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        fb_likes.addChildEventListener(_fb_likes_child_listener);

        _prof_img_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    image_user.clearColorFilter();
                    _drawer_image_user.clearColorFilter();
                    Glide.with(getApplicationContext()).asBitmap().load(_childValue.get("url").toString()).centerCrop().into(new com.bumptech.glide.request.target.BitmapImageViewTarget(image_user) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            androidx.core.graphics.drawable.RoundedBitmapDrawable circularBitmapDrawable = androidx.core.graphics.drawable.RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            image_user.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                    Glide.with(getApplicationContext()).asBitmap().load(_childValue.get("url").toString()).centerCrop().into(new com.bumptech.glide.request.target.BitmapImageViewTarget(_drawer_image_user) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            androidx.core.graphics.drawable.RoundedBitmapDrawable circularBitmapDrawable = androidx.core.graphics.drawable.RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            _drawer_image_user.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                    hasPic = true;
                }
                img_maps.put(_childKey, _childValue.get("url").toString());
                ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    Glide.with(getApplicationContext()).asBitmap().load(_childValue.get("url").toString()).centerCrop().into(new com.bumptech.glide.request.target.BitmapImageViewTarget(image_user) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            androidx.core.graphics.drawable.RoundedBitmapDrawable circularBitmapDrawable = androidx.core.graphics.drawable.RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            image_user.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                    Glide.with(getApplicationContext()).asBitmap().load(_childValue.get("url").toString()).centerCrop().into(new com.bumptech.glide.request.target.BitmapImageViewTarget(_drawer_image_user) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            androidx.core.graphics.drawable.RoundedBitmapDrawable circularBitmapDrawable = androidx.core.graphics.drawable.RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            _drawer_image_user.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
                img_maps.put(_childKey, _childValue.get("url").toString());
                ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        prof_img.addChildEventListener(_prof_img_child_listener);

        _check_quota_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot _param1) {
                double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();

            }
        };

        _check_quota_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
                double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();

            }
        };

        _check_quota_upload_success_listener = new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> _param1) {
                final String _downloadUrl = _param1.getResult().toString();

            }
        };

        _check_quota_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
                final long _totalByteCount = _param1.getTotalByteCount();
                if (FileUtil.isExistFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/tm-testfile"))) {
                    FileUtil.deleteFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/tm-testfile"));
                }
            }
        };

        _check_quota_delete_success_listener = new OnSuccessListener() {
            @Override
            public void onSuccess(Object _param1) {

            }
        };

        _check_quota_failure_listener = new OnFailureListener() {
            @Override
            public void onFailure(Exception _param1) {
                final String _message = _param1.getMessage();
                if (FileUtil.isExistFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/tm-testfile"))) {
                    FileUtil.deleteFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/tm-testfile"));
                }
                d.setCancelable(false);
                d.setTitle("Daily quota is exhausted!");
                d.setMessage("Sorry for the inconveniences, but the daily quota limit is reached and you can't read, edit, or upload anything during this time until tomorrow at 07:00 UTC.");
                d.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        _resetDialog();
                    }
                });
                d.setNegativeButton("Read error message", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        _resetDialog();
                        _customSnack(_message, 2);
                    }
                });
                d.create().show();
            }
        };

        _upload_storage_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot _param1) {
                double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();

            }
        };

        _upload_storage_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
                double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();

            }
        };

        _upload_storage_upload_success_listener = new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> _param1) {
                final String _downloadUrl = _param1.getResult().toString();

            }
        };

        _upload_storage_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
                final long _totalByteCount = _param1.getTotalByteCount();

            }
        };

        _upload_storage_delete_success_listener = new OnSuccessListener() {
            @Override
            public void onSuccess(Object _param1) {

            }
        };

        _upload_storage_failure_listener = new OnFailureListener() {
            @Override
            public void onFailure(Exception _param1) {
                final String _message = _param1.getMessage();
                _customSnack(_message, 2);
            }
        };

        _music_image_upload_progress_listener = new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot _param1) {
                double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();

            }
        };

        _music_image_download_progress_listener = new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot _param1) {
                double _progressValue = (100.0 * _param1.getBytesTransferred()) / _param1.getTotalByteCount();

            }
        };

        _music_image_upload_success_listener = new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> _param1) {
                final String _downloadUrl = _param1.getResult().toString();

            }
        };

        _music_image_download_success_listener = new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot _param1) {
                final long _totalByteCount = _param1.getTotalByteCount();

            }
        };

        _music_image_delete_success_listener = new OnSuccessListener() {
            @Override
            public void onSuccess(Object _param1) {

            }
        };

        _music_image_failure_listener = new OnFailureListener() {
            @Override
            public void onFailure(Exception _param1) {
                final String _message = _param1.getMessage();
                _customSnack(_message, 2);
            }
        };

        _drawer_linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {

            }
        });

        _drawer_linear_usr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                intent.setClass(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);
                _drawer.closeDrawer(GravityCompat.START);
            }
        });

        _drawer_linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                i2.setData(Uri.parse("https://zrytezene.xyz"));
                i2.setAction(Intent.ACTION_VIEW);
                startActivity(i2);
                _drawer.closeDrawer(GravityCompat.START);
            }
        });

        _drawer_linear_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                intent.setClass(getApplicationContext(), UploadActivity.class);
                startActivity(intent);
                _drawer.closeDrawer(GravityCompat.START);
            }
        });

        _drawer_linear_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                intent.setClass(getApplicationContext(), ThemesActivity.class);
                startActivity(intent);
                _drawer.closeDrawer(GravityCompat.START);
            }
        });

        _drawer_linear_themesstr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                intent.setClass(getApplicationContext(), ThemesstrActivity.class);
                startActivity(intent);
                _drawer.closeDrawer(GravityCompat.START);
            }
        });

        _drawer_linear_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                intent.setClass(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
                _drawer.closeDrawer(GravityCompat.START);
            }
        });

        _drawer_linear_discord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                i2.setData(Uri.parse("https://discord.gg/ERpx6dv"));
                i2.setAction(Intent.ACTION_VIEW);
                startActivity(i2);
                _drawer.closeDrawer(GravityCompat.START);
            }
        });

        _drawer_linear_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                intent.setClass(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                _drawer.closeDrawer(GravityCompat.START);
            }
        });

        _drawer_linear_daedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                intent.setClass(getApplicationContext(), DaeditActivity.class);
                startActivity(intent);
                _drawer.closeDrawer(GravityCompat.START);
            }
        });

        _drawer_linear_logcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                intent.setClass(getApplicationContext(), LogcatActivity.class);
                startActivity(intent);
                _drawer.closeDrawer(GravityCompat.START);
            }
        });

        _Auth_create_user_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
            }
        };

        _Auth_sign_in_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
            }
        };

        _Auth_reset_password_listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> _param1) {
                final boolean _success = _param1.isSuccessful();
            }
        };
    }

    private void initializeLogic() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        getSupportActionBar().hide();
        hscroll1.setVerticalScrollBarEnabled(false);
        hscroll1.setHorizontalScrollBarEnabled(false);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        x_tab = new com.google.android.material.tabs.TabLayout(StreamerActivity.this);
        x_tab.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        x_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        x_tab.setTabGravity(TabLayout.GRAVITY_CENTER);
        x_tab.setSelectedTabIndicatorHeight(6);
        x_tab.addTab(x_tab.newTab().setText("Library"));
        x_tab.getChildAt(0).setPadding(70, 0, 70, 0);
        x_tab.addTab(x_tab.newTab().setText("My Favs"));
        x_tab.addTab(x_tab.newTab().setText("Most Played"));
        x_tab.addTab(x_tab.newTab().setText("My Playlists"));

        linear_title.removeView(linear_title_shdw);
        linear_title.addView(x_tab);
        linear_title.addView(linear_title_shdw);

        x_tab.addOnTabSelectedListener(new com.google.android.material.tabs.TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(com.google.android.material.tabs.TabLayout.Tab tab) {
                _setMenu(tab.getPosition());
            }

            @Override
            public void onTabUnselected(com.google.android.material.tabs.TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(com.google.android.material.tabs.TabLayout.Tab tab) {
            }

        });

        linear2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View p1, MotionEvent p2) {
                switch (p2.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        _f = p2.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        _t = p2.getX();
                        if (((_f - _t) < -150)) {
                            currentlyPlaying = "";
                            openNum = 0;
                            obj.setTarget(linear2);
                            obj.setPropertyName("translationX");
                            obj.setFloatValues((float) (0), (float) (SketchwareUtil.getDisplayWidthPixels(getApplicationContext()) + SketchwareUtil.getDisplayHeightPixels(getApplicationContext())));
                            obj.setInterpolator(new DecelerateInterpolator());
                            obj.setDuration(500);
                            obj.start();
                            _customNav(theme_map.get(0).get("colorBackground").toString());
                            //tmservice._removeFocus();
                            //tmservice._resetMp();
                            currentlyMap.clear();
                            currentlyChild.clear();
                        }
                        break;
                }
                return true;
            }
        });
        fb_likes.removeEventListener(_fb_likes_child_listener);
        dialog_list.add("Profile");
        dialog_list.add("Delete");
        dialog_list.add("Comments");
        dialog_list.add("Add to my playlist");
        dialog_list.add("Cancel");
        playlistMap = new Gson().fromJson(data.getString("playlist", ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        for (int _repeat142 = 0; _repeat142 < playlistMap.size(); _repeat142++) {
            playlistString.add(playlistMap.get(playlistString.size()).get("name").toString());
        }
        playlistString.add("Add a playlist");
        Listview2Adapter = new ArrayAdapter<String>(StreamerActivity.this, R.layout.playlist_clickable, R.id.text_playlist, playlistString) {
            @Override
            public View getView(int _position, View _convertView, ViewGroup _parent) {
                View _view = super.getView(_position, _convertView, _parent);
                ((TextView) _view.findViewById(R.id.text_playlist)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
                if (_position == playlistString.size() - 1) {
                    ((ImageView) _view.findViewById(R.id.image_playlist)).setImageResource(R.drawable.ic_add_white);
                } else {
                    ((ImageView) _view.findViewById(R.id.image_playlist)).setImageResource(R.drawable.ic_album_white);
                }
                _view.findViewById(R.id.linear_playlist).setBackground(Drawables.getSelectableDrawableFor(Color.parseColor(theme_map.get(0).get("colorRipple").toString())));
                _view.findViewById(R.id.linear_playlist).setClickable(true);
                ((TextView) _view.findViewById(R.id.text_playlist)).setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
                ((ImageView) _view.findViewById(R.id.image_playlist)).setColorFilter(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()), PorterDuff.Mode.MULTIPLY);
                final double _pos = _position;
                _view.findViewById(R.id.linear_playlist).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View _view) {
                        _addGrid(_pos);
                    }
                });
                _view.findViewById(R.id.linear_playlist).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View _view) {
                        _delGrid(_pos);
                        return true;
                    }
                });
                return _view;
            }

        };
        listview2 = new GridView(StreamerActivity.this);

        listview2.setLayoutParams(new GridView.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT));
        listview2.setNumColumns(GridView.AUTO_FIT);
        listview2.setVerticalSpacing(2);
        listview2.setHorizontalSpacing(2);
        listview2.setColumnWidth((int) SketchwareUtil.getDip(getApplicationContext(), 118));
        listview2.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        linear1.addView(listview2);
        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int _pos, long id) {
            }
        });

        listview2.setAdapter(Listview2Adapter);
        _loadTheme();
        _bindSvc();
        try {
            android.content.pm.PackageInfo packageInfo = StreamerActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception _e) {
        }
        text_title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        text_title.setMarqueeRepeatLimit(-1);
        text_title.setSingleLine(true);
        text_title.setSelected(true);
        android.content.res.ColorStateList clrb = new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{Color.TRANSPARENT});
        android.graphics.drawable.RippleDrawable ripdrb = new android.graphics.drawable.RippleDrawable(clrb, null, null);
        seekbar1.setBackground(ripdrb);
        linear2.setTranslationY(SketchwareUtil.getDip(getApplicationContext(), 500));
        listview1.setAdapter(new Listview1Adapter(upload_list));
        listview1.setVisibility(View.GONE);
        listview2.setVisibility(View.GONE);
        x_tab.setEnabled(false);
        {
            LinearLayout _tabStrip = ((LinearLayout) x_tab.getChildAt(0));
            _tabStrip.setEnabled(false);
            for (int i = 0; i < _tabStrip.getChildCount(); i++) {
                _tabStrip.getChildAt(i).setClickable(false);
            }
        }
        text_playlist.setVisibility(View.GONE);
        edittext_search.setVisibility(View.GONE);
        Typeface tsans = Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf");
        Typeface tbold = Typeface.createFromAsset(getAssets(), "fonts/googlesansbold.ttf");
        text_zryte.setTypeface(tsans, 0);
        text_zene.setTypeface(tbold, 0);
        text_playlist.setTypeface(tsans, 0);
        text_data.setTypeface(tsans, 0);
        text_title.setTypeface(tsans, 0);
        text_artist.setTypeface(tsans, 0);
        text_current.setTypeface(tsans, 0);
        text_duration.setTypeface(tsans, 0);
        edittext_search.setTypeface(tsans, 0);
        _drawer_text_zryte.setTypeface(tsans, 0);
        _drawer_text_zene.setTypeface(tbold, 0);
        _drawer_text_user.setTypeface(tsans, 0);
        _drawer_text_email.setTypeface(tsans, 0);
        _drawer_text_upload.setTypeface(tsans, 0);
        _drawer_text_theme.setTypeface(tsans, 0);
        _drawer_text_themesstr.setTypeface(tsans, 0);
        _drawer_text_info.setTypeface(tsans, 0);
        _drawer_text_logcat.setTypeface(tsans, 0);
        _drawer_text_discord.setTypeface(tsans, 0);
        _drawer_text_daedit.setTypeface(tsans, 0);
        _drawer_text_settings.setTypeface(tsans, 0);
        if (data.getString("taptarget", "").equals("1")) {
            //_NewTapTarget(image_user, "Account", "Click here or by clicking account information on the drawer for accessing your account info & settings", "#2196F3");
        } else {
            if (!data.getString("taptarget", "").equals("2")) {
                //_NewTapTarget(image_drawer, "Menu", "Click here for accessing some extra menu", "#2196F3");
            }
        }
        if (data.getString("nightcore", "").equals("1")) {
            image_nightcore.setAlpha((float) (1.0d));
        } else {
            image_nightcore.setAlpha((float) (0.5d));
        }
        if (data.getString("fvsAsc", "").equals("0")) {
            image_repeat.setAlpha((float) (0.5d));
            image_repeat.setImageResource(R.drawable.ic_repeat_white);
        } else {
            if (data.getString("fvsAsc", "").equals("1")) {
                image_repeat.setAlpha((float) (1.0d));
                image_repeat.setImageResource(R.drawable.ic_repeat_white);
            } else {
                if (data.getString("fvsAsc", "").equals("2")) {
                    image_repeat.setAlpha((float) (1.0d));
                    image_repeat.setImageResource(R.drawable.ic_repeat_one_white);
                } else {
                    image_repeat.setAlpha((float) (1.0d));
                    image_repeat.setImageResource(R.drawable.ic_shuffle_white);
                }
            }
        }
        _firebase_storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/teammusic-tw.appspot.com/o/tm-testfile?alt=media&token=ec852b8b-438c-457a-887d-b289968971ec").getFile(new File(FileUtil.getPackageDataDir(getApplicationContext()).concat("/tm-testfile"))).addOnSuccessListener(_check_quota_download_success_listener).addOnFailureListener(_check_quota_failure_listener).addOnProgressListener(_check_quota_download_progress_listener);
        IntentFilter filr = new IntentFilter(ZryteZenePlay.ACTION_UPDATE);
		registerReceiver(listenerReceiver, filr);
        zz.requestAction("request-media");
    }

    private BroadcastReceiver listenerReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && intent.getAction() != null) {
				if (intent.getAction().equals(ZryteZenePlay.ACTION_UPDATE)) {
					String m = intent.getStringExtra("update");
					if (m.equals("on-prepared")) {
                        _CoreProgressLoading(false);
                        zz.setPlaying(true);
                        zz.setCurrentDuration(0);
                        zz.setDuration(intent.getIntExtra("data",0)/1000);
                        seekbar1.setProgress(0);
                        seekbar1.setMax(zz.getDuration());
                        _showPlayer();
                    } else if (m.equals("on-reqmedia")) {
                    } else if (m.equals("on-tick")) {
                        zz.setCurrentDuration(intent.getIntExtra("data",0));
                        seekbar1.setProgress(zz.getCurrentDuration()/1000);
                    } else if (m.equals("on-completion")) {
                        zz.setPlaying(false);
                    } else if (m.equals("on-error")) {
                        zz.addError(intent.getStringExtra("data"));
                    } else if (m.equals("on-seekerror")) {
                    } else if (m.equals("on-initialized")) {
                    } else if (m.equals("on-bufferupdate")) {
                        zz.setBufferingUpdate(intent.getIntExtra("data",0));
                    } else if (m.equals("request-play")) {
                    } else if (m.equals("request-pause")) {
                        zz.setPlaying(false);
                    } else if (m.equals("request-resume")) {
                        zz.setPlaying(true);
                    } else if (m.equals("request-stop")) {
                        zz.setPlaying(false);
                    } else if (m.equals("request-seek")) {
                        zz.setCurrentDuration(intent.getIntExtra("data",0)/1000);
                    } else if (m.equals("request-restart")) {
                        zz.setCurrentDuration(0);
                    } else if (m.equals("request-reset")) {
                    }
				}
			}
		}
	};

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (_drawer.isDrawerOpen(GravityCompat.START)) {
            _drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isPlaylist) {
                isPlaylist = false;
                childkey.clear();
                upload_list.clear();
                listview1.setAdapter(new Listview1Adapter(upload_list));
                listview1.setVisibility(View.GONE);
                listview2.setVisibility(View.VISIBLE);
                text_playlist.setVisibility(View.GONE);
                text_zryte.setVisibility(View.VISIBLE);
                text_zene.setVisibility(View.VISIBLE);
                image_search.setVisibility(View.VISIBLE);
                image_user.setVisibility(View.VISIBLE);
                image_drawer.setImageResource(R.drawable.ic_menu);
                x_tab.setVisibility(View.VISIBLE);
            } else {
                if (isSearching) {
                    isSearching = false;
                    x_tab.setVisibility(View.VISIBLE);
                    edittext_search.setVisibility(View.GONE);
                    text_zryte.setVisibility(View.VISIBLE);
                    text_zene.setVisibility(View.VISIBLE);
                    image_search.setVisibility(View.VISIBLE);
                    image_user.setVisibility(View.VISIBLE);
                    image_drawer.setImageResource(R.drawable.ic_menu);
                    _setMenu(tabsPos);
                    _abandonFocus();
                } else {
                    finish();
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(0);
        }
        _loadTheme();
        if (!(openNum == 0)) {
            _customNav(theme_map.get(0).get("colorPrimary").toString());
        }
        if (hasPic) {
            image_user.clearColorFilter();
            _drawer_image_user.clearColorFilter();
        }
        /*if (tmservice != null) {
            if (!currentlyPlaying.equals("") && !tmservice._isMpNull()) {
                if (currentlyMap.get(currentlyChild.indexOf(currentlyPlaying)).containsKey("img")) {
                    image_album.clearColorFilter();
                }
                if (adminsList.contains(currentlyMap.get(currentlyChild.indexOf(currentlyPlaying)).get("uid").toString())) {
                    text_artist.setTextColor(Color.parseColor(theme_map.get(0).get("colorButton").toString()));
                } else {
                    text_artist.setTextColor(Color.parseColor(theme_map.get(0).get("colorPrimaryCardText").toString()));
                }
            }
        }*/
        ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _unbindSvc();
    }

    private void _shadow(final View _v, final double _n) {
        _v.setElevation((float) _n);
    }

    private void _customNav(final String _color) {
        //Code From StackOverFlow.com And Converted By TeamWorks DEV
        if (Build.VERSION.SDK_INT >= 21) {
            Window w = this.getWindow();
            w.setNavigationBarColor(Color.parseColor(_color));
        }
    }

    private int getDominantColor(Bitmap bitmap) {
        if (bitmap == null) {
            return Color.TRANSPARENT;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = width * height;
        int[] pixels = new int[size];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int color;
        int r = 0;
        int g = 0;
        int b = 0;
        int a;
        int count = 0;
        for (int i = 0; i < pixels.length; i++) {
            color = pixels[i];
            a = Color.alpha(color);
            if (a > 0) {
                r += Color.red(color);
                g += Color.green(color);
                b += Color.blue(color);
                count++;
            }
        }
        r /= count;
        g /= count;
        b /= count;
        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;
        color = 0xFF000000 | r | g | b;
        return color;
    }

    private boolean isDark(int color) {
        return androidx.core.graphics.ColorUtils.calculateLuminance(color) < 0.5;
    }

    private void _customSnack(final String _txt, final double _icon) {
        // Create the Snackbar
        ViewGroup containerLayout = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);

        com.google.android.material.snackbar.Snackbar snackbar = com.google.android.material.snackbar.Snackbar.make(containerLayout, "", com.google.android.material.snackbar.Snackbar.LENGTH_LONG);
        // Get the Snackbar's layout view
        com.google.android.material.snackbar.Snackbar.SnackbarLayout layout = (com.google.android.material.snackbar.Snackbar.SnackbarLayout) snackbar.getView();
        // Inflate our custom view
        View snackview = getLayoutInflater().inflate(R.layout.custom_snack, null);
        // Configure the view
        ImageView image = snackview.findViewById(R.id.imageview);
        if (_icon == 0) {
            image.setImageResource(R.drawable.ic_info_outline_white);
            layout.setBackgroundColor(Color.parseColor("#2090F0"));
        } else {
            if (_icon == 1) {
                image.setImageResource(R.drawable.ic_done_white);
                layout.setBackgroundColor(Color.parseColor("#48B048"));
            } else {
                if (_icon == 2) {
                    image.setImageResource(R.drawable.ic_exit_white);
                    layout.setBackgroundColor(Color.parseColor("#E03830"));
                }
            }
        }
        TextView text = snackview.findViewById(R.id.textview);
        text.setText(_txt);
        text.setTextColor(Color.WHITE);
        text.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
        layout.setPadding(0, 0, 0, 0);
        // Add the view to the Snackbar's layout
        layout.addView(snackview, 0);
        // Show the Snackbar
        snackbar.show();
    }

    private void _Rounded(final double _kiri_atas, final double _kanan_atas, final double _kiri_bawah, final double _kanan_bawah, final String _warna, final View _view) {
        Double atas_kiri = _kiri_atas;
        Double atas_kanan = _kanan_atas;
        Double bawah_kiri = _kiri_bawah;
        Double bawah_kanan = _kanan_bawah;
        android.graphics.drawable.GradientDrawable s = new android.graphics.drawable.GradientDrawable();
        s.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
        s.setCornerRadii(new float[]{atas_kiri.floatValue(), atas_kiri.floatValue(), atas_kanan.floatValue(), atas_kanan.floatValue(), bawah_kiri.floatValue(), bawah_kiri.floatValue(), bawah_kanan.floatValue(), bawah_kanan.floatValue()});
        s.setColor(Color.parseColor(_warna));
        _view.setBackground(s);
    }

    private void _circleRipple(final String _color, final View _v) {
        android.content.res.ColorStateList clrb = new android.content.res.ColorStateList(new int[][]{new int[]{}}, new int[]{Color.parseColor(_color)});
        android.graphics.drawable.RippleDrawable ripdrb = new android.graphics.drawable.RippleDrawable(clrb, null, null);
        _v.setBackground(ripdrb);
    }

    private void _loadTheme() {
        theme_map = new Gson().fromJson(data.getString("themesjson", ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            Window w = this.getWindow();
            w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.setStatusBarColor(Color.parseColor(theme_map.get(0).get("colorBackground").toString()));
        }
        _customNav(theme_map.get(0).get("colorBackground").toString());
        _Rounded(SketchwareUtil.getDip(getApplicationContext(), 5), 0, 0, 0, theme_map.get(0).get("colorButton").toString(), linear12);
        _Rounded(SketchwareUtil.getDip(getApplicationContext(), (int) (7.5d)), SketchwareUtil.getDip(getApplicationContext(), (int) (7.5d)), SketchwareUtil.getDip(getApplicationContext(), (int) (7.5d)), SketchwareUtil.getDip(getApplicationContext(), (int) (7.5d)), theme_map.get(0).get("colorBackgroundCard").toString(), linear_title2);
        _Rounded(SketchwareUtil.getDip(getApplicationContext(), 5), 0, 0, SketchwareUtil.getDip(getApplicationContext(), 5), theme_map.get(0).get("colorPrimaryCard").toString(), linear7);
        if (theme_map.get(0).get("shadow").toString().equals("1")) {
            _shadow(linear7, 15);
            _shadow(linear_title2, 10);
            if (isDark(Color.parseColor(theme_map.get(0).get("colorBackground").toString()))) {
                linear_title_shdw.setBackgroundColor(0xFF808080);
                _drawer_linear6.setBackgroundColor(0xFF808080);
                _drawer_linear7.setBackgroundColor(0xFF808080);
                _drawer_linear8.setBackgroundColor(0xFF808080);
                _drawer_linear9.setBackgroundColor(0xFF808080);
                _drawer_linear10.setBackgroundColor(0xFF808080);
            } else {
                linear_title_shdw.setBackgroundColor(0xFFBDBDBD);
                _drawer_linear6.setBackgroundColor(0xFFBDBDBD);
                _drawer_linear7.setBackgroundColor(0xFFBDBDBD);
                _drawer_linear8.setBackgroundColor(0xFFBDBDBD);
                _drawer_linear9.setBackgroundColor(0xFFBDBDBD);
                _drawer_linear10.setBackgroundColor(0xFFBDBDBD);
            }
        } else {
            _shadow(linear7, 0);
            _shadow(linear_title2, 0);
            linear_title_shdw.setBackgroundColor(Color.TRANSPARENT);
            _drawer_linear6.setBackgroundColor(Color.TRANSPARENT);
            _drawer_linear7.setBackgroundColor(Color.TRANSPARENT);
            _drawer_linear8.setBackgroundColor(Color.TRANSPARENT);
            _drawer_linear9.setBackgroundColor(Color.TRANSPARENT);
            _drawer_linear10.setBackgroundColor(Color.TRANSPARENT);
        }
        text_zryte.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundCardText").toString()));
        text_zene.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundCardText").toString()));
        text_playlist.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundCardText").toString()));
        edittext_search.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundCardText").toString()));
        //x_tab.setSelectedTabIndicatorColor(Color.parseColor(theme_map.get(0).get("colorPrimary").toString()));
        //x_tab.setTabTextColors(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()), Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));

        {
            ViewGroup _vg = (ViewGroup) x_tab.getChildAt(0);
            int _tabsCount = _vg.getChildCount();
            for (int j = 0; j < _tabsCount; j++) {
                ViewGroup _vgTab = (ViewGroup) _vg.getChildAt(j);
                int _tabChildsCount = _vgTab.getChildCount();
                for (int i = 0; i < _tabChildsCount; i++) {
                    View _tabViewChild = _vgTab.getChildAt(i);
                    if (_tabViewChild instanceof TextView) {
                        ((TextView) _tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
                        ((TextView) _tabViewChild).setAllCaps(false);
                    }
                }
            }
        }
        image_prev.setColorFilter(Color.parseColor(theme_map.get(0).get("colorPrimaryCardImage").toString()), PorterDuff.Mode.MULTIPLY);
        image_play.setColorFilter(Color.parseColor(theme_map.get(0).get("colorPrimaryCardImage").toString()), PorterDuff.Mode.MULTIPLY);
        image_next.setColorFilter(Color.parseColor(theme_map.get(0).get("colorPrimaryCardImage").toString()), PorterDuff.Mode.MULTIPLY);
        text_title.setTextColor(Color.parseColor(theme_map.get(0).get("colorPrimaryCardText").toString()));
        text_artist.setTextColor(Color.parseColor(theme_map.get(0).get("colorPrimaryCardText").toString()));
        text_current.setTextColor(Color.parseColor(theme_map.get(0).get("colorPrimaryCardText").toString()));
        text_duration.setTextColor(Color.parseColor(theme_map.get(0).get("colorPrimaryCardText").toString()));
        _circleRipple(theme_map.get(0).get("colorRipple").toString(), image_search);
        _circleRipple(theme_map.get(0).get("colorRipple").toString(), image_menu);
        _circleRipple(theme_map.get(0).get("colorRipple").toString(), image_prev);
        _circleRipple(theme_map.get(0).get("colorRipple").toString(), image_play);
        _circleRipple(theme_map.get(0).get("colorRipple").toString(), image_next);
        _circleRipple(theme_map.get(0).get("colorRipple").toString(), image_favs);
        _circleRipple(theme_map.get(0).get("colorRipple").toString(), image_comment);
        _circleRipple(theme_map.get(0).get("colorRipple").toString(), image_repeat);
        _circleRipple(theme_map.get(0).get("colorRipple").toString(), image_nightcore);
        _circleRipple(theme_map.get(0).get("colorRipple").toString(), image_download);
        _circleRipple(theme_map.get(0).get("colorRipple").toString(), image_lyrics);
        _circleRipple(theme_map.get(0).get("colorRipple").toString(), image_share);
        _circleRipple(theme_map.get(0).get("colorRipple").toString(), image_drawer);
        _circleRipple(theme_map.get(0).get("colorRipple").toString(), image_user);
        linear2.setBackgroundColor(Color.parseColor(theme_map.get(0).get("colorPrimary").toString()));
        linear1.setBackgroundColor(Color.parseColor(theme_map.get(0).get("colorBackground").toString()));
        image_search.setColorFilter(Color.parseColor(theme_map.get(0).get("colorBackgroundCardImage").toString()), PorterDuff.Mode.MULTIPLY);
        image_favs.setColorFilter(Color.parseColor(theme_map.get(0).get("colorPrimaryImage").toString()), PorterDuff.Mode.MULTIPLY);
        image_comment.setColorFilter(Color.parseColor(theme_map.get(0).get("colorPrimaryImage").toString()), PorterDuff.Mode.MULTIPLY);
        image_repeat.setColorFilter(Color.parseColor(theme_map.get(0).get("colorPrimaryImage").toString()), PorterDuff.Mode.MULTIPLY);
        image_nightcore.setColorFilter(Color.parseColor(theme_map.get(0).get("colorPrimaryImage").toString()), PorterDuff.Mode.MULTIPLY);
        image_download.setColorFilter(Color.parseColor(theme_map.get(0).get("colorPrimaryImage").toString()), PorterDuff.Mode.MULTIPLY);
        image_lyrics.setColorFilter(Color.parseColor(theme_map.get(0).get("colorPrimaryImage").toString()), PorterDuff.Mode.MULTIPLY);
        image_share.setColorFilter(Color.parseColor(theme_map.get(0).get("colorPrimaryImage").toString()), PorterDuff.Mode.MULTIPLY);
        image_menu.setColorFilter(Color.parseColor(theme_map.get(0).get("colorPrimaryImage").toString()), PorterDuff.Mode.MULTIPLY);
        image_drawer.setColorFilter(Color.parseColor(theme_map.get(0).get("colorBackgroundCardImage").toString()), PorterDuff.Mode.MULTIPLY);
        image_user.setColorFilter(Color.parseColor(theme_map.get(0).get("colorBackgroundCardImage").toString()), PorterDuff.Mode.MULTIPLY);
        _drawer_linear_upload.setBackground(Drawables.getSelectableDrawableFor(Color.parseColor(theme_map.get(0).get("colorRipple").toString())));
        _drawer_linear_upload.setClickable(true);
        _drawer_linear_theme.setBackground(Drawables.getSelectableDrawableFor(Color.parseColor(theme_map.get(0).get("colorRipple").toString())));
        _drawer_linear_theme.setClickable(true);
        _drawer_linear_themesstr.setBackground(Drawables.getSelectableDrawableFor(Color.parseColor(theme_map.get(0).get("colorRipple").toString())));
        _drawer_linear_themesstr.setClickable(true);
        _drawer_linear_info.setBackground(Drawables.getSelectableDrawableFor(Color.parseColor(theme_map.get(0).get("colorRipple").toString())));
        _drawer_linear_info.setClickable(true);
        _drawer_linear_logcat.setBackground(Drawables.getSelectableDrawableFor(Color.parseColor(theme_map.get(0).get("colorRipple").toString())));
        _drawer_linear_logcat.setClickable(true);
        _drawer_linear_discord.setBackground(Drawables.getSelectableDrawableFor(Color.parseColor(theme_map.get(0).get("colorRipple").toString())));
        _drawer_linear_discord.setClickable(true);
        _drawer_linear_daedit.setBackground(Drawables.getSelectableDrawableFor(Color.parseColor(theme_map.get(0).get("colorRipple").toString())));
        _drawer_linear_daedit.setClickable(true);
        _drawer_linear_settings.setBackground(Drawables.getSelectableDrawableFor(Color.parseColor(theme_map.get(0).get("colorRipple").toString())));
        _drawer_linear_settings.setClickable(true);
        _drawer_image_user.setColorFilter(Color.parseColor(theme_map.get(0).get("colorBackgroundImage").toString()), PorterDuff.Mode.MULTIPLY);
        _drawer_imageview_upload.setColorFilter(Color.parseColor(theme_map.get(0).get("colorBackgroundImage").toString()), PorterDuff.Mode.MULTIPLY);
        _drawer_imageview_theme.setColorFilter(Color.parseColor(theme_map.get(0).get("colorBackgroundImage").toString()), PorterDuff.Mode.MULTIPLY);
        _drawer_imageview_themesstr.setColorFilter(Color.parseColor(theme_map.get(0).get("colorBackgroundImage").toString()), PorterDuff.Mode.MULTIPLY);
        _drawer_imageview_info.setColorFilter(Color.parseColor(theme_map.get(0).get("colorBackgroundImage").toString()), PorterDuff.Mode.MULTIPLY);
        _drawer_imageview_logcat.setColorFilter(Color.parseColor(theme_map.get(0).get("colorBackgroundImage").toString()), PorterDuff.Mode.MULTIPLY);
        _drawer_imageview_daedit.setColorFilter(Color.parseColor(theme_map.get(0).get("colorBackgroundImage").toString()), PorterDuff.Mode.MULTIPLY);
        _drawer_imageview_settings.setColorFilter(Color.parseColor(theme_map.get(0).get("colorBackgroundImage").toString()), PorterDuff.Mode.MULTIPLY);
        seekbar1.getProgressDrawable().setColorFilter(Color.parseColor(theme_map.get(0).get("colorButton").toString()), PorterDuff.Mode.SRC_IN);

        seekbar1.getThumb().setColorFilter(Color.parseColor(theme_map.get(0).get("colorButton").toString()), PorterDuff.Mode.SRC_IN);
        _drawer_linear1.setBackgroundColor(Color.parseColor(theme_map.get(0).get("colorBackground").toString()));
        _drawer_text_zryte.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
        _drawer_text_zene.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
        _drawer_text_user.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
        _drawer_text_email.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
        _drawer_text_upload.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
        _drawer_text_theme.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
        _drawer_text_themesstr.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
        _drawer_text_info.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
        _drawer_text_logcat.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
        _drawer_text_discord.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
        _drawer_text_daedit.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
        _drawer_text_settings.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
        text_data.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
        if (!isDark(Color.parseColor(theme_map.get(0).get("colorBackground").toString()))) {
            _BlackStatusBarIcons();
        }
        progressbar1.getIndeterminateDrawable().setColorFilter(Color.parseColor(theme_map.get(0).get("colorButton").toString()), PorterDuff.Mode.SRC_IN);
        edittext_search.setHintTextColor(Color.parseColor(theme_map.get(0).get("colorHint").toString()));
    }

    private void _refreshLikes() {
        //if (!tmservice._isMpNull()) {
            if (likeChild.contains(currentlyPlaying.concat(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
                image_favs.setImageResource(R.drawable.ic_favorite);
            } else {
                image_favs.setImageResource(R.drawable.ic_favorite_border);
            }
        //}
    }

    private void _CoreProgressLoading(final boolean _ifShow) {
        if (_ifShow) {
            if (coreprog == null) {
                coreprog = new ProgressDialog(this);
                coreprog.setCancelable(false);
                coreprog.setCanceledOnTouchOutside(false);

                coreprog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                coreprog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT));

            }
            coreprog.setMessage(null);
            coreprog.show();
            LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _coreView = _inflater.inflate(R.layout.custom_dialog, null);
            final ImageView imageview1 = _coreView.findViewById(R.id.imageview1);
            com.bumptech.glide.Glide.with(getApplicationContext()).load(R.raw.partyblob).into(imageview1);
            coreprog.setContentView(_coreView);
        } else {
            if (coreprog != null) {
                coreprog.dismiss();
            }
        }
    }

    private void _BlackStatusBarIcons() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void _downloadFile(final String _url, final String _path) {
        try {
            String _fileName = URLUtil.guessFileName(_url, null, null);
            java.io.File _filePath = new java.io.File(_path + "/" + _fileName);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(_url));
            request.setDescription(_url);
            request.setTitle(_fileName);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setDestinationUri(Uri.fromFile(_filePath));
            final DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            final long downloadID = downloadManager.enqueue(request);
        } catch (Exception _error) {
            _customSnack(_error.getMessage(), 2);
        }
    }

    private void _setMenu(final double _position) {
        tabsPos = _position;
        listview2.setVisibility(View.GONE);
        listview1.setVisibility(View.VISIBLE);
        if (_position == 0) {
            childkey.clear();
            upload_list.clear();
            double _tmpNum = 0;
            for (int _repeat404 = 0; _repeat404 < oldListmap.size(); _repeat404++) {
                childkey.add(0, oldChildKey.get((int) (_tmpNum)));
                {
                    HashMap<String, Object> _item = new HashMap<>();
                    _item = oldListmap.get((int) _tmpNum);
                    upload_list.add(0, _item);
                }
                _tmpNum++;
            }
            int _index = listview1.getFirstVisiblePosition();
            View _view = listview1.getChildAt(0);
            int _top = (_view == null) ? 0 : _view.getTop();
            listview1.setAdapter(new Listview1Adapter(upload_list));
            listview1.setSelectionFromTop(_index, _top);
        }
        if (_position == 1) {
            childkey.clear();
            upload_list.clear();
            double _tmpNum = 0;
            for (int _repeat58 = 0; _repeat58 < like_tmp.size(); _repeat58++) {
                if (like_tmp.get((int) _tmpNum).get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && oldChildKey.contains(like_tmp.get((int) _tmpNum).get("key").toString())) {
                    childkey.add(0, like_tmp.get((int) _tmpNum).get("key").toString());
                    {
                        HashMap<String, Object> _item = new HashMap<>();
                        _item = oldListmap.get(oldChildKey.indexOf(like_tmp.get((int) _tmpNum).get("key").toString()));
                        upload_list.add(0, _item);
                    }
                }
                _tmpNum++;
            }
            int _index = listview1.getFirstVisiblePosition();
            View _view = listview1.getChildAt(0);
            int _top = (_view == null) ? 0 : _view.getTop();
            listview1.setAdapter(new Listview1Adapter(upload_list));
            listview1.setSelectionFromTop(_index, _top);
        }
        if (_position == 2) {
            childkey.clear();
            upload_list.clear();
            double _tmpNum = 0;
            for (int _repeat439 = 0; _repeat439 < oldListmap.size(); _repeat439++) {
                double _tmpNum2 = 0;
                boolean _tmp_bool = false;
                for (int _repeat450 = 0; _repeat450 < upload_list.size(); _repeat450++) {
                    if ((Double.parseDouble(oldListmap.get((int) _tmpNum).get("view").toString()) > Double.parseDouble(upload_list.get((int) _tmpNum2).get("view").toString())) || (Double.parseDouble(oldListmap.get((int) _tmpNum).get("view").toString()) == Double.parseDouble(upload_list.get((int) _tmpNum2).get("view").toString()))) {
                        if (!_tmp_bool) {
                            childkey.add((int) (_tmpNum2), oldChildKey.get((int) (_tmpNum)));
                            {
                                HashMap<String, Object> _item = new HashMap<>();
                                _item = oldListmap.get((int) _tmpNum);
                                upload_list.add((int) _tmpNum2, _item);
                            }
                            _tmp_bool = true;
                        }
                    }
                    _tmpNum2++;
                }
                if (!_tmp_bool) {
                    childkey.add(oldChildKey.get((int) (_tmpNum)));
                    {
                        HashMap<String, Object> _item = new HashMap<>();
                        _item = oldListmap.get((int) _tmpNum);
                        upload_list.add(_item);
                    }
                }
                _tmpNum++;
            }
            int _index = listview1.getFirstVisiblePosition();
            View _view = listview1.getChildAt(0);
            int _top = (_view == null) ? 0 : _view.getTop();
            listview1.setAdapter(new Listview1Adapter(upload_list));
            listview1.setSelectionFromTop(_index, _top);
        }
        if (_position == 3) {
            childkey.clear();
            upload_list.clear();
            listview1.setVisibility(View.GONE);
            listview2.setVisibility(View.VISIBLE);
            listview1.setAdapter(new Listview1Adapter(upload_list));
        }
    }

    private void _resetDialog() {
        d = null;
        d = new AlertDialog.Builder(this);
    }

    private void _showPlayer() {
        openNum = 2;
        linear2.setTranslationX((float) (0));
        obj.setTarget(linear2);
        obj.setPropertyName("translationY");
        obj.setFloatValues(SketchwareUtil.getDip(getApplicationContext(), 500), SketchwareUtil.getDip(getApplicationContext(), -200));
        obj.setInterpolator(new DecelerateInterpolator());
        obj.setDuration(1000);
        obj.start();
        obj2.setTarget(image_menu);
        obj2.setPropertyName("rotation");
        obj2.setFloatValues((float) (180), (float) (0));
        obj2.setInterpolator(new DecelerateInterpolator());
        obj2.setDuration(1500);
        obj2.start();
        obj3.setTarget(linear7);
        obj3.setPropertyName("alpha");
        obj3.setFloatValues((float) (0), (float) (1));
        obj3.setInterpolator(new DecelerateInterpolator());
        obj3.setDuration(500);
        obj3.start();
        _customNav(theme_map.get(0).get("colorPrimary").toString());
    }

    private void _addToPlaylist(final double _position, final double _listviewpos) {
        if (!((playlistString.size() - 1) == _position)) {
            playKeys = new Gson().fromJson(playlistMap.get((int) _position).get("keys").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
            {
                HashMap<String, Object> _item = new HashMap<>();
                _item.put("key", childkey.get((int) (_listviewpos)));
                playKeys.add(_item);
            }

            playlistMap.get((int) _position).put("keys", new Gson().toJson(playKeys));
            data.edit().putString("playlist", new Gson().toJson(playlistMap)).commit();
            playKeys.clear();
            _customSnack("Added!", 1);
        } else {

            LinearLayout mylayout = new LinearLayout(StreamerActivity.this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            mylayout.setLayoutParams(params);
            mylayout.setOrientation(LinearLayout.VERTICAL);

            final EditText myedittext = new EditText(StreamerActivity.this);
            myedittext.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
            myedittext.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

            mylayout.addView(myedittext);
            d.setView(mylayout);
            myedittext.setHint("Enter your Playlist name");
            d.setCancelable(false);
            d.setTitle("Your Playlist name");
            d.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface _dialog, int _which) {
                    _resetDialog();
                    playlistString.clear();
                    {
                        HashMap<String, Object> _item = new HashMap<>();
                        _item.put("key", childkey.get((int) (_listviewpos)));
                        playKeys.add(_item);
                    }

                    {
                        HashMap<String, Object> _item = new HashMap<>();
                        _item.put("name", myedittext.getText().toString());
                        playlistMap.add(_item);
                    }

                    playlistMap.get(playlistMap.size() - 1).put("keys", new Gson().toJson(playKeys));
                    data.edit().putString("playlist", new Gson().toJson(playlistMap)).commit();
                    playKeys.clear();
                    for (int _repeat28 = 0; _repeat28 < playlistMap.size(); _repeat28++) {
                        playlistString.add(playlistMap.get(playlistString.size()).get("name").toString());
                    }
                    playlistString.add("Add a playlist");
                    ((BaseAdapter) listview2.getAdapter()).notifyDataSetChanged();
                    _customSnack("Added!", 1);
                }
            });
            d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface _dialog, int _which) {
                    _resetDialog();
                }
            });
            d.create().show();
        }
    }

    private void _deleteSongInPlaylist(final double _position) {
        playKeys = new Gson().fromJson(playlistMap.get((int) numPlaylist).get("keys").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        playKeys.remove((int) (_position));
        childkey.remove((int) (_position));
        upload_list.remove((int) (_position));
        int _index = listview1.getFirstVisiblePosition();
        View _view = listview1.getChildAt(0);
        int _top = (_view == null) ? 0 : _view.getTop();
        listview1.setAdapter(new Listview1Adapter(upload_list));
        listview1.setSelectionFromTop(_index, _top);
        playlistMap.get((int) numPlaylist).put("keys", new Gson().toJson(playKeys));
        data.edit().putString("playlist", new Gson().toJson(playlistMap)).commit();
        playKeys.clear();
        _customSnack("Deleted!", 1);
    }

    private void _addGrid(final double _position) {
        if (!((playlistString.size() - 1) == _position)) {
            isPlaylist = true;
            childkey.clear();
            upload_list.clear();
            listview2.setVisibility(View.GONE);
            listview1.setVisibility(View.VISIBLE);
            text_zryte.setVisibility(View.GONE);
            text_zene.setVisibility(View.GONE);
            image_search.setVisibility(View.GONE);
            image_user.setVisibility(View.GONE);
            x_tab.setVisibility(View.GONE);
            text_playlist.setVisibility(View.VISIBLE);
            image_drawer.setImageResource(R.drawable.ic_arrow_back_white);
            text_playlist.setText(playlistMap.get((int) _position).get("name").toString());
            numPlaylist = _position;
            playKeys = new Gson().fromJson(playlistMap.get((int) _position).get("keys").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
            double _tmpNum = 0;
            for (int _repeat38 = 0; _repeat38 < playKeys.size(); _repeat38++) {
                if (oldChildKey.contains(playKeys.get((int) _tmpNum).get("key").toString())) {
                    childkey.add(0, playKeys.get((int) _tmpNum).get("key").toString());
                    {
                        HashMap<String, Object> _item = new HashMap<>();
                        _item = oldListmap.get(oldChildKey.indexOf(playKeys.get((int) _tmpNum).get("key").toString()));
                        upload_list.add(0, _item);
                    }
                    int _index = listview1.getFirstVisiblePosition();
                    View _view = listview1.getChildAt(0);
                    int _top = (_view == null) ? 0 : _view.getTop();
                    listview1.setAdapter(new Listview1Adapter(upload_list));
                    listview1.setSelectionFromTop(_index, _top);
                }
                _tmpNum++;
            }
            playKeys.clear();
        } else {

            LinearLayout mylayout = new LinearLayout(StreamerActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mylayout.setLayoutParams(params);
            mylayout.setOrientation(LinearLayout.VERTICAL);
            final EditText myedittext = new EditText(StreamerActivity.this);
            myedittext.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
            myedittext.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            mylayout.addView(myedittext);
            d.setView(mylayout);
            myedittext.setHint("Enter your Playlist name");
            d.setCancelable(false);
            d.setTitle("Your Playlist name");
            d.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface _dialog, int _which) {
                    _resetDialog();
                    playlistString.clear();
                    {
                        HashMap<String, Object> _item = new HashMap<>();
                        _item.put("name", myedittext.getText().toString());
                        playlistMap.add(_item);
                    }
                    playlistMap.get(playlistMap.size() - 1).put("keys", "[]");
                    data.edit().putString("playlist", new Gson().toJson(playlistMap)).commit();
                    playKeys.clear();
                    for (int _repeat66 = 0; _repeat66 < playlistMap.size(); _repeat66++) {
                        playlistString.add(playlistMap.get(playlistString.size()).get("name").toString());
                    }
                    playlistString.add("Add a playlist");
                    ((BaseAdapter) listview2.getAdapter()).notifyDataSetChanged();
                    _customSnack("Added!", 1);
                }
            });
            d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface _dialog, int _which) {
                    _resetDialog();
                }
            });
            d.create().show();
        }
    }

    private void _delGrid(final double _position) {
        if (!((playlistString.size() - 1) == _position)) {
            d.setCancelable(false);
            d.setTitle("Delete");
            d.setMessage("Are you sure to delete this playlist?");
            d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface _dialog, int _which) {
                    _resetDialog();
                    playlistMap.remove((int) (_position));
                    playlistString.remove((int) (_position));
                    ((BaseAdapter) listview2.getAdapter()).notifyDataSetChanged();
                    data.edit().putString("playlist", new Gson().toJson(playlistMap)).commit();
                    _customSnack("Deleted!", 1);
                }
            });
            d.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface _dialog, int _which) {
                    _resetDialog();
                }
            });
            d.create().show();
        }
    }

    private void _play(final String _key) {
        _CoreProgressLoading(true);
        final double _position = currentlyChild.indexOf(_key);
        currentlyPlaying = _key;
        text_title.setText(currentlyMap.get((int) _position).get("name").toString());
        if (usrname_list.contains(currentlyMap.get((int) _position).get("uid").toString())) {
            text_artist.setText(profile_map.get(usrname_list.indexOf(currentlyMap.get((int) _position).get("uid").toString())).get("username").toString());
        } else {
            text_artist.setText(currentlyMap.get((int) _position).get("uid").toString());
        }
        if (adminsList.contains(currentlyMap.get((int) _position).get("uid").toString())) {
            text_artist.setTextColor(Color.parseColor(theme_map.get(0).get("colorButton").toString()));
        } else {
            text_artist.setTextColor(Color.parseColor(theme_map.get(0).get("colorPrimaryCardText").toString()));
        }
        if (currentlyMap.get((int) _position).containsKey("img")) {
            image_album.clearColorFilter();
            Glide.with(getApplicationContext()).load(currentlyMap.get((int) _position).get("img").toString()).centerCrop().into(image_album);
        } else {
            image_album.setImageResource(R.drawable.ic_album_white);
            image_album.setColorFilter(Color.parseColor(theme_map.get(0).get("colorButtonText").toString()), PorterDuff.Mode.MULTIPLY);
        }
        zz.requestAction("play", currentlyMap.get((int) _position).get("url").toString());
    }

    private void _shape(final double _tl, final double _tr, final double _bl, final double _br, final String _BGcolor, final String _Scolor, final double _Swidth, final View _view) {
        Double tlr = _tl;
        Double trr = _tr;
        Double blr = _bl;
        Double brr = _br;
        Double sw = _Swidth;
        android.graphics.drawable.GradientDrawable s = new android.graphics.drawable.GradientDrawable();
        s.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
        s.setCornerRadii(new float[]{tlr.floatValue(), tlr.floatValue(), trr.floatValue(), trr.floatValue(), blr.floatValue(), blr.floatValue(), brr.floatValue(), brr.floatValue()});
        s.setColor(Color.parseColor(_BGcolor));
        s.setStroke(sw.intValue(), Color.parseColor(_Scolor));
        _view.setBackground(s);
    }

    private void _abandonFocus() {
        View _tmpView = this.getCurrentFocus();
        if (_tmpView != null) {
            android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(_tmpView.getWindowToken(), 0);
        }
    }

    private void _mpPreparedListener() {
        final double _position = currentlyChild.indexOf(currentlyPlaying);
        if (data.getString("nightcore", "").equals("1")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                float _tmpFloat = 1.10f + ((float) Double.parseDouble(data.getString("nightcoreSpeed", "")) * 0.05f);
                //tmservice._setNightcore(_tmpFloat);
            } else {
                data.edit().putString("nightcore", "0").commit();
                image_nightcore.setAlpha((float) (0.5d));
                _customSnack("Android Lollipop or lower doesn't support nightcore feature!", 2);
            }
        }
        _CoreProgressLoading(false);
        _refreshLikes();
        if (openNum == 0) {
            _showPlayer();
        }
        upload_map = new HashMap<>();
        upload_map = currentlyMap.get((int) _position);
        upload_map.put("view", String.valueOf((long) (Double.parseDouble(upload_map.get("view").toString()) + 1)));
        upload_text.child(currentlyPlaying).updateChildren(upload_map);
    }

    private void _mpErrorListener(final String _errorMsg) {
        _CoreProgressLoading(false);
        _customSnack(_errorMsg, 2);
        currentlyPlaying = "";
        openNum = 0;
        obj.setTarget(linear2);
        obj.setPropertyName("translationX");
        obj.setFloatValues((float) (0), (float) (SketchwareUtil.getDisplayWidthPixels(getApplicationContext()) + SketchwareUtil.getDisplayHeightPixels(getApplicationContext())));
        obj.setInterpolator(new DecelerateInterpolator());
        obj.setDuration(500);
        obj.start();
        _customNav(theme_map.get(0).get("colorBackground").toString());
        zz.clear();
        currentlyMap.clear();
        currentlyChild.clear();
    }

    private void _mpBufferingUpdate(final double _percent) {
        try {
            //seekbar1.setSecondaryProgress(((int) _percent * tmservice._getSongDuration()) / 100000);
        } catch (Exception _e) {
        }
    }

    private void _mpCompletionListener() {
        final double _position = currentlyChild.indexOf(currentlyPlaying);
        if (data.getString("fvsAsc", "").equals("0")) {
            if (currentlyMap.size() > (_position + 1)) {
                _play(currentlyChild.get((int) (_position + 1)));
            } else {
                currentlyPlaying = "";
                openNum = 0;
                obj.setTarget(linear2);
                obj.setPropertyName("translationX");
                obj.setFloatValues((float) (0), (float) (SketchwareUtil.getDisplayWidthPixels(getApplicationContext()) + SketchwareUtil.getDisplayHeightPixels(getApplicationContext())));
                obj.setInterpolator(new DecelerateInterpolator());
                obj.setDuration(500);
                obj.start();
                _customNav(theme_map.get(0).get("colorBackground").toString());
                zz.clear();
                currentlyMap.clear();
                currentlyChild.clear();
            }
        } else {
            if (data.getString("fvsAsc", "").equals("1")) {
                if (currentlyMap.size() > (_position + 1)) {
                    _play(currentlyChild.get((int) (_position + 1)));
                } else {
                    _play(currentlyChild.get(0));
                }
            } else {
                if (data.getString("fvsAsc", "").equals("2")) {
                    //tmservice._mpSeek(0);
                    //tmservice._mpStart();
                    zz.requestAction("restart-song");
                    upload_map = new HashMap<>();
                    upload_map = currentlyMap.get((int) _position);
                    upload_map.put("view", String.valueOf((long) (Double.parseDouble(upload_map.get("view").toString()) + 1)));
                    upload_text.child(currentlyPlaying).updateChildren(upload_map);
                } else {
                    _play(currentlyChild.get(SketchwareUtil.getRandom(0, currentlyMap.size() - 1)));
                }
            }
        }
    }

    private void _updateTime() {
        try {
            if (zz.isPlaying()) {
                image_play.setImageResource(R.drawable.ic_pause_white);
                seekbar1.setProgress(zz.getCurrentDuration() / 1000);
            } else {
                image_play.setImageResource(R.drawable.ic_play_arrow_white);
            }
        } catch (Exception _e) {
        }
    }

    private void _handleMpError(final String _msg) {
        _CoreProgressLoading(false);
        _customSnack(_msg, 2);
        currentlyPlaying = "";
        openNum = 0;
        obj.setTarget(linear2);
        obj.setPropertyName("translationX");
        obj.setFloatValues((float) (0), (float) (SketchwareUtil.getDisplayWidthPixels(getApplicationContext()) + SketchwareUtil.getDisplayHeightPixels(getApplicationContext())));
        obj.setInterpolator(new DecelerateInterpolator());
        obj.setDuration(500);
        obj.start();
        _customNav(theme_map.get(0).get("colorBackground").toString());
        zz.requestAction("reset");
        currentlyMap.clear();
        currentlyChild.clear();
    }

    private void _bindSvc() {
    }

    private void _unbindSvc() {
    }

    @Deprecated
    public void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

    @Deprecated
    public int getLocationX(View _v) {
        int[] _location = new int[2];
        _v.getLocationInWindow(_location);
        return _location[0];
    }

    @Deprecated
    public int getLocationY(View _v) {
        int[] _location = new int[2];
        _v.getLocationInWindow(_location);
        return _location[1];
    }

    @Deprecated
    public int getRandom(int _min, int _max) {
        Random random = new Random();
        return random.nextInt(_max - _min + 1) + _min;
    }

    @Deprecated
    public float getDip(int _input) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
    }

    @Deprecated
    public int getDisplayWidthPixels() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Deprecated
    public int getDisplayHeightPixels() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    public static class Drawables {
        public static android.graphics.drawable.Drawable getSelectableDrawableFor(int color) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                android.graphics.drawable.StateListDrawable stateListDrawable = new android.graphics.drawable.StateListDrawable();
                stateListDrawable.addState(
                        new int[]{android.R.attr.state_pressed},
                        new android.graphics.drawable.ColorDrawable(Color.parseColor("#ffffff"))
                );
                stateListDrawable.addState(
                        new int[]{android.R.attr.state_focused},
                        new android.graphics.drawable.ColorDrawable(Color.parseColor("#00ffffff"))
                );
                stateListDrawable.addState(
                        new int[]{},
                        new android.graphics.drawable.ColorDrawable(Color.parseColor("#00ffffff"))
                );
                return stateListDrawable;
            } else {
                android.content.res.ColorStateList pressedColor = android.content.res.ColorStateList.valueOf(color);
                android.graphics.drawable.ColorDrawable defaultColor = new android.graphics.drawable.ColorDrawable(Color.parseColor("#00ffffff"));

                android.graphics.drawable.Drawable rippleColor = getRippleColor(color);
                return new android.graphics.drawable.RippleDrawable(
                        pressedColor,
                        defaultColor,
                        rippleColor
                );
            }
        }

        private static android.graphics.drawable.Drawable getRippleColor(int color) {
            float[] outerRadii = new float[8];
            Arrays.fill(outerRadii, 0);
            android.graphics.drawable.shapes.RoundRectShape r = new android.graphics.drawable.shapes.RoundRectShape(outerRadii, null, null);

            android.graphics.drawable.ShapeDrawable shapeDrawable = new
                    android.graphics.drawable.ShapeDrawable(r);
            shapeDrawable.getPaint().setColor(color);
            return shapeDrawable;
        }

        private static int lightenOrDarken(int color, double fraction) {
            if (canLighten(color, fraction)) {
                return lighten(color, fraction);
            } else {
                return darken(color, fraction);
            }
        }

        private static int lighten(int color, double fraction) {
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            red = lightenColor(red, fraction);
            green = lightenColor(green, fraction);
            blue = lightenColor(blue, fraction);
            int alpha = Color.alpha(color);
            return Color.argb(alpha, red, green, blue);
        }

        private static int darken(int color, double fraction) {
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            red = darkenColor(red, fraction);
            green = darkenColor(green, fraction);
            blue = darkenColor(blue, fraction);
            int alpha = Color.alpha(color);

            return Color.argb(alpha, red, green, blue);
        }

        private static boolean canLighten(int color, double fraction) {
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            return canLightenComponent(red, fraction)
                    && canLightenComponent(green, fraction)
                    && canLightenComponent(blue, fraction);
        }

        private static boolean canLightenComponent(int colorComponent, double fraction) {
            int red = Color.red(colorComponent);
            int green = Color.green(colorComponent);
            int blue = Color.blue(colorComponent);
            return red + (red * fraction) < 255
                    && green + (green * fraction) < 255
                    && blue + (blue * fraction) < 255;
        }

        private static int darkenColor(int color, double fraction) {
            return (int) Math.max(color - (color * fraction), 0);
        }

        private static int lightenColor(int color, double fraction) {
            return (int) Math.min(color + (color * fraction), 255);
        }
    }

    public static class CircleDrawables {
        public static android.graphics.drawable.Drawable getSelectableDrawableFor(int color) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                android.graphics.drawable.StateListDrawable stateListDrawable = new android.graphics.drawable.StateListDrawable();
                stateListDrawable.addState(
                        new int[]{android.R.attr.state_pressed},
                        new android.graphics.drawable.ColorDrawable(Color.parseColor("#ffffff"))
                );
                stateListDrawable.addState(
                        new int[]{android.R.attr.state_focused},
                        new android.graphics.drawable.ColorDrawable(Color.parseColor("#00ffffff"))
                );
                stateListDrawable.addState(
                        new int[]{},
                        new android.graphics.drawable.ColorDrawable(Color.parseColor("#00ffffff"))
                );
                return stateListDrawable;
            } else {
                android.content.res.ColorStateList pressedColor = android.content.res.ColorStateList.valueOf(color);
                android.graphics.drawable.ColorDrawable defaultColor = new android.graphics.drawable.ColorDrawable(Color.parseColor("#00ffffff"));

                android.graphics.drawable.Drawable rippleColor = getRippleColor(color);
                return new android.graphics.drawable.RippleDrawable(
                        pressedColor,
                        defaultColor,
                        rippleColor
                );
            }
        }

        private static android.graphics.drawable.Drawable getRippleColor(int color) {
            float[] outerRadii = new float[180];
            Arrays.fill(outerRadii, 80);
            android.graphics.drawable.shapes.RoundRectShape r = new android.graphics.drawable.shapes.RoundRectShape(outerRadii, null, null);

            android.graphics.drawable.ShapeDrawable shapeDrawable = new
                    android.graphics.drawable.ShapeDrawable(r);
            shapeDrawable.getPaint().setColor(color);
            return shapeDrawable;
        }

        private static int lightenOrDarken(int color, double fraction) {
            if (canLighten(color, fraction)) {
                return lighten(color, fraction);
            } else {
                return darken(color, fraction);
            }
        }

        private static int lighten(int color, double fraction) {
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            red = lightenColor(red, fraction);
            green = lightenColor(green, fraction);
            blue = lightenColor(blue, fraction);
            int alpha = Color.alpha(color);
            return Color.argb(alpha, red, green, blue);
        }

        private static int darken(int color, double fraction) {
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            red = darkenColor(red, fraction);
            green = darkenColor(green, fraction);
            blue = darkenColor(blue, fraction);
            int alpha = Color.alpha(color);

            return Color.argb(alpha, red, green, blue);
        }

        private static boolean canLighten(int color, double fraction) {
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            return canLightenComponent(red, fraction)
                    && canLightenComponent(green, fraction)
                    && canLightenComponent(blue, fraction);
        }

        private static boolean canLightenComponent(int colorComponent, double fraction) {
            int red = Color.red(colorComponent);
            int green = Color.green(colorComponent);
            int blue = Color.blue(colorComponent);
            return red + (red * fraction) < 255
                    && green + (green * fraction) < 255
                    && blue + (blue * fraction) < 255;
        }

        private static int darkenColor(int color, double fraction) {
            return (int) Math.max(color - (color * fraction), 0);
        }

        private static int lightenColor(int color, double fraction) {
            return (int) Math.min(color + (color * fraction), 255);
        }
    }

    public class Listview1Adapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> _data;

        public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _view, ViewGroup _viewGroup) {
            LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View _v = _view;
            if (_v == null) {
                _v = _inflater.inflate(R.layout.list, null);
            }

            final LinearLayout linear_base = _v.findViewById(R.id.linear_base);
            final ImageView imageview4 = _v.findViewById(R.id.imageview4);
            final LinearLayout linear3 = _v.findViewById(R.id.linear3);
            final LinearLayout linear4 = _v.findViewById(R.id.linear4);
            final ImageView imageview5 = _v.findViewById(R.id.imageview5);
            final ImageView imageview6 = _v.findViewById(R.id.imageview6);
            final LinearLayout linear5 = _v.findViewById(R.id.linear5);
            final TextView textview1 = _v.findViewById(R.id.textview1);
            final TextView textview2 = _v.findViewById(R.id.textview2);
            final LinearLayout linear2 = _v.findViewById(R.id.linear2);
            final ImageView imageview1 = _v.findViewById(R.id.imageview1);
            final TextView textview3 = _v.findViewById(R.id.textview3);
            final ImageView imageview2 = _v.findViewById(R.id.imageview2);
            final TextView textview4 = _v.findViewById(R.id.textview4);
            final ImageView imageview3 = _v.findViewById(R.id.imageview3);
            final TextView textview5 = _v.findViewById(R.id.textview5);

            try {
                if (!animateList.contains(String.valueOf((long) (_position)))) {
                    animateList.add(String.valueOf((long) (_position)));
                    TranslateAnimation _animate = new TranslateAnimation(0, 0, linear_base.getHeight(), 0);
                    _animate.setDuration(500);
                    _animate.setFillAfter(true);
                    linear_base.startAnimation(_animate);
                }
                textview1.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
                if (adminsList.contains(upload_list.get(_position).get("uid").toString())) {
                    textview2.setTextColor(Color.parseColor(theme_map.get(0).get("colorButton").toString()));
                    android.graphics.drawable.GradientDrawable _gD = new android.graphics.drawable.GradientDrawable();
                    _gD.setColor(Color.parseColor(theme_map.get(0).get("colorBackground").toString()));
                    _gD.setShape(android.graphics.drawable.GradientDrawable.OVAL);
                    _gD.setStroke(5, Color.parseColor(theme_map.get(0).get("colorButton").toString()));
                    imageview6.setBackground(_gD);
                } else {
                    textview2.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundText").toString()));
                    imageview6.setBackgroundColor(Color.TRANSPARENT);
                }
                imageview1.setColorFilter(Color.parseColor(theme_map.get(0).get("colorBackgroundImage").toString()), PorterDuff.Mode.MULTIPLY);
                textview3.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundImage").toString()));
                imageview2.setColorFilter(Color.parseColor(theme_map.get(0).get("colorButton").toString()), PorterDuff.Mode.MULTIPLY);
                textview4.setTextColor(Color.parseColor(theme_map.get(0).get("colorButton").toString()));
                imageview3.setColorFilter(Color.parseColor(theme_map.get(0).get("colorBackgroundImage").toString()), PorterDuff.Mode.MULTIPLY);
                textview5.setTextColor(Color.parseColor(theme_map.get(0).get("colorBackgroundImage").toString()));
                linear_base.setBackground(Drawables.getSelectableDrawableFor(Color.parseColor(theme_map.get(0).get("colorRipple").toString())));
                linear_base.setClickable(true);
                linear3.setBackgroundColor(Color.parseColor(theme_map.get(0).get("colorButton").toString()));
                imageview5.setColorFilter(Color.parseColor(theme_map.get(0).get("colorButtonText").toString()), PorterDuff.Mode.MULTIPLY);
                textview1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
                textview2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
                textview3.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
                textview4.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
                textview5.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
                if (img_maps.containsKey(upload_list.get(_position).get("uid").toString())) {
                    imageview6.clearColorFilter();
                    Glide.with(getApplicationContext()).asBitmap().load(img_maps.get(upload_list.get(_position).get("uid").toString()).toString()).centerCrop().into(new com.bumptech.glide.request.target.BitmapImageViewTarget(imageview6) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            androidx.core.graphics.drawable.RoundedBitmapDrawable circularBitmapDrawable = androidx.core.graphics.drawable.RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            imageview6.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                } else {
                    imageview6.setColorFilter(Color.parseColor(theme_map.get(0).get("colorBackgroundImage").toString()), PorterDuff.Mode.MULTIPLY);
                    imageview6.setImageResource(R.drawable.ic_person);
                }
                if (data.getString("showPreview", "").equals("1")) {
                    if (upload_list.get(_position).containsKey("img")) {
                        linear3.setVisibility(View.GONE);
                        imageview4.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(Uri.parse(upload_list.get(_position).get("img").toString())).into(imageview4);
                    } else {
                        linear3.setVisibility(View.VISIBLE);
                        imageview4.setVisibility(View.GONE);
                    }
                } else {
                    linear3.setVisibility(View.GONE);
                    imageview4.setVisibility(View.GONE);
                }
                textview1.setText(upload_list.get(_position).get("name").toString());
                if (usrname_list.contains(upload_list.get(_position).get("uid").toString())) {
                    textview2.setText(profile_map.get(usrname_list.indexOf(upload_list.get(_position).get("uid").toString())).get("username").toString());
                } else {
                    textview2.setText(upload_list.get(_position).get("uid").toString());
                }
                if (Double.parseDouble(upload_list.get(_position).get("view").toString()) > 999) {
                    textview3.setText(new DecimalFormat("0.00").format(Double.parseDouble(upload_list.get(_position).get("view").toString()) / 1000).concat("K"));
                } else {
                    textview3.setText(upload_list.get(_position).get("view").toString());
                }
                if (likes_map.containsKey(childkey.get(_position))) {
                    textview4.setText(likes_map.get(childkey.get(_position)).toString());
                } else {
                    textview4.setText("0");
                }
                if (commentsMap.containsKey(childkey.get(_position))) {
                    textview5.setText(commentsMap.get(childkey.get(_position)).toString());
                } else {
                    textview5.setText("0");
                }
                linear_base.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View _view) {
                        //if (tmservice != null) {
                            currentlyMap.clear();
                            currentlyChild.clear();
                            for (int _repeat244 = 0; _repeat244 < upload_list.size(); _repeat244++) {
                                {
                                    HashMap<String, Object> _item = new HashMap<>();
                                    _item = upload_list.get(currentlyMap.size());
                                    currentlyMap.add(_item);
                                }
                            }
                            for (int _repeat246 = 0; _repeat246 < childkey.size(); _repeat246++) {
                                currentlyChild.add(childkey.get(currentlyChild.size()));
                            }
                            _play(currentlyChild.get(_position));
                        //}
                    }
                });
                linear_base.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View _view) {
                        if (!(tabsPos == 3)) {
                            d.setCancelable(false);
                            d.setAdapter(new ArrayAdapter(StreamerActivity.this, android.R.layout.simple_list_item_1, dialog_list), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dia, int _pos_dialog) {
                                    _resetDialog();
                                    if (_pos_dialog == 0) {
                                        intent.setClass(getApplicationContext(), ProfileActivity.class);
                                        intent.putExtra("uid", upload_list.get(_position).get("uid").toString());
                                        startActivity(intent);
                                    } else {
                                        if (_pos_dialog == 1) {
                                            if (upload_list.get(_position).get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) || isAdmin) {
                                                d.setCancelable(false);
                                                d.setTitle("Delete Music?");
                                                d.setMessage("Are You Sure To Delete ".concat(upload_list.get(_position).get("name").toString().concat("?")));
                                                d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface _dialog, int _which) {
                                                        _firebase_storage.getReferenceFromUrl(upload_list.get(_position).get("url").toString()).delete().addOnSuccessListener(_upload_storage_delete_success_listener).addOnFailureListener(_upload_storage_failure_listener);
                                                        if (upload_list.get(_position).containsKey("img")) {
                                                            _firebase_storage.getReferenceFromUrl(upload_list.get(_position).get("img").toString()).delete().addOnSuccessListener(_music_image_delete_success_listener).addOnFailureListener(_music_image_failure_listener);
                                                        }
                                                        upload_text.child(childkey.get(_position)).removeValue();
                                                        _customSnack("Delete success!", 1);
                                                        _resetDialog();
                                                    }
                                                });
                                                d.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface _dialog, int _which) {
                                                        _resetDialog();
                                                    }
                                                });
                                                d.create().show();
                                            } else {
                                                _customSnack("Can't delete! Access denied.", 2);
                                            }
                                        } else {
                                            if (_pos_dialog == 2) {
                                                intent.setClass(getApplicationContext(), MessageActivity.class);
                                                intent.putExtra("key", childkey.get(_position));
                                                startActivity(intent);
                                            } else {
                                                if (_pos_dialog == 3) {
                                                    d.setCancelable(false);
                                                    d.setAdapter(new ArrayAdapter(StreamerActivity.this, android.R.layout.simple_list_item_1, playlistString), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dia, int _pos_dialog2) {
                                                            _resetDialog();
                                                            _addToPlaylist(_pos_dialog2, _position);
                                                        }
                                                    });
                                                    d.show();
                                                } else {
                                                    if (_pos_dialog == 4) {

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            });

                            d.show();
                        } else {
                            d.setCancelable(false);
                            d.setTitle("Delete");
                            d.setMessage("Are you sure to delete this song from your playlist?");
                            d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface _dialog, int _which) {
                                    _resetDialog();
                                    _deleteSongInPlaylist(_position);
                                }
                            });
                            d.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface _dialog, int _which) {
                                    _resetDialog();
                                }
                            });
                            d.create().show();
                        }
                        return true;
                    }
                });
            } catch (Exception _e) {
                showMessage("DEBUG: " + _e.getMessage());
            }

            return _v;
        }
    }
}
