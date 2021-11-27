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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    ImageView cancel, search;
    LinearLayout home, profile, support;
    FloatingActionButton btncart;
    String ID, IdSearch = "";
    EditText keysearch;
    TextView tieude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);
        matching();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ID = extras.getString("idUser");
            if (ID.contains("-")) {
                IdSearch = ID.split("-")[1];
                ID = ID.split("-")[0];
                tieude.setText("Đơn ngày " + IdSearch.substring(0, 2)
                        + "/" + IdSearch.substring(2, 4)
                        + "/20" + IdSearch.substring(4, 6));
            }
        }
        show();
        Home();
        ProFile();
        cart();
        quaylai();
        Support();
        Timkiem();
    }

    private void Timkiem() {
        search = (ImageView) findViewById(R.id.img_timkiem_donhang_user);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = keysearch.getText().toString().trim();
                if (!time.equals("")) {
                    if (time.contains("-")) {
                        String[] tach = time.split("-");
                        if (tach.length != 3) {
                            Toast.makeText(ListOrderActivity.this, "Nội dung tìm kiếm phải là Ngày-Tháng-Năm\nVD: 25-03-2021", Toast.LENGTH_SHORT).show();
                        } else {
                            if (tach[2].length() != 4 || tach[1].length() > 2 || tach[0].length() > 2) {
                                Toast.makeText(ListOrderActivity.this, "Nội dung tìm kiếm phải là Ngày-Tháng-Năm\nVD: 25-03-2021", Toast.LENGTH_SHORT).show();
                            } else {
                                String iddonhang = "";
                                if (tach[0].length() < 2)
                                    iddonhang += "0" + tach[0];
                                else
                                    iddonhang += tach[0];

                                if (tach[1].length() < 2)
                                    iddonhang += "0" + tach[1];
                                else
                                    iddonhang += tach[1];
                                iddonhang += tach[2].substring(2);
                                Intent intent = new Intent(ListOrderActivity.this, ListOrderActivity.class);
                                intent.putExtra("idUser", ID + "-" + iddonhang);
                                finish();
                                startActivity(intent);
                            }
                        }
                    } else {
                        Toast.makeText(ListOrderActivity.this, "Nội dung tìm kiếm phải là Ngày-Tháng-Năm\nVD: 25-03-2021", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(ListOrderActivity.this, ListOrderActivity.class);
                    intent.putExtra("idUser", ID);
                    finish();
                    startActivity(intent);
                }
            }
        });
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DonHang");
        if (IdSearch.equals("")) {
            reference.child("DH" + ID.split("K")[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot != null) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String maMon = "";
                            String trangthai = "";
                            String somon = "";
                            String tongcong = "";
                            String maDH = "";
                            for (DataSnapshot value : snapshot.child(data.getKey()).getChildren()) {
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
                            cartlist.add(new CheckoutListDomain(maMon, ID, somon, maDH, trangthai, tongcong));
                            adapterListOrder = new CheckoutlistAdapter(ListOrderActivity.this, cartlist);
                            listOrder.setAdapter(adapterListOrder);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            reference.child("DH" + ID.split("K")[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot != null) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String maMon = "";
                            String trangthai = "";
                            String somon = "";
                            String tongcong = "";
                            String maDH = "";
                            if (data.getKey().toUpperCase().contains(IdSearch.toUpperCase())) {
                                for (DataSnapshot value : snapshot.child(data.getKey()).getChildren()) {
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
                                cartlist.add(new CheckoutListDomain(maMon, ID, somon, maDH, trangthai, tongcong));
                                adapterListOrder = new CheckoutlistAdapter(ListOrderActivity.this, cartlist);
                                listOrder.setAdapter(adapterListOrder);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

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
        search = (ImageView) findViewById(R.id.img_timkiem_donhang_user);
        keysearch = (EditText) findViewById(R.id.et_search_donhang_user);
        tieude = (TextView) findViewById(R.id.textView27);
    }
}