package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView tshirt, sport, fdress,sweater,glasses,walletbBagPurse,hat,shoes,headphone,laptop,watches,mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        tshirt = findViewById(R.id.t_shirts);
        sport = findViewById(R.id.sports);
        fdress = findViewById(R.id.f_dress);
        sweater = findViewById(R.id.sweater);
        glasses = findViewById(R.id.glass);
        walletbBagPurse = findViewById(R.id.purse_bag);
        hat = findViewById(R.id.hats);
        shoes = findViewById(R.id.shoes);
        headphone = findViewById(R.id.headphones);
        laptop = findViewById(R.id.laptop);
        watches = findViewById(R.id.watches);
        mobile = findViewById(R.id.mobiles);

        tshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,  AdminAddNewProductActivity.class);
                intent.putExtra("category", "Tshirt");
                startActivity(intent);
            }
        });
        sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,  AdminAddNewProductActivity.class);
                intent.putExtra("category", "Sport Tshirt");
            }
        });
        fdress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,  AdminAddNewProductActivity.class);
                intent.putExtra("category", "Female Dresses");
            }
        });
        sweater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,  AdminAddNewProductActivity.class);
                intent.putExtra("category", "Sweater");
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,  AdminAddNewProductActivity.class);
                intent.putExtra("category", "Glasses");
            }
        });
        walletbBagPurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,  AdminAddNewProductActivity.class);
                intent.putExtra("category", "Wallet Purse Bag");
            }
        });
        hat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,  AdminAddNewProductActivity.class);
                intent.putExtra("category", "Hat");
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,  AdminAddNewProductActivity.class);
                intent.putExtra("category", "Shoes");
            }
        });
        headphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,  AdminAddNewProductActivity.class);
                intent.putExtra("category", "Headphone");
            }
        });
        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,  AdminAddNewProductActivity.class);
                intent.putExtra("category", "Laptop");
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,  AdminAddNewProductActivity.class);
                intent.putExtra("category", "Watches");
            }
        });
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,  AdminAddNewProductActivity.class);
                intent.putExtra("category", "Mobile");


            }
        });
    }
}
