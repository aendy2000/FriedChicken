package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
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

@RequiresApi(api = Build.VERSION_CODES.N)
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
    NumberFormat formatter = new DecimalFormat("#,###");
    int TongCong;

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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("GioHang");
        reference.child("GH" + ID.split("K")[1]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int TongTien = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) data.getValue();
                    checkOutlist.add(new CheckoutDomain(data.getKey(),
                            hashMap.get("Ten").toString(),
                            hashMap.get("Gia").toString(),
                            hashMap.get("SoLuong").toString(),
                            hashMap.get("Tong").toString(),
                            hashMap.get("HinhAnh").toString(),
                            ID));
                    TongTien += Integer.valueOf(hashMap.get("Tong").toString());
                }
                TongCong = TongTien;
                result.setText(formatter.format(TongTien) + " VND");
                adapterRecyclerCheckout = new CheckOutAdapter(CheckoutActivity.this, checkOutlist);
                recyclerCheckout.setAdapter(adapterRecyclerCheckout);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference referenceTK = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        referenceTK.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
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
                if (!hoten.getText().toString().trim().equals("")
                        && !hoten.getText().toString().trim().equals("")
                        && !hoten.getText().toString().trim().equals("")) {

                    AlertDialog.Builder mydialog = new AlertDialog.Builder(CheckoutActivity.this);
                    mydialog.setTitle("Xác nhận");
                    mydialog.setMessage("Bạn chắc chắn muốn mua hàng?");
                    mydialog.setIcon(R.drawable.cauhoi);
                    mydialog.setPositiveButton("[ĐẶT HÀNG]", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            LocalDateTime myDateObj = LocalDateTime.now();
                            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyMMdd-HH:mm:ss");
                            String maDonHang = myDateObj.format(myFormatObj);

                            DatabaseReference referenceGioHang = FirebaseDatabase.getInstance().getReference("GioHang");
                            DatabaseReference referenceDonHang = FirebaseDatabase.getInstance().getReference("DonHang");
                            referenceGioHang.child("GH" + ID.split("K")[1]).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int somon = 0;
                                    for (DataSnapshot data : snapshot.getChildren()) {
                                        HashMap<String, Object> hashMap = (HashMap<String, Object>) data.getValue();
                                        referenceDonHang.child("DH" + ID.split("K")[1]).child(maDonHang).child(data.getKey()).child("Ten").setValue(hashMap.get("Ten").toString());
                                        referenceDonHang.child("DH" + ID.split("K")[1]).child(maDonHang).child(data.getKey()).child("Gia").setValue(hashMap.get("Gia").toString());
                                        referenceDonHang.child("DH" + ID.split("K")[1]).child(maDonHang).child(data.getKey()).child("SoLuong").setValue(hashMap.get("SoLuong").toString());
                                        referenceDonHang.child("DH" + ID.split("K")[1]).child(maDonHang).child(data.getKey()).child("Tong").setValue(hashMap.get("Tong").toString());
                                        referenceDonHang.child("DH" + ID.split("K")[1]).child(maDonHang).child(data.getKey()).child("HinhAnh").setValue(hashMap.get("HinhAnh").toString());
                                        somon++;
                                    }
                                    referenceDonHang.child("DH" + ID.split("K")[1]).child(maDonHang).child("TrangThai").child("Trangthai").setValue("Chờ duyệt");
                                    referenceDonHang.child("DH" + ID.split("K")[1]).child(maDonHang).child("SoMon").child("Somon").setValue(String.valueOf(somon));
                                    referenceDonHang.child("DH" + ID.split("K")[1]).child(maDonHang).child("TongCong").child("Tongcong").setValue(String.valueOf(TongCong));
                                    referenceDonHang.child("DH" + ID.split("K")[1]).child(maDonHang).child("ThongTin").child("Ten").setValue(hoten.getText().toString().trim());
                                    referenceDonHang.child("DH" + ID.split("K")[1]).child(maDonHang).child("ThongTin").child("SDT").setValue(sdt.getText().toString().trim());
                                    referenceDonHang.child("DH" + ID.split("K")[1]).child(maDonHang).child("ThongTin").child("DiaChi").setValue(diachi.getText().toString().trim());

                                    referenceGioHang.child("GH" + ID.split("K")[1]).removeValue();
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
                    mydialog.setNegativeButton("[HỦY BỎ]", new DialogInterface.OnClickListener() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = mydialog.create();
                    alertDialog.show();

                } else {
                    Toast.makeText(CheckoutActivity.this, "Phải nhập đầy đủ thông tin giao hàng!", Toast.LENGTH_SHORT).show();
                }
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