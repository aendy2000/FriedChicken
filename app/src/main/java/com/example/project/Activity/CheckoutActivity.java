package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.CartAdapter;
import com.example.project.Adapter.CheckOutAdapter;
import com.example.project.Domain.CartDomain;
import com.example.project.Domain.CheckoutDomain;
import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class CheckoutActivity extends AppCompatActivity {
    RecyclerView recyclerCheckout;
    RecyclerView.Adapter adapterRecyclerCheckout;
    ImageView cancel;
    TextView somon, result;
    Button checkout;
    LinearLayout profile, home, donhang, support;
    FloatingActionButton btncart;
    String ID, ndDonHang;
    EditText hoten, sdt, diachi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        matching();

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        ID = extras.getString("idUser");
        ndDonHang = extras.getString("ResultCheckout");
        show();
        Cancel();
        ProFile();
        Home();
        cart();
        DatHang();
        Donhang();
        Support();
    }
    private void Support() {
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sup = new Intent(CheckoutActivity.this, SupportActivity.class);
                sup.putExtra("idUser", ID);
                startActivity(sup);
            }
        });
    }
    private void Donhang() {
        donhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent order = new Intent(CheckoutActivity.this, ListOrderActivity.class);
                order.putExtra("idUser", ID);
                startActivity(order);
            }
        });
    }
    private void show() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerCheckout.setLayoutManager(linearLayoutManager);
        ArrayList<CheckoutDomain> checkOutlist = new ArrayList<>();
        String[] tachDanhMuc = ndDonHang.split("-------------------------------------");
        for (int i = 0; i < tachDanhMuc.length - 1; i++) {
            int subDmuc = tachDanhMuc[i].split("DanhMuc:")[1].indexOf("Ma:");
            int subMa = tachDanhMuc[i].split("Ma:")[1].indexOf("Ten:");
            int subTen = tachDanhMuc[i].split("Ten:")[1].indexOf("Gia:");
            int subGia = tachDanhMuc[i].split("Gia:")[1].indexOf("SoLuong:");
            int subSoluong = tachDanhMuc[i].split("SoLuong:")[1].indexOf("Tong:");
            int subTong = tachDanhMuc[i].split("Tong:")[1].indexOf("HinhAnh:");
            String dMuc = tachDanhMuc[i].toString().split("DanhMuc:")[1].substring(0, subDmuc);
            String dMa = tachDanhMuc[i].toString().split("Ma:")[1].substring(0, subMa);
            String dTen = tachDanhMuc[i].toString().split("Ten:")[1].substring(0, subTen);
            String dGia = tachDanhMuc[i].toString().split("Gia:")[1].substring(0, subGia);
            String dSoLuong = tachDanhMuc[i].toString().split("SoLuong:")[1].substring(0, subSoluong);
            String dTong = tachDanhMuc[i].toString().split("Tong:")[1].substring(0, subTong);
            String dHinhAnh = tachDanhMuc[i].toString().split("HinhAnh:")[1];
            checkOutlist.add(new CheckoutDomain(dMa, dTen, dGia, dSoLuong, dTong, dHinhAnh, ID));
        }

        result.setText(tachDanhMuc[tachDanhMuc.length - 1].toString().split("TongCong:")[1]);
        adapterRecyclerCheckout = new CheckOutAdapter(CheckoutActivity.this, checkOutlist);
        recyclerCheckout.setAdapter(adapterRecyclerCheckout);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("TaiKhoan");
        reference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                    hoten.setText(hashMap.get("Ten").toString());
                    sdt.setText(hashMap.get("SDT").toString());
                    diachi.setText(hashMap.get("DiaChi").toString());
                } catch (Exception e) {
                    e.toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void DatHang() {
        checkout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String giohang = "GH" + ID.split("K")[1];
                FirebaseDatabase databaseremove = FirebaseDatabase.getInstance();
                DatabaseReference referenceremove = databaseremove.getReference("GioHang");
                referenceremove.child(giohang).removeValue();

                LocalDateTime myDateObj = LocalDateTime.now();
                DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyMMdd-HH:mm:ss");
                String maDonHang = myDateObj.format(myFormatObj);
                FirebaseDatabase databaseadd = FirebaseDatabase.getInstance();
                DatabaseReference referenceradd = databaseadd.getReference("DonHang");
                referenceradd.child("DH" + ID.split("K")[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String[] tachDanhMuc = ndDonHang.split("-------------------------------------");
                        for (int i = 0; i < tachDanhMuc.length - 1; i++) {
                            int subDmuc = tachDanhMuc[i].split("DanhMuc:")[1].indexOf("Ma:");
                            int subMa = tachDanhMuc[i].split("Ma:")[1].indexOf("Ten:");
                            int subTen = tachDanhMuc[i].split("Ten:")[1].indexOf("Gia:");
                            int subGia = tachDanhMuc[i].split("Gia:")[1].indexOf("SoLuong:");
                            int subSoluong = tachDanhMuc[i].split("SoLuong:")[1].indexOf("Tong:");
                            int subTong = tachDanhMuc[i].split("Tong:")[1].indexOf("HinhAnh:");
                            String dMuc = tachDanhMuc[i].toString().split("DanhMuc:")[1].substring(0, subDmuc);
                            String dMa = tachDanhMuc[i].toString().split("Ma:")[1].substring(0, subMa);
                            String dTen = tachDanhMuc[i].toString().split("Ten:")[1].substring(0, subTen);
                            String dGia = tachDanhMuc[i].toString().split("Gia:")[1].substring(0, subGia);
                            String dSoLuong = tachDanhMuc[i].toString().split("SoLuong:")[1].substring(0, subSoluong);
                            String dTong = tachDanhMuc[i].toString().split("Tong:")[1].substring(0, subTong);
                            String dHinhAnh = tachDanhMuc[i].toString().split("HinhAnh:")[1];
                            referenceradd.child("DH" + ID.split("K")[1]).child("|" + maDonHang).child(dMuc).child(dMa).child("Ten").setValue(dTen);
                            referenceradd.child("DH" + ID.split("K")[1]).child("|" + maDonHang).child(dMuc).child(dMa).child("Gia").setValue(dGia);
                            referenceradd.child("DH" + ID.split("K")[1]).child("|" + maDonHang).child(dMuc).child(dMa).child("SoLuong").setValue(dSoLuong);
                            referenceradd.child("DH" + ID.split("K")[1]).child("|" + maDonHang).child(dMuc).child(dMa).child("Tong").setValue(dTong);
                            referenceradd.child("DH" + ID.split("K")[1]).child("|" + maDonHang).child(dMuc).child(dMa).child("HinhAnh").setValue(dHinhAnh);
                            int iSoMon = 1 + i;
                            somon.setText(iSoMon + " món");
                        }
                        referenceradd.child("DH" + ID.split("K")[1]).child("|" + maDonHang).child("TrangThai").setValue("Chờ duyệt");
                        referenceradd.child("DH" + ID.split("K")[1]).child("|" + maDonHang).child("SoMon").setValue(somon.getText());
                        referenceradd.child("DH" + ID.split("K")[1]).child("|" + maDonHang).child("TongCong").setValue(tachDanhMuc[tachDanhMuc.length - 1].split("TongCong:")[1]);
                        referenceradd.child("DH" + ID.split("K")[1]).child("|" + maDonHang).child("ThongTin").child("Ten").setValue(hoten.getText().toString());
                        referenceradd.child("DH" + ID.split("K")[1]).child("|" + maDonHang).child("ThongTin").child("SDT").setValue(sdt.getText().toString());
                        referenceradd.child("DH" + ID.split("K")[1]).child("|" + maDonHang).child("ThongTin").child("DiaChi").setValue(diachi.getText().toString());

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                FirebaseDatabase databasetk = FirebaseDatabase.getInstance();
                DatabaseReference referencetk = databasetk.getReference("TaiKhoan");
                referencetk.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshottk) {
                        try {
                            HashMap<String, Object> hashMaptk = (HashMap<String, Object>) snapshottk.getValue();
                            String name = hashMaptk.get("Ten").toString();
                            finishAffinity();
                            Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("idUser", ID);
                            bundle.putSerializable("nameUser", name);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            Toast.makeText(CheckoutActivity.this, "Đặt hàng thành công", Toast.LENGTH_LONG).show();
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

    private void Cancel() {
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
                Intent Cart = new Intent(CheckoutActivity.this, CartActivity.class);
                Cart.putExtra("idUser", ID);
                startActivity(Cart);
            }
        });
    }

    private void ProFile() {
        Intent idPro = new Intent(CheckoutActivity.this, ProfileActivity.class);
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
                            Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
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
        cancel = (ImageView) findViewById(R.id.img_checkout_cancel);
        checkout = (Button) findViewById(R.id.btn_checkout_dathang);
        btncart = (FloatingActionButton) findViewById(R.id.card_btn_checkout);
        home = (LinearLayout) findViewById(R.id.homeBtn_checkout);
        profile = (LinearLayout) findViewById(R.id.liner_Propfile_checkout);
        somon = (TextView) findViewById(R.id.tv_somon);
        hoten = (EditText) findViewById(R.id.et_checkout_hoten);
        sdt = (EditText) findViewById(R.id.et_checkout_sdt);
        diachi = (EditText) findViewById(R.id.et_checkout_diachi);
        recyclerCheckout = (RecyclerView) findViewById(R.id.rcv_checkout_result);
        result = (TextView) findViewById(R.id.tv_TongCong_checkout);
        donhang = (LinearLayout) findViewById(R.id.linear_order_checkout);
        support = (LinearLayout) findViewById(R.id.Linear_support_checkout);

    }
}