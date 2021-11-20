package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.example.project.Adapter.ProductManagerAdapter;
import com.example.project.Domain.ProductManagerDomain;
import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductManagerActivity extends AppCompatActivity {
    String ID;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ImageView cancel;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manager);
        matching();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        ID = extras.getString("idData");

        load();
        Add();
    }

    private void Add() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductManagerActivity.this, AddProductActivity.class);
                intent.putExtra("MaSanPham", "Add");
                intent.putExtra("MaDanhMuc", "Add");
                startActivity(intent);
                finish();
            }
        });
    }

    private void load() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ArrayList<ProductManagerDomain> productlist = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(ID);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren()) {
                    String value = data.getValue().toString();
                    String ten = value.substring(value.indexOf("Ten") + 4, value.indexOf(", Gia"));
                    String gia = value.substring(value.indexOf("Gia") + 4, value.length() - 1);
                    String mota = value.substring(value.indexOf("MoTa") + 5, value.indexOf(", Ten"));
                    String masp = data.getKey();
                    String hinhanh = value.substring(value.indexOf("HinhAnh") + 8, value.indexOf(", MoTa"));
                    String danhmuc = value.substring(value.indexOf("DanhMuc") + 8, value.indexOf(", HinhAnh"));
                    productlist.add(new ProductManagerDomain(danhmuc, masp, ten, gia, mota, hinhanh));
                }
                adapter = new ProductManagerAdapter(ProductManagerActivity.this, productlist);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void matching() {
        recyclerView = (RecyclerView) findViewById(R.id.rcv_product_manager);
        cancel = (ImageView) findViewById(R.id.img_product_manager_cancel);
        add = (Button) findViewById(R.id.btn_product_manager_add);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}