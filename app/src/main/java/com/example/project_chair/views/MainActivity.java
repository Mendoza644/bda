package com.example.project_chair.views;


import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_chair.R;
import com.example.project_chair.model.TokenUser;
import com.example.project_chair.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.RegEx;

import static android.view.View.INVISIBLE;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private EditText mEditextName;
    private EditText mEditextEmail;
    private EditText mEditextPassword;
    private EditText mEditextApellido;
    private EditText mEditextConfirmP;
    private Button mButton;
    private String name = "";
    private String email = "";
    private String password = "";
    private String apellido = "";
    private String confirmPass = "";
    private Boolean isValid = false;
    private Boolean isValidPassword = false;
    private String regexPattern = "/^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])([A-Za-z\\d$@$!%*?&]|[^ ]){8,15}$/";
    private TextInputLayout layout_name;
    private TextInputLayout layout_surname;
    private TextInputLayout layout_email;
    private TextInputLayout layout_password;
    private TextInputLayout layout_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inflate elements

       /* mDatabase = FirebaseDatabase.getInstance().getReference();*/
        mAuth = FirebaseAuth.getInstance();
        mEditextEmail = findViewById(R.id.edit_correo);
        mEditextName = findViewById(R.id.edit_name);
        mEditextPassword = findViewById(R.id.edit_contrasena);
        mEditextApellido = findViewById(R.id.edit_apellido);
        mEditextConfirmP = findViewById(R.id.edit_confirmacion);
        layout_name = findViewById(R.id.layout_name);
        layout_surname = findViewById(R.id.layout_apellido);
        layout_email = findViewById(R.id.layout_correo);
        layout_password = findViewById(R.id.layout_contrasena);
        layout_confirm = findViewById(R.id.layout_confirmacion);
        mButton = findViewById(R.id.btnregistro);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.registerToolbar);
        setSupportActionBar(myToolbar);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButton.setVisibility(View.INVISIBLE);
                name = mEditextName.getText().toString();
                apellido = mEditextApellido.getText().toString();
                email = mEditextEmail.getText().toString();
                password = mEditextPassword.getText().toString();
                confirmPass = mEditextConfirmP.getText().toString();
                if (validateEmptyFields()) {
                        registerUser(name, apellido,email);
                } else {
                    Toast.makeText(MainActivity.this, "Debe completar los campos, todos son obligatorios", Toast.LENGTH_SHORT).show();
                    mButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private Boolean validateEmptyFields(){
        if(mEditextName.getText().toString().trim().isEmpty() && mEditextApellido.getText().toString().trim().isEmpty() && mEditextEmail.getText().toString().trim().isEmpty()  && mEditextPassword.getText().toString().trim().isEmpty() && mEditextConfirmP.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "¡Algo a salido mal, completa todos los campos!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(mEditextName.getText().toString().trim())){
            layout_name.setError("El campo esta vacío");
            isValid = false;
        }else if (TextUtils.isEmpty(mEditextApellido.getText().toString().trim())){
            layout_surname.setError("El campo esta vacío");
            isValid = false;
        }else if (TextUtils.isEmpty(mEditextEmail.getText().toString().trim())){
            layout_email.setError("El campo esta vacío");
            isValid = false;
        }else if (TextUtils.isEmpty(mEditextPassword.getText().toString().trim())){
            layout_password.setError("El campo esta vacío");
            isValid = false;
        }else if (TextUtils.isEmpty(mEditextConfirmP.getText().toString().trim())){
            layout_confirm.setError("El campo esta vacío");
            isValid = false;
        }else {
            isValid = true;
        }
        return isValid;
    }

    private Boolean validatePasswordRegex(){
        if (password.equals(confirmPass)){
            if (regexPattern.matches(confirmPass)){
                    isValidPassword= true;
            } else {
                layout_confirm.setError("La contraseña debe ser de 8 caracteres, alfanumérica y al menos una mayúscula y un carácter especial");
                isValidPassword = false;
            }
        } else {
            layout_password.setError("Las contraseñas no coinciden");
            layout_confirm.setError("Las contraseñas no coinciden");
            isValidPassword = false;
        }
        return isValidPassword;
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            updateUI(currentUser);
        }else {
            updateUI(null);
        }

    }

    private void registerUser(final String nombre, final String apellido, final String correo ) {
        try {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           FirebaseUser user = mAuth.getCurrentUser();
                           Log.e("Nuevo usuario", "createUserWithEmail:success");
                            User userModel = new User(nombre, apellido, correo);

                          FirebaseFirestore.getInstance()
                                   .collection("Usuario")
                                   .document(user.getUid())
                                  .set(userModel);
                           user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(mEditextName.getText().toString().trim()).build());
                           getTokenFromCloudMessagingService(user);
                       }
                       else {
                           Log.d("debug ", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()));
                           Toast.makeText(MainActivity.this, "No se pudo registrar", Toast.LENGTH_SHORT).show();
                           updateUI(null);
                       }
                }
            });
        } catch (Exception e) {
            Log.d("debug", e.getMessage());
        }
    }
    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this,"Tu usuario ha sido creado con exito",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, PrincipalActivity.class));
            finish();
        }else {
            Toast.makeText(this,"El correo utilizado ya existe, intente de nuevo",Toast.LENGTH_LONG).show();
            mButton.setVisibility(View.VISIBLE);
        }
    }

    private void getTokenFromCloudMessagingService(final FirebaseUser userAuth){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()){
                    Log.w("token", "getInstanceId Failed", task.getException());
                }
                final String token = Objects.requireNonNull(task.getResult()).getToken();
                String user = userAuth.getUid();
                String device = "Android";

                final TokenUser tokenUser = new TokenUser(token, device, user);
                FirebaseFirestore.getInstance().collection("TokenUsuario").whereEqualTo("deviceToken", token).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            ArrayList tokenUserModel = (ArrayList) Objects.requireNonNull(task.getResult()).toObjects(TokenUser.class);
                            if (tokenUserModel.size() == 0){
                                FirebaseFirestore.getInstance().collection("TokenUsuario").document().set(tokenUser);
                                updateUI(userAuth);
                            }
                        } else {
                            Log.i("token_exist", token);
                        }
                    }
                });
            }
        });

    }
}