package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.project.Adapter.CategoryAdapter;
import com.example.project.Adapter.CategoryAdminAdapter;
import com.example.project.Domain.CategoryAdminDomain;
import com.example.project.Domain.CategoryDomain;
import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainAdminActivity extends AppCompatActivity {
    TextView name;
    RecyclerView recyclerViewDanhMuc;
    RecyclerView.Adapter recyclerViewDanhMucAdapter;
    FloatingActionButton logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        matching();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        name.setText("Xin chào " + extras.getString("nameUser") + " !");
        Logout();
        addviewRCV();
    }

    private void addviewRCV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewDanhMuc.setLayoutManager(linearLayoutManager);

        ArrayList<CategoryAdminDomain> categoryList = new ArrayList<>();
        categoryList.add(new CategoryAdminDomain("Tài Khoản", "TaiKhoan", "admin_user"));
        categoryList.add(new CategoryAdminDomain("Danh Mục", "DanhMuc", "admin_category"));
        categoryList.add(new CategoryAdminDomain("Sản Phẩm", "SanPham", "admin_food"));
        categoryList.add(new CategoryAdminDomain("Giỏ Hàng", "GioHang", "admin_cart"));
        categoryList.add(new CategoryAdminDomain("Đơn Hàng", "DonHang", "admin_order"));
        recyclerViewDanhMucAdapter = new CategoryAdminAdapter(MainAdminActivity.this, categoryList);
        recyclerViewDanhMuc.setAdapter(recyclerViewDanhMucAdapter);

    }

    private void Logout() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void matching() {
        name = (TextView) findViewById(R.id.tv_tenAdmin);
        logout = (FloatingActionButton) findViewById(R.id.float_mainAdmin_logout);
        recyclerViewDanhMuc = (RecyclerView) findViewById(R.id.rcv_mainAdmin_danhmuc);

    }
}