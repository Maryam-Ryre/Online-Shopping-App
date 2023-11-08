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
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import Model.Products;
import ViewHolder.ProductViewHolder;

public class CategoriesDisplayUserActivity extends AppCompatActivity {

    private ImageView utShirts, usportsTShirts, ufemaleDresses, usweaters;
    private ImageView uglasses, uhatsCaps, uwalletsBagsPurses, ushoes;
    private ImageView uheadPhonesHandsfree, uLaptops, uwatches, umobilePhones;


    private String cduserType = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_display_user);


        /*Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){ // if there is an intent, coming from admin activity

            userType = getIntent().getExtras().get("Admin").toString();
        }*/

        cduserType = getIntent().getExtras().get("uType").toString();

        //Toast.makeText(CategoriesDisplayUserActivity.this, "USER:  "+ cduserType, Toast.LENGTH_SHORT).show();


        utShirts = (ImageView )findViewById(R.id.t_shirts_userDisp);
        usportsTShirts = (ImageView )findViewById(R.id.spors_t_shirts_userDisp);
        ufemaleDresses = (ImageView )findViewById(R.id.female_dresses_userDisp);
        usweaters = (ImageView )findViewById(R.id.sweaters_userDisp);

        uglasses = (ImageView )findViewById(R.id.glasses_userDisp);
        uhatsCaps = (ImageView )findViewById(R.id.hats_caps_userDisp);
        uwalletsBagsPurses = (ImageView )findViewById(R.id.purses_bags_wallets_userDisp);
        ushoes = (ImageView )findViewById(R.id.shoes_userDisp);

        uheadPhonesHandsfree = (ImageView )findViewById(R.id.headphones_handsfree_userDisp);
        uLaptops = (ImageView )findViewById(R.id.laptop_pc_userDisp);
        uwatches = (ImageView )findViewById(R.id.watches_userDisp);
        umobilePhones = (ImageView )findViewById(R.id.mobilephones_userDisp);

        utShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoriesDisplayUserActivity.this, DisplayACategoryActivity.class);
                intent.putExtra("uType", cduserType);
                intent.putExtra("category", "tShirts");  // key, value
                startActivity(intent);
            }
        });


        usportsTShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoriesDisplayUserActivity.this, DisplayACategoryActivity.class);
                intent.putExtra("uType", cduserType);
                intent.putExtra("category", "Sports tShirts");  // key, value
                startActivity(intent);
            }
        });


        ufemaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoriesDisplayUserActivity.this, DisplayACategoryActivity.class);
                intent.putExtra("uType", cduserType);
                intent.putExtra("category", "Female Dresses");
                startActivity(intent);
            }
        });

        usweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoriesDisplayUserActivity.this, DisplayACategoryActivity.class);
                intent.putExtra("uType", cduserType);
                intent.putExtra("category", "Sweaters");
                startActivity(intent);
            }
        });


        uglasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoriesDisplayUserActivity.this, DisplayACategoryActivity.class);
                intent.putExtra("uType", cduserType);
                intent.putExtra("category", "Glasses");
                startActivity(intent);
            }
        });

        uhatsCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoriesDisplayUserActivity.this, DisplayACategoryActivity.class);
                intent.putExtra("uType", cduserType);
                intent.putExtra("category", "Hats Caps");
                startActivity(intent);
            }
        });

        uwalletsBagsPurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoriesDisplayUserActivity.this, DisplayACategoryActivity.class);
                intent.putExtra("uType", cduserType);
                intent.putExtra("category", "Wallets Bags Purses");
                startActivity(intent);
            }
        });


        ushoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoriesDisplayUserActivity.this, DisplayACategoryActivity.class);
                intent.putExtra("uType", cduserType);
                intent.putExtra("category", "Shoes");
                startActivity(intent);
            }
        });


        uheadPhonesHandsfree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoriesDisplayUserActivity.this, DisplayACategoryActivity.class);
                intent.putExtra("uType", cduserType);
                intent.putExtra("category", "HeadPhones Handsfree");
                startActivity(intent);
            }
        });


        uLaptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoriesDisplayUserActivity.this, DisplayACategoryActivity.class);
                intent.putExtra("uType", cduserType);
                intent.putExtra("category", "Laptops");
                startActivity(intent);
            }
        });

        uwatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoriesDisplayUserActivity.this, DisplayACategoryActivity.class);
                intent.putExtra("uType", cduserType);
                intent.putExtra("category", "Watches");
                startActivity(intent);
            }
        });



        umobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoriesDisplayUserActivity.this, DisplayACategoryActivity.class);
                intent.putExtra("uType", cduserType);
                intent.putExtra("category", "Mobile Phones");
                startActivity(intent);
            }
        });

    }
}