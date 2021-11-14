package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableRow;
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

public class ProfileActivity extends AppCompatActivity {
    EditText hoten, sodienthoai, email, ngay, thang, namsinh, diachi;
    TextView doimk, dangxuat;
    RadioButton nam, nu;
    Button sua;
    String ID, gioitinh, ngaysinh;
    ImageView back;
    LinearLayout home, donhang, support;
    FloatingActionButton btncart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        matching();
        Bundle extras = getIntent().getExtras();
        ID = extras.getString("idProf");
        changPass();
        load();
        edit();
        Back();
        dangXuat();
        Home();
        cart();
        Donhang();
        Support();
    }

    private void Donhang() {
        donhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent order = new Intent(ProfileActivity.this, ListOrderActivity.class);
                order.putExtra("idUser", ID);
                startActivity(order);
            }
        });
    }

    private void cart() {
        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Cart = new Intent(ProfileActivity.this, CartActivity.class);
                Cart.putExtra("idUser", ID);
                startActivity(Cart);
            }
        });

    }

    private void Support() {
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sup = new Intent(ProfileActivity.this, SupportActivity.class);
                sup.putExtra("idUser", ID);
                startActivity(sup);
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
                            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
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

    private void dangXuat() {
        dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                startActivity(new Intent(ProfileActivity.this, IntroActivity.class));
            }
        });
    }

    private void changPass() {
        doimk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent doiPass = new Intent(ProfileActivity.this, DoipassActivity.class);
                doiPass.putExtra("doiMk", ID);
                startActivity(doiPass);
            }
        });
    }

    private void load() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myReference = database.getReference("TaiKhoan");
        myReference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                    hoten.setText(hashMap.get("Ten").toString());
                    email.setText(hashMap.get("Email").toString());
                    sodienthoai.setText(hashMap.get("SDT").toString());
                    ngaysinh = hashMap.get("NgaySinh").toString();
                    String[] ngaythangnamsinh = ngaysinh.split("-");
                    ngay.setText(ngaythangnamsinh[0]);
                    thang.setText(ngaythangnamsinh[1]);
                    namsinh.setText(ngaythangnamsinh[2]);

                    gioitinh = hashMap.get("GioiTinh").toString();
                    if (gioitinh.equals("Nam"))
                        nam.setChecked(true);
                    else
                        nu.setChecked(true);
                    diachi.setText(hashMap.get("DiaChi").toString());

                    if (hoten.isEnabled() == true) {
                        hoten.setEnabled(false);
                        email.setEnabled(false);
                        sodienthoai.setEnabled(false);
                        ngay.setEnabled(false);
                        thang.setEnabled(false);
                        namsinh.setEnabled(false);
                        nam.setEnabled(false);
                        nu.setEnabled(false);
                        diachi.setEnabled(false);
                    }
                } catch (Exception e) {
                    Log.d("Loi JSON", e.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Loi chi tiet", "LoadPost:onCanceled", error.toException());
            }
        });
    }

    private void edit() {
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hoten.isEnabled() == false) {
                    sua.setText("Lưu");
                    hoten.setEnabled(true);
                    email.setEnabled(true);
                    ngay.setEnabled(true);
                    thang.setEnabled(true);
                    namsinh.setEnabled(true);
                    nam.setEnabled(true);
                    nu.setEnabled(true);
                    diachi.setEnabled(true);
                } else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("TaiKhoan");
                    sua.setText("Chỉnh sửa thông tin");


                    myRef.child(ID).child("Ten").setValue(hoten.getText().toString().trim());
                    myRef.child(ID).child("Email").setValue(email.getText().toString().trim());
                    myRef.child(ID).child("DiaChi").setValue(diachi.getText().toString().trim());
                    ngaysinh = ngay.getText().toString().trim() + "-" + thang.getText().toString().trim() + "-" + namsinh.getText().toString().trim();
                    myRef.child(ID).child("NgaySinh").setValue(ngaysinh);

                    if (nam.isChecked())
                        gioitinh = "Nam";
                    else
                        gioitinh = "Nữ";
                    myRef.child(ID).child("GioiTinh").setValue(gioitinh);

                    hoten.setEnabled(false);
                    email.setEnabled(false);
                    ngay.setEnabled(false);
                    thang.setEnabled(false);
                    namsinh.setEnabled(false);
                    nam.setEnabled(false);
                    nu.setEnabled(false);
                    diachi.setEnabled(false);

                    Toast.makeText(ProfileActivity.this, "Đã lưu thay đổi", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void Back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void matching() {
        sua = (Button) findViewById(R.id.btn_pf_edit);
        hoten = (EditText) findViewById(R.id.et_pf_hoten);
        email = (EditText) findViewById(R.id.et_pf_email);
        ngay = (EditText) findViewById(R.id.et_pf_ngay);
        thang = (EditText) findViewById(R.id.et_pf_thang);
        namsinh = (EditText) findViewById(R.id.et_pf_namsinh);
        nam = (RadioButton) findViewById(R.id.rdo_pf_nam);
        nu = (RadioButton) findViewById(R.id.rdo_pf_nu);
        diachi = (EditText) findViewById(R.id.et_pf_diachi);
        sodienthoai = (EditText) findViewById(R.id.et_pf_sdt);
        back = (ImageView) findViewById(R.id.img_profile_back);
        doimk = (TextView) findViewById(R.id.tv_pf_doimatkhau);
        dangxuat = (TextView) findViewById(R.id.tv_pf_dangxuat);
        home = (LinearLayout) findViewById(R.id.homeBtn_Profile_profile);
        btncart = (FloatingActionButton) findViewById(R.id.card_btn_profile);
        donhang = (LinearLayout) findViewById(R.id.linnear_order_profile);
        support = (LinearLayout) findViewById(R.id.Linear_support_profile);

    }
}