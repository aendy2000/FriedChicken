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
import com.example.project.Adapter.CheckoutlistAdapter;
import com.example.project.Domain.CheckoutListDomain;
import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ListOrderActivity extends AppCompatActivity {

    RecyclerView listOrder;
    RecyclerView.Adapter adapterListOrder;
    ImageView cancel;
    LinearLayout home, profile, support;
    FloatingActionButton btncart;
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);
        matching();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        ID = extras.getString("idUser");
        show();
        Home();
        ProFile();
        cart();
        quaylai();
        Support();
    }
    private void Support() {
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sup = new Intent(ListOrderActivity.this, SupportActivity.class);
                sup.putExtra("idUser", ID);
                startActivity(sup);
            }
        });
    }
    private void show() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listOrder.setLayoutManager(linearLayoutManager);

        ArrayList<CheckoutListDomain> cartlist = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("DonHang");
        reference.child("DH" + ID.split("K")[1]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {

                } else {
                    String key = snapshot.getValue().toString();
                    String maDH;
                    String soMon;
                    String trangThai;
                    String tongCong;
                    String danhmuc;
                    String maMon;

                    String[] tach = key.split("[|]");
                    for (int i = 1; i < tach.length; i++) {
                        maDH = "";
                        soMon = "";
                        trangThai = "";
                        tongCong = "";
                        maMon = "";

                        maDH = tach[i].split("SoMon")[0].substring(0, tach[i].indexOf("SoMon") - 2);
                        soMon = tach[i].split("SoMon")[1].substring(1, tach[i].split("SoMon")[1].indexOf("món") + 3);
                        if (tach[i].indexOf("MonGa") != -1) {
                            String[] mamonMG = tach[i].split("MG");
                            for (int j = 1; j < mamonMG.length; j++) {
                                maMon+= "MonGa;MG" + mamonMG[j].substring(0, 10) + ";";
                            }
                        }
                        if (tach[i].indexOf("ComBo") != -1) {
                            String[] mamonCB = tach[i].split("CB");
                            for (int k = 1; k < mamonCB.length; k++) {
                                maMon+= "ComBo;CB" + mamonCB[k].substring(0, 10) + ";";
                            }
                        }
                        if (tach[i].indexOf("AnVat") != -1) {
                            String[] mamonCB = tach[i].split("AV");
                            for (int l = 1; l < mamonCB.length; l++) {
                                maMon+= "AnVat;AV" + mamonCB[l].substring(0, 10) + ";";
                            }
                        }
                        if (tach[i].indexOf("Chờ duyệt") != -1)
                            trangThai = "Chờ duyệt";
                        else if (tach[i].indexOf("Đã duyệt") != -1)
                            trangThai = "Đã duyệt";
                        else if (tach[i].indexOf("Đang giao") != -1)
                            trangThai = "Đang giao";
                        else if (tach[i].indexOf("Đã giao") != -1)
                            trangThai = "Đã giao";
                        else if (tach[i].indexOf("Không thể giao") != -1)
                            trangThai = "Không thể giao";
                        else if (tach[i].indexOf("Đã hủy") != -1)
                            trangThai = "Đã hủy";
                        tongCong = tach[i].split("TongCong")[1].substring(1, tach[i].split("TongCong")[1].indexOf("VND") + 3);
                        cartlist.add(new CheckoutListDomain(maMon, ID, soMon, maDH, trangThai, tongCong));
                        adapterListOrder = new CheckoutlistAdapter(ListOrderActivity.this, cartlist);
                        listOrder.setAdapter(adapterListOrder);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    private void cart() {
        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Cart = new Intent(ListOrderActivity.this, CartActivity.class);
                Cart.putExtra("idUser", ID);
                startActivity(Cart);
            }
        });
    }

    private void ProFile() {
        Intent idPro = new Intent(ListOrderActivity.this, ProfileActivity.class);
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
                            Intent intent = new Intent(ListOrderActivity.this, MainActivity.class);
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

    private void matching() {
        profile = (LinearLayout) findViewById(R.id.liner_listorder_Propfile);
        home = (LinearLayout) findViewById(R.id.homeBtn_listorder);
        btncart = (FloatingActionButton) findViewById(R.id.card_listorder_btn);
        cancel = (ImageView) findViewById(R.id.img_listorder_cancel);
        listOrder = (RecyclerView) findViewById(R.id.rcv_listOrder);
        support = (LinearLayout) findViewById(R.id.Linear_support_listorder);

    }
}