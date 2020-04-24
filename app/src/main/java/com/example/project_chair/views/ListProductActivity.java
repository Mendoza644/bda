package com.example.project_chair.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_chair.R;
import com.example.project_chair.adapters.AdapterProduct;
import com.example.project_chair.model.Category;
import com.example.project_chair.model.CategoryProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ListProductActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    List<CategoryProduct> productList;
    private RecyclerView mrecyclerView;
    private AdapterProduct adapterProduct;
    DocumentReference documentReference;


    private void getCategoryProduct(String category) {
        firebaseFirestore.collection("Producto")
                .whereEqualTo("Category",category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("documentos", document.getId() + " => " + document.getData());
                                CategoryProduct categoryProduct = document.toObject(CategoryProduct.class);

                                productList.add(
                                        new CategoryProduct(
                                                categoryProduct.getId(), categoryProduct.getName(), categoryProduct.getDescription()));

                                adapterProduct.notifyDataSetChanged();                            }
                        } else {
                            Log.d("ErrorDocumentos", "Error getting documents: ", task.getException());
                        }
                    }
                });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String category = getIntent().getStringExtra("category");
        firebaseFirestore = FirebaseFirestore.getInstance();
        getCategoryProduct(category);
        setContentView(R.layout.activity_list_product);
        mrecyclerView = findViewById(R.id.recyclerView_list);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterProduct = new AdapterProduct(this,productList);
        mrecyclerView.setAdapter(adapterProduct);





    }


}

