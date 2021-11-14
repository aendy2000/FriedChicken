package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DoipassActivity extends AppCompatActivity {
    EditText matkhaucu, matkhaumoi, matkhaumoi2;
    Button luu, huy;
    String ID, trangthai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doipass);
        matching();
        Bundle extras = getIntent().getExtras();
        ID = extras.getString("doiMk");
        save();
        cancel();
    }

    private void cancel() {
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                            if(hashMap.get("Password").toString().equals(matkhaucu.getText().toString().trim())) {
                                if(matkhaumoi.getText().toString().trim().equals(matkhaucu.getText().toString().trim())){
                                    Toast.makeText(DoipassActivity.this, "Mật khẩu mới không được trùng với mật khẩu cũ", Toast.LENGTH_LONG).show();
                                } else {
                                    if (matkhaumoi.getText().toString().trim().equals(matkhaumoi2.getText().toString().trim())) {
                                        myReference.child(ID).child("Password").setValue(matkhaumoi.getText().toString().trim());
                                        Toast.makeText(DoipassActivity.this, "Mật khẩu đã được thay đổi", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(DoipassActivity.this, "Mật khẩu mới không trùng khớp", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else {
                                Toast.makeText(DoipassActivity.this, "Mật khẩu cũ không đúng", Toast.LENGTH_LONG).show();
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
    }
}