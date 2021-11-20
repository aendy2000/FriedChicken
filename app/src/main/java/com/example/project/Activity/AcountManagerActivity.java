package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.project.Adapter.ManagerAccountListMainAdapter;
import com.example.project.Domain.ManagerAccountListMainDomain;
import com.example.project.R;

import java.util.ArrayList;

public class AcountManagerActivity extends AppCompatActivity {
    ImageView cancel;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount_manager);
        matching();
        load();
    }

    private void load() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ArrayList<ManagerAccountListMainDomain> list = new ArrayList<>();
        list.add(new ManagerAccountListMainDomain("QUẢN LÍ NGƯỜI DÙNG", "manageruser", "1"));
        list.add(new ManagerAccountListMainDomain("QUẢN LÍ TÀI KHOẢN ADMIN", "manageradmin", "2"));
        list.add(new ManagerAccountListMainDomain("THÊM TÀI KHOẢN ADMIN", "adduser", "3"));
        adapter = new ManagerAccountListMainAdapter(AcountManagerActivity.this, list);
        recyclerView.setAdapter(adapter);
    }

    private void matching() {
        recyclerView = (RecyclerView) findViewById(R.id.rcv_list_manager_account_main);
        cancel = (ImageView) findViewById(R.id.img_accountlist_manager_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}