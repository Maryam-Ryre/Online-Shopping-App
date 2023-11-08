package com.example.smpRee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminManageProductsActivity extends AppCompatActivity {

    private Button applyChangesBtn, pDeleteBtn;
    private EditText name, price, description;
    private ImageView imageView;

    private String productID = "";
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_products);

        productID = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        pDeleteBtn = (Button)findViewById(R.id.delete_product_manage_btn);
        applyChangesBtn = (Button)findViewById(R.id.apply_changes_manage_btn);
        name = (EditText) findViewById(R.id.product_name_manage);
        price = (EditText) findViewById(R.id.product_price_manage);
        description = (EditText) findViewById(R.id.product_description_manage);
        imageView = findViewById(R.id.product_image_manage);

        displayParticularProductInfo();


        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                applyChanges();

            }
        });

        pDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteProduct();
            }
        });
    }

    private void deleteProduct() {

        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(AdminManageProductsActivity.this, "Product Deleted", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AdminManageProductsActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void applyChanges() {

        String sName = name.getText().toString();
        String sPrice = price.getText().toString();
        String sDescription = description.getText().toString();

        if(sName.equals("")){

            Toast.makeText(this, "Can't be empty", Toast.LENGTH_SHORT).show();
        }
       else if (sPrice.equals("")){
            Toast.makeText(this, "Can't be empty", Toast.LENGTH_SHORT).show();
        }
       else if(sDescription.equals("")){
            Toast.makeText(this, "Can't be empty", Toast.LENGTH_SHORT).show();
        }
       else{

            //store using hashmap
            HashMap<String, Object> productMap = new HashMap<>();
            //putting data to it
            productMap.put("pid", productID);
            productMap.put("description", sDescription);
            productMap.put("price", sPrice);
            productMap.put("pname", sName);

            //update query

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        Toast.makeText(AdminManageProductsActivity.this, "Changes Applied", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminManageProductsActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }

    }

    private void displayParticularProductInfo() {

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    String pName = snapshot.child("pname").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String pDescription = snapshot.child("description").getValue().toString();
                    String pImage = snapshot.child("image").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);

                    // load and display into field ujname
                    Picasso.get().load(pImage).into(imageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}