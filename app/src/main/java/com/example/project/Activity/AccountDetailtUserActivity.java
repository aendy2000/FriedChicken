package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AccountDetailtUserActivity extends AppCompatActivity {
    ImageView ava, cancel, changepro;
    EditText ten, sdt, email, ngay, thang, namsinh, diachi;
    RadioButton nam, nu;
    Button xoaUser, xoaAd, sua;
    String ID, Role, avarCu, test = "";
    TextView doimk, quenmk;
    Uri uri;
    private static final int MY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detailt_user);
        matching();
        changepro.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ID = bundle.getString("idUser");
            Role = bundle.getString("Role");
            if (Role.equals("User")) {
                xoaAd.setVisibility(View.GONE);
                sua.setVisibility(View.GONE);
                doimk.setVisibility(View.GONE);
                quenmk.setVisibility(View.GONE);
            } else {
                xoaUser.setVisibility(View.GONE);
            }
            load();
            Xoa();
            Sua();
            changeAva();
            Doimatkhau();
        }
    }

    private void Doimatkhau() {
        doimk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent doiPass = new Intent(AccountDetailtUserActivity.this, DoipassActivity.class);
                doiPass.putExtra("doiMk", ID);
                startActivity(doiPass);
            }
        });
    }

    private void changeAva() {
        changepro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            uri = data.getData();
            test = uri.toString();
            Picasso.with(getApplication()).load(uri).into(ava);
        } else {
            return;
        }
    }

    private void Sua() {
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ten.isEnabled() == false) {
                    ten.setEnabled(true);
                    email.setEnabled(true);
                    sdt.setEnabled(true);
                    ngay.setEnabled(true);
                    thang.setEnabled(true);
                    namsinh.setEnabled(true);
                    nam.setEnabled(true);
                    nu.setEnabled(true);
                    diachi.setEnabled(true);
                    changepro.setVisibility(View.VISIBLE);
                    sua.setText("Lưu");
                    xoaAd.setText("HỦY");
                } else {
                    AlertDialog.Builder mydialog = new AlertDialog.Builder(AccountDetailtUserActivity.this);
                    mydialog.setTitle("Xác nhận");
                    mydialog.setMessage("Lưu thay đổi?");
                    mydialog.setIcon(R.drawable.cauhoi);
                    mydialog.setPositiveButton("[LƯU]", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            StorageReference khoAnh = FirebaseStorage.getInstance().getReference("Image-Upload");
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
                            reference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (test.equals("")) {
                                        reference.child(ID).child("Ten").setValue(ten.getText().toString().trim());
                                        reference.child(ID).child("SDT").setValue(sdt.getText().toString().trim());
                                        reference.child(ID).child("Email").setValue(email.getText().toString().trim());
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
                                        reference.child(ID).child("NgaySinh").setValue(ngaysinh);
                                        if (nam.isChecked())
                                            reference.child(ID).child("GioiTinh").setValue("Nam");
                                        else
                                            reference.child(ID).child("GioiTinh").setValue("Nữ");
                                        reference.child(ID).child("DiaChi").setValue(diachi.getText().toString().trim());
                                        Toast.makeText(AccountDetailtUserActivity.this, "Đã lưu chỉnh sửa", Toast.LENGTH_SHORT).show();

                                        ten.setEnabled(false);
                                        email.setEnabled(false);
                                        sdt.setEnabled(false);
                                        ngay.setEnabled(false);
                                        thang.setEnabled(false);
                                        namsinh.setEnabled(false);
                                        nam.setEnabled(false);
                                        nu.setEnabled(false);
                                        diachi.setEnabled(false);
                                        changepro.setVisibility(View.GONE);
                                        sua.setText("Sửa");
                                        xoaAd.setText("XÓA");

                                        load();

                                    } else {
                                        ContentResolver contentResolver = getContentResolver();
                                        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

                                        StorageReference fileReference = khoAnh.child(System.currentTimeMillis() + "." + mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)));
                                        fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        reference.child(ID).child("Ten").setValue(ten.getText().toString().trim());
                                                        reference.child(ID).child("SDT").setValue(sdt.getText().toString().trim());
                                                        reference.child(ID).child("Email").setValue(email.getText().toString().trim());
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
                                                        reference.child(ID).child("NgaySinh").setValue(ngaysinh);
                                                        if (nam.isChecked())
                                                            reference.child(ID).child("GioiTinh").setValue("Nam");
                                                        else
                                                            reference.child(ID).child("GioiTinh").setValue("Nữ");
                                                        reference.child(ID).child("DiaChi").setValue(diachi.getText().toString().trim());
                                                        reference.child(ID).child("HinhDaiDien").setValue(uri.toString());
                                                        Toast.makeText(AccountDetailtUserActivity.this, "Đã lưu chỉnh sửa", Toast.LENGTH_SHORT).show();

                                                        ten.setEnabled(false);
                                                        email.setEnabled(false);
                                                        sdt.setEnabled(false);
                                                        ngay.setEnabled(false);
                                                        thang.setEnabled(false);
                                                        namsinh.setEnabled(false);
                                                        nam.setEnabled(false);
                                                        nu.setEnabled(false);
                                                        diachi.setEnabled(false);
                                                        changepro.setVisibility(View.GONE);
                                                        sua.setText("Sửa");
                                                        xoaAd.setText("XÓA");

                                                        load();
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AccountDetailtUserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                    mydialog.setNegativeButton("[QUAY LẠI]", new DialogInterface.OnClickListener() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = mydialog.create();
                    alertDialog.show();
                }
            }
        });
    }

    private void Xoa() {
        xoaUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mydialog = new AlertDialog.Builder(AccountDetailtUserActivity.this);
                mydialog.setTitle("Xác nhận");
                mydialog.setMessage("Xóa tài khoản " + ID.substring(2) + "?");
                mydialog.setIcon(R.drawable.cauhoi);
                mydialog.setPositiveButton("[XÓA]", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
                        reference.child(ID).removeValue();
                        Toast.makeText(AccountDetailtUserActivity.this, "Đã xóa tài khoản " + ID.substring(2) + "!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(AccountDetailtUserActivity.this, AccountUserListManagerActivity.class));
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
            }
        });
        xoaAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (xoaAd.getText().equals("XÓA")) {
                    AlertDialog.Builder mydialog = new AlertDialog.Builder(AccountDetailtUserActivity.this);
                    mydialog.setTitle("Xác nhận");
                    mydialog.setMessage("Xóa tài khoản " + ID.substring(2) + "?");
                    mydialog.setIcon(R.drawable.cauhoi);
                    mydialog.setPositiveButton("[XÓA]", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
                            reference.child(ID).removeValue();
                            Toast.makeText(AccountDetailtUserActivity.this, "Đã xóa tài khoản " + ID.substring(2) + "!", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(AccountDetailtUserActivity.this, AccountAdminListManagerActivity.class));
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
                    AlertDialog.Builder mydialog = new AlertDialog.Builder(AccountDetailtUserActivity.this);
                    mydialog.setTitle("Xác nhận");
                    mydialog.setMessage("Hủy bỏ thay đổi?");
                    mydialog.setIcon(R.drawable.cauhoi);
                    mydialog.setPositiveButton("[HỦY BỎ]", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            load();
                            test = "";
                            sua.setText("Sửa");
                            xoaAd.setText("XÓA");
                            changepro.setVisibility(View.GONE);
                            if (avarCu.equals("profileuser")) {
                                ava.setImageResource(getResources().getIdentifier("profileuser", "drawable", getPackageName()));
                            } else {
                                Picasso.with(AccountDetailtUserActivity.this).load(avarCu).into(ava);
                            }
                        }
                    });
                    mydialog.setNegativeButton("[QUAY LẠI]", new DialogInterface.OnClickListener() {
                        @SuppressLint("WrongConstant")
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog alertDialog = mydialog.create();
                    alertDialog.show();
                }
            }
        });
    }

    private void load() {

        ten.setEnabled(false);
        email.setEnabled(false);
        sdt.setEnabled(false);
        ngay.setEnabled(false);
        thang.setEnabled(false);
        namsinh.setEnabled(false);
        nam.setEnabled(false);
        nu.setEnabled(false);
        diachi.setEnabled(false);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        reference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                ten.setText(hashMap.get("Ten").toString());
                sdt.setText(hashMap.get("SDT").toString());
                email.setText(hashMap.get("Email").toString());
                ngay.setText(hashMap.get("NgaySinh").toString().substring(0, 2));
                thang.setText(hashMap.get("NgaySinh").toString().substring(3, 5));
                namsinh.setText(hashMap.get("NgaySinh").toString().substring(6));
                if (hashMap.get("GioiTinh").equals("Nam"))
                    nam.setChecked(true);
                else
                    nu.setChecked(true);
                diachi.setText(hashMap.get("DiaChi").toString());
                if (hashMap.get("HinhDaiDien") != null) {
                    Picasso.with(AccountDetailtUserActivity.this).load(hashMap.get("HinhDaiDien").toString()).into(ava);
                    avarCu = hashMap.get("HinhDaiDien").toString();
                } else {
                    avarCu = "profileuser";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void matching() {
        doimk = (TextView) findViewById(R.id.tv_doimk_detailt_admin);
        xoaUser = (Button) findViewById(R.id.btn_user_detailt_delete);
        xoaAd = (Button) findViewById(R.id.btn_admin_detailt_delete);
        sua = (Button) findViewById(R.id.btn_admin_detailt_edit);
        ten = (EditText) findViewById(R.id.et_user_detailt_hoten);
        email = (EditText) findViewById(R.id.et_user_detailt_email);
        ngay = (EditText) findViewById(R.id.et_user_detailt_ngay);
        thang = (EditText) findViewById(R.id.et_user_detailt_thang);
        namsinh = (EditText) findViewById(R.id.et_user_detailt_namsinh);
        nam = (RadioButton) findViewById(R.id.rdo_user_detailt_nam);
        nu = (RadioButton) findViewById(R.id.rdo_user_detailt_nu);
        diachi = (EditText) findViewById(R.id.et_user_detailt_diachi);
        sdt = (EditText) findViewById(R.id.et_user_detailt_sdt);
        cancel = (ImageView) findViewById(R.id.img_user_detailt_back);
        ava = (ImageView) findViewById(R.id.img_avatar_user_detailt);
        changepro = (ImageView) findViewById(R.id.img_change_profile_admin);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                if (Role.equals("User"))
                    startActivity(new Intent(AccountDetailtUserActivity.this, AccountUserListManagerActivity.class));
                else
                    startActivity(new Intent(AccountDetailtUserActivity.this, AccountAdminListManagerActivity.class));
            }
        });
    }
}