package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class ProfileActivity extends AppCompatActivity {
    EditText hoten, sodienthoai, email, ngay, thang, namsinh, diachi;
    TextView doimk, dangxuat;
    RadioButton nam, nu;
    Button sua, luu, huy;
    String ID, gioitinh, ngaysinh, avarCu, test = "";
    ImageView back, changepro, ava;
    LinearLayout home, donhang, support;
    FloatingActionButton btncart;
    Uri uri;
    private static final int MY_REQUEST_CODE = 100;

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
        changeAva();
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

                    if (hashMap.get("HinhDaiDien") != null) {
                        Picasso.with(ProfileActivity.this).load(hashMap.get("HinhDaiDien").toString()).into(ava);
                        avarCu = hashMap.get("HinhDaiDien").toString();
                    } else {
                        avarCu = "profileuser";
                    }

                    gioitinh = hashMap.get("GioiTinh").toString();
                    if (gioitinh.equals("Nam"))
                        nam.setChecked(true);
                    else
                        nu.setChecked(true);
                    diachi.setText(hashMap.get("DiaChi").toString());

                    hoten.setEnabled(false);
                    email.setEnabled(false);
                    sodienthoai.setEnabled(false);
                    ngay.setEnabled(false);
                    thang.setEnabled(false);
                    namsinh.setEnabled(false);
                    nam.setEnabled(false);
                    nu.setEnabled(false);
                    diachi.setEnabled(false);
                    sua.setVisibility(View.VISIBLE);
                    luu.setVisibility(View.INVISIBLE);
                    huy.setVisibility(View.INVISIBLE);
                    changepro.setVisibility(View.GONE);
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
                    hoten.setEnabled(true);
                    email.setEnabled(true);
                    ngay.setEnabled(true);
                    thang.setEnabled(true);
                    namsinh.setEnabled(true);
                    nam.setEnabled(true);
                    nu.setEnabled(true);
                    diachi.setEnabled(true);
                    sua.setVisibility(View.INVISIBLE);
                    luu.setVisibility(View.VISIBLE);
                    huy.setVisibility(View.VISIBLE);
                    changepro.setVisibility(View.VISIBLE);
                }
            }
        });
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mydialog = new AlertDialog.Builder(ProfileActivity.this);
                mydialog.setTitle("Xác nhận");
                mydialog.setMessage("Hủy bỏ thay đổi?");
                mydialog.setIcon(R.drawable.cauhoi);
                mydialog.setPositiveButton("[HỦY BỎ]", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        test = "";
                        if (avarCu.equals("profileuser")) {
                            ava.setImageResource(getResources().getIdentifier("profileuser", "drawable", getPackageName()));
                        } else {
                            Picasso.with(ProfileActivity.this).load(avarCu).into(ava);
                        }
                        load();
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
        });
        luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mydialog = new AlertDialog.Builder(ProfileActivity.this);
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
                                    reference.child(ID).child("Ten").setValue(hoten.getText().toString().trim());
                                    reference.child(ID).child("SDT").setValue(sodienthoai.getText().toString().trim());
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
                                    Toast.makeText(ProfileActivity.this, "Đã lưu chỉnh sửa", Toast.LENGTH_SHORT).show();
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
                                                    reference.child(ID).child("Ten").setValue(hoten.getText().toString().trim());
                                                    reference.child(ID).child("SDT").setValue(sodienthoai.getText().toString().trim());
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
                                                    Toast.makeText(ProfileActivity.this, "Đã lưu chỉnh sửa", Toast.LENGTH_SHORT).show();
                                                    load();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        luu = (Button) findViewById(R.id.btn_pf_luu);
        huy = (Button) findViewById(R.id.btn_pf_huy);
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
        changepro = (ImageView) findViewById(R.id.img_change_profile);
        ava = (ImageView) findViewById(R.id.img_avatar_profile);
    }
}