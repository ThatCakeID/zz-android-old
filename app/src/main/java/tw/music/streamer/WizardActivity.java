package tw.music.streamer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class WizardActivity extends AppCompatActivity {

    private Timer _timer = new Timer();

    private FloatingActionButton _fab;
    private double currentSetup = 0;
    private boolean isLogin = false;

    private ArrayList<String> listSetup = new ArrayList<>();

    private LinearLayout linear2;
    private LinearLayout linear6;
    private LinearLayout linear10;
    private LinearLayout linear17;
    private LinearLayout linear3;
    private TextView textview2;
    private TextView textview3;
    private LinearLayout linear7;
    private LinearLayout linear4;
    private LinearLayout linear16;
    private LinearLayout linear5;
    private TextView textview4;
    private TextView textview5;
    private Button button2;
    private LinearLayout linear11;
    private LinearLayout linear13;
    private LinearLayout linear12;
    private ImageView imageview3;
    private TextView textview6;
    private TextView textview7;
    private LinearLayout linear14;
    private TextView textview8;
    private EditText edittext1;
    private TextView textview9;
    private EditText edittext2;
    private TextView textview10;
    private Button button1;
    private LinearLayout linear15;
    private TextView textview11;
    private TextView textview12;
    private ScrollView vscroll1;
    private CheckBox checkbox1;
    private LinearLayout linear18;
    private TextView textview13;
    private Button button3;

    private FirebaseAuth Auth;
    private OnCompleteListener<AuthResult> _Auth_create_user_listener;
    private OnCompleteListener<AuthResult> _Auth_sign_in_listener;
    private OnCompleteListener<Void> _Auth_reset_password_listener;
    private TimerTask delay;
    private SharedPreferences data;
    private Intent intent = new Intent();
    private AlertDialog.Builder dialog;
    private ProgressDialog coreprog;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.wizard);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        _fab = findViewById(R.id._fab);

        linear2 = findViewById(R.id.linear2);
        linear6 = findViewById(R.id.linear6);
        linear10 = findViewById(R.id.linear10);
        linear17 = findViewById(R.id.linear17);
        linear3 = findViewById(R.id.linear3);
        textview2 = findViewById(R.id.textview2);
        textview3 = findViewById(R.id.textview3);
        linear7 = findViewById(R.id.linear7);
        linear4 = findViewById(R.id.linear4);
        linear16 = findViewById(R.id.linear16);
        linear5 = findViewById(R.id.linear5);
        textview4 = findViewById(R.id.textview4);
        textview5 = findViewById(R.id.textview5);
        button2 = findViewById(R.id.button2);
        linear11 = findViewById(R.id.linear11);
        linear13 = findViewById(R.id.linear13);
        linear12 = findViewById(R.id.linear12);
        imageview3 = findViewById(R.id.imageview3);
        textview6 = findViewById(R.id.textview6);
        textview7 = findViewById(R.id.textview7);
        linear14 = findViewById(R.id.linear14);
        textview8 = findViewById(R.id.textview8);
        edittext1 = findViewById(R.id.edittext1);
        textview9 = findViewById(R.id.textview9);
        edittext2 = findViewById(R.id.edittext2);
        textview10 = findViewById(R.id.textview10);
        button1 = findViewById(R.id.button1);
        linear15 = findViewById(R.id.linear15);
        textview11 = findViewById(R.id.textview11);
        textview12 = findViewById(R.id.textview12);
        vscroll1 = findViewById(R.id.vscroll1);
        checkbox1 = findViewById(R.id.checkbox1);
        linear18 = findViewById(R.id.linear18);
        textview13 = findViewById(R.id.textview13);
        button3 = findViewById(R.id.button3);
        Auth = FirebaseAuth.getInstance();
        data = getSharedPreferences("teamdata", Activity.MODE_PRIVATE);
        dialog = new AlertDialog.Builder(this);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                _grantPermission();
            }
        });

        edittext1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                final String _charSeq = _param1.toString();
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(edittext1.getText().toString()).matches())
                    edittext1.setError(null);
                else
                    edittext1.setError("Invalid email!");
            }

            @Override
            public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {

            }

            @Override
            public void afterTextChanged(Editable _param1) {

            }
        });

        textview10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {

                LinearLayout mylayout = new LinearLayout(WizardActivity.this);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                mylayout.setLayoutParams(params);
                mylayout.setOrientation(LinearLayout.VERTICAL);

                final EditText myedittext = new EditText(WizardActivity.this);
                myedittext.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
                myedittext.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                myedittext.setText(edittext1.getText().toString());

                mylayout.addView(myedittext);
                dialog.setView(mylayout);
                myedittext.setHint("Enter your Email");
                dialog.setCancelable(false);
                dialog.setTitle("Reset Password");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        _resetDialog();
                        _abandonFocus();
                        if (myedittext.getText().toString().trim().length() > 0) {
                            Auth.sendPasswordResetEmail(myedittext.getText().toString()).addOnCompleteListener(_Auth_reset_password_listener);
                            _customSnack("Check your e-mail to change password.", 1);
                        } else {
                            _customSnack("Fill those blank!", 2);
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        _resetDialog();
                        _abandonFocus();
                    }
                });
                dialog.create().show();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (edittext1.getText().toString().trim().equals("")) {
                    _customSnack("Fill those blanks!", 2);
                    edittext1.requestFocus();
                } else {
                    if (edittext2.getText().toString().equals("")) {
                        _customSnack("Fill those blanks!", 2);
                        edittext2.requestFocus();
                    } else {
                        _CoreProgressLoading(true);
                        if (isLogin) {
                            Auth.signInWithEmailAndPassword(edittext1.getText().toString().trim(), edittext2.getText().toString()).addOnCompleteListener(WizardActivity.this, _Auth_sign_in_listener);
                        } else {
                            Auth.createUserWithEmailAndPassword(edittext1.getText().toString().trim(), edittext2.getText().toString()).addOnCompleteListener(WizardActivity.this, _Auth_create_user_listener);
                        }
                    }
                }
            }
        });

        textview12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (isLogin) {
                    isLogin = false;
                    button1.setText("Sign Up");
                    textview12.setText("Sign In");
                    textview10.setVisibility(View.GONE);
                    textview11.setText("Old user?");
                } else {
                    isLogin = true;
                    button1.setText("Login");
                    textview12.setText("Sign Up");
                    textview10.setVisibility(View.VISIBLE);
                    textview11.setText("New user?");
                }
            }
        });

        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton _param1, boolean _param2) {
                final boolean _isChecked = _param2;
                if (_isChecked) {
                    button3.setEnabled(true);
                    button3.setAlpha((float) (1.0d));
                } else {
                    button3.setEnabled(false);
                    button3.setAlpha((float) (0.5d));
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                _nextSetup();
            }
        });

        _fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                _nextSetup();
            }
        });

        _Auth_create_user_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
                _CoreProgressLoading(false);
                if (_success) {
                    Auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                _customSnack("Check your email to verify your account.", 1);
                            } else {
                                _customSnack("Account verification failed to send!", 2);
                            }

                        }
                    });
                    FirebaseAuth.getInstance().signOut();
                } else {
                    _customSnack(_errorMessage, 2);
                    FirebaseAuth.getInstance().signOut();
                }
            }
        };

        _Auth_sign_in_listener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> _param1) {
                final boolean _success = _param1.isSuccessful();
                final String _errorMessage = _param1.getException() != null ? _param1.getException().getMessage() : "";
                _CoreProgressLoading(false);
                if (_success) {
                    if (Auth.getCurrentUser().isEmailVerified()) {
                        _nextSetup();
                    } else {
                        dialog.setCancelable(false);
                        dialog.setTitle("Email is not verified");
                        dialog.setMessage("Would you to re-verify? ");
                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface _dialog, int _which) {
                                _resetDialog();
                                Auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@androidx.annotation.NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            _customSnack("Open your email to verify your account.", 1);
                                        } else {
                                            _customSnack("Account verification failed to send!", 2);
                                        }

                                    }
                                });
                                FirebaseAuth.getInstance().signOut();
                            }
                        });
                        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface _dialog, int _which) {
                                _resetDialog();
                                FirebaseAuth.getInstance().signOut();
                            }
                        });
                        dialog.create().show();
                    }
                } else {
                    _customSnack(_errorMessage, 2);
                    FirebaseAuth.getInstance().signOut();
                }
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
        button1.setAllCaps(false);
        button2.setAllCaps(false);
        _fab.hide();

        edittext1.getBackground().setColorFilter(Color.parseColor("#2196F3"), PorterDuff.Mode.SRC_ATOP);
        edittext2.getBackground().setColorFilter(Color.parseColor("#2196F3"), PorterDuff.Mode.SRC_ATOP);

        edittext1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        _customNav("#FFFFFF");
        _BlackStatusBarIcons();
        _setFont();
        linear2.setVisibility(View.GONE);
        linear6.setVisibility(View.GONE);
        linear10.setVisibility(View.GONE);
        textview10.setVisibility(View.GONE);
        _shape(SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), "#2196F3", "#FFFFFF", 0, button1);
        _shape(SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), "#3F51B5", "#FFFFFF", 0, button2);
        _shape(SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), SketchwareUtil.getDip(getApplicationContext(), (int) (2.5d)), "#2196F3", "#FFFFFF", 0, button3);
        _shadow(linear7, 10);
        _shadow(linear11, 10);
        button3.setEnabled(false);
        button3.setAlpha((float) (0.5d));
        textview13.setText("MIT License\nLast updated: 2019-10-05\n\nCopyright © 2017 ZryteZene All rights reserved.\n\nRedistribution and use in source and binary forms, with or without modification,\nare permitted provided that the following conditions are met:\n* Redistributions of source code must retain the above copyright notice, this\n  list of conditions and the following disclaimer.\n* Redistributions in binary form must reproduce the above copyright notice, this\n  list of conditions and the following disclaimer in the documentation and/or\n  other materials provided with the distribution.\n* Neither the name of ZryteZene nor the names of its contributors may be used to\n  endorse or promote products derived from this software without specific prior\n  written permission.\n\nTHIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS “AS IS” AND\nANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED\nWARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE\nDISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR\nANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES\n(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;\nLOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON\nANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS\nSOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n\n\nTERMS AND CONDITIONS\nLast updated: 2020-04-05\n\n1. Introduction\n\nWelcome to Zrytezene (“Company”, “we”, “our”, “us”)!\n\nThese Terms of Service (“Terms”, “Terms of Service”) govern your use of our website located at https://zrytezene.web.app (together or individually “Service”) operated by ZryteZene.\n\nOur Privacy Policy also governs your use of our Service and explains how we collect, safeguard and disclose information that results from your use of our web pages.\n\nYour agreement with us includes these Terms and our Privacy Policy (“Agreements”). You acknowledge that you have read and understood Agreements, and agree to be bound of them.\n\nIf you do not agree with (or cannot comply with) Agreements, then you may not use the Service, but please let us know by emailing at zrytezene@gmail.com so we can try to find a solution. These Terms apply to all visitors, users and others who wish to access or use Service.\n\n2. Communications\n\nBy using our Service, you agree to subscribe to newsletters, marketing or promotional materials and other information we may send. However, you may opt out of receiving any, or all, of these communications from us by following the unsubscribe link or by emailing at zrytezene@gmail.com.\n\n3. Contests, Sweepstakes and Promotions\n\nAny contests, sweepstakes or other promotions (collectively, “Promotions”) made available through Service may be governed by rules that are separate from these Terms of Service. If you participate in any Promotions, please review the applicable rules as well as our Privacy Policy. If the rules for a Promotion conflict with these Terms of Service, Promotion rules will apply.\n\n4. Content\n\nOur Service allows you to post, link, store, share and otherwise make available certain information, text, graphics, videos, or other material (“Content”). You are responsible for Content that you post on or through Service, including its legality, reliability, and appropriateness.\n\nBy posting Content on or through Service, You represent and warrant that: (i) Content is yours (you own it) and/or you have the right to use it and the right to grant us the rights and license as provided in these Terms, and (ii) that the posting of your Content on or through Service does not violate the privacy rights, publicity rights, copyrights, contract rights or any other rights of any person or entity. We reserve the right to terminate the account of anyone found to be infringing on a copyright.\n\nYou retain any and all of your rights to any Content you submit, post or display on or through Service and you are responsible for protecting those rights. We take no responsibility and assume no liability for Content you or any third party posts on or through Service. However, by posting Content using Service you grant us the right and license to use, modify, publicly perform, publicly display, reproduce, and distribute such Content on and through Service. You agree that this license includes the right for us to make your Content available to other users of Service, who may also use your Content subject to these Terms.\n\nZryteZene has the right but not the obligation to monitor and edit all Content provided by users.\n\nIn addition, Content found on or through this Service are the property of ZryteZene or used with permission. You may not distribute, modify, transmit, reuse, download, repost, copy, or use said Content, whether in whole or in part, for commercial purposes or for personal gain, without express advance written permission from us.\n\n5. Prohibited Uses\n\nYou may use Service only for lawful purposes and in accordance with Terms. You agree not to use Service:\n\n0.1. In any way that violates any applicable national or international law or regulation.\n\n0.2. For the purpose of exploiting, harming, or attempting to exploit or harm minors in any way by exposing them to inappropriate content or otherwise.\n\n0.3. To transmit, or procure the sending of, any advertising or promotional material, including any “junk mail”, “chain letter,” “spam,” or any other similar solicitation.\n\n0.4. To impersonate or attempt to impersonate Company, a Company employee, another user, or any other person or entity.\n\n0.5. In any way that infringes upon the rights of others, or in any way is illegal, threatening, fraudulent, or harmful, or in connection with any unlawful, illegal, fraudulent, or harmful purpose or activity.\n\n0.6. To engage in any other conduct that restricts or inhibits anyone’s use or enjoyment of Service, or which, as determined by us, may harm or offend Company or users of Service or expose them to liability.\n\nAdditionally, you agree not to:\n\n0.1. Use Service in any manner that could disable, overburden, damage, or impair Service or interfere with any other party’s use of Service, including their ability to engage in real time activities through Service.\n\n0.2. Use any robot, spider, or other automatic device, process, or means to access Service for any purpose, including monitoring or copying any of the material on Service.\n\n0.3. Use any manual process to monitor or copy any of the material on Service or for any other unauthorized purpose without our prior written consent.\n\n0.4. Use any device, software, or routine that interferes with the proper working of Service.\n\n0.5. Introduce any viruses, trojan horses, worms, logic bombs, or other material which is malicious or technologically harmful.\n\n0.6. Attempt to gain unauthorized access to, interfere with, damage, or disrupt any parts of Service, the server on which Service is stored, or any server, computer, or database connected to Service.\n\n0.7. Attack Service via a denial-of-service attack or a distributed denial-of-service attack.\n\n0.8. Take any action that may damage or falsify Company rating.\n\n0.9. Otherwise attempt to interfere with the proper working of Service.\n\n6. Analytics\n\nWe may use third-party Service Providers to monitor and analyze the use of our Service.\n\n7. No Use By Minors\n\nService is intended only for access and use by individuals at least eighteen (18) years old. By accessing or using Service, you warrant and represent that you are at least eighteen (18) years of age and with the full authority, right, and capacity to enter into this agreement and abide by all of the terms and conditions of Terms. If you are not at least eighteen (18) years old, you are prohibited from both the access and usage of Service.\n\n8. Accounts\n\nWhen you create an account with us, you guarantee that you are above the age of 18, and that the information you provide us is accurate, complete, and current at all times. Inaccurate, incomplete, or obsolete information may result in the immediate termination of your account on Service.\n\nYou are responsible for maintaining the confidentiality of your account and password, including but not limited to the restriction of access to your computer and/or account. You agree to accept responsibility for any and all activities or actions that occur under your account and/or password, whether your password is with our Service or a third-party service. You must notify us immediately upon becoming aware of any breach of security or unauthorized use of your account.\n\nYou may not use as a username the name of another person or entity or that is not lawfully available for use, a name or trademark that is subject to any rights of another person or entity other than you, without appropriate authorization. You may not use as a username any name that is offensive, vulgar or obscene.\n\nWe reserve the right to refuse service, terminate accounts, remove or edit content, or cancel orders in our sole discretion.\n\n9. Intellectual Property\n\nService and its original content (excluding Content provided by users), features and functionality are and will remain the exclusive property of ZryteZene and its licensors. Service is protected by copyright, trademark, and other laws of and foreign countries. Our trademarks may not be used in connection with any product or service without the prior written consent of ZryteZene.\n\n10. Copyright Policy\n\nWe respect the intellectual property rights of others. It is our policy to respond to any claim that Content posted on Service infringes on the copyright or other intellectual property rights (“Infringement”) of any person or entity.\n\nIf you are a copyright owner, or authorized on behalf of one, and you believe that the copyrighted work has been copied in a way that constitutes copyright infringement, please submit your claim via email to zrytezene@gmail.com, with the subject line: “Copyright Infringement” and include in your claim a detailed description of the alleged Infringement as detailed below, under “DMCA Notice and Procedure for Copyright Infringement Claims”\n\nYou may be held accountable for damages (including costs and attorneys’ fees) for misrepresentation or bad-faith claims on the infringement of any Content found on and/or through Service on your copyright.\n\n11. DMCA Notice and Procedure for Copyright Infringement Claims\n\nYou may submit a notification pursuant to the Digital Millennium Copyright Act (DMCA) by providing our Copyright Agent with the following information in writing (see 17 U.S.C 512(c)(3) for further detail):\n\n0.1. an electronic or physical signature of the person authorized to act on behalf of the owner of the copyright’s interest;\n\n0.2. a description of the copyrighted work that you claim has been infringed, including the URL (i.e., web page address) of the location where the copyrighted work exists or a copy of the copyrighted work;\n\n0.3. identification of the URL or other specific location on Service where the material that you claim is infringing is located;\n\n0.4. your address, telephone number, and email address;\n\n0.5. a statement by you that you have a good faith belief that the disputed use is not authorized by the copyright owner, its agent, or the law;\n\n0.6. a statement by you, made under penalty of perjury, that the above information in your notice is accurate and that you are the copyright owner or authorized to act on the copyright owner’s behalf.\n\nYou can contact our Copyright Agent via email at zrytezene@gmail.com.\n\n12. Error Reporting and Feedback\n\nYou may provide us either directly at zrytezene@gmail.com or via third party sites and tools with information and feedback concerning errors, suggestions for improvements, ideas, problems, complaints, and other matters related to our Service (“Feedback”). You acknowledge and agree that: (i) you shall not retain, acquire or assert any intellectual property right or other right, title or interest in or to the Feedback; (ii) Company may have development ideas similar to the Feedback; (iii) Feedback does not contain confidential information or proprietary information from you or any third party; and (iv) Company is not under any obligation of confidentiality with respect to the Feedback. In the event the transfer of the ownership to the Feedback is not possible due to applicable mandatory laws, you grant Company and its affiliates an exclusive, transferable, irrevocable, free-of-charge, sub-licensable, unlimited and perpetual right to use (including copy, modify, create derivative works, publish, distribute and commercialize) Feedback in any manner and for any purpose.\n\n13. Links To Other Web Sites\n\nOur Service may contain links to third party web sites or services that are not owned or controlled by ZryteZene.\n\nZryteZene has no control over, and assumes no responsibility for the content, privacy policies, or practices of any third party web sites or services. We do not warrant the offerings of any of these entities/individuals or their websites.\n\nFor example, the outlined Terms of Service have been created using PolicyMaker.io, a free web application for generating high-quality legal documents. PolicyMaker’s free Terms and Conditions generator is an easy-to-use free tool for creating an excellent Terms of Service template for a website, blog, online store or app.\n\nYOU ACKNOWLEDGE AND AGREE THAT COMPANY SHALL NOT BE RESPONSIBLE OR LIABLE, DIRECTLY OR INDIRECTLY, FOR ANY DAMAGE OR LOSS CAUSED OR ALLEGED TO BE CAUSED BY OR IN CONNECTION WITH USE OF OR RELIANCE ON ANY SUCH CONTENT, GOODS OR SERVICES AVAILABLE ON OR THROUGH ANY SUCH THIRD PARTY WEB SITES OR SERVICES.\n\nWE STRONGLY ADVISE YOU TO READ THE TERMS OF SERVICE AND PRIVACY POLICIES OF ANY THIRD PARTY WEB SITES OR SERVICES THAT YOU VISIT.\n\n14. Disclaimer Of Warranty\n\nTHESE SERVICES ARE PROVIDED BY COMPANY ON AN “AS IS” AND “AS AVAILABLE” BASIS. COMPANY MAKES NO REPRESENTATIONS OR WARRANTIES OF ANY KIND, EXPRESS OR IMPLIED, AS TO THE OPERATION OF THEIR SERVICES, OR THE INFORMATION, CONTENT OR MATERIALS INCLUDED THEREIN. YOU EXPRESSLY AGREE THAT YOUR USE OF THESE SERVICES, THEIR CONTENT, AND ANY SERVICES OR ITEMS OBTAINED FROM US IS AT YOUR SOLE RISK.\n\nNEITHER COMPANY NOR ANY PERSON ASSOCIATED WITH COMPANY MAKES ANY WARRANTY OR REPRESENTATION WITH RESPECT TO THE COMPLETENESS, SECURITY, RELIABILITY, QUALITY, ACCURACY, OR AVAILABILITY OF THE SERVICES. WITHOUT LIMITING THE FOREGOING, NEITHER COMPANY NOR ANYONE ASSOCIATED WITH COMPANY REPRESENTS OR WARRANTS THAT THE SERVICES, THEIR CONTENT, OR ANY SERVICES OR ITEMS OBTAINED THROUGH THE SERVICES WILL BE ACCURATE, RELIABLE, ERROR-FREE, OR UNINTERRUPTED, THAT DEFECTS WILL BE CORRECTED, THAT THE SERVICES OR THE SERVER THAT MAKES IT AVAILABLE ARE FREE OF VIRUSES OR OTHER HARMFUL COMPONENTS OR THAT THE SERVICES OR ANY SERVICES OR ITEMS OBTAINED THROUGH THE SERVICES WILL OTHERWISE MEET YOUR NEEDS OR EXPECTATIONS.\n\nCOMPANY HEREBY DISCLAIMS ALL WARRANTIES OF ANY KIND, WHETHER EXPRESS OR IMPLIED, STATUTORY, OR OTHERWISE, INCLUDING BUT NOT LIMITED TO ANY WARRANTIES OF MERCHANTABILITY, NON-INFRINGEMENT, AND FITNESS FOR PARTICULAR PURPOSE.\n\nTHE FOREGOING DOES NOT AFFECT ANY WARRANTIES WHICH CANNOT BE EXCLUDED OR LIMITED UNDER APPLICABLE LAW.\n\n15. Limitation Of Liability\n\nEXCEPT AS PROHIBITED BY LAW, YOU WILL HOLD US AND OUR OFFICERS, DIRECTORS, EMPLOYEES, AND AGENTS HARMLESS FOR ANY INDIRECT, PUNITIVE, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGE, HOWEVER IT ARISES (INCLUDING ATTORNEYS’ FEES AND ALL RELATED COSTS AND EXPENSES OF LITIGATION AND ARBITRATION, OR AT TRIAL OR ON APPEAL, IF ANY, WHETHER OR NOT LITIGATION OR ARBITRATION IS INSTITUTED), WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE, OR OTHER TORTIOUS ACTION, OR ARISING OUT OF OR IN CONNECTION WITH THIS AGREEMENT, INCLUDING WITHOUT LIMITATION ANY CLAIM FOR PERSONAL INJURY OR PROPERTY DAMAGE, ARISING FROM THIS AGREEMENT AND ANY VIOLATION BY YOU OF ANY FEDERAL, STATE, OR LOCAL LAWS, STATUTES, RULES, OR REGULATIONS, EVEN IF COMPANY HAS BEEN PREVIOUSLY ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. EXCEPT AS PROHIBITED BY LAW, IF THERE IS LIABILITY FOUND ON THE PART OF COMPANY, IT WILL BE LIMITED TO THE AMOUNT PAID FOR THE PRODUCTS AND/OR SERVICES, AND UNDER NO CIRCUMSTANCES WILL THERE BE CONSEQUENTIAL OR PUNITIVE DAMAGES. SOME STATES DO NOT ALLOW THE EXCLUSION OR LIMITATION OF PUNITIVE, INCIDENTAL OR CONSEQUENTIAL DAMAGES, SO THE PRIOR LIMITATION OR EXCLUSION MAY NOT APPLY TO YOU.\n\n16. Termination\n\nWe may terminate or suspend your account and bar access to Service immediately, without prior notice or liability, under our sole discretion, for any reason whatsoever and without limitation, including but not limited to a breach of Terms.\n\nIf you wish to terminate your account, you may simply discontinue using Service.\n\nAll provisions of Terms which by their nature should survive termination shall survive termination, including, without limitation, ownership provisions, warranty disclaimers, indemnity and limitations of liability.\n\n17. Governing Law\n\nThese Terms shall be governed and construed in accordance with the laws of Indonesia, which governing law applies to agreement without regard to its conflict of law provisions.\n\nOur failure to enforce any right or provision of these Terms will not be considered a waiver of those rights. If any provision of these Terms is held to be invalid or unenforceable by a court, the remaining provisions of these Terms will remain in effect. These Terms constitute the entire agreement between us regarding our Service and supersede and replace any prior agreements we might have had between us regarding Service.\n\n18. Changes To Service\n\nWe reserve the right to withdraw or amend our Service, and any service or material we provide via Service, in our sole discretion without notice. We will not be liable if for any reason all or any part of Service is unavailable at any time or for any period. From time to time, we may restrict access to some parts of Service, or the entire Service, to users, including registered users.\n\n19. Amendments To Terms\n\nWe may amend Terms at any time by posting the amended terms on this site. It is your responsibility to review these Terms periodically.\n\nYour continued use of the Platform following the posting of revised Terms means that you accept and agree to the changes. You are expected to check this page frequently so you are aware of any changes, as they are binding on you.\n\nBy continuing to access or use our Service after any revisions become effective, you agree to be bound by the revised terms. If you do not agree to the new terms, you are no longer authorized to use Service.\n\n20. Waiver And Severability\n\nNo waiver by Company of any term or condition set forth in Terms shall be deemed a further or continuing waiver of such term or condition or a waiver of any other term or condition, and any failure of Company to assert a right or provision under Terms shall not constitute a waiver of such right or provision.\n\nIf any provision of Terms is held by a court or other tribunal of competent jurisdiction to be invalid, illegal or unenforceable for any reason, such provision shall be eliminated or limited to the minimum extent such that the remaining provisions of Terms will continue in full force and effect.\n\n21. Acknowledgement\n\nBY USING SERVICE OR OTHER SERVICES PROVIDED BY US, YOU ACKNOWLEDGE THAT YOU HAVE READ THESE TERMS OF SERVICE AND AGREE TO BE BOUND BY THEM.\n\n22. Contact Us\n\nPlease send your feedback, comments, requests for technical support by email: zrytezene@gmail.com.\n\n\nPRIVACY POLICY\nEffective date: 2020-04-05\n\n1. Introduction\n\nWelcome to Zrytezene.\n\nZryteZene (“us”, “we”, or “our”) operates https://zrytezene.web.app (hereinafter referred to as “Service”).\n\nOur Privacy Policy governs your visit to https://zrytezene.web.app, and explains how we collect, safeguard and disclose information that results from your use of our Service.\n\nWe use your data to provide and improve Service. By using Service, you agree to the collection and use of information in accordance with this policy. Unless otherwise defined in this Privacy Policy, the terms used in this Privacy Policy have the same meanings as in our Terms and Conditions.\n\nOur Terms and Conditions (“Terms”) govern all use of our Service and together with the Privacy Policy constitutes your agreement with us (“agreement”).\n\n2. Definitions\n\nSERVICE means the https://zrytezene.web.app website operated by ZryteZene.\n\nPERSONAL DATA means data about a living individual who can be identified from those data (or from those and other information either in our possession or likely to come into our possession).\n\nUSAGE DATA is data collected automatically either generated by the use of Service or from Service infrastructure itself (for example, the duration of a page visit).\n\nCOOKIES are small files stored on your device (computer or mobile device).\n\nDATA CONTROLLER means a natural or legal person who (either alone or jointly or in common with other persons) determines the purposes for which and the manner in which any personal data are, or are to be, processed. For the purpose of this Privacy Policy, we are a Data Controller of your data.\n\nDATA PROCESSORS (OR SERVICE PROVIDERS) means any natural or legal person who processes the data on behalf of the Data Controller. We may use the services of various Service Providers in order to process your data more effectively.\n\nDATA SUBJECT is any living individual who is the subject of Personal Data.\n\nTHE USER is the individual using our Service. The User corresponds to the Data Subject, who is the subject of Personal Data.\n\n3. Information Collection and Use\n\nWe collect several different types of information for various purposes to provide and improve our Service to you.\n\n4. Types of Data Collected\n\nPersonal Data\n\nWhile using our Service, we may ask you to provide us with certain personally identifiable information that can be used to contact or identify you (“Personal Data”). Personally identifiable information may include, but is not limited to:\n\n0.1. Email address\n\n0.2. First name and last name\n\n0.3. Phone number\n\n0.4. Address, Country, State, Province, ZIP/Postal code, City\n\n0.5. Cookies and Usage Data\n\nWe may use your Personal Data to contact you with newsletters, marketing or promotional materials and other information that may be of interest to you. You may opt out of receiving any, or all, of these communications from us by following the unsubscribe link.\n\nUsage Data\n\nWe may also collect information that your browser sends whenever you visit our Service or when you access Service by or through any device (“Usage Data”).\n\nThis Usage Data may include information such as your computer’s Internet Protocol address (e.g. IP address), browser type, browser version, the pages of our Service that you visit, the time and date of your visit, the time spent on those pages, unique device identifiers and other diagnostic data.\n\nWhen you access Service with a device, this Usage Data may include information such as the type of device you use, your device unique ID, the IP address of your device, your device operating system, the type of Internet browser you use, unique device identifiers and other diagnostic data.\n\nTracking Cookies Data\n\nWe use cookies and similar tracking technologies to track the activity on our Service and we hold certain information.\n\nCookies are files with a small amount of data which may include an anonymous unique identifier. Cookies are sent to your browser from a website and stored on your device. Other tracking technologies are also used such as beacons, tags and scripts to collect and track information and to improve and analyze our Service.\n\nYou can instruct your browser to refuse all cookies or to indicate when a cookie is being sent. However, if you do not accept cookies, you may not be able to use some portions of our Service.\n\nExamples of Cookies we use:\n\n0.1. Session Cookies: We use Session Cookies to operate our Service.\n\n0.2. Preference Cookies: We use Preference Cookies to remember your preferences and various settings.\n\n0.3. Security Cookies: We use Security Cookies for security purposes.\n\n0.4. Advertising Cookies: Advertising Cookies are used to serve you with advertisements that may be relevant to you and your interests.\n\nOther Data\n\nWhile using our Service, we may also collect the following information: sex, age, date of birth, place of birth, passport details, citizenship, registration at place of residence and actual address, telephone number (work, mobile), details of documents on education, qualification, professional training, employment agreements, non-disclosure agreements, information on bonuses and compensation, information on marital status, family members, social security (or other taxpayer identification) number, office location and other data.\n\n5. Use of Data\n\nZryteZene uses the collected data for various purposes:\n\n0.1. to provide and maintain our Service;\n\n0.2. to notify you about changes to our Service;\n\n0.3. to allow you to participate in interactive features of our Service when you choose to do so;\n\n0.4. to provide customer support;\n\n0.5. to gather analysis or valuable information so that we can improve our Service;\n\n0.6. to monitor the usage of our Service;\n\n0.7. to detect, prevent and address technical issues;\n\n0.8. to fulfil any other purpose for which you provide it;\n\n0.9. to carry out our obligations and enforce our rights arising from any contracts entered into between you and us, including for billing and collection;\n\n0.10. to provide you with notices about your account and/or subscription, including expiration and renewal notices, email-instructions, etc.;\n\n0.11. to provide you with news, special offers and general information about other goods, services and events which we offer that are similar to those that you have already purchased or enquired about unless you have opted not to receive such information;\n\n0.12. in any other way we may describe when you provide the information;\n\n0.13. for any other purpose with your consent.\n\n6. Retention of Data\n\nWe will retain your Personal Data only for as long as is necessary for the purposes set out in this Privacy Policy. We will retain and use your Personal Data to the extent necessary to comply with our legal obligations (for example, if we are required to retain your data to comply with applicable laws), resolve disputes, and enforce our legal agreements and policies.\n\nWe will also retain Usage Data for internal analysis purposes. Usage Data is generally retained for a shorter period, except when this data is used to strengthen the security or to improve the functionality of our Service, or we are legally obligated to retain this data for longer time periods.\n\n7. Transfer of Data\n\nYour information, including Personal Data, may be transferred to – and maintained on – computers located outside of your state, province, country or other governmental jurisdiction where the data protection laws may differ from those of your jurisdiction.\n\nIf you are located outside Indonesia and choose to provide information to us, please note that we transfer the data, including Personal Data, to Indonesia and process it there.\n\nYour consent to this Privacy Policy followed by your submission of such information represents your agreement to that transfer.\n\nZryteZene will take all the steps reasonably necessary to ensure that your data is treated securely and in accordance with this Privacy Policy and no transfer of your Personal Data will take place to an organisation or a country unless there are adequate controls in place including the security of your data and other personal information.\n\n8. Disclosure of Data\n\nWe may disclose personal information that we collect, or you provide:\n\n0.1. Disclosure for Law Enforcement.\n\nUnder certain circumstances, we may be required to disclose your Personal Data if required to do so by law or in response to valid requests by public authorities.\n\n0.2. Business Transaction.\n\nIf we or our subsidiaries are involved in a merger, acquisition or asset sale, your Personal Data may be transferred.\n\n0.3. Other cases. We may disclose your information also:\n\n0.3.1. to our subsidiaries and affiliates;\n\n0.3.2. to contractors, service providers, and other third parties we use to support our business;\n\n0.3.3. to fulfill the purpose for which you provide it;\n\n0.3.4. for the purpose of including your company’s logo on our website;\n\n0.3.5. for any other purpose disclosed by us when you provide the information;\n\n0.3.6. with your consent in any other cases;\n\n0.3.7. if we believe disclosure is necessary or appropriate to protect the rights, property, or safety of the Company, our customers, or others.\n\n9. Security of Data\n\nThe security of your data is important to us but remember that no method of transmission over the Internet or method of electronic storage is 100% secure. While we strive to use commercially acceptable means to protect your Personal Data, we cannot guarantee its absolute security.\n\n10. Service Providers\n\nWe may employ third party companies and individuals to facilitate our Service (“Service Providers”), provide Service on our behalf, perform Service-related services or assist us in analysing how our Service is used.\n\nThese third parties have access to your Personal Data only to perform these tasks on our behalf and are obligated not to disclose or use it for any other purpose.\n\n11. Analytics\n\nWe may use third-party Service Providers to monitor and analyze the use of our Service.\n\n12. CI/CD tools\n\nWe may use third-party Service Providers to automate the development process of our Service.\n\n13. Behavioral Remarketing\n\nWe may use remarketing services to advertise on third party websites to you after you visited our Service. We and our third-party vendors use cookies to inform, optimise and serve ads based on your past visits to our Service.\n\n14. Links to Other Sites\n\nOur Service may contain links to other sites that are not operated by us. If you click a third party link, you will be directed to that third party’s site. We strongly advise you to review the Privacy Policy of every site you visit.\n\nWe have no control over and assume no responsibility for the content, privacy policies or practices of any third party sites or services.\n\nFor example, the outlined Privacy Policy has been created using PolicyMaker.io, a free web application for generating high-quality legal documents. PolicyMaker’s online free privacy policy generator is an easy-to-use tool for creating privacy policy template for a website, blog, online store or app.\n\n15. Children’s Privacy\n\nOur Services are not intended for use by children under the age of 18 (“Child” or “Children”).\n\nWe do not knowingly collect personally identifiable information from Children under 18. If you become aware that a Child has provided us with Personal Data, please contact us. If we become aware that we have collected Personal Data from Children without verification of parental consent, we take steps to remove that information from our servers.\n\n16. Changes to This Privacy Policy\n\nWe may update our Privacy Policy from time to time. We will notify you of any changes by posting the new Privacy Policy on this page.\n\nWe will let you know via email and/or a prominent notice on our Service, prior to the change becoming effective and update “effective date” at the top of this Privacy Policy.\n\nYou are advised to review this Privacy Policy periodically for any changes. Changes to this Privacy Policy are effective when they are posted on this page.\n\n17. Contact Us\n\nIf you have any questions about this Privacy Policy, please contact us by email: zrytezene@gmail.com.\n\n\nDISCLAIMER\nLast updated: 2020-04-05\n\nWEBSITE DISCLAIMER\n\nThe information provided by ZryteZene (“Company”, “we”, “our”, “us”) on https://zrytezene.web.app (the “Site”) is for general informational purposes only. All information on the Site is provided in good faith, however we make no representation or warranty of any kind, express or implied, regarding the accuracy, adequacy, validity, reliability, availability, or completeness of any information on the Site.\n\nUNDER NO CIRCUMSTANCE SHALL WE HAVE ANY LIABILITY TO YOU FOR ANY LOSS OR DAMAGE OF ANY KIND INCURRED AS A RESULT OF THE USE OF THE SITE OR RELIANCE ON ANY INFORMATION PROVIDED ON THE SITE. YOUR USE OF THE SITE AND YOUR RELIANCE ON ANY INFORMATION ON THE SITE IS SOLELY AT YOUR OWN RISK.\n\nEXTERNAL LINKS DISCLAIMER\n\nThe Site may contain (or you may be sent through the Site) links to other websites or content belonging to or originating from third parties or links to websites and features. Such external links are not investigated, monitored, or checked for accuracy, adequacy, validity, reliability, availability or completeness by us.\n\nFor example, the outlined Disclaimer has been created using PolicyMaker.io, a free web application for generating high-quality legal documents. PolicyMaker’s free website Disclaimer generator is an easy-to-use tool for creating an excellent Disclaimer template for a website, blog, eCommerce store or app.\n\nWE DO NOT WARRANT, ENDORSE, GUARANTEE, OR ASSUME RESPONSIBILITY FOR THE ACCURACY OR RELIABILITY OF ANY INFORMATION OFFERED BY THIRD-PARTY WEBSITES LINKED THROUGH THE SITE OR ANY WEBSITE OR FEATURE LINKED IN ANY BANNER OR OTHER ADVERTISING. WE WILL NOT BE A PARTY TO OR IN ANY WAY BE RESPONSIBLE FOR MONITORING ANY TRANSACTION BETWEEN YOU AND THIRD-PARTY PROVIDERS OF PRODUCTS OR SERVICES.\n\nAFFILIATES DISCLAIMER\n\nThe Site may contain links to affiliate websites, and we may receive an affiliate commission for any purchases or actions made by you on the affiliate websites using such links.\n\nERRORS AND OMISSIONS DISCLAIMER\n\nWhile we have made every attempt to ensure that the information contained in this site has been obtained from reliable sources, ZryteZene is not responsible for any errors or omissions or for the results obtained from the use of this information. All information in this site is provided “as is”, with no guarantee of completeness, accuracy, timeliness or of the results obtained from the use of this information, and without warranty of any kind, express or implied, including, but not limited to warranties of performance, merchantability, and fitness for a particular purpose.\n\nIn no event will ZryteZene, its related partnerships or corporations, or the partners, agents or employees thereof be liable to you or anyone else for any decision made or action taken in reliance on the information in this Site or for any consequential, special or similar damages, even if advised of the possibility of such damages.\n\nGUEST CONTRIBUTORS DISCLAIMER\n\nThis Site may include content from guest contributors and any views or opinions expressed in such posts are personal and do not represent those of ZryteZene or any of its staff or affiliates unless explicitly stated.\n\nLOGOS AND TRADEMARKS DISCLAIMER\n\nAll logos and trademarks of third parties referenced on https://zrytezene.web.app are the trademarks and logos of their respective owners. Any inclusion of such trademarks or logos does not imply or constitute any approval, endorsement or sponsorship of ZryteZene by such owners.\n\nCONTACT US\n\nShould you have any feedback, comments, requests for technical support or other inquiries, please contact us by email: zrytezene@gmail.com.\n");
    }

    @Override
    public void onStart() {
        super.onStart();

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
    public void onBackPressed() {

    }

    private void _BlackStatusBarIcons() {
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
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

    private void _setFont() {
        Typeface tpf = Typeface.createFromAsset(getAssets(), "fonts/googlesans.ttf");
        textview2.setTypeface(tpf, 0);
        textview3.setTypeface(tpf, 0);
        textview4.setTypeface(tpf, 0);
        textview5.setTypeface(tpf, 0);
        textview6.setTypeface(tpf, 0);
        textview7.setTypeface(tpf, 0);
        textview8.setTypeface(tpf, 0);
        edittext1.setTypeface(tpf, 0);
        textview9.setTypeface(tpf, 0);
        edittext2.setTypeface(tpf, 0);
        textview10.setTypeface(tpf, 0);
        button1.setTypeface(tpf, 0);
        textview11.setTypeface(tpf, 0);
        textview12.setTypeface(tpf, 0);
        textview13.setTypeface(tpf, 0);
        checkbox1.setTypeface(tpf, 0);
        button2.setTypeface(tpf, 0);
    }

    private void _nextSetup() {
        currentSetup++;
        if (currentSetup < 6) {
            linear2.setVisibility(View.GONE);
            linear6.setVisibility(View.GONE);
            linear10.setVisibility(View.GONE);
            linear17.setVisibility(View.GONE);
            _fab.hide();
            if (currentSetup==2) {
                linear2.setVisibility(View.VISIBLE);
                _customNav("#FFFFFF");
                _BlackStatusBarIcons();
                _fab.show();
            }
            if (currentSetup==3) {
                linear6.setVisibility(View.VISIBLE);
                _customNav("#FFFFFF");
                getWindow().getDecorView().setSystemUiVisibility(0);
                getWindow().setStatusBarColor(Color.parseColor("#3F51B5"));
            }
            if (currentSetup==4) {
                linear10.setVisibility(View.VISIBLE);
                _customNav("#FFFFFF");
                getWindow().getDecorView().setSystemUiVisibility(0);
                getWindow().setStatusBarColor(Color.parseColor("#2196F3"));
            }
            if (currentSetup==5) {
                linear17.setVisibility(View.VISIBLE);
                _customNav("#FFFFFF");
                _BlackStatusBarIcons();
            }
        } else {
            data.edit().putString("setup", "1").commit();
            data.edit().putString("license", "1").commit();
            intent.setClass(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private void _customNav(final String _color) {
        this.getWindow().setNavigationBarColor(Color.parseColor(_color));
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


    private void _shadow(final View _v, final double _n) {
        _v.setElevation((float) _n);
    }


    private void _grantPermission() {
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            showMessage("Please allow the notification settings");
            Intent opd = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            opd.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(opd);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK}, 1001);
                } else {
                    _nextSetup();
                }
            } else {
                _nextSetup();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK) != PackageManager.PERMISSION_GRANTED) {
                _customSnack("Please tap \"Grant\" to continue.", 0);
            } else {
                button2.setText("Next");
            }
        }
    }


    private void _resetDialog() {
        dialog = null;
        dialog = new AlertDialog.Builder(this);
    }


    private void _customSnack(final String _txt, final double _icon) {
        ViewGroup containerLayout = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        com.google.android.material.snackbar.Snackbar snackbar = com.google.android.material.snackbar.Snackbar.make(containerLayout, "", com.google.android.material.snackbar.Snackbar.LENGTH_LONG);
        com.google.android.material.snackbar.Snackbar.SnackbarLayout layout = (com.google.android.material.snackbar.Snackbar.SnackbarLayout) snackbar.getView();
        View snackview = getLayoutInflater().inflate(R.layout.custom_snack, null);
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
        layout.addView(snackview, 0);
        snackbar.show();
    }


    private void _rippleLib() {
    }

    public void drawableclass() {
    }

    private void _rippleEffect(final View _view, final String _color) {
        _view.setBackground(Drawables.getSelectableDrawableFor(Color.parseColor(_color)));
        _view.setClickable(true);
    }

    private void _abandonFocus() {
        View _tmpView = this.getCurrentFocus();
        if (_tmpView != null) {
            android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(_tmpView.getWindowToken(), 0);
        }
    }

    private void _nxtStep() {

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

}
