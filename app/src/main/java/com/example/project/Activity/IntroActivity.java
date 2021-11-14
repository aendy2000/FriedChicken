package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class IntroActivity extends AppCompatActivity {
    Button startBtn;
    EditText taikhoan, matkhau;
    TextView dangky;
    String ID;
    String TAG = "FIREBASE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        matching();
        register();
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("TaiKhoan");
                ID = "TK" + taikhoan.getText().toString().trim();
                reference.child(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                            if (hashMap == null) {
                                Toast.makeText(IntroActivity.this, "Tài khoản đăng nhập không đúng!\nvui lòng kiểm tra lại", Toast.LENGTH_LONG).show();
                            } else {
                                if (hashMap.get("Password").toString().equals(matkhau.getText().toString())) {
                                    if(hashMap.get("Role").toString().equals("User")) {
                                        Intent login = new Intent(IntroActivity.this, MainActivity.class);
                                        login.putExtra("idUser", ID);
                                        login.putExtra("nameUser", hashMap.get("Ten").toString().trim());
                                        startActivity(login);
                                    } else {

                                    }

                                } else {
                                    Toast.makeText(IntroActivity.this, "Mật khẩu đăng nhập không đúng!\nvui lòng kiểm tra lại", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
                    }
                });
            }
        });
    }

    private void register() {
        dangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroActivity.this, DangkyActivity.class));
            }
        });
    }

    private void matching() {
        startBtn = (Button) findViewById(R.id.btn_dangnhap);
        taikhoan = (EditText) findViewById(R.id.et_sdt);
        matkhau = (EditText) findViewById(R.id.et_password);
        dangky = (TextView) findViewById(R.id.tv_dangKyTaiKhoan);
    }
}