package tw.music.streamer;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Timer _timer = new Timer();
    //private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();

    private HashMap<String, Object> map = new HashMap<>();
    private boolean isBanned = false;
    private double banNum = 0;
    private HashMap<String, Object> tmpMapVar = new HashMap<>();

    private ArrayList<HashMap<String, Object>> profile_map = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> theme_map = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> tmpMap = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> bansMap = new ArrayList<>();

    private LinearLayout linear1;
    private LinearLayout linear2;
    private LinearLayout linear6;
    private LinearLayout linear7;
    private LinearLayout lineartm;
    private LinearLayout linear4;
    private ImageView imageview1;
    private TextView textview11;
    private TextView textview5;
    private LinearLayout linear5;
    private Button button1;
    private LinearLayout linear3;
    private ImageView image_user;
    private EditText einput_name;
    private TextView textview7;
    private TextView textview8;
    private TextView textview9;
    private TextView textview10;
    private TextView textview1;
    private TextView textview2;
    private TextView textview6;
    private TextView textview3;
    private ImageView imageview2;
    private TextView textview4;

    private SharedPreferences data;
    private DatabaseReference update_db;// = FirebaseDatabase.getInstance().getReference("update/version");
    private ChildEventListener _update_db_child_listener;
    private Intent intent = new Intent();
    private AlertDialog.Builder d;
    private DatabaseReference profile;// = FirebaseDatabase.getInstance().getReference("profile/text");
    private ChildEventListener _profile_child_listener;
    private FirebaseAuth Auth;
    private OnCompleteListener<AuthResult> _Auth_create_user_listener;
    private OnCompleteListener<AuthResult> _Auth_sign_in_listener;
    private OnCompleteListener<Void> _Auth_reset_password_listener;
    private Intent activityChanger = new Intent();
    private DatabaseReference prof_img;// = FirebaseDatabase.getInstance().getReference("profile/image");
    private ChildEventListener _prof_img_child_listener;
    private TimerTask timer;
    private ObjectAnimator objectanim3 = new ObjectAnimator();
    private RequestNetwork internetchecker;
    private RequestNetwork.RequestListener _internetchecker_request_listener;
    private TimerTask delaynointernet;
    private DatabaseReference prof_bans;// = FirebaseDatabase.getInstance().getReference("profile/bans");
    private ChildEventListener _prof_bans_child_listener;
    private RequestNetwork rn;
    private RequestNetwork.RequestListener _rn_request_listener;
    private android.content.pm.PackageInfo packageInfo;
    private boolean _isNoInternet = false;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.main);
        //com.google.firebase.FirebaseApp.initializeApp(this);
        //initialize(_savedInstanceState);
        //initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        linear6 = findViewById(R.id.linear6);
        linear7 = findViewById(R.id.linear7);
        lineartm = findViewById(R.id.lineartm);
        linear4 = findViewById(R.id.linear4);
        imageview1 = findViewById(R.id.imageview1);
        textview11 = findViewById(R.id.textview11);
        textview5 = findViewById(R.id.textview5);
        linear5 = findViewById(R.id.linear5);
        button1 = findViewById(R.id.button1);
        linear3 = findViewById(R.id.linear3);
        image_user = findViewById(R.id.image_user);
        einput_name = findViewById(R.id.einput_name);
        textview7 = findViewById(R.id.textview7);
        textview8 = findViewById(R.id.textview8);
        textview9 = findViewById(R.id.textview9);
        textview10 = findViewById(R.id.textview10);
        textview1 = findViewById(R.id.textview1);
        textview2 = findViewById(R.id.textview2);
        textview6 = findViewById(R.id.textview6);
        textview3 = findViewById(R.id.textview3);
        imageview2 = findViewById(R.id.imageview2);
        textview4 = findViewById(R.id.textview4);
        data = getSharedPreferences("teamdata", Activity.MODE_PRIVATE);
        d = new AlertDialog.Builder(this);
        Auth = FirebaseAuth.getInstance();
        internetchecker = new RequestNetwork(this);
        rn = new RequestNetwork(this);

        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                intent.setData(Uri.parse("https://zrytezene.xyz"));
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (einput_name.getText().toString().trim().equals("") || (einput_name.getText().toString().trim().length() > 30)) {
                    _customSnack("Username field can't be empty or more than 30 characters! ", 2);
                } else {
                    map = new HashMap<>();
                    map.put("profile", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    map.put("username", einput_name.getText().toString().trim());
                    map.put("bio", "Hi, i'm using ZryteZene now!");
                    profile.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);
                    activityChanger.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(activityChanger);
                    finish();
                }
            }
        });

        image_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                activityChanger.setClass(getApplicationContext(), FilepickerActivity.class);
                activityChanger.putExtra("fileType", "image/*");
                startActivity(activityChanger);
            }
        });

        textview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                _randomEmoteUwU();
            }
        });

        imageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                _randomEmoteUwU();
            }
        });

        _update_db_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if ((Double.parseDouble(_childValue.get("version").toString()) > (double) packageInfo.versionCode) || _childValue.get("closed").toString().equals("1")) {
                    update_db.removeEventListener(_update_db_child_listener);
                    profile.removeEventListener(_profile_child_listener);
                    prof_img.removeEventListener(_prof_img_child_listener);
                    prof_bans.removeEventListener(_prof_bans_child_listener);
                    activityChanger.setClass(getApplicationContext(), UpdateappActivity.class);
                    activityChanger.putExtra("updateData", new Gson().toJson(_childValue));
                    startActivity(activityChanger);
                    finish();
                } else {
                    tmpMapVar = new HashMap<>(_childValue);
                    prof_img.addChildEventListener(_prof_img_child_listener);
                    if (_checkPermission() && (data.getString("setup", "").equals("1") && ((FirebaseAuth.getInstance().getCurrentUser() != null) && data.getString("license", "").equals("1")))) {
                        if (Auth.getCurrentUser().isEmailVerified()) {
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
                                    if (profile_map.size() > 0) {
                                        double _tmpNum = 0;
                                        boolean _endTask = false;
                                        for (int _repeat35 = 0; _repeat35 < profile_map.size(); _repeat35++) {
                                            if (profile_map.get((int) _tmpNum).get("profile").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                                _endTask = true;
                                                if (!profile_map.get((int) _tmpNum).containsKey("bio")) {
                                                    map = new HashMap<>();
                                                    map = profile_map.get((int) _tmpNum);
                                                    map.put("bio", "Hi, i'm using ZryteZene now!");
                                                    profile.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(map);
                                                }
                                                prof_bans.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot _dataSnapshot) {
                                                        bansMap = new ArrayList<>();
                                                        try {
                                                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                                                            };
                                                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                                                HashMap<String, Object> _map = _data.getValue(_ind);
                                                                bansMap.add(_map);
                                                            }
                                                        } catch (Exception _e) {
                                                            _e.printStackTrace();
                                                        }
                                                        if (bansMap.size() > 0) {
                                                            prof_bans.addChildEventListener(_prof_bans_child_listener);
                                                        } else {
                                                            _chkChglog();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError _databaseError) {
                                                    }
                                                });
                                            } else {
                                                if ((_tmpNum == (profile_map.size() - 1)) && !_endTask) {
                                                    linear1.setVisibility(View.GONE);
                                                    linear2.setVisibility(View.VISIBLE);
                                                    lineartm.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                            _tmpNum++;
                                        }
                                    } else {
                                        linear1.setVisibility(View.GONE);
                                        linear2.setVisibility(View.VISIBLE);
                                        lineartm.setVisibility(View.INVISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError _databaseError) {
                                }
                            });
                        } else {
                            FirebaseAuth.getInstance().signOut();
                            SketchwareUtil.showMessage(getApplicationContext(), "Your account isn't verified! Please relogin.");
                            update_db.removeEventListener(_update_db_child_listener);
                            profile.removeEventListener(_profile_child_listener);
                            prof_img.removeEventListener(_prof_img_child_listener);
                            prof_bans.removeEventListener(_prof_bans_child_listener);
                            activityChanger.setClass(getApplicationContext(), WizardActivity.class);
                            startActivity(activityChanger);
                            finish();
                        }
                    } else {
                        update_db.removeEventListener(_update_db_child_listener);
                        profile.removeEventListener(_profile_child_listener);
                        prof_img.removeEventListener(_prof_img_child_listener);
                        prof_bans.removeEventListener(_prof_bans_child_listener);
                        activityChanger.setClass(getApplicationContext(), WizardActivity.class);
                        startActivity(activityChanger);
                        finish();
                    }
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
        update_db.addChildEventListener(_update_db_child_listener);

        _profile_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

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
        profile.addChildEventListener(_profile_child_listener);

        _prof_img_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
                    if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        image_user.clearColorFilter();
                        Glide.with(getApplicationContext()).asBitmap().load(_childValue.get("url").toString()).centerCrop().into(new com.bumptech.glide.request.target.BitmapImageViewTarget(image_user) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                androidx.core.graphics.drawable.RoundedBitmapDrawable circularBitmapDrawable = androidx.core.graphics.drawable.RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                image_user.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if ((FirebaseAuth.getInstance().getCurrentUser() != null)) {
                    if (_childKey.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        Glide.with(getApplicationContext()).asBitmap().load(_childValue.get("url").toString()).centerCrop().into(new com.bumptech.glide.request.target.BitmapImageViewTarget(image_user) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                androidx.core.graphics.drawable.RoundedBitmapDrawable circularBitmapDrawable = androidx.core.graphics.drawable.RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                image_user.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                    }
                }
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

        objectanim3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator _param1) {

            }

            @Override
            public void onAnimationEnd(Animator _param1) {
                internetchecker.startRequestNetwork(RequestNetworkController.GET, "https://www.google.com", "A", _internetchecker_request_listener);
            }

            @Override
            public void onAnimationCancel(Animator _param1) {

            }

            @Override
            public void onAnimationRepeat(Animator _param1) {

            }
        });

        _internetchecker_request_listener = new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String _param1, String _param2) {
                final String _tag = _param1;
                final String _response = _param2;
                if (_isNoInternet) {
                    _isNoInternet = false;
                    textview4.setText("Thx!");
                    delaynointernet = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    objectanim3.setTarget(linear4);
                                    objectanim3.setPropertyName("translationY");
                                    objectanim3.setFloatValues(SketchwareUtil.getDip(getApplicationContext(), -75), (float) (0));
                                    objectanim3.setDuration(500);
                                    objectanim3.setInterpolator(new DecelerateInterpolator());
                                    objectanim3.start();
                                }
                            });
                        }
                    };
                    _timer.schedule(delaynointernet, 1500);
                } else {
                    internetchecker.startRequestNetwork(RequestNetworkController.GET, "https://www.google.com", "A", _internetchecker_request_listener);
                }
            }

            @Override
            public void onErrorResponse(String _param1, String _param2) {
                final String _tag = _param1;
                final String _message = _param2;
                if (_isNoInternet) {
                    internetchecker.startRequestNetwork(RequestNetworkController.GET, "https://www.google.com", "A", _internetchecker_request_listener);
                } else {
                    _isNoInternet = true;
                    textview4.setText("Please enable your internet!");
                    _randomEmoteUwU();
                    objectanim3.setTarget(linear4);
                    objectanim3.setPropertyName("translationY");
                    objectanim3.setFloatValues((float) (0), SketchwareUtil.getDip(getApplicationContext(), -75));
                    objectanim3.setDuration(500);
                    objectanim3.setInterpolator(new DecelerateInterpolator());
                    objectanim3.start();
                }
            }
        };

        _prof_bans_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(_childKey)) {
                    isBanned = true;
                    textview9.setText("Reason : ".concat(_childValue.get("reason").toString()));
                    linear1.setVisibility(View.GONE);
                    linear6.setVisibility(View.VISIBLE);
                    lineartm.setVisibility(View.GONE);
                } else {
                    if ((banNum == (bansMap.size() - 1)) && !isBanned) {
                        _chkChglog();
                    }
                }
                banNum++;
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
        prof_bans.addChildEventListener(_prof_bans_child_listener);

        _rn_request_listener = new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String _param1, String _param2) {
                final String _tag = _param1;
                final String _response = _param2;

            }

            @Override
            public void onErrorResponse(String _param1, String _param2) {
                final String _tag = _param1;
                final String _message = _param2;

            }
        };

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
        try {
            packageInfo = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            textview6.setText(" " + packageInfo.versionName);
        } catch (Exception _e) {
        }
        einput_name.getBackground().setColorFilter(Color.parseColor("#2196F3"), PorterDuff.Mode.SRC_ATOP);
        update_db.removeEventListener(_update_db_child_listener);
        prof_img.removeEventListener(_prof_img_child_listener);
        prof_bans.removeEventListener(_prof_bans_child_listener);
        linear2.setVisibility(View.GONE);
        linear6.setVisibility(View.GONE);
        linear7.setVisibility(View.GONE);
        textview11.setVisibility(View.GONE);
        image_user.setColorFilter(0xFF2196F3, PorterDuff.Mode.MULTIPLY);
        textview1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
        textview2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesansbold.ttf"), 0);
        textview3.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesansbold.ttf"), 0);
        textview4.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
        textview5.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
        textview6.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
        textview7.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
        textview8.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
        textview9.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
        textview10.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
        textview11.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
        einput_name.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
        button1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf"), 0);
        _shape(SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), "#2196F3", "#FFFFFF", 0, button1);
        lineartm.setTranslationY(SketchwareUtil.getDip(getApplicationContext(), -75));
        if (data.getString("fvsAsc", "").equals("")) {
            data.edit().putString("fvsAsc", "0").commit();
        }
        if (data.getString("nightcore", "").equals("") || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            data.edit().putString("nightcore", "0").commit();
        }
        if (data.getString("griddata", "").equals("")) {
            {
                HashMap<String, Object> _item = new HashMap<>();
                _item.put("themesname", "$default_1$");
                theme_map.add(_item);
            }

            theme_map.get(0).put("themesjson", "[{\"colorPrimary\":\"#2196F3\",\"colorPrimaryDark\":\"#1769AA\",\"colorBackgroundText\":\"#000000\",\"colorBackground\":\"#FFFFFF\",\"colorButton\":\"#F50057\",\"shadow\":\"1\",\"colorHint\":\"#A8A8A8\",\"colorRipple\":\"#40000000\",\"colorPrimaryCardImage\":\"#000000\",\"version\":\"2\",\"colorPrimaryText\":\"#FFFFFF\",\"colorPrimaryImage\":\"#FFFFFF\",\"colorBackgroundImage\":\"#2196F3\",\"colorBackgroundCardText\":\"#000000\",\"statusbarIcon\":\"1\",\"colorBackgroundCard\":\"#FFFFFF\",\"colorButtonText\":\"#FFFFFF\",\"colorPrimaryCardText\":\"#000000\",\"colorPrimaryCard\":\"#FFFFFF\",\"colorBackgroundCardImage\":\"#000000\"}]");
            theme_map.get(0).put("themesinfo", "The default style of TeamMusic");
            theme_map.get(0).put("themesauthor", "TeamWorks DEV");
            theme_map.get(0).put("os-thm-version", "2");
            {
                HashMap<String, Object> _item = new HashMap<>();
                _item.put("themesname", "$default_2$");
                theme_map.add(_item);
            }

            theme_map.get(1).put("themesjson", "[{\"colorPrimary\":\"#2196F3\",\"colorPrimaryDark\":\"#252525\",\"colorBackgroundText\":\"#FFFFFF\",\"colorBackground\":\"#252525\",\"colorButton\":\"#F50057\",\"shadow\":\"1\",\"colorHint\":\"#808080\",\"colorRipple\":\"#40FFFFFF\",\"colorPrimaryCardImage\":\"#98A0A8\",\"version\":\"2\",\"colorPrimaryText\":\"#FFFFFF\",\"colorPrimaryImage\":\"#FFFFFF\",\"colorBackgroundImage\":\"#2196F3\",\"colorBackgroundCardText\":\"#98A0A8\",\"statusbarIcon\":\"1\",\"colorBackgroundCard\":\"#404040\",\"colorButtonText\":\"#FFFFFF\",\"colorPrimaryCardText\":\"#98A0A8\",\"colorPrimaryCard\":\"#404040\",\"colorBackgroundCardImage\":\"#98A0A8\"}]");
            theme_map.get(1).put("themesinfo", "A Material dark theme for TeamMusic.");
            theme_map.get(1).put("themesauthor", "Axhyre");
            theme_map.get(1).put("os-thm-version", "2");
            data.edit().putString("griddata", new Gson().toJson(theme_map)).commit();
        } else {
            theme_map = new Gson().fromJson(data.getString("griddata", ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
            tmpMapVar = new HashMap<>();
            tmpMapVar = theme_map.get(0);
            if (!new Gson().toJson(tmpMapVar).equals("{\"themesjson\":\"[{\\\"colorPrimary\\\":\\\"#2196F3\\\",\\\"colorPrimaryDark\\\":\\\"#1769AA\\\",\\\"colorBackgroundText\\\":\\\"#000000\\\",\\\"colorBackground\\\":\\\"#FFFFFF\\\",\\\"colorButton\\\":\\\"#F50057\\\",\\\"shadow\\\":\\\"1\\\",\\\"colorHint\\\":\\\"#A8A8A8\\\",\\\"colorRipple\\\":\\\"#40000000\\\",\\\"colorPrimaryCardImage\\\":\\\"#000000\\\",\\\"version\\\":\\\"2\\\",\\\"colorPrimaryText\\\":\\\"#FFFFFF\\\",\\\"colorPrimaryImage\\\":\\\"#FFFFFF\\\",\\\"colorBackgroundImage\\\":\\\"#2196F3\\\",\\\"colorBackgroundCardText\\\":\\\"#000000\\\",\\\"statusbarIcon\\\":\\\"1\\\",\\\"colorBackgroundCard\\\":\\\"#FFFFFF\\\",\\\"colorButtonText\\\":\\\"#FFFFFF\\\",\\\"colorPrimaryCardText\\\":\\\"#000000\\\",\\\"colorPrimaryCard\\\":\\\"#FFFFFF\\\",\\\"colorBackgroundCardImage\\\":\\\"#000000\\\"}]\",\"themesauthor\":\"TeamWorks DEV\",\"themesname\":\"$default_1$\",\"themesinfo\":\"The default style of TeamMusic\",\"os-thm-version\":\"2\"}")) {
                theme_map.get(0).put("themesname", "$default_1$");
                theme_map.get(0).put("themesjson", "[{\"colorPrimary\":\"#2196F3\",\"colorPrimaryDark\":\"#1769AA\",\"colorBackgroundText\":\"#000000\",\"colorBackground\":\"#FFFFFF\",\"colorButton\":\"#F50057\",\"shadow\":\"1\",\"colorHint\":\"#A8A8A8\",\"colorRipple\":\"#40000000\",\"colorPrimaryCardImage\":\"#000000\",\"version\":\"2\",\"colorPrimaryText\":\"#FFFFFF\",\"colorPrimaryImage\":\"#FFFFFF\",\"colorBackgroundImage\":\"#2196F3\",\"colorBackgroundCardText\":\"#000000\",\"statusbarIcon\":\"1\",\"colorBackgroundCard\":\"#FFFFFF\",\"colorButtonText\":\"#FFFFFF\",\"colorPrimaryCardText\":\"#000000\",\"colorPrimaryCard\":\"#FFFFFF\",\"colorBackgroundCardImage\":\"#000000\"}]");
                theme_map.get(0).put("themesinfo", "The default style of TeamMusic");
                theme_map.get(0).put("themesauthor", "TeamWorks DEV");
                theme_map.get(0).put("os-thm-version", "2");
                data.edit().putString("griddata", new Gson().toJson(theme_map)).commit();
            }
            if (theme_map.size() > 1) {
                tmpMapVar = theme_map.get(1);
                if (!new Gson().toJson(tmpMapVar).equals("{\"themesjson\":\"[{\\\"colorPrimary\\\":\\\"#2196F3\\\",\\\"colorPrimaryDark\\\":\\\"#252525\\\",\\\"colorBackgroundText\\\":\\\"#FFFFFF\\\",\\\"colorBackground\\\":\\\"#252525\\\",\\\"colorButton\\\":\\\"#F50057\\\",\\\"shadow\\\":\\\"1\\\",\\\"colorHint\\\":\\\"#808080\\\",\\\"colorRipple\\\":\\\"#40FFFFFF\\\",\\\"colorPrimaryCardImage\\\":\\\"#98A0A8\\\",\\\"version\\\":\\\"2\\\",\\\"colorPrimaryText\\\":\\\"#FFFFFF\\\",\\\"colorPrimaryImage\\\":\\\"#FFFFFF\\\",\\\"colorBackgroundImage\\\":\\\"#2196F3\\\",\\\"colorBackgroundCardText\\\":\\\"#98A0A8\\\",\\\"statusbarIcon\\\":\\\"1\\\",\\\"colorBackgroundCard\\\":\\\"#404040\\\",\\\"colorButtonText\\\":\\\"#FFFFFF\\\",\\\"colorPrimaryCardText\\\":\\\"#98A0A8\\\",\\\"colorPrimaryCard\\\":\\\"#404040\\\",\\\"colorBackgroundCardImage\\\":\\\"#98A0A8\\\"}]\",\"themesauthor\":\"Axhyre\",\"themesname\":\"$default_2$\",\"themesinfo\":\"A Material dark theme for TeamMusic.\",\"os-thm-version\":\"2\"}")) {
                    {
                        HashMap<String, Object> _item = new HashMap<>();
                        _item.put("themesname", "$default_2$");
                        theme_map.add(1, _item);
                    }

                    theme_map.get(1).put("themesjson", "[{\"colorPrimary\":\"#2196F3\",\"colorPrimaryDark\":\"#252525\",\"colorBackgroundText\":\"#FFFFFF\",\"colorBackground\":\"#252525\",\"colorButton\":\"#F50057\",\"shadow\":\"1\",\"colorHint\":\"#808080\",\"colorRipple\":\"#40FFFFFF\",\"colorPrimaryCardImage\":\"#98A0A8\",\"version\":\"2\",\"colorPrimaryText\":\"#FFFFFF\",\"colorPrimaryImage\":\"#FFFFFF\",\"colorBackgroundImage\":\"#2196F3\",\"colorBackgroundCardText\":\"#98A0A8\",\"statusbarIcon\":\"1\",\"colorBackgroundCard\":\"#404040\",\"colorButtonText\":\"#FFFFFF\",\"colorPrimaryCardText\":\"#98A0A8\",\"colorPrimaryCard\":\"#404040\",\"colorBackgroundCardImage\":\"#98A0A8\"}]");
                    theme_map.get(1).put("themesinfo", "A Material dark theme for TeamMusic.");
                    theme_map.get(1).put("themesauthor", "Axhyre");
                    theme_map.get(1).put("os-thm-version", "2");
                    data.edit().putString("griddata", new Gson().toJson(theme_map)).commit();
                }
            } else {
                {
                    HashMap<String, Object> _item = new HashMap<>();
                    _item.put("themesname", "$default_2$");
                    theme_map.add(_item);
                }

                theme_map.get(1).put("themesjson", "[{\"colorPrimary\":\"#2196F3\",\"colorPrimaryDark\":\"#252525\",\"colorBackgroundText\":\"#FFFFFF\",\"colorBackground\":\"#252525\",\"colorButton\":\"#F50057\",\"shadow\":\"1\",\"colorHint\":\"#808080\",\"colorRipple\":\"#40FFFFFF\",\"colorPrimaryCardImage\":\"#98A0A8\",\"version\":\"2\",\"colorPrimaryText\":\"#FFFFFF\",\"colorPrimaryImage\":\"#FFFFFF\",\"colorBackgroundImage\":\"#2196F3\",\"colorBackgroundCardText\":\"#98A0A8\",\"statusbarIcon\":\"1\",\"colorBackgroundCard\":\"#404040\",\"colorButtonText\":\"#FFFFFF\",\"colorPrimaryCardText\":\"#98A0A8\",\"colorPrimaryCard\":\"#404040\",\"colorBackgroundCardImage\":\"#98A0A8\"}]");
                theme_map.get(1).put("themesinfo", "A Material dark theme for TeamMusic.");
                theme_map.get(1).put("themesauthor", "Axhyre");
                theme_map.get(1).put("os-thm-version", "2");
                data.edit().putString("griddata", new Gson().toJson(theme_map)).commit();
            }
        }
        if (data.getString("playlist", "").equals("")) {
            data.edit().putString("playlist", "[]").commit();
        }
        if (data.getString("errorCountdown", "").equals("")) {
            data.edit().putString("errorCountdown", "30").commit();
        }
        if (data.getString("nightcoreSpeed", "").equals("")) {
            data.edit().putString("nightcoreSpeed", "2").commit();
        }
        if (data.getString("showPreview", "").equals("")) {
            data.edit().putString("showPreview", "1").commit();
        }
        if (!data.getString("lastPath", "").equals("")) {
            data.edit().remove("lastPath").commit();
        }
        if (data.getString("playerUI", "").equals("")) {
            data.edit().putString("playerUI", "0").commit();
        }
        if (data.getString("fpuMode", "").equals("")) {
            data.edit().putString("fpuMode", "0").commit();
        }
        if (data.getString("showVisualizer", "").equals("")) {
            data.edit().putString("showVisualizer", "1").commit();
        }
        if (data.getString("colorTransparency", "").equals("")) {
            data.edit().putString("colorTransparency", "100").commit();
        }
        if (!data.getString("themesactive", "").equals("")) {
            data.edit().remove("themesactive").commit();
        }
        if (data.getString("themesjson", "").equals("")) {
            data.edit().putString("themesjson", "[{\"colorPrimary\":\"#2196F3\",\"colorPrimaryDark\":\"#1769AA\",\"colorBackgroundText\":\"#000000\",\"colorBackground\":\"#FFFFFF\",\"colorButton\":\"#F50057\",\"shadow\":\"1\",\"colorHint\":\"#A8A8A8\",\"colorRipple\":\"#40000000\",\"colorPrimaryCardImage\":\"#000000\",\"version\":\"2\",\"colorPrimaryText\":\"#FFFFFF\",\"colorPrimaryImage\":\"#FFFFFF\",\"colorBackgroundImage\":\"#2196F3\",\"colorBackgroundCardText\":\"#000000\",\"statusbarIcon\":\"1\",\"colorBackgroundCard\":\"#FFFFFF\",\"colorButtonText\":\"#FFFFFF\",\"colorPrimaryCardText\":\"#000000\",\"colorPrimaryCard\":\"#FFFFFF\",\"colorBackgroundCardImage\":\"#000000\"}]").commit();
        } else {
            tmpMap = new Gson().fromJson(data.getString("themesjson", ""), new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
            if (tmpMap.get(0).containsKey("version")) {
                if (!tmpMap.get(0).get("version").toString().equals("2")) {
                    data.edit().putString("themesjson", "[{\"colorPrimary\":\"#2196F3\",\"colorPrimaryDark\":\"#1769AA\",\"colorBackgroundText\":\"#000000\",\"colorBackground\":\"#FFFFFF\",\"colorButton\":\"#F50057\",\"shadow\":\"1\",\"colorHint\":\"#A8A8A8\",\"colorRipple\":\"#40000000\",\"colorPrimaryCardImage\":\"#000000\",\"version\":\"2\",\"colorPrimaryText\":\"#FFFFFF\",\"colorPrimaryImage\":\"#FFFFFF\",\"colorBackgroundImage\":\"#2196F3\",\"colorBackgroundCardText\":\"#000000\",\"statusbarIcon\":\"1\",\"colorBackgroundCard\":\"#FFFFFF\",\"colorButtonText\":\"#FFFFFF\",\"colorPrimaryCardText\":\"#000000\",\"colorPrimaryCard\":\"#FFFFFF\",\"colorBackgroundCardImage\":\"#000000\"}]").commit();
                }
            } else {
                data.edit().putString("themesjson", "[{\"colorPrimary\":\"#2196F3\",\"colorPrimaryDark\":\"#1769AA\",\"colorBackgroundText\":\"#000000\",\"colorBackground\":\"#FFFFFF\",\"colorButton\":\"#F50057\",\"shadow\":\"1\",\"colorHint\":\"#A8A8A8\",\"colorRipple\":\"#40000000\",\"colorPrimaryCardImage\":\"#000000\",\"version\":\"2\",\"colorPrimaryText\":\"#FFFFFF\",\"colorPrimaryImage\":\"#FFFFFF\",\"colorBackgroundImage\":\"#2196F3\",\"colorBackgroundCardText\":\"#000000\",\"statusbarIcon\":\"1\",\"colorBackgroundCard\":\"#FFFFFF\",\"colorButtonText\":\"#FFFFFF\",\"colorPrimaryCardText\":\"#000000\",\"colorPrimaryCard\":\"#FFFFFF\",\"colorBackgroundCardImage\":\"#000000\"}]").commit();
            }
        }
        if (data.getString("cookies", "").equals("0")) {
            textview11.setVisibility(View.VISIBLE);
            imageview1.setImageResource(R.drawable.no_network_emote02);
            textview11.setText("GIVE ME BACK MY COOKIES!");
        }
        if (_checkPermission()) {
            if (FileUtil.isExistFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/tmp.png"))) {
                FileUtil.deleteFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/tmp.png"));
            }
            if (FileUtil.isExistFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/tmp_user.png"))) {
                FileUtil.deleteFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/tmp_user.png"));
            }
            if (FileUtil.isExistFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/tm-testfile"))) {
                FileUtil.deleteFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/tm-testfile"));
            }
        }
        _checkCookie();
		/*
Glide.with(getApplicationContext()).load(Uri.parse("'-'")).into(image_user);
*/
    }

    private boolean _checkPermission() {
        if (Build.VERSION.SDK_INT < 33) {
            return !(androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_DENIED || androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_DENIED);
        } else {
            return true;
        }
    }

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

    }

    @Override
    public void onStart() {
        super.onStart();
        _DarkMode();
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

    private void _customNav(final String _color) {
        //Code From StackOverFlow.com And Converted By TeamWorks DEV
        if (Build.VERSION.SDK_INT >= 21) {
            Window w = this.getWindow();
            w.setNavigationBarColor(Color.parseColor(_color));
        }
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


    private void _randomEmoteUwU() {
        textview3.setVisibility(View.GONE);
        imageview2.setVisibility(View.GONE);
        double _randomDouble = SketchwareUtil.getRandom(0, 11);
        if (_randomDouble == 0) {
            textview3.setText(":>");
            textview3.setVisibility(View.VISIBLE);
        }
        if (_randomDouble == 1) {
            textview3.setText("UwU");
            textview3.setVisibility(View.VISIBLE);
        }
        if (_randomDouble == 2) {
            textview3.setText("OwO");
            textview3.setVisibility(View.VISIBLE);
        }
        if (_randomDouble == 3) {
            textview3.setText("^_^");
            textview3.setVisibility(View.VISIBLE);
        }
        if (_randomDouble == 4) {
            textview3.setText(">_<");
            textview3.setVisibility(View.VISIBLE);
        }
        if (_randomDouble == 5) {
            textview3.setText("'-'");
            textview3.setVisibility(View.VISIBLE);
        }
        if (_randomDouble == 6) {
            textview3.setText("-,-");
            textview3.setVisibility(View.VISIBLE);
        }
        if (_randomDouble == 7) {
            imageview2.setImageResource(R.drawable.no_network_emote01);
            imageview2.setVisibility(View.VISIBLE);
        }
        if (_randomDouble == 8) {
            imageview2.setImageResource(R.drawable.no_network_emote02);
            imageview2.setVisibility(View.VISIBLE);
        }
        if (_randomDouble == 9) {
            imageview2.setImageResource(R.drawable.no_network_emote03);
            imageview2.setVisibility(View.VISIBLE);
        }
        if (_randomDouble == 10) {
            imageview2.setImageResource(R.drawable.no_network_emote04);
            imageview2.setVisibility(View.VISIBLE);
        }
        if (_randomDouble == 11) {
            imageview2.setImageResource(R.drawable.no_network_emote05);
            imageview2.setVisibility(View.VISIBLE);
        }
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


    private void _DarkMode() {
        int nightModeFlags = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            linear1.setBackgroundColor(0xFF252525);
            linear2.setBackgroundColor(0xFF252525);
            linear6.setBackgroundColor(0xFF252525);
            einput_name.setTextColor(0xFFFFFFFF);
            textview4.setTextColor(0xFFFFFFFF);
            textview7.setTextColor(0xFFFFFFFF);
            textview8.setTextColor(0xFFFFFFFF);
            textview9.setTextColor(0xFFFFFFFF);
            getWindow().setStatusBarColor(Color.parseColor("#252525"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(0);
            }
            _customNav("#252525");
        } else {
            linear1.setBackgroundColor(0xFFFFFFFF);
            linear2.setBackgroundColor(0xFFFFFFFF);
            linear6.setBackgroundColor(0xFFFFFFFF);
            einput_name.setTextColor(0xFF000000);
            textview4.setTextColor(0xFF000000);
            textview7.setTextColor(0xFF000000);
            textview8.setTextColor(0xFF000000);
            textview9.setTextColor(0xFF000000);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                getWindow().setStatusBarColor(Color.parseColor("#BDBDBD"));
            }
            _customNav("#FFFFFF");
        }
    }


    private void _chkChglog() {
        if (data.getString("lastVersion", "").equals("")) {
            _dispChglog();
        } else {
            if ((double) packageInfo.versionCode > Double.parseDouble(data.getString("lastVersion", ""))) {
                _dispChglog();
            } else {
                activityChanger.setClass(getApplicationContext(), StreamerActivity.class);
                startActivity(activityChanger);
                finish();
            }
        }
    }


    private void _dispChglog() {
        d.setCancelable(false);
        d.setTitle("ZryteZene ".concat(packageInfo.versionName.concat(" Changelog")));
        d.setMessage(tmpMapVar.get("msg").toString());
        d.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface _dialog, int _which) {
                data.edit().putString("lastVersion", String.valueOf((long) ((double) packageInfo.versionCode))).commit();
                activityChanger.setClass(getApplicationContext(), StreamerActivity.class);
                startActivity(activityChanger);
                finish();
            }
        });
        d.create().show();
    }


    private void _checkCookie() {
        if (getApplicationContext().getPackageName().equals("tw.music.streamer")) {
            update_db.addChildEventListener(_update_db_child_listener);
            internetchecker.startRequestNetwork(RequestNetworkController.GET, "https://www.google.com", "A", _internetchecker_request_listener);
        } else {
            linear1.setVisibility(View.GONE);
            linear7.setVisibility(View.VISIBLE);
        }
    }


    private void _shadow(final View _v, final double _n) {
        _v.setElevation((float) _n);
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

}
