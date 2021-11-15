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
                for (DataSnapshot record: snapshot.getChildren()){
                    String[] somon = record.getValue().toString().split("[}},]");
                    int dem = 0;
                    for (int i = 0; i < somon.length; i++) {
                        if(somon[i].indexOf("MG") != -1) {
                            String[] tachAV = somon[i].split("MG");
                            dem += tachAV.length - 1;
                        } else if(somon[i].indexOf("CB") != -1) {
                            String[] tachAV = somon[i].split("CB");
                            dem += tachAV.length - 1;
                        } else if(somon[i].indexOf("AV") != -1) {
                            String[] tachAV = somon[i].split("AV");
                            dem += tachAV.length - 1;
                        }
                    }
                    cartlist.add(new CartManagerDomain(record.getKey(), String.valueOf(dem)));
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