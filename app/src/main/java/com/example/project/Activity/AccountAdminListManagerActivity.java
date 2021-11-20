package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.project.Adapter.AccountListUserAdapter;
import com.example.project.Domain.AccountListUserDomain;
import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AccountAdminListManagerActivity extends AppCompatActivity {
    ImageView cancel;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_admin_list_manager);
        matching();
        load();
    }

    private void load() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ArrayList<AccountListUserDomain> list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    if(data.getValue().toString().substring(data.getValue().toString().indexOf("Role") + 5, data.getValue().toString().indexOf(", SDT")).equals("Admin")){
                        String ten = data.getValue().toString().substring(data.getValue().toString().indexOf("Ten") + 4, data.getValue().toString().indexOf(", Password"));
                        String hinhanh = data.getValue().toString().substring(data.getValue().toString().indexOf("HinhDaiDien") + 12, data.getValue().toString().indexOf(", Ten"));
                        if(hinhanh.indexOf("https://") == -1)
                            hinhanh = "Null";
                        String role = data.getValue().toString().substring(data.getValue().toString().indexOf("Role") + 5, data.getValue().toString().indexOf(", SDT"));
                        list.add(new AccountListUserDomain(data.getKey(), ten, hinhanh, role));
                    }
                }
                adapter = new AccountListUserAdapter(AccountAdminListManagerActivity.this, list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void matching() {
        cancel = (ImageView) findViewById(R.id.img_account_admin_list_cancel);
        recyclerView = (RecyclerView) findViewById(R.id.rcv_list_account_admin);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}