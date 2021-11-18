package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project.Activity.OrderDetailtManagerActivity;
import com.example.project.Domain.OrderCategoryDomain;
import com.example.project.R;

import java.util.ArrayList;

public class OrderCategoryAdapter extends RecyclerView.Adapter<OrderCategoryAdapter.OrderCategoryViewHolder> {
    ArrayList<OrderCategoryDomain> listOrder;
    Context myContex;

    public OrderCategoryAdapter(Context context, ArrayList<OrderCategoryDomain> listOrder) {
        this.listOrder = listOrder;
        this.myContex = context;
    }

    @NonNull
    @Override
    public OrderCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order_category, parent, false);
        return new OrderCategoryAdapter.OrderCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCategoryViewHolder holder, int position) {
        OrderCategoryDomain orderCategoryDomain = listOrder.get(position);
        if (orderCategoryDomain == null)
            return;
        holder.ten.setText(orderCategoryDomain.getTrangthai());
        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(orderCategoryDomain.getHinhanh(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.hinhanh);
        if (position % 2 == 0)
            holder.layout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.category_background3));
        else
            holder.layout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.category_background2));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myContex, OrderDetailtManagerActivity.class);
                intent.putExtra("TrangThai", orderCategoryDomain.getTrangthai());
                myContex.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listOrder != null)
            return listOrder.size();
        return 0;
    }

    public class OrderCategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView hinhanh;
        TextView ten;
        ConstraintLayout layout;

        public OrderCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ten = itemView.findViewById(R.id.tv_order_category_name);
            hinhanh = itemView.findViewById(R.id.img_Oder_category);
            layout = itemView.findViewById(R.id.listOderCategory);
        }
    }
}

