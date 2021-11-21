package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    String Trangthai;
    ImageView cancel;
    RecyclerView.Adapter adapter;
    String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detailt_manager);
        matching();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Trangthai = extras.getString("TrangThai");
            danhmuc.setText(Trangthai);
        }
        show();
    }

    private void show() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(linearLayoutManager);
        ArrayList<CheckoutListDomain> cartlist = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DonHang");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        reference.child("DH" + ID.split("K")[1]).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getValue() == null) {
//
//                } else {
//                    String key = snapshot.getValue().toString();
//                    String maDH;
//                    String soMon;
//                    String trangThai;
//                    String tongCong;
//                    String danhmuc;
//                    String maMon;
//
//                    String[] tach = key.split("[|]");
//                    for (int i = 1; i < tach.length; i++) {
//                        maDH = "";
//                        soMon = "";
//                        trangThai = "";
//                        tongCong = "";
//                        maMon = "";
//
//                        maDH = tach[i].split("SoMon")[0].substring(0, tach[i].indexOf("SoMon") - 2);
//                        soMon = tach[i].split("SoMon")[1].substring(1, tach[i].split("SoMon")[1].indexOf("món") + 3);
//                        if (tach[i].indexOf("MonGa") != -1) {
//                            String[] mamonMG = tach[i].split("MG");
//                            for (int j = 1; j < mamonMG.length; j++) {
//                                maMon+= "MonGa;MG" + mamonMG[j].substring(0, 10) + ";";
//                            }
//                        }
//                        if (tach[i].indexOf("ComBo") != -1) {
//                            String[] mamonCB = tach[i].split("CB");
//                            for (int k = 1; k < mamonCB.length; k++) {
//                                maMon+= "ComBo;CB" + mamonCB[k].substring(0, 10) + ";";
//                            }
//                        }
//                        if (tach[i].indexOf("AnVat") != -1) {
//                            String[] mamonCB = tach[i].split("AV");
//                            for (int l = 1; l < mamonCB.length; l++) {
//                                maMon+= "AnVat;AV" + mamonCB[l].substring(0, 10) + ";";
//                            }
//                        }
//                        if (tach[i].indexOf("Chờ duyệt") != -1)
//                            trangThai = "Chờ duyệt";
//                        else if (tach[i].indexOf("Đã duyệt") != -1)
//                            trangThai = "Đã duyệt";
//                        else if (tach[i].indexOf("Đang giao") != -1)
//                            trangThai = "Đang giao";
//                        else if (tach[i].indexOf("Đã giao") != -1)
//                            trangThai = "Đã giao";
//                        else if (tach[i].indexOf("Không thể giao") != -1)
//                            trangThai = "Không thể giao";
//                        else if (tach[i].indexOf("Đã hủy") != -1)
//                            trangThai = "Đã hủy";
//                        tongCong = tach[i].split("TongCong")[1].substring(1, tach[i].split("TongCong")[1].indexOf("VND") + 3);
//                        cartlist.add(new CheckoutListDomain(maMon, ID, soMon, maDH, trangThai, tongCong));

//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void matching() {
        danhmuc = (TextView) findViewById(R.id.tv_danhmuc_orderdetailt_category_manager);
        list = (RecyclerView) findViewById(R.id.rcv_orderdetailt_manager);
        cancel = (ImageView) findViewById(R.id.img_cancel_order_detailt_category);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}