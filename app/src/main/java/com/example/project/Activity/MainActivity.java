package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.project.Adapter.CategoryAdapter;
import com.example.project.Domain.CategoryDomain;
import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    RecyclerView.Adapter adapter;
    RecyclerView recyclerViewCategoryList;
    LinearLayout profile, donhang, support;
    FloatingActionButton btncart;
    String ID;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.tv_tenUser);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        ID = extras.getString("idUser");
        name.setText("Xin chào " + extras.getString("nameUser") + " !");
        profile();
        recyclerViewCategory();
        cart();
        Donhang();
        Support();
    }

    private void Support() {
        support = (LinearLayout) findViewById(R.id.Linear_support);
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sup = new Intent(MainActivity.this, SupportActivity.class);
                sup.putExtra("idUser", ID);
                startActivity(sup);
            }
        });
    }

    private void Donhang() {
        donhang = (LinearLayout) findViewById(R.id.linner_order);
        donhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent order = new Intent(MainActivity.this, ListOrderActivity.class);
                order.putExtra("idUser", ID);
                startActivity(order);
            }
        });
    }

    private void cart() {
        btncart = (FloatingActionButton) findViewById(R.id.card_btn_main);

        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Cart = new Intent(MainActivity.this, CartActivity.class);
                Cart.putExtra("idUser", ID);
                startActivity(Cart);
            }
        });
    }

    private void profile() {
        profile = (LinearLayout) findViewById(R.id.liner_Propfile);
        Intent idPro = new Intent(MainActivity.this, ProfileActivity.class);
        idPro.putExtra("idProf", ID);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(idPro);
            }
        });
    }

    private void recyclerViewCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCategoryList = findViewById(R.id.recyclerView);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        ArrayList<CategoryDomain> categoryList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DanhMuc");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String[] splitCategory = snapshot.getValue().toString().split("[}],");
                for (int i = splitCategory.length - 1; i >= 0; i--) {
                    if (splitCategory[i].substring(splitCategory[i].indexOf("Ten=") + 4).indexOf("}}") != -1) {
                        categoryList.add(new CategoryDomain(splitCategory[i].substring(splitCategory[i].indexOf("Ten=") + 4, splitCategory[i].length() - 2),
                                splitCategory[i].substring(splitCategory[i].indexOf("HinhAnh=") + 8, splitCategory[i].indexOf(", Ten")), ID));
                        adapter = new CategoryAdapter(MainActivity.this, categoryList);
                        recyclerViewCategoryList.setAdapter(adapter);
                    } else {
                        categoryList.add(new CategoryDomain(splitCategory[i].substring(splitCategory[i].indexOf("Ten=") + 4),
                                splitCategory[i].substring(splitCategory[i].indexOf("HinhAnh=") + 8, splitCategory[i].indexOf(", Ten")), ID));
                        adapter = new CategoryAdapter(MainActivity.this, categoryList);
                        recyclerViewCategoryList.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}