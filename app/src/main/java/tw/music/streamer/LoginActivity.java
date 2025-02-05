package tw.music.streamer;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.text.TextUtils;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText zzEmail, zzPassword;
    private MaterialButton btnLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle a) {
        super.onCreate(a);
        setContentView(R.layout.login);
        initVariables(getApplicationContext());
        initOnClick(getApplicationContext());
        initFirebase(getApplicationContext());
        initLogic(getApplicationContext());
    }

    private void initVariables(final Context a) {
        zzEmail = findViewById(R.id.etEmail);
        zzPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void initFirebase(final Context a) {
        auth = FirebaseAuth.getInstance();
    }

    private void initOnClick(final Context a) {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void initLogic(final Context a) {
    }

    private void loginUser() {
        String email = zzEmail.getText().toString().trim();
        String password = zzPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            zzEmail.setError("Email is required.");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            zzPassword.setError("Password is required.");
            return;
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}