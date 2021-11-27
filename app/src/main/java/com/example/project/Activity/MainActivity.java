package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    ImageView tim;
    EditText keySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.tv_tenUser);
        keySearch = (EditText) findViewById(R.id.et_search_sanpham_main);
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
        Timkiem();
    }

    private void Timkiem() {
        tim = (ImageView) findViewById(R.id.img_timkiem_pro_main);
        tim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!keySearch.getText().toString().trim().equals("")) {
                    Intent intent = new Intent(MainActivity.this, ListFoodActivity.class);
                    intent.putExtra("keySearch", keySearch.getText().toString().trim());
                    intent.putExtra("idUser", ID);
                    intent.putExtra("Food", "DataNull");
                    intent.putExtra("DanhMuc", keySearch.getText().toString().trim());
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên món ăn cần tìm", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                for (DataSnapshot data : snapshot.getChildren()) {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) data.getValue();
                    categoryList.add(new CategoryDomain(hashMap.get("Ten").toString(), hashMap.get("HinhAnh").toString(), ID, data.getKey()));
                    adapter = new CategoryAdapter(MainActivity.this, categoryList);
                    recyclerViewCategoryList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}