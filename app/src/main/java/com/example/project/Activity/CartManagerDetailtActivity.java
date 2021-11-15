package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        if (extras == null) {
            return;
        }
        ID = extras.getString("idGioHang");
        load();
    }

    private void load() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCartDetailt.setLayoutManager(linearLayoutManager);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("GioHang");
        reference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {

                } else {
                    String key = snapshot.getValue().toString();
                    String[] tach = key.split("[}}],");
                    String[] maMonAn = new String[tach.length];
                    for (int i = 0; i < tach.length; i++) {
                        if (tach[i].indexOf("MG") != -1) {
                            maMonAn[i] = "MonGa;" + tach[i].substring(tach[i].indexOf("MG"), tach[i].indexOf("MG") + 12);
                        } else if (tach[i].indexOf("CB") != -1) {
                            maMonAn[i] = "ComBo;" + tach[i].substring(tach[i].indexOf("CB"), tach[i].indexOf("CB") + 12);
                        } else if (tach[i].indexOf("AV") != -1) {
                            maMonAn[i] = "AnVat;" + tach[i].substring(tach[i].indexOf("AV"), tach[i].indexOf("AV") + 12);
                        }
                    }
                    addCart(maMonAn);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addCart(String[] maMonAn) {
        ArrayList<CartDomain> cartlist = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("GioHang");
        for (int i = 0; i < maMonAn.length; i++) {
            reference.child(ID).child(maMonAn[i].split(";")[0]).child(maMonAn[i].split(";")[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                    String danhmuc = "";
                    if (snapshot.getKey().indexOf("MG") != -1)
                        danhmuc = "MonGa";
                    else if (snapshot.getKey().indexOf("CB") != -1)
                        danhmuc = "ComBo";
                    else if (snapshot.getKey().indexOf("AV") != -1)
                        danhmuc = "AnVat";
                    cartlist.add(new CartDomain(danhmuc, snapshot.getKey(),
                            hashMap.get("Ten").toString(),
                            hashMap.get("Gia").toString(),
                            hashMap.get("Tong").toString(),
                            hashMap.get("SoLuong").toString(),
                            hashMap.get("HinhAnh").toString(), ID));

                    recyclerViewCartDetailtAdapter = new CartAdapter(CartManagerDetailtActivity.this, cartlist);
                    recyclerViewCartDetailt.setAdapter(recyclerViewCartDetailtAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
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