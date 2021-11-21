package com.example.project.Activity;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class CartActivity extends AppCompatActivity {
    RecyclerView cart;
    RecyclerView.Adapter adapterCart;
    LinearLayout profile, home, donhang, support;
    String ID, giohang;
    ImageView cancel;
    Button thanhtoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        matching();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ID = extras.getString("idUser");
            giohang = "GH" + ID.split("K")[1];
        }

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
                startActivity(CheckOut);
            }
        });
    }

    private void Cart() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cart.setLayoutManager(linearLayoutManager);
        ArrayList<CartDomain> cartlist = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("GioHang");
        reference.child(giohang).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) data.getValue();
                    cartlist.add(new CartDomain(data.getKey(),
                            hashMap.get("Ten").toString(),
                            hashMap.get("Gia").toString(),
                            hashMap.get("Tong").toString(),
                            hashMap.get("SoLuong").toString(),
                            hashMap.get("HinhAnh").toString(), ID));
                }
                adapterCart = new CartAdapter(CartActivity.this, cartlist);
                cart.setAdapter(adapterCart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        donhang = (LinearLayout) findViewById(R.id.linear_order_cart);
        support = (LinearLayout) findViewById(R.id.Linear_support_cart);

    }
}