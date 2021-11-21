package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.project.Adapter.CartAdapter;
import com.example.project.Adapter.CartManagerAdapter;
import com.example.project.Domain.CartDomain;
import com.example.project.Domain.CartManagerDomain;
import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CartManagerActivity extends AppCompatActivity {
    String ID;
    RecyclerView.Adapter cartmanagerAdapter;
    RecyclerView cartmanager;
    ImageView cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_manager);
        matching();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        ID = extras.getString("idData");

        load();
    }

    private void matching() {
        cartmanager = (RecyclerView) findViewById(R.id.rcv_cart_manager);
        cancel = (ImageView) findViewById(R.id.img_cart_manager_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void load() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cartmanager.setLayoutManager(linearLayoutManager);
        ArrayList<CartManagerDomain> cartlist = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("GioHang");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot record: snapshot.getChildren()){
                    for (DataSnapshot value : snapshot.child(record.getKey()).getChildren()) {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) value.getValue();
                        i++;
                    }
                    cartlist.add(new CartManagerDomain(record.getKey(), String.valueOf(i)));
                }
                cartmanagerAdapter = new CartManagerAdapter(CartManagerActivity.this, cartlist);
                cartmanager.setAdapter(cartmanagerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}