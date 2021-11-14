package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.project.Adapter.CheckOutAdapter;
import com.example.project.Domain.CheckoutDomain;
import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDetailt extends AppCompatActivity {
    RecyclerView recyclerCheckout;
    RecyclerView.Adapter adapterRecyclerCheckout;
    String ID, ndDonHang, maDonHang;
    TextView result, hoten, sdt, diachi;
    String luuDanhmuc = "";
    ImageView cancel;
    int sum;
    LinearLayout home, profile, order, support;
    FloatingActionButton btncart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detailt);
        matching();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        ID = extras.getString("idUser");
        ndDonHang = extras.getString("ResultMaMonOrder");
        maDonHang = extras.getString("ngayMuaHang");
        getValue();
        cancel();
        ProFile();
        Home();
        cart();
        Support();
    }
    private void Support() {
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sup = new Intent(OrderDetailt.this, SupportActivity.class);
                sup.putExtra("idUser", ID);
                startActivity(sup);
            }
        });
    }
    private void cart() {
        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Cart = new Intent(OrderDetailt.this, CartActivity.class);
                Cart.putExtra("idUser", ID);
                startActivity(Cart);
            }
        });
    }

    private void ProFile() {
        Intent idPro = new Intent(OrderDetailt.this, ProfileActivity.class);
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
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("TaiKhoan");
                reference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                            String name = hashMap.get("Ten").toString();
                            finishAffinity();
                            Intent intent = new Intent(OrderDetailt.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("idUser", ID);
                            bundle.putSerializable("nameUser", name);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void cancel() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getValue() {
        String[] maMonAn = ndDonHang.split("=");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerCheckout.setLayoutManager(linearLayoutManager);
        ArrayList<CheckoutDomain> checkOutlist = new ArrayList<>();
        FirebaseDatabase databasegetfood = FirebaseDatabase.getInstance();
        DatabaseReference referencegetfood = databasegetfood.getReference("DonHang");
        referencegetfood.child("DH" + ID.split("K")[1]).child(maDonHang).child("ThongTin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                hoten.setText(hashMap.get("Ten").toString());
                sdt.setText(hashMap.get("SDT").toString());
                diachi.setText(hashMap.get("DiaChi").toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        for (int i = 0; i < maMonAn.length; i++) {
            referencegetfood.child("DH" + ID.split("K")[1]).child(maDonHang).child(maMonAn[i].split(";")[0]).child(maMonAn[i].split(";")[1]).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    if (luuDanhmuc.indexOf(danhmuc) == -1)
                        luuDanhmuc += danhmuc + ";";

                    checkOutlist.add(new CheckoutDomain(snapshot.getKey(),
                            hashMap.get("Ten").toString(),
                            hashMap.get("Gia").toString(),
                            hashMap.get("SoLuong").toString(),
                            hashMap.get("Tong").toString(),
                            hashMap.get("HinhAnh").toString(), ID));

                    sum += Integer.valueOf(hashMap.get("Tong").toString().split("\\.")[0]);
                    String sTonggia = "";
                    if (sum < 1000) {
                        sTonggia = sum + ".000 VND";
                    } else if (sum < 10000) {
                        String[] formatGia = String.valueOf(sum).split("");
                        String chuoi = formatGia[1] + ".";
                        for (int i = 2; i < formatGia.length; i++)
                            chuoi += formatGia[i];
                        sTonggia = chuoi + ".000 VND";
                    } else if (sum < 100000) {
                        String[] formatGia = String.valueOf(sum).split("");
                        String chuoi = formatGia[1] + formatGia[2] + ".";
                        for (int i = 3; i < formatGia.length; i++)
                            chuoi += formatGia[i];
                        sTonggia = chuoi + ".000 VND";
                    } else if (sum < 1000000) {
                        String[] formatGia = String.valueOf(sum).split("");
                        String chuoi = formatGia[1] + formatGia[2] + formatGia[3] + ".";
                        for (int i = 4; i < formatGia.length; i++)
                            chuoi += formatGia[i];
                        sTonggia = chuoi + ".000 VND";
                    }
                    result.setText(sTonggia);
                    adapterRecyclerCheckout = new CheckOutAdapter(OrderDetailt.this, checkOutlist);
                    recyclerCheckout.setAdapter(adapterRecyclerCheckout);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Loi", error.toString());
                }
            });
        }
    }

    private void matching() {
        cancel = (ImageView) findViewById(R.id.img_orderdetailt_cancel);
        recyclerCheckout = (RecyclerView) findViewById(R.id.rcv_orderdetailt);
        result = (TextView) findViewById(R.id.tv_TongCong_orderdetailt);
        profile = (LinearLayout) findViewById(R.id.liner_orderdetailt_Propfile);
        home = (LinearLayout) findViewById(R.id.homeBtn_orderdetailt);
        btncart = (FloatingActionButton) findViewById(R.id.card_orderdetailt_btn);
        order = (LinearLayout) findViewById(R.id.linear_order_orderdetailt);
        hoten = (TextView) findViewById(R.id.food_SumPrice_checkout4);
        sdt = (TextView) findViewById(R.id.food_SumPrice_checkout5);
        diachi = (TextView) findViewById(R.id.food_SumPrice_checkout6);
        support = (LinearLayout) findViewById(R.id.Linear_support_orderdetailt);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}