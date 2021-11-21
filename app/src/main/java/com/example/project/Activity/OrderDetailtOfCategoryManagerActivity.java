package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.project.Adapter.CheckOutAdapter;
import com.example.project.Adapter.CheckoutlistAdapter;
import com.example.project.Domain.CheckoutDomain;
import com.example.project.Domain.CheckoutListDomain;
import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderDetailtOfCategoryManagerActivity extends AppCompatActivity {
    RecyclerView recyclerCheckout;
    RecyclerView.Adapter adapterRecyclerCheckout;
    String ID, ndDonHang, maDonHang;
    TextView result, hoten, sdt, diachi;
    ImageView cancel;
    NumberFormat format = new DecimalFormat("#,###");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detailt_of_category_manager);
        matching();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        ID = extras.getString("idUser");
        maDonHang = extras.getString("ngayMuaHang");
        getValue();
        Cancel();
    }
    private void Cancel() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getValue() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerCheckout.setLayoutManager(linearLayoutManager);
        ArrayList<CheckoutDomain> checkOutlist = new ArrayList<>();
        DatabaseReference referencegetfood = FirebaseDatabase.getInstance().getReference("DonHang");
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
        referencegetfood.child("DH" + ID.split("K")[1]).child(maDonHang).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int tonggia = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    String maMon = "";
                    String ten = "";
                    String gia = "";
                    String sl = "";
                    String tong = "";
                    String hinhanh = "";
                    if (data.getKey().indexOf("SP-") != -1) {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) data.getValue();
                        maMon = data.getKey();
                        ten = hashMap.get("Ten").toString();
                        gia = hashMap.get("Gia").toString();
                        sl = hashMap.get("SoLuong").toString();
                        tong = hashMap.get("Tong").toString();
                        hinhanh = hashMap.get("HinhAnh").toString();
                        tonggia += Integer.valueOf(hashMap.get("Tong").toString());
                        checkOutlist.add(new CheckoutDomain(maMon, ten, gia, sl, tong, hinhanh, ID));
                    }
                }
                result.setText(format.format(tonggia));
                adapterRecyclerCheckout = new CheckOutAdapter(OrderDetailtOfCategoryManagerActivity.this, checkOutlist);
                recyclerCheckout.setAdapter(adapterRecyclerCheckout);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void matching() {
        cancel = (ImageView) findViewById(R.id.img_orderdetailt_managerOfcategory_cancel);
        recyclerCheckout = (RecyclerView) findViewById(R.id.rcv_orderdetailt_managerOfcategory);
        result = (TextView) findViewById(R.id.tv_TongCong_orderdetailt_managerOfcategory);
        hoten = (TextView) findViewById(R.id.food_SumPrice_checkout4_managerOfcategory);
        sdt = (TextView) findViewById(R.id.food_SumPrice_checkout5_managerOfcategory);
        diachi = (TextView) findViewById(R.id.food_SumPrice_checkout6_managerOfcategory);
    }
}