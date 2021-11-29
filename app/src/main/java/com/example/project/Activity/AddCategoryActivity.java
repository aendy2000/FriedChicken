package com.example.project.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

public class AddCategoryActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 100;
    EditText ten;
    ImageView cancel, hinhanh;
    Button themhinh, luu;
    Uri uri;
    String Madanhmuc, test = "";
    TextView tieude;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        matching();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Đang tải, vui lòng chờ....");
        Bundle bundle = getIntent().getExtras();
        Madanhmuc = bundle.getString("MaDanhMuc");
        if (Madanhmuc.equals("Add")) {
            tieude.setText("THÊM DANH MỤC");

        } else {
            tieude.setText("CHỈNH SỬA DANH MỤC");
            luu.setText("Lưu chỉnh sửa");
            load();
        }

        ThemandUpdate();
        themhinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemHinh();
            }
        });
    }

    private void load() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DanhMuc");
        reference.child(Madanhmuc).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                ten.setText(hashMap.get("Ten").toString());
                uri = Uri.parse(hashMap.get("HinhAnh").toString());
                Picasso.with(AddCategoryActivity.this).load(uri).into(hinhanh);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ThemHinh() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            uri = data.getData();
            test = uri.toString();
            Picasso.with(getApplication()).load(uri).into(hinhanh);
        } else {
            return;
        }
    }

    private void ThemandUpdate() {
        StorageReference khoAnh = FirebaseStorage.getInstance().getReference("Image-Upload");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DanhMuc");
        luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Madanhmuc.equals("Add")) {
                    if (!test.equals("") && !ten.getText().toString().trim().equals("")) {
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (uri == null) {
                                } else {
                                    AlertDialog.Builder mydialog = new AlertDialog.Builder(AddCategoryActivity.this);
                                    mydialog.setTitle("Xác nhận");
                                    mydialog.setMessage("Thêm danh mục?");
                                    mydialog.setIcon(R.drawable.cauhoi);
                                    mydialog.setPositiveButton("[THÊM]", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialog.show();
                                            int id = 0;
                                            for (DataSnapshot data : snapshot.getChildren()) {
                                                id = Integer.valueOf(data.getKey().substring(data.getKey().indexOf("-") + 1));
                                            }
                                            id++;
                                            String formatID;
                                            if (id < 10)
                                                formatID = "DM-00000" + id;
                                            else if (id < 100)
                                                formatID = "DM-0000" + id;
                                            else if (id < 1000)
                                                formatID = "DM-000" + id;
                                            else if (id < 10000)
                                                formatID = "DM-00" + id;
                                            else if (id < 100000)
                                                formatID = "DM-0" + id;
                                            else
                                                formatID = "DM-" + id;

                                            ContentResolver contentResolver = getContentResolver();
                                            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

                                            StorageReference fileReference = khoAnh.child(System.currentTimeMillis() + "." + mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)));
                                            fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            reference.child(formatID).child("Ten").setValue(ten.getText().toString().trim().toUpperCase());
                                                            reference.child(formatID).child("HinhAnh").setValue(uri.toString());
                                                            Toast.makeText(AddCategoryActivity.this, "Đã thêm danh mục", Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(AddCategoryActivity.this, CategoryManagerActivity.class);
                                                            intent.putExtra("idData", "DanhMuc");
                                                            dialog.dismiss();
                                                            finish();
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AddCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Toast.makeText(AddCategoryActivity.this, "Bạn phải nhập đầy đủ tên và chọn hình ảnh!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    reference.child(Madanhmuc).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            AlertDialog.Builder mydialog = new AlertDialog.Builder(AddCategoryActivity.this);
                            mydialog.setTitle("Xác nhận");
                            mydialog.setMessage("Lưu chỉnh sửa danh mục?");
                            mydialog.setIcon(R.drawable.cauhoi);
                            mydialog.setPositiveButton("[LƯU]", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (test.equals("")) {
                                        reference.child(Madanhmuc).child("Ten").setValue(ten.getText().toString().toUpperCase().trim());
                                        Toast.makeText(AddCategoryActivity.this, "Đã lưu chỉnh sửa Danh Mục", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(AddCategoryActivity.this, CategoryManagerActivity.class);
                                        intent.putExtra("idData", "DanhMuc");
                                        finish();
                                        startActivity(intent);
                                    } else {
                                        dialog.show();
                                        ContentResolver contentResolver = getContentResolver();
                                        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

                                        StorageReference fileReference = khoAnh.child(System.currentTimeMillis() + "." + mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)));
                                        fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        reference.child(Madanhmuc).child("Ten").setValue(ten.getText().toString().trim().toUpperCase());
                                                        reference.child(Madanhmuc).child("HinhAnh").setValue(uri.toString());
                                                        Toast.makeText(AddCategoryActivity.this, "Đã lưu chỉnh sửa Danh Mục", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(AddCategoryActivity.this, CategoryManagerActivity.class);
                                                        intent.putExtra("idData", "DanhMuc");
                                                        dialog.dismiss();
                                                        finish();
                                                        startActivity(intent);
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AddCategoryActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
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
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private void matching() {
        ten = (EditText) findViewById(R.id.et_add_category_tendm);
        cancel = (ImageView) findViewById(R.id.img_add_category_cancel);
        hinhanh = (ImageView) findViewById(R.id.img_add_category_hinhanh);
        themhinh = (Button) findViewById(R.id.btn_add_category_hinhanh);
        luu = (Button) findViewById(R.id.btn_add_category_luu);
        tieude = (TextView) findViewById(R.id.textView53);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCategoryActivity.this, CategoryManagerActivity.class);
                intent.putExtra("idData", "DanhMuc");
                finish();
                startActivity(intent);
            }
        });
    }
}