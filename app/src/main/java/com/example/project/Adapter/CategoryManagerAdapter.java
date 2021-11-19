package com.example.project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Activity.AddCategoryActivity;
import com.example.project.Activity.CategoryManagerActivity;
import com.example.project.Domain.CategoryManagerDomain;
import com.example.project.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryManagerAdapter extends RecyclerView.Adapter<CategoryManagerAdapter.CategoryManagerViewHolder> {
    ArrayList<CategoryManagerDomain> categoryManagerDomains;
    Context myContex;

    public CategoryManagerAdapter(Context context, ArrayList<CategoryManagerDomain> categoryManagerDomains) {
        this.categoryManagerDomains = categoryManagerDomains;
        this.myContex = context;
    }

    @NonNull
    @Override
    public CategoryManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_list_category_manager, parent, false);
        return new CategoryManagerAdapter.CategoryManagerViewHolder(inflate);
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryManagerViewHolder holder, int position) {
        CategoryManagerDomain categoryManagerDomain = categoryManagerDomains.get(position);
        holder.name.setText(categoryManagerDomain.getTen());
        Picasso.with(myContex).load(categoryManagerDomain.getHinhanh()).into(holder.image);

        holder.Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myContex, AddCategoryActivity.class);
                intent.putExtra("MaDanhMuc", categoryManagerDomain.getId());
                ((CategoryManagerActivity)myContex).finish();
                myContex.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference("DanhMuc");
                database.child(categoryManagerDomain.getId()).removeValue();
                Toast.makeText(myContex, "Đã xóa danh mục", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(myContex, CategoryManagerActivity.class);
                intent.putExtra("idData", "DanhMuc");
                ((CategoryManagerActivity)myContex).finish();
                myContex.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        if(categoryManagerDomains != null)
            return categoryManagerDomains.size();
        return 0;
    }

    public class CategoryManagerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image, delete;
        ConstraintLayout Layout;

        public CategoryManagerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name_category_manager);
            image = itemView.findViewById(R.id.img_list_category_manager);
            Layout = itemView.findViewById(R.id.listCategoryManager);
            delete = itemView.findViewById(R.id.img_delete_category);
        }
    }
}
