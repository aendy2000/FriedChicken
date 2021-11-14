package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FoodDetailtActivity extends AppCompatActivity {

    TextView ten, mota, gia, tong, soluong, hinhanh;
    Button addtocart;
    ImageView cancel, imgFood, cong, tru;
    String sMonAn, ID, danhmuc;
    LinearLayout home, profile, donhang, support;
    FloatingActionButton btncart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detailt);
        matching();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        sMonAn = bundle.getString("Food");
        danhmuc = bundle.getString("Food").split("")[1] + bundle.getString("Food").split("")[2];
        ID = bundle.getString("idUser");
        if (danhmuc.equals("CB"))
            danhmuc = "ComBo";
        else if (danhmuc.equals("MG"))
            danhmuc = "MonGa";
        else if (danhmuc.equals("AV"))
            danhmuc = "AnVat";
        load();
        tangsl();
        giamsl();
        quaylai();
        ProFile();
        Home();
        cart();
        Donhang();
        Support();

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference referenceTaiKhoan = database.getReference("TaiKhoan");
                referenceTaiKhoan.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                        addGioHang(hashMap.get("GioHang").toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void Support() {
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sup = new Intent(FoodDetailtActivity.this, SupportActivity.class);
                sup.putExtra("idUser", ID);
                startActivity(sup);
            }
        });
    }

    private void Donhang() {
        donhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent order = new Intent(FoodDetailtActivity.this, ListOrderActivity.class);
                order.putExtra("idUser", ID);
                startActivity(order);
            }
        });
    }

    private void cart() {
        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Cart = new Intent(FoodDetailtActivity.this, CartActivity.class);
                Cart.putExtra("idUser", ID);
                startActivity(Cart);
            }
        });
    }

    private void ProFile() {
        Intent idPro = new Intent(FoodDetailtActivity.this, ProfileActivity.class);
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
                            Intent intent = new Intent(FoodDetailtActivity.this, MainActivity.class);
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

    private void addGioHang(String Magiohang) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("GioHang");
        reference.child(Magiohang).child(danhmuc).child(sMonAn).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    HashMap<String, Object> hashMapGioHang = (HashMap<String, Object>) snapshot.getValue();
                    if (hashMapGioHang == null) {
                        reference.child(Magiohang).child(danhmuc).child(sMonAn).child("Gia").setValue(gia.getText().toString());
                        reference.child(Magiohang).child(danhmuc).child(sMonAn).child("Ten").setValue(ten.getText().toString());
                        reference.child(Magiohang).child(danhmuc).child(sMonAn).child("SoLuong").setValue(soluong.getText().toString());
                        reference.child(Magiohang).child(danhmuc).child(sMonAn).child("Tong").setValue(tong.getText().toString());
                        reference.child(Magiohang).child(danhmuc).child(sMonAn).child("HinhAnh").setValue(hinhanh.getText().toString());
                    } else {

                        int tongsl = Integer.valueOf(soluong.getText().toString()) + Integer.valueOf(hashMapGioHang.get("SoLuong").toString());
                        int tonggia = Integer.valueOf(gia.getText().toString().trim().split("\\.")[0]) * tongsl;
                        String sTonggia = "";
                        if (tonggia < 1000) {
                            sTonggia = tonggia + ".000 VND";
                        } else if (tonggia < 10000) {
                            String[] formatGia = String.valueOf(tonggia).split("");
                            String chuoi = formatGia[1] + ".";
                            for (int i = 2; i < formatGia.length; i++)
                                chuoi += formatGia[i];
                            sTonggia = chuoi + ".000 VND";
                        } else if (tonggia < 100000) {
                            String[] formatGia = String.valueOf(tonggia).split("");
                            String chuoi = formatGia[1] + formatGia[2] + ".";
                            for (int i = 3; i < formatGia.length; i++)
                                chuoi += formatGia[i];
                            sTonggia = chuoi + ".000 VND";
                        } else if (tonggia < 1000000) {
                            String[] formatGia = String.valueOf(tonggia).split("");
                            String chuoi = formatGia[1] + formatGia[2] + formatGia[3] + ".";
                            for (int i = 4; i < formatGia.length; i++)
                                chuoi += formatGia[i];
                            sTonggia = chuoi + ".000 VND";
                        }
                        reference.child(Magiohang).child(danhmuc).child(sMonAn).child("Gia").setValue(gia.getText().toString());
                        reference.child(Magiohang).child(danhmuc).child(sMonAn).child("Ten").setValue(ten.getText().toString());
                        reference.child(Magiohang).child(danhmuc).child(sMonAn).child("SoLuong").setValue(String.valueOf(tongsl));
                        reference.child(Magiohang).child(danhmuc).child(sMonAn).child("Tong").setValue(sTonggia);
                        reference.child(Magiohang).child(danhmuc).child(sMonAn).child("HinhAnh").setValue(hinhanh.getText().toString());

                    }
                    Toast.makeText(FoodDetailtActivity.this, "Đã thêm " + ten.getText().toString() + " vào giỏ hàng!", Toast.LENGTH_LONG).show();
                    Intent Cart = new Intent(FoodDetailtActivity.this, CartActivity.class);
                    Cart.putExtra("idUser", ID);
                    startActivity(Cart);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void tangsl() {
        cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer sl = Integer.valueOf(soluong.getText().toString().trim());
                if (sl < 100) {
                    sl++;
                    Integer Tong = Integer.valueOf(gia.getText().toString().trim().split("\\.")[0]) * sl;
                    if (Tong < 1000) {
                        tong.setText(Tong + ".000 VND");
                    } else if (Tong < 10000) {
                        String[] formatGia = Tong.toString().split("");
                        String chuoi = formatGia[1] + ".";
                        for (int i = 2; i < formatGia.length; i++)
                            chuoi += formatGia[i];
                        tong.setText(chuoi + ".000 VND");
                    } else if (Tong < 100000) {
                        String[] formatGia = Tong.toString().split("");
                        String chuoi = formatGia[1] + formatGia[2] + ".";
                        for (int i = 3; i < formatGia.length; i++)
                            chuoi += formatGia[i];
                        tong.setText(chuoi + ".000 VND");
                    } else if (Tong < 1000000) {
                        String[] formatGia = Tong.toString().split("");
                        String chuoi = formatGia[1] + formatGia[2] + formatGia[3] + ".";
                        for (int i = 4; i < formatGia.length; i++)
                            chuoi += formatGia[i];
                        tong.setText(chuoi + ".000 VND");
                    }
                    soluong.setText(sl + "");
                } else {
                    Toast.makeText(FoodDetailtActivity.this, "Số lượng không quá 100", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void giamsl() {
        tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer sl = Integer.valueOf(soluong.getText().toString().trim());
                if (sl > 1) {
                    sl--;
                    Integer Tong = Integer.valueOf(gia.getText().toString().trim().split("\\.")[0]) * sl;
                    if (Tong < 1000) {
                        tong.setText(Tong + ".000 VND");
                    } else if (Tong < 10000) {
                        String[] formatGia = Tong.toString().split("");
                        String chuoi = formatGia[1] + ".";
                        for (int i = 2; i < formatGia.length; i++)
                            chuoi += formatGia[i];
                        tong.setText(chuoi + ".000 VND");
                    } else if (Tong < 100000) {
                        String[] formatGia = Tong.toString().split("");
                        String chuoi = formatGia[1] + formatGia[2] + ".";
                        for (int i = 3; i < formatGia.length; i++)
                            chuoi += formatGia[i];
                        tong.setText(chuoi + ".000 VND");
                    } else if (Tong < 1000000) {
                        String[] formatGia = Tong.toString().split("");
                        String chuoi = formatGia[1] + formatGia[2] + formatGia[3] + ".";
                        for (int i = 4; i < formatGia.length; i++)
                            chuoi += formatGia[i];
                        tong.setText(chuoi + ".000 VND");
                    }
                    soluong.setText(sl + "");
                } else {
                    Toast.makeText(FoodDetailtActivity.this, "Số lượng không bé hơn 1", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void load() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("SanPham");
        reference.child(danhmuc).child(sMonAn).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                ten.setText(hashMap.get("Ten").toString().trim());
                mota.setText(hashMap.get("MoTa").toString().trim());
                gia.setText(hashMap.get("Gia").toString().trim());
                imgFood.setImageResource(getResources().getIdentifier(hashMap.get("HinhAnh").toString().trim(), "drawable", getPackageName()));
                hinhanh.setText(hashMap.get("HinhAnh").toString().trim());
                soluong.setText("1");
                tong.setText(hashMap.get("Gia").toString().trim());
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

    private void matching() {
        ten = (TextView) findViewById(R.id.tv_fooddetailt_ten);
        gia = (TextView) findViewById(R.id.tv_fooddetailt_gia);
        mota = (TextView) findViewById(R.id.tv_fooddetailt_mota);
        tong = (TextView) findViewById(R.id.tv_fooddetailt_tong);
        soluong = (TextView) findViewById(R.id.tv_fooddetailt_soluong);
        addtocart = (Button) findViewById(R.id.btn_fooddetailt_addtocart);
        cancel = (ImageView) findViewById(R.id.img_fooddetailt_cancel);
        imgFood = (ImageView) findViewById(R.id.img_fooddetailt_hinhanh);
        cong = (ImageView) findViewById(R.id.img_fooddetailt_max);
        tru = (ImageView) findViewById(R.id.img_fooddetailt_min);
        profile = (LinearLayout) findViewById(R.id.liner_Propfile_fooddetailt);
        home = (LinearLayout) findViewById(R.id.homeBtn_fooddetailt);
        hinhanh = (TextView) findViewById(R.id.tv_hinhanh_foodetailt);
        btncart = (FloatingActionButton) findViewById(R.id.card_btn_fooddetailt);
        donhang = (LinearLayout) findViewById(R.id.linear_order_fooddetailt);
        support = (LinearLayout) findViewById(R.id.Linear_support_fooddetailt);

    }

}