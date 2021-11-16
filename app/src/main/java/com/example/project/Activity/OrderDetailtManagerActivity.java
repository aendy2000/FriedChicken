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

public class OrderDetailtManagerActivity extends AppCompatActivity {
    TextView danhmuc;
    RecyclerView list;
    String trangthai;
    ImageView cancel;
    RecyclerView.Adapter adapter;
    String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detailt_manager);
        matching();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        trangthai = extras.getString("TrangThai");
        danhmuc.setText(trangthai);
        load();
    }

    private void load() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(linearLayoutManager);
        ArrayList<CheckoutListDomain> orderlist = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("DonHang");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String[] taikhoan = snapshot.getValue().toString().split("DH");
                String TaiKhoanGH = "";
                for (int i = 1; i <= taikhoan.length - 1; i++) {
                    TaiKhoanGH = "AdminTK" + taikhoan[i].split("[={|]")[0];
                    String[] donhang = taikhoan[i].split("[|]");
                    String maDH;
                    String soMon;
                    String trangThai;
                    String tongCong;
                    String danhmuc;
                    String maMon;

                    for (int j = 1; j < donhang.length; j++) {
                        maDH = "";
                        soMon = "";
                        trangThai = "";
                        tongCong = "";
                        maMon = "";

                        maDH = donhang[j].split("SoMon")[0].substring(0, donhang[j].indexOf("SoMon") - 2);
                        soMon = donhang[j].split("SoMon")[1].substring(1, donhang[j].split("SoMon")[1].indexOf("món") + 3);
                        if (donhang[j].indexOf("MonGa") != -1) {
                            String[] mamonMG = donhang[j].split("MG");
                            for (int q = 1; q < mamonMG.length; q++) {
                                maMon += "MonGa;MG" + mamonMG[q].substring(0, 10) + ";";
                            }
                        }
                        if (donhang[j].indexOf("ComBo") != -1) {
                            String[] mamonCB = donhang[j].split("CB");
                            for (int k = 1; k < mamonCB.length; k++) {
                                maMon += "ComBo;CB" + mamonCB[k].substring(0, 10) + ";";
                            }
                        }
                        if (donhang[j].indexOf("AnVat") != -1) {
                            String[] mamonCB = donhang[j].split("AV");
                            for (int l = 1; l < mamonCB.length; l++) {
                                maMon += "AnVat;AV" + mamonCB[l].substring(0, 10) + ";";
                            }
                        }
                        if (donhang[j].indexOf("Chờ duyệt") != -1)
                            trangThai = "Chờ duyệt";
                        else if (donhang[j].indexOf("Đã duyệt") != -1)
                            trangThai = "Đã duyệt";
                        else if (donhang[j].indexOf("Đang giao") != -1)
                            trangThai = "Đang giao";
                        else if (donhang[j].indexOf("Đã giao") != -1)
                            trangThai = "Đã giao";
                        else if (donhang[j].indexOf("Không thể giao") != -1)
                            trangThai = "Không thể giao";
                        else if (donhang[j].indexOf("Đã hủy") != -1)
                            trangThai = "Đã hủy";
                        tongCong = donhang[j].split("TongCong")[1].substring(1, donhang[j].split("TongCong")[1].indexOf("VND") + 3);
                        String m = "";
                        if (trangThai.equals(trangthai)) {
                            orderlist.add(new CheckoutListDomain(maMon, TaiKhoanGH, soMon, maDH, trangThai, tongCong));
                            adapter = new CheckoutlistAdapter(OrderDetailtManagerActivity.this, orderlist);
                            list.setAdapter(adapter);
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
        cancel = (ImageView) findViewById(R.id.img_cancel_order_detailt_category);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}