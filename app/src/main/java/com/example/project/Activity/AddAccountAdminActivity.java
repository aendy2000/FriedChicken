package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddAccountAdminActivity extends AppCompatActivity {

    EditText ten, sdt, email, ngay, thang, namsinh, diachi, taikhoan, pass, rePass;
    RadioButton nam, nu;
    Button add, huy;
    ImageView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account_admin);
        matching();
        Huy();
        Cancel();
        Add();
    }

    private void Add() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            if (data.getKey().toUpperCase().substring(2).equals(taikhoan.getText().toString().trim().toUpperCase())) {
                                Toast.makeText(AddAccountAdminActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if (!ten.getText().toString().trim().equals("")
                                || !sdt.getText().toString().trim().equals("")
                                || !email.getText().toString().trim().equals("")
                                || !ngay.getText().toString().trim().equals("")
                                || !thang.getText().toString().trim().equals("")
                                || !namsinh.getText().toString().trim().equals("")
                                || (nam.isChecked() == true || nu.isChecked() == true)
                                || !diachi.getText().toString().trim().equals("")
                                || !taikhoan.getText().toString().trim().equals("")
                                || !pass.getText().toString().trim().equals("")
                                || !rePass.getText().toString().trim().equals("")) {

                            if (taikhoan.getText().toString().indexOf(" ") != -1) {
                                Toast.makeText(AddAccountAdminActivity.this, "Tài khoản không được chứa ký tự khoảng trắng!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                if (pass.getText().toString().trim().equals(rePass.getText().toString().trim())) {
                                    if (sdt.getText().toString().length() > 9 && sdt.getText().toString().length() < 12) {

                                        if (email.getText().toString().trim().indexOf("@") != -1
                                                && email.getText().toString().trim().indexOf(".com") != -1
                                                && email.getText().toString().trim().indexOf("@.com") == -1
                                                && email.getText().toString().trim().indexOf(" ") == -1) {

                                            AlertDialog.Builder mydialog = new AlertDialog.Builder(AddAccountAdminActivity.this);
                                            mydialog.setTitle("Xác nhận");
                                            mydialog.setMessage("Thêm tài khoản " + taikhoan.getText().toString() + "?");
                                            mydialog.setIcon(R.drawable.cauhoi);
                                            mydialog.setPositiveButton("[THÊM]", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    reference.child("TK" + taikhoan.getText().toString().toUpperCase()).child("Ten").setValue(ten.getText().toString().trim());
                                                    reference.child("TK" + taikhoan.getText().toString().toUpperCase()).child("SDT").setValue(sdt.getText().toString().trim());
                                                    reference.child("TK" + taikhoan.getText().toString().toUpperCase()).child("Role").setValue("Admin");
                                                    reference.child("TK" + taikhoan.getText().toString().toUpperCase()).child("Password").setValue(pass.getText().toString().trim());
                                                    String ngaysinh = "";
                                                    if (Integer.valueOf(ngay.getText().toString()) < 10) {
                                                        if (Integer.valueOf(thang.getText().toString()) < 10)
                                                            ngaysinh = "0" + Integer.valueOf(ngay.getText().toString())
                                                                    + "-0" + Integer.valueOf(thang.getText().toString())
                                                                    + "-" + namsinh.getText().toString();
                                                        else
                                                            ngaysinh = "0" + Integer.valueOf(ngay.getText().toString())
                                                                    + "-" + thang.getText().toString()
                                                                    + "-" + namsinh.getText().toString();
                                                    } else {
                                                        if (Integer.valueOf(thang.getText().toString()) < 10)
                                                            ngaysinh = ngay.getText().toString()
                                                                    + "-0" + Integer.valueOf(thang.getText().toString())
                                                                    + "-" + namsinh.getText().toString();
                                                        else
                                                            ngaysinh = ngay.getText().toString()
                                                                    + "-" + thang.getText().toString()
                                                                    + "-" + namsinh.getText().toString();
                                                    }
                                                    reference.child("TK" + taikhoan.getText().toString().toUpperCase()).child("NgaySinh").setValue(ngaysinh);
                                                    if (nam.isChecked())
                                                        reference.child("TK" + taikhoan.getText().toString().toUpperCase()).child("GioiTinh").setValue("Nam");
                                                    else
                                                        reference.child("TK" + taikhoan.getText().toString().toUpperCase()).child("GioiTinh").setValue("Nữ");
                                                    reference.child("TK" + taikhoan.getText().toString().toUpperCase()).child("Email").setValue(email.getText().toString().trim());
                                                    reference.child("TK" + taikhoan.getText().toString().toUpperCase()).child("DiaChi").setValue(diachi.getText().toString().trim());

                                                    Toast.makeText(AddAccountAdminActivity.this, "Đã thêm tài khoản " + taikhoan.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                                                    finish();
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
                                            Toast.makeText(AddAccountAdminActivity.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } else {
                                        Toast.makeText(AddAccountAdminActivity.this, "Số điện thoại không hợp lệ!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(AddAccountAdminActivity.this, "Mật khẩu không trùng khớp!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(AddAccountAdminActivity.this, "Phải nhập đủ tất cả các trường!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void Huy() {
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    private void matching() {
        ten = (EditText) findViewById(R.id.et_add_admin_hoten);
        sdt = (EditText) findViewById(R.id.et_add_admin_sdt);
        email = (EditText) findViewById(R.id.et_add_admin_email);
        ngay = (EditText) findViewById(R.id.et_add_admin_ngay);
        thang = (EditText) findViewById(R.id.et_add_admin_thang);
        namsinh = (EditText) findViewById(R.id.et_add_admin_namsinh);
        diachi = (EditText) findViewById(R.id.et_add_admin_diachi);
        taikhoan = (EditText) findViewById(R.id.et_add_admin_taikhoan);
        pass = (EditText) findViewById(R.id.et_add_admin_matkhau);
        rePass = (EditText) findViewById(R.id.et_add_admin_nhaplaimatkhau);

        nam = (RadioButton) findViewById(R.id.et_add_admin_nam);
        nu = (RadioButton) findViewById(R.id.et_add_admin_nu);

        add = (Button) findViewById(R.id.et_add_admin_them);
        huy = (Button) findViewById(R.id.et_add_admin_huy);

        cancel = (ImageView) findViewById(R.id.img_add_admin_cancel);
    }
}