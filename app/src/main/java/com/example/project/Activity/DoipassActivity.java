package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class DoipassActivity extends AppCompatActivity {
    EditText matkhaucu, matkhaumoi, matkhaumoi2;
    Button luu, huy;
    String ID;
    ImageView profile, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doipass);
        matching();
        Bundle extras = getIntent().getExtras();
        ID = extras.getString("doiMk");
        load();
        save();
        Cancel();
    }

    private void load() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myReference = database.getReference("TaiKhoan");
        myReference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                if (hashMap.get("HinhDaiDien") != null) {
                    Picasso.with(DoipassActivity.this).load(hashMap.get("HinhDaiDien").toString()).into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Cancel() {
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mydialog = new AlertDialog.Builder(DoipassActivity.this);
                mydialog.setTitle("X??c nh???n");
                mydialog.setMessage("H???y b??? thay ?????i m???t kh???u?");
                mydialog.setIcon(R.drawable.cauhoi);
                mydialog.setPositiveButton("[H???Y B???]", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                mydialog.setNegativeButton("[QUAY L???I]", new DialogInterface.OnClickListener() {
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

    private void save() {
        luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myReference = database.getReference("TaiKhoan");
                myReference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                            if (!matkhaumoi.getText().toString().trim().equals("")
                                    || !matkhaumoi2.getText().toString().trim().equals("")
                                    || !matkhaucu.getText().toString().trim().equals("")) {
                                if (hashMap.get("Password").toString().equals(matkhaucu.getText().toString().trim())) {
                                    if (matkhaumoi.getText().toString().trim().equals(matkhaucu.getText().toString().trim())) {
                                        Toast.makeText(DoipassActivity.this, "M???t kh???u m???i kh??ng ???????c tr??ng v???i m???t kh???u c??", Toast.LENGTH_LONG).show();
                                    } else {
                                        if (matkhaumoi.getText().toString().trim().equals(matkhaumoi2.getText().toString().trim())) {
                                            AlertDialog.Builder mydialog = new AlertDialog.Builder(DoipassActivity.this);
                                            mydialog.setTitle("X??c nh???n");
                                            mydialog.setMessage("Thay ?????i m???t kh???u?");
                                            mydialog.setIcon(R.drawable.cauhoi);
                                            mydialog.setPositiveButton("[L??U]", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    myReference.child(ID).child("Password").setValue(matkhaumoi.getText().toString().trim());
                                                    Toast.makeText(DoipassActivity.this, "M???t kh???u ???? ???????c thay ?????i", Toast.LENGTH_LONG).show();
                                                    finish();
                                                }
                                            });
                                            mydialog.setNegativeButton("[H???Y B???]", new DialogInterface.OnClickListener() {
                                                @SuppressLint("WrongConstant")
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            });
                                            AlertDialog alertDialog = mydialog.create();
                                            alertDialog.show();
                                        } else {
                                            Toast.makeText(DoipassActivity.this, "M???t kh???u kh??ng tr??ng kh???p!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(DoipassActivity.this, "M???t kh???u c?? kh??ng ????ng!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(DoipassActivity.this, "H??y ?????m b???o c??c tr?????ng ?????u ???????c nh???p!", Toast.LENGTH_LONG).show();
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
        });
    }

    private void matching() {
        matkhaucu = (EditText) findViewById(R.id.et_dmk_pass);
        matkhaumoi = (EditText) findViewById(R.id.et_dmk_pass1);
        matkhaumoi2 = (EditText) findViewById(R.id.et_dmk_pass2);
        luu = (Button) findViewById(R.id.btn_dmk_luu);
        huy = (Button) findViewById(R.id.btn_dmk_huy);
        profile = (ImageView) findViewById(R.id.img_profile_changepass);
        cancel = (ImageView) findViewById(R.id.img_changepass_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}