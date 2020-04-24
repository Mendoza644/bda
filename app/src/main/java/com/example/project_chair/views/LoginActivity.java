package com.example.project_chair.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project_chair.R;
import com.example.project_chair.model.TokenUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    private EditText mEditextEmail;
    private EditText mEditextPassword;
    private Button btnLogin;
    private String email = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.LoginToolbar);
        setSupportActionBar(myToolbar);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mEditextEmail = findViewById(R.id.edit_correo_sesion);
        mEditextPassword = findViewById(R.id.edit_contrasena_sesion);
        btnLogin = findViewById(R.id.btnLogin);

        email = mEditextEmail.getText().toString().trim();
        password = mEditextPassword.getText().toString().trim();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = mEditextEmail.getText().toString().trim();
                password = mEditextPassword.getText().toString().trim();
                if (!email.isEmpty() && !password.isEmpty()){
                    signIn(email, password);
                 }
                else{
                Toast.makeText(LoginActivity.this, "Los campos son necesarios", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signIn(String correo, String contrasena){
        mAuth.signInWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.e("LOGUEADO", "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        getTokenFromCloudMessagingService(user);
                    } else {
                        updateUI(null);
                    }
                }
                else {
                    updateUI(null);
                }
            }
        });

    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent newIntent = new Intent(this, PrincipalActivity.class);
            Toast.makeText(this, "Ya existe una sesion activa", Toast.LENGTH_LONG).show();
            startActivity(newIntent);
            finish();
        } else {
            Log.i("Error Session", getString(R.string.warning_message));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        } else {
            updateUI(null);
        }
    }

    private void getTokenFromCloudMessagingService(final FirebaseUser userAuth){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()){
                    Log.w("token", "getInstance failed", task.getException());
                } else {
                    final String token = Objects.requireNonNull(task.getResult()).getToken();
                    String user = userAuth.getUid();
                    String device = "Android";

                    final TokenUser tokenUser = new TokenUser(token, device, user);
                    FirebaseFirestore.getInstance().collection("TokenUsuario").whereEqualTo("deviceToken", token).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                ArrayList tokenUserModel =(ArrayList) Objects.requireNonNull(task.getResult()).toObjects(TokenUser.class);
                                if (tokenUserModel.size() == 0){
                                    FirebaseFirestore.getInstance().collection("TokenUsuario").document().set(tokenUser);
                                    updateUI(userAuth);
                                }
                            }else {
                                Log.i("token_exist", token);
                            }
                        }
                    });
                }
            }
        });
    }
}
