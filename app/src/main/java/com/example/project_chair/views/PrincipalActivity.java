package com.example.project_chair.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_chair.R;
import com.example.project_chair.adapters.MyAdapter;
import com.example.project_chair.model.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;


public class PrincipalActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    //List to store all the categories
    List<Category> categoryList;

    //the recyclerView
    RecyclerView recyclerView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Toolbar myToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(myToolbar);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getCategories();

        //initializing the productlist
        categoryList = new ArrayList<>();
        adapter = new MyAdapter(this, categoryList);
        recyclerView.setAdapter(adapter);

    }

    private void getCategories() {
        mFirestore.collection("Categoria")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("documentos", document.getId() + " => " + document.getData());
                                Category categoria = document.toObject(Category.class);

                                categoryList.add(
                                        new Category(
                                                categoria.getId(), categoria.getName(),
                                                R.drawable.electrodomesticos));

                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("ErrorDocumentos", "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drawer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sesion:
                Intent sesion = new Intent(this, LoginActivity.class);
                startActivity(sesion);
                finish();
                return true;

            case R.id.action_register:
                Intent registro = new Intent(this, MainActivity.class);
                startActivity(registro);
                finish();
                return true;

            case R.id.action_profile:
                Intent profile = new Intent(this, ProfileActivity.class);
                startActivity(profile);
                finish();
                return true;
            case R.id.action_logout:
                mFirestore.collection("TokenUsuario").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ListIterator documents = Objects.requireNonNull(task.getResult()).getDocuments().listIterator();

                        }
                    }
                });




                return true;
            default:

                return super.onOptionsItemSelected(item);

        }
    }

}
