package com.example.project_chair.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_chair.R;
import com.example.project_chair.model.CategoryProduct;
import com.example.project_chair.views.ListProductActivity;

import java.util.List;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ProductHolder> {
    private List<CategoryProduct> listProducts;

    public AdapterProduct(ListProductActivity listProductActivity, List<CategoryProduct> listProducts) {
        this.listProducts = listProducts;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
CategoryProduct category_product = listProducts.get(position);
holder.nameProduct.setText(category_product.getName());
holder.descriptionProduct.setText(category_product.getName());
holder.imageViewProduct.setImageResource(R.drawable.electrodomesticos);

    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
private TextView nameProduct;
private TextView descriptionProduct;
private ImageView imageViewProduct;



        public ProductHolder(@NonNull View itemView) {
            super(itemView);
nameProduct = itemView.findViewById(R.id.txt_nameProduct);
descriptionProduct = itemView.findViewById(R.id.txt_des);
imageViewProduct = itemView.findViewById(R.id.img_product);

        }
    }

}
