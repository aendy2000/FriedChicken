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
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.CheckoutlistAdapter;
import com.example.project.Domain.CheckoutListDomain;
import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDetailtManagerActivity extends AppCompatActivity {
    TextView danhmuc;
    RecyclerView list;
    String Trangthai, idSearch = "";
    ImageView cancel, imgSearch;
    RecyclerView.Adapter adapter;
    String ID;
    EditText keySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detailt_manager);
        matching();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Trangthai = extras.getString("TrangThai");
            danhmuc.setText(Trangthai);
            if (Trangthai.contains("-")) {
                idSearch = Trangthai.split("-")[1];
                Trangthai = Trangthai.split("-")[0];
                danhmuc.setText(Trangthai + ": " + idSearch);
            }
        }
        show();
        timkiem();
    }

    private void timkiem() {
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!keySearch.getText().toString().trim().equals("")) {
                    Intent intent = new Intent(OrderDetailtManagerActivity.this, OrderDetailtManagerActivity.class);
                    intent.putExtra("TrangThai", Trangthai + "-" + keySearch.getText().toString().trim());
                    finish();
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(OrderDetailtManagerActivity.this, OrderDetailtManagerActivity.class);
                    intent.putExtra("TrangThai", Trangthai);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    private void show() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(linearLayoutManager);
        ArrayList<CheckoutListDomain> cartlist = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DonHang");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (idSearch.equals("")) {
                    for (DataSnapshot dh : snapshot.getChildren()) {
                        for (DataSnapshot data : snapshot.child(dh.getKey()).getChildren()) {
                            String maMon = "";
                            String trangthai = "";
                            String somon = "";
                            String tongcong = "";
                            String maDH = "";
                            ID = dh.getKey().split("DH")[1];
                            for (DataSnapshot value : snapshot.child(dh.getKey()).child(data.getKey()).getChildren()) {
                                HashMap<String, Object> hashMap = (HashMap<String, Object>) value.getValue();
                                if (value.getKey().indexOf("SP") != -1) {
                                    maDH = data.getKey();
                                    maMon = value.getKey();
                                } else if (value.getKey().equals("TrangThai")) {
                                    trangthai = hashMap.get("Trangthai").toString();
                                } else if (value.getKey().equals("SoMon")) {
                                    somon = hashMap.get("Somon").toString();
                                } else if (value.getKey().equals("TongCong")) {
                                    tongcong = hashMap.get("Tongcong").toString();
                                }
                            }
                            if (trangthai.equals(Trangthai)) {
                                cartlist.add(new CheckoutListDomain(maMon, "Admin-TK" + ID, somon, maDH, trangthai, tongcong));
                                adapter = new CheckoutlistAdapter(OrderDetailtManagerActivity.this, cartlist);
                                list.setAdapter(adapter);
                            }
                        }
                    }
                } else {
                    for (DataSnapshot dh : snapshot.getChildren()) {
                        for (DataSnapshot data : snapshot.child(dh.getKey()).getChildren()) {
                            String maMon = "";
                            String trangthai = "";
                            String somon = "";
                            String tongcong = "";
                            String maDH = "";
                            ID = dh.getKey().split("DH")[1];
                            if (dh.getKey().toUpperCase().contains(idSearch.toUpperCase())) {
                                for (DataSnapshot value : snapshot.child(dh.getKey()).child(data.getKey()).getChildren()) {
                                    HashMap<String, Object> hashMap = (HashMap<String, Object>) value.getValue();
                                    if (value.getKey().indexOf("SP") != -1) {
                                        maDH = data.getKey();
                                        maMon = value.getKey();
                                    } else if (value.getKey().equals("TrangThai")) {
                                        trangthai = hashMap.get("Trangthai").toString();
                                    } else if (value.getKey().equals("SoMon")) {
                                        somon = hashMap.get("Somon").toString();
                                    } else if (value.getKey().equals("TongCong")) {
                                        tongcong = hashMap.get("Tongcong").toString();
                                    }
                                }
                                if (trangthai.equals(Trangthai)) {
                                    cartlist.add(new CheckoutListDomain(maMon, "Admin-TK" + ID, somon, maDH, trangthai, tongcong));
                                    adapter = new CheckoutlistAdapter(OrderDetailtManagerActivity.this, cartlist);
                                    list.setAdapter(adapter);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void matching() {
        danhmuc = (TextView) findViewById(R.id.tv_danhmuc_orderdetailt_category_manager);
        list = (RecyclerView) findViewById(R.id.rcv_orderdetailt_manager);
        keySearch = (EditText) findViewById(R.id.et_search_donhang_admin);
        imgSearch = (ImageView) findViewById(R.id.img_timkiem_donhang_admin);
        cancel = (ImageView) findViewById(R.id.img_cancel_order_detailt_category);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}