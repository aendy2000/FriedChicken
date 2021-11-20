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

import com.example.project.Adapter.CategoryManagerAdapter;
import com.example.project.Domain.CartDomain;
import com.example.project.Domain.CategoryManagerDomain;
import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryManagerActivity extends AppCompatActivity {
    String ID;
    ImageView cancel;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manager);

        matching();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        ID = extras.getString("idData");
        load();
        them();
    }

    private void them() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryManagerActivity.this, AddCategoryActivity.class);
                intent.putExtra("MaDanhMuc", "Add");
                startActivity(intent);
                finish();
            }
        });
    }

    private void matching() {
        cancel = (ImageView) findViewById(R.id.img_category_manager_cancel);
        add = (Button) findViewById(R.id.btn_category_manager_add);
        recyclerView = (RecyclerView) findViewById(R.id.rcv_category_manager);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void load() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ArrayList<CategoryManagerDomain> categorylist = new ArrayList<>();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference(ID);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    String dataaa = snapshot.child(data.getKey()).getValue().toString();
                    String hinhanh = dataaa.substring(dataaa.indexOf("HinhAnh=") + 8, dataaa.indexOf(", Ten="));
                    String ten = dataaa.substring(dataaa.indexOf("Ten=") + 4, dataaa.length() - 1);
                    categorylist.add(new CategoryManagerDomain(hinhanh, ten, data.getKey()));
                    adapter = new CategoryManagerAdapter(CategoryManagerActivity.this, categorylist);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}