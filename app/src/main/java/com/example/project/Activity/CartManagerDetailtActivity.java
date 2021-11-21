package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.project.Adapter.CartAdapter;
import com.example.project.Domain.CartDomain;
import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CartManagerDetailtActivity extends AppCompatActivity {
    String ID;
    ImageView cancel;
    RecyclerView recyclerViewCartDetailt;
    RecyclerView.Adapter recyclerViewCartDetailtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_manager_detailt);
        matching();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ID = extras.getString("idGioHang");
        }
        load();
    }

    private void load() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCartDetailt.setLayoutManager(linearLayoutManager);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ArrayList<CartDomain> cartlist = new ArrayList<>();
        DatabaseReference reference = database.getReference("GioHang");
        reference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {

                } else {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) data.getValue();
                        cartlist.add(new CartDomain(data.getKey(),
                                hashMap.get("Ten").toString(),
                                hashMap.get("Gia").toString(),
                                hashMap.get("Tong").toString(),
                                hashMap.get("SoLuong").toString(),
                                hashMap.get("HinhAnh").toString(),"Admin-TK" + ID.split("GH")[1]));

                    }
                    recyclerViewCartDetailtAdapter = new CartAdapter(CartManagerDetailtActivity.this, cartlist);
                    recyclerViewCartDetailt.setAdapter(recyclerViewCartDetailtAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void matching() {
        recyclerViewCartDetailt = (RecyclerView) findViewById(R.id.rcv_cart_manager_detailt);
        cancel = (ImageView) findViewById(R.id.img_cart_manager_detailt_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}