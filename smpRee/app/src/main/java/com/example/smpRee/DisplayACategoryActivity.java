package com.example.smpRee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import Model.Products;
import ViewHolder.ProductViewHolder;

public class DisplayACategoryActivity extends AppCompatActivity {

    private DatabaseReference uProductsRef;
    private String uCategoryName;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private String dacuserType = "";

    private boolean match = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_acategory);

        recyclerView = findViewById(R.id.each_category_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        dacuserType= getIntent().getExtras().get("uType").toString();
        uCategoryName = getIntent().getExtras().get("category").toString();

        //Toast.makeText(DisplayACategoryActivity.this, "IN "+uCategoryName, Toast.LENGTH_SHORT).show();


        //node in database that contain all products
        uProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(uProductsRef, Products.class).build();

            Query query = uProductsRef.orderByChild("category").equalTo(uCategoryName);

            options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(query, Products.class).build();


            FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new
                FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        //le21
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (dacuserType.equals("Admin")) {

                                    Intent intent = new Intent(DisplayACategoryActivity.this, AdminManageProductsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(DisplayACategoryActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                            }
                        });
                    }


                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                        //return null;

                        // access products item layout
                        View view = LayoutInflater.from(parent.getContext()).
                                inflate(R.layout.product_items_layout, parent, false);


                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;


                    }
                };



        //set the adapter
        //populate recyclerview
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}