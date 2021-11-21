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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.CategoryProductAdapter;
import com.example.project.Domain.CategoryProductDomain;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    Uri uri;
    private static final int MY_REQUEST_CODE = 100;
    Spinner categorryPro;
    CategoryProductAdapter adapterCate;
    String category, maSP, maDM, test = "";
    EditText ten, gia, mota;
    ImageView cancel, hinhanh;
    Button themhinhanh, luu;
    TextView tieude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        matching();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            maSP = bundle.getString("MaSanPham");
            maDM = bundle.getString("MaDanhMuc");
            if (maSP.equals("Add")) {
                getListCategory();
            } else {
                tieude.setText("SỬA SẢN PHẨM");
                luu.setText("LƯU CHỈNH SỬA");
                getListCategoryReload();
                loadData();
            }
        }
        Luu();
        themhinhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemHinh();
            }
        });
        categorryPro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterCate.getItem(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void Luu() {
        StorageReference khoAnh = FirebaseStorage.getInstance().getReference("Image-Upload");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SanPham");
        luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (maDM.equals("Add")) {
                    if (!test.equals("") && !ten.getText().toString().trim().equals("")
                            && !gia.getText().toString().trim().equals("")
                            && !mota.getText().toString().trim().equals("")
                            && !category.equals("")) {
                        AlertDialog.Builder mydialog = new AlertDialog.Builder(AddProductActivity.this);
                        mydialog.setTitle("Xác nhận");
                        mydialog.setMessage("Thêm sản phẩm?");
                        mydialog.setIcon(R.drawable.cauhoi);
                        mydialog.setPositiveButton("[THÊM]", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        int id = 0;
                                        for (DataSnapshot data : snapshot.getChildren()) {
                                            id = Integer.valueOf(data.getKey().split("-")[1]);
                                        }
                                        id++;
                                        String formatID;
                                        if (id < 10)
                                            formatID = "SP-00000" + id;
                                        else if (id < 100)
                                            formatID = "SP-0000" + id;
                                        else if (id < 1000)
                                            formatID = "SP-000" + id;
                                        else if (id < 10000)
                                            formatID = "SP-00" + id;
                                        else if (id < 100000)
                                            formatID = "SP-0" + id;
                                        else
                                            formatID = "SP-" + id;

                                        ContentResolver contentResolver = getContentResolver();
                                        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

                                        StorageReference fileReference = khoAnh.child(System.currentTimeMillis() + "." + mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)));
                                        fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {

                                                        reference.child(formatID).child("Ten").setValue(ten.getText().toString().trim());
                                                        reference.child(formatID).child("Gia").setValue(gia.getText().toString());
                                                        reference.child(formatID).child("MoTa").setValue(mota.getText().toString().trim());
                                                        reference.child(formatID).child("DanhMuc").setValue(category);
                                                        reference.child(formatID).child("HinhAnh").setValue(uri.toString());

                                                        Toast.makeText(AddProductActivity.this, "Đã thêm một sản phẩm mới", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(AddProductActivity.this, ProductManagerActivity.class);
                                                        intent.putExtra("idData", "SanPham");
                                                        finish();
                                                        startActivity(intent);
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
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
                        Toast.makeText(AddProductActivity.this, "Hãy đảm bảo không một trường nào còn bỏ trống!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!ten.getText().toString().trim().equals("")
                            && !gia.getText().toString().trim().equals("")
                            && !mota.getText().toString().trim().equals("")
                            && !category.equals("")) {


                        AlertDialog.Builder mydialog = new AlertDialog.Builder(AddProductActivity.this);
                        mydialog.setTitle("Xác nhận");
                        mydialog.setMessage("Lưu chỉnh sửa?");
                        mydialog.setIcon(R.drawable.cauhoi);
                        mydialog.setPositiveButton("[LƯU]", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                reference.child(maSP).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (test.equals("")) {

                                            reference.child(maSP).child("Ten").setValue(ten.getText().toString().trim());
                                            reference.child(maSP).child("Gia").setValue(gia.getText().toString());
                                            reference.child(maSP).child("MoTa").setValue(mota.getText().toString().trim());
                                            reference.child(maSP).child("DanhMuc").setValue(category);

                                            Toast.makeText(AddProductActivity.this, "Đã lưu chỉnh sửa sản phẩm", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(AddProductActivity.this, ProductManagerActivity.class);
                                            intent.putExtra("idData", "SanPham");
                                            finish();
                                            startActivity(intent);
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

                                                            reference.child(maSP).child("Ten").setValue(ten.getText().toString().trim());
                                                            reference.child(maSP).child("Gia").setValue(gia.getText().toString());
                                                            reference.child(maSP).child("MoTa").setValue(mota.getText().toString().trim());
                                                            reference.child(maSP).child("DanhMuc").setValue(category);
                                                            reference.child(maSP).child("HinhAnh").setValue(uri.toString());
                                                            Toast.makeText(AddProductActivity.this, "Đã lưu chỉnh sửa sản phẩm", Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(AddProductActivity.this, ProductManagerActivity.class);
                                                            intent.putExtra("idData", "SanPham");
                                                            finish();
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AddProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    private void loadData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SanPham");
        reference.child(maSP).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                ten.setText(hashMap.get("Ten").toString());
                gia.setText(hashMap.get("Gia").toString());
                mota.setText(hashMap.get("MoTa").toString());
                uri = Uri.parse(hashMap.get("HinhAnh").toString());
                Picasso.with(AddProductActivity.this).load(uri).into(hinhanh);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getListCategoryReload() {
        List<CategoryProductDomain> list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DanhMuc");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int index = 0;
                int i = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    list.add(new CategoryProductDomain(data.getValue().toString().substring(data.getValue().toString().indexOf("Ten") + 4, data.getValue().toString().length() - 1), data.getKey()));
                    if (data.getKey().equals(maDM))
                        index = i;
                    i++;
                }
                adapterCate = new CategoryProductAdapter(AddProductActivity.this, R.layout.item_selected_category_product, list);
                categorryPro.setAdapter(adapterCate);
                categorryPro.setSelection(index);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getListCategory() {
        List<CategoryProductDomain> list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DanhMuc");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    list.add(new CategoryProductDomain(data.getValue().toString().substring(data.getValue().toString().indexOf("Ten") + 4, data.getValue().toString().length() - 1), data.getKey()));
                }
                adapterCate = new CategoryProductAdapter(AddProductActivity.this, R.layout.item_selected_category_product, list);
                categorryPro.setAdapter(adapterCate);
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

    private void matching() {
        categorryPro = (Spinner) findViewById(R.id.spn_category_product);
        ten = (EditText) findViewById(R.id.et_add_product_tenpro);
        gia = (EditText) findViewById(R.id.et_add_product_gia);
        mota = (EditText) findViewById(R.id.et_add_product_mota);
        cancel = (ImageView) findViewById(R.id.img_add_product_cancel);
        hinhanh = (ImageView) findViewById(R.id.img_add_product_hinhanh);
        themhinhanh = (Button) findViewById(R.id.btn_add_product_hinhanh);
        luu = (Button) findViewById(R.id.btn_add_product_luu);
        tieude = (TextView) findViewById(R.id.tv_tieude_add_product);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProductActivity.this, ProductManagerActivity.class);
                intent.putExtra("idData", "SanPham");
                finish();
                startActivity(intent);
            }
        });
    }
}