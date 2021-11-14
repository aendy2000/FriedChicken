package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class DangkyActivity extends AppCompatActivity {

    EditText hoten, sodienthoai, email, ngay, thang, namsinh, diachi, mk, remk;
    RadioButton nam, nu;
    Button dangky, huy;
    String TAG = "FIREBASE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);
        matching();

        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themTaiKhoan();
            }
        });

    }

    private void themTaiKhoan() {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("TaiKhoan");

            String gioitinh;
            if (nam.isChecked())
                gioitinh = "Nam";
            else
                gioitinh = "Nữ";

            myRef.child("TK" + sodienthoai.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                        if (hashMap == null) {
                            String TaiKhoanId = "TK" + sodienthoai.getText().toString().trim();
                            String sname = hoten.getText().toString().trim();
                            String ssdt = sodienthoai.getText().toString().trim();
                            String semail = email.getText().toString().trim();
                            String ssinhnhat = ngay.getText().toString().trim() + "-" + thang.getText().toString().trim() + "-" + namsinh.getText().toString().trim();
                            String sgioitinh = gioitinh;
                            String sdiachi = diachi.getText().toString().trim();
                            String smatkhau = mk.getText().toString().trim();
                            String sgiohang = "GH" + sodienthoai.getText().toString().trim();
                            if (smatkhau.equals(remk.getText().toString().trim())) {
                                myRef.child(TaiKhoanId).child("DiaChi").setValue(sdiachi);
                                myRef.child(TaiKhoanId).child("Email").setValue(semail);
                                myRef.child(TaiKhoanId).child("GioHang").setValue(sgiohang);
                                myRef.child(TaiKhoanId).child("GioiTinh").setValue(sgioitinh);
                                myRef.child(TaiKhoanId).child("NgaySinh").setValue(ssinhnhat);
                                myRef.child(TaiKhoanId).child("Password").setValue(smatkhau);
                                myRef.child(TaiKhoanId).child("SDT").setValue(ssdt);
                                myRef.child(TaiKhoanId).child("Ten").setValue(sname);
                                myRef.child(TaiKhoanId).child("Role").setValue("User");
                                Toast.makeText(DangkyActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();

                                Intent main = new Intent(DangkyActivity.this, IntroActivity.class);
                                startActivity(main);
                            } else {
                                Toast.makeText(DangkyActivity.this, "Mật khẩu không trùng khớp", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(DangkyActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error:" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void matching() {
        huy = (Button) findViewById(R.id.btn_dk_huy);
        dangky = (Button) findViewById(R.id.btn_dk_dangky);
        hoten = (EditText) findViewById(R.id.et_dk_hoten);
        email = (EditText) findViewById(R.id.et_dk_email);
        ngay = (EditText) findViewById(R.id.et_dk_ngay);
        thang = (EditText) findViewById(R.id.et_dk_thang);
        namsinh = (EditText) findViewById(R.id.et_dk_namsinh);
        nam = (RadioButton) findViewById(R.id.rdo_dk_nam);
        nu = (RadioButton) findViewById(R.id.rdo_dk_nu);
        diachi = (EditText) findViewById(R.id.et_dk_diachi);
        mk = (EditText) findViewById(R.id.et_dk_password);
        remk = (EditText) findViewById(R.id.et_dk_password2);
        sodienthoai = (EditText) findViewById(R.id.et_dk_sdt);
    }
}