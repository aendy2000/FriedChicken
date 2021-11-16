package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.project.Adapter.CheckoutlistAdapter;
import com.example.project.Adapter.OrderCategoryAdapter;
import com.example.project.Domain.OrderCategoryDomain;
import com.example.project.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrderManagerActivity extends AppCompatActivity {
    ImageView cancel;
    RecyclerView category;
    RecyclerView.Adapter categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manager);
        matching();
        load();
    }

    private void load() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        category.setLayoutManager(linearLayoutManager);
        ArrayList<OrderCategoryDomain> list = new ArrayList<>();
        list.add(new OrderCategoryDomain("Chờ duyệt", "cho_duyet"));
        list.add(new OrderCategoryDomain("Đã duyệt", "check_order"));
        list.add(new OrderCategoryDomain("Đang giao", "giao_hang"));
        list.add(new OrderCategoryDomain("Đã giao", "da_giao"));
        list.add(new OrderCategoryDomain("Không thể giao", "giaohang_thatbai"));
        list.add(new OrderCategoryDomain("Đã hủy", "cross_order"));
        categoryAdapter = new OrderCategoryAdapter(OrderManagerActivity.this, list);
        category.setAdapter(categoryAdapter);
    }

    private void matching() {
        cancel = (ImageView) findViewById(R.id.img_cancel_order_category);
        category = (RecyclerView) findViewById(R.id.rcv_list_order_category_manager);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}