package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.project.Adapter.CartAdapter;
import com.example.project.Domain.CartDomain;
import com.example.project.Domain.FoodDomain;
import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {
    RecyclerView cart;
    RecyclerView.Adapter adapterCart;
    LinearLayout profile, home, donhang, support;
    String ID, giohang, Result = "", luuDanhmuc = "";
    ImageView cancel;
    Button thanhtoan;
    TextView checkout;
    int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        matching();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        ID = extras.getString("idUser");
        giohang = "GH" + ID.split("K")[1];
        Home();
        ProFile();
        CanCel();
        Cart();
        dathang();
        Donhang();
        Support();
    }
    private void Support() {
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sup = new Intent(CartActivity.this, SupportActivity.class);
                sup.putExtra("idUser", ID);
                startActivity(sup);
            }
        });
    }

    private void Donhang() {
        donhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent order = new Intent(CartActivity.this, ListOrderActivity.class);
                order.putExtra("idUser", ID);
                startActivity(order);
            }
        });
    }

    private void dathang() {
        thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent CheckOut = new Intent(CartActivity.this, CheckoutActivity.class);
                CheckOut.putExtra("idUser", ID);
                CheckOut.putExtra("ResultCheckout", checkout.getText().toString());
                startActivity(CheckOut);
            }
        });
    }

    private void Cart() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cart.setLayoutManager(linearLayoutManager);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("GioHang");
        reference.child(giohang).addListenerForSingleValueEvent(new ValueEventListener() {
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
            reference.child(giohang).child(maMonAn[i].split(";")[0]).child(maMonAn[i].split(";")[1]).addListenerForSingleValueEvent(new ValueEventListener() {
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

                    if (luuDanhmuc.indexOf(danhmuc) == -1)
                        luuDanhmuc += danhmuc + ";";

                    Result += "DanhMuc:" + danhmuc +
                            "Ma:" + snapshot.getKey() +
                            "Ten:" + hashMap.get("Ten").toString() +
                            "Gia:" + hashMap.get("Gia").toString() +
                            "SoLuong:" + hashMap.get("SoLuong").toString() +
                            "Tong:" + hashMap.get("Tong").toString() +
                            "HinhAnh:" + hashMap.get("HinhAnh").toString() +
                            "-------------------------------------";
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
                    checkout.setText(Result + "TongCong:" + sTonggia);

                    adapterCart = new CartAdapter(CartActivity.this, cartlist);
                    cart.setAdapter(adapterCart);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void CanCel() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void ProFile() {
        Intent idPro = new Intent(CartActivity.this, ProfileActivity.class);
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
                            Intent intent = new Intent(CartActivity.this, MainActivity.class);
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
        cart = (RecyclerView) findViewById(R.id.recyclerView_cart);
        profile = (LinearLayout) findViewById(R.id.liner_cart_Propfile);
        home = (LinearLayout) findViewById(R.id.homeBtn_cart);
        cancel = (ImageView) findViewById(R.id.img_cart_cancel);
        thanhtoan = (Button) findViewById(R.id.btn_cart_datHang);
        checkout = (TextView) findViewById(R.id.tv_checkout);
        donhang = (LinearLayout) findViewById(R.id.linear_order_cart);
        support = (LinearLayout) findViewById(R.id.Linear_support_cart);

    }
}