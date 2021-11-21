package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.project.Adapter.FoodAdapter;
import com.example.project.Domain.FoodDomain;
import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ListFoodActivity extends AppCompatActivity {
    TextView danhmuc;
    RecyclerView.Adapter adapterFood;
    RecyclerView recyclerViewfood;
    LinearLayout home, profile, donhang, support;
    String sDanhMuc, tendm, ID;
    ImageView cancel;
    FloatingActionButton btncart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);
        matching();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sDanhMuc = bundle.getString("Food");
            tendm = bundle.getString("DanhMuc");
            ID = bundle.getString("idUser");
            danhmuc.setText(sDanhMuc);
        }
        Home();
        recyclerViewCategory();
        Profile();
        quaylai();
        cart();
        Donhang();
        Support();
    }

    private void Support() {
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sup = new Intent(ListFoodActivity.this, SupportActivity.class);
                sup.putExtra("idUser", ID);
                startActivity(sup);
            }
        });
    }

    private void Donhang() {
        donhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent order = new Intent(ListFoodActivity.this, ListOrderActivity.class);
                order.putExtra("idUser", ID);
                startActivity(order);
            }
        });
    }

    private void cart() {
        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Cart = new Intent(ListFoodActivity.this, CartActivity.class);
                Cart.putExtra("idUser", ID);
                startActivity(Cart);
            }
        });
    }

    private void quaylai() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Profile() {
        Intent idPro = new Intent(ListFoodActivity.this, ProfileActivity.class);
        idPro.putExtra("idProf", ID);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(idPro);
            }
        });
    }

    private void Home() {
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void recyclerViewCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewfood = findViewById(R.id.recyclerView_food);
        recyclerViewfood.setLayoutManager(linearLayoutManager);

        ArrayList<FoodDomain> foodList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("SanPham");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot record : snapshot.getChildren()) {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) record.getValue();
                    if (hashMap.get("DanhMuc").toString().equals(sDanhMuc))
                        foodList.add(new FoodDomain(record.getKey(),
                                hashMap.get("Ten").toString(),
                                hashMap.get("MoTa").toString(),
                                hashMap.get("Gia").toString(),
                                hashMap.get("HinhAnh").toString(), ID));
                }
                adapterFood = new FoodAdapter(ListFoodActivity.this, foodList);
                recyclerViewfood.setAdapter(adapterFood);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void matching() {
        danhmuc = (TextView) findViewById(R.id.textView6);
        home = (LinearLayout) findViewById(R.id.homeBtn2);
        profile = (LinearLayout) findViewById(R.id.liner_listfood_Propfile);
        cancel = (ImageView) findViewById(R.id.img_listfood_cancel);
        btncart = (FloatingActionButton) findViewById(R.id.card_listfood_btn);
        donhang = (LinearLayout) findViewById(R.id.linear_order_listfood);
        support = (LinearLayout) findViewById(R.id.Linear_support_listfood);
    }
}