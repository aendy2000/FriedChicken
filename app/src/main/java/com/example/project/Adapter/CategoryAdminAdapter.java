package com.example.project.Adapter;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.project.Activity.AcountManagerActivity;
import com.example.project.Activity.CartManagerActivity;
import com.example.project.Activity.CategoryManagerActivity;
import com.example.project.Activity.ListFoodActivity;
import com.example.project.Activity.OrderManagerActivity;
import com.example.project.Activity.ProductManagerActivity;
import com.example.project.Domain.CategoryAdminDomain;
import com.example.project.Domain.CategoryDomain;
import com.example.project.R;

import java.util.ArrayList;

public class CategoryAdminAdapter extends RecyclerView.Adapter<CategoryAdminAdapter.ViewHolder> {
    ArrayList<CategoryAdminDomain> categoryAdminDomains;
    Context myContex;

    public CategoryAdminAdapter(Context context, ArrayList<CategoryAdminDomain> categoryAdminDomain) {
        this.categoryAdminDomains = categoryAdminDomain;
        this.myContex = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cat_admin, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryAdminDomain categoryAdminDomain = categoryAdminDomains.get(position);
        holder.categoryName.setText(categoryAdminDomain.getTendm());
        String picUrl = categoryAdminDomain.getHinhanhdm();
        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(picUrl, "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.categoryPic);
        if(position % 2 == 0)
            holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.category_background3));
        else
            holder.mainLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.category_background2));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(categoryAdminDomain.getMadm().equals("TaiKhoan")){
                    Intent intent = new Intent(myContex, AcountManagerActivity.class);
                    intent.putExtra("idData", categoryAdminDomain.getMadm());
                    myContex.startActivity(intent);
                } else if(categoryAdminDomain.getMadm().equals("DanhMuc")){
                    Intent intent = new Intent(myContex, CategoryManagerActivity.class);
                    intent.putExtra("idData", categoryAdminDomain.getMadm());
                    myContex.startActivity(intent);
                } else if(categoryAdminDomain.getMadm().equals("SanPham")){
                    Intent intent = new Intent(myContex, ProductManagerActivity.class);
                    intent.putExtra("idData", categoryAdminDomain.getMadm());
                    myContex.startActivity(intent);
                } else if(categoryAdminDomain.getMadm().equals("GioHang")){
                    Intent intent = new Intent(myContex, CartManagerActivity.class);
                    intent.putExtra("idData", categoryAdminDomain.getMadm());
                    myContex.startActivity(intent);
                }  else if(categoryAdminDomain.getMadm().equals("DonHang")){
                    Intent intent = new Intent(myContex, OrderManagerActivity.class);
                    intent.putExtra("idData", categoryAdminDomain.getMadm());
                    myContex.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (categoryAdminDomains != null) {
            return categoryAdminDomains.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryPic;
        ConstraintLayout mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryAdminName);
            categoryPic = itemView.findViewById(R.id.categoryAdminPic);
            mainLayout = itemView.findViewById(R.id.mainAdminLayout);
        }
    }
}
